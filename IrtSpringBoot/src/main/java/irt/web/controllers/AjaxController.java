package irt.web.controllers;

import irt.web.entities.component.ComponentEntity;
import irt.web.entities.component.repositories.ComponentsRepository;
import irt.web.view.workers.component.PartNumbers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ajax")
public class AjaxController {

	private static final Logger logger = LogManager.getLogger();

	@Autowired
	private ComponentsRepository componentsRepository;

	@RequestMapping(value="partnumber", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String[]>  getPartNumbers(@RequestParam(value="term", required=false) String partNumber){
		logger.entry(partNumber);

		String[] partnumbers;
		if(partNumber!=null && !(partNumber=partNumber.trim()).isEmpty()){
			Page<ComponentEntity> page = componentsRepository.findFirst10ByPartNumberContaining(PartNumbers.dbFormat(partNumber), new PageRequest(0, 10, new Sort(Direction.ASC, "partNumber")));
			List<ComponentEntity> ces = page.getContent();
			partnumbers = new String[ces.size()];
			for(int i=0; i<partnumbers.length; i++)
				partnumbers[i]= PartNumbers.format(ces.get(i).getPartNumber());
		}else
			partnumbers = null;

		return logger.exit(new ResponseEntity<String[]>(partnumbers, HttpStatus.CREATED));
	}
}
