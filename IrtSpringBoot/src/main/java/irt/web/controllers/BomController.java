package irt.web.controllers;

import irt.web.entities.bom.BomEntity;
import irt.web.view.BomView;
import irt.web.view.beans.ComponentBean;
import irt.web.view.beans.ComponentBean.Status;
import irt.web.view.workers.component.PartNumbers;
import irt.web.workers.beans.OrderNameVisibility;
import irt.web.workers.excel.ExcelBuilder;
import irt.web.workers.excel.OrderNameVisibilityWorker;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("bom")
public class BomController {

	private static final Logger logger = LogManager.getLogger();

	@Lazy
	@Autowired
	private BomView bomView;
	@Autowired
	private ExcelBuilder excelBuilder;
	@Autowired
	private OrderNameVisibilityWorker orderNameVisibilityWorker;

	//Make menu BOM active
	@ModelAttribute("menuBOM")
	public Boolean  attrSelectedMenu(){
		return true;
	}

	@ModelAttribute("bomView")
	public BomView  attrBomView(){
		return bomView;
	}

	@ModelAttribute("onvs")
	public List<OrderNameVisibility>  attrONVs(@RequestParam Map<String, String> allParams, @CookieValue(value="bomSetting", required=false)String bomSetting, HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException{
		logger.entry(allParams);

		ObjectMapper om = new ObjectMapper();
		boolean reset = allParams.containsKey("reset");
		List<OrderNameVisibility> onvs = orderNameVisibilityWorker.getOrderNameVisibilities(reset);

		if(!reset){

			if(bomView.getComponentEntity()==null && bomSetting!=null && allParams.size()<=3){
				allParams = om.readValue(bomSetting, new TypeReference<HashMap<String,String>>(){});
				if(allParams.containsKey("partNumber")){
					bomView.setComponent(allParams.get("partNumber"));
					getBOM();
				}
			}

			if(allParams.size()>3){
				for(OrderNameVisibility onv: onvs){
					onv.setVisible(allParams.get("cb_"+onv.getId())!=null);
					onv.setOrder(Integer.parseInt(allParams.get("num_"+onv.getId())));
				}
				String json = om.writeValueAsString(allParams);
				response.addCookie(new Cookie("bomSetting", json));
			}
			Collections.sort(onvs);
		}

		return onvs;
	}

	@RequestMapping
	public String getBom(final ComponentBean componentBean){
		logger.entry(componentBean);

		List<BomEntity> bom = null;
		if(componentBean.getPartNumber()==null)
			componentBean.setPartNumber(bomView.getPartNumber());

		else if(!PartNumbers.equals(componentBean.getPartNumber(), bomView.getPartNumber())){

			if(bomView.setComponent(componentBean.getPartNumber())) {
				bom = getBOM();
				if(bom!=null)
					componentBean.setStatus(Status.SUCCESS);
				else
					componentBean.setStatus(null);
			} else
				componentBean.setStatus(Status.ERROR);
		}

		componentBean.setBom(getBOM()!=null);

		logger.exit(componentBean);
		return "bom";
	}

	@RequestMapping("/excel/{partNumber}")
	public ResponseEntity<byte[]> getBom(@PathVariable String partNumber){
		logger.entry(partNumber);

		if(bomView.getPartNumber()==null){
			bomView.setComponent(partNumber);
		}

		byte[] excel = null;
		HttpHeaders headers = new HttpHeaders();
		HttpStatus httpStatus;

		try {

			excel = excelBuilder.getExcel(bomView, orderNameVisibilityWorker, "/static/images/logo.png");
			headers.setContentLength(excel.length);
			headers.set("Content-Disposition", "filename=\"" + bomView.getPartNumber() + ".xlsx\"");
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			httpStatus = HttpStatus.OK;

		} catch (IOException e) {
			logger.catching(e);
			httpStatus = HttpStatus.NOT_FOUND;
		}

		return new ResponseEntity<byte[]>(excel, headers, httpStatus);
	}

	private List<BomEntity> getBOM() {
		return bomView.hasBOM() ? bomView.getBOM(bomView.getComponentEntity().getId()) : null;
	}
}
