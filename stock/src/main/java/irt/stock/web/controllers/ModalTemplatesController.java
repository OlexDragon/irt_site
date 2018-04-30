package irt.stock.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import irt.stock.data.jpa.beans.Company.CompanyType;
import irt.stock.data.jpa.beans.CompanyQty;
import irt.stock.data.jpa.beans.Component;
import irt.stock.data.jpa.beans.PartNumber;
import irt.stock.data.jpa.repositories.BomRepository;
import irt.stock.data.jpa.repositories.CompanyRepository;
import irt.stock.data.jpa.repositories.ComponentRepository;
import irt.stock.data.jpa.repositories.PartNumberRepository;

@Controller
@RequestMapping("modal")
public class ModalTemplatesController {

	@Autowired private CompanyRepository companyRepository;
	@Autowired private PartNumberRepository partNumberRepository;
	@Autowired private ComponentRepository componentRepository;
	@Autowired private BomRepository bomRepository;

	@GetMapping("to_co_mfr/{componentId}")
	public String getMoveToCoMfr(@PathVariable Long componentId, Model model){
		model.addAttribute("component", partNumberRepository.findById(componentId).get());
		model.addAttribute("coMfrs", companyRepository.findByType(CompanyType.CO_MANUFACTURER));
		return "modal/move_to_co_mfr :: moveToCoMfr";
	}

	@GetMapping("to_bulk/{componentId}")
	public String getMoveToBulk(@PathVariable Long componentId, Model model){

		model.addAttribute("component", componentRepository.findById(componentId).get());
		return "modal/move_to_bulk :: moveToBulk";
	}

	@GetMapping("mfr_to_bulk/{componentId}")
	public String getMoveFromMfrToBulk(@PathVariable Long componentId, Model model){

		final Component component = componentRepository.findById(componentId).get();
		model.addAttribute("component", component);
		model.addAttribute("companyQties", component.getCompanyQties().stream().filter(cq->cq.getQty()>0).toArray(CompanyQty[]::new));
		return "modal/move_to_bulk :: moveMfrToBulk";
	}

	@GetMapping("to_stock/{componentId}")
	public String getMoveToStock(@PathVariable Long componentId, Model model){

		final Component component = componentRepository.findById(componentId).get();
		model.addAttribute("component", component);
		model.addAttribute("companyQties", component.getCompanyQties().stream().filter(cq->cq.getQty()>0).toArray(CompanyQty[]::new));
		return "modal/move_to_stock :: moveToStock";
	}

	@GetMapping("to_assembly/{componentId}")
	public String getMoveToAssembly(@PathVariable Long componentId, Model model){

		final List<PartNumber> pcas = bomRepository.findTopPartNumberByIdComponentId(componentId);
		model.addAttribute("pcas", pcas);

		final Component component = componentRepository.findById(componentId).get();
		model.addAttribute("component", component);

		model.addAttribute("companyQties", component.getCompanyQties().stream().filter(cq->cq.getQty()>0).toArray(CompanyQty[]::new));

		return "modal/move_to_assembly :: moveToAssembly";
	}
}
