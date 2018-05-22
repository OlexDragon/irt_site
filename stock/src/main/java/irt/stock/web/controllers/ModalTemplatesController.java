package irt.stock.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import irt.stock.data.jpa.beans.BomComponent;
import irt.stock.data.jpa.beans.Company.CompanyType;
import irt.stock.data.jpa.beans.CompanyQty;
import irt.stock.data.jpa.beans.Component;
import irt.stock.data.jpa.beans.PartNumber;
import irt.stock.data.jpa.repositories.BomRepository;
import irt.stock.data.jpa.repositories.CompanyRepository;
import irt.stock.data.jpa.repositories.ComponentRepository;

@Controller
@RequestMapping("modal")
public class ModalTemplatesController {

	@Autowired private CompanyRepository companyRepository;
	@Autowired private ComponentRepository componentRepository;
	@Autowired private BomRepository bomRepository;

	@GetMapping("to_co_mfr/{componentId}")
	public String moveToCoMfr(@PathVariable Long componentId, Model model){
		model.addAttribute("component", componentRepository.findById(componentId).get());
		model.addAttribute("coMfrs", companyRepository.findByType(CompanyType.CO_MANUFACTURER));
		return "modal/move_to_co_mfr :: moveToCoMfr";
	}

	@GetMapping("to_bulk/{componentId}")
	public String moveToBulk(@PathVariable Long componentId, Model model){

		componentRepository
		.findById(componentId)
		.ifPresent(component->model.addAttribute("component", component));

		return "modal/move_to_bulk :: moveToBulk";
	}

	@GetMapping("mfr_to_bulk/{componentId}")
	public String moveFromMfrToBulk(@PathVariable Long componentId, Model model){

		final Component component = componentRepository.findById(componentId).get();
		model.addAttribute("component", component);
		model.addAttribute("companyQties", component.getCompanyQties().stream().filter(cq->cq.getQty()>0).toArray(CompanyQty[]::new));
		return "modal/move_to_bulk :: moveMfrToBulk";
	}

	@GetMapping("to_stock/{componentId}")
	public String moveToStock(@PathVariable Long componentId, Model model){

		final Component component = componentRepository.findById(componentId).get();
		model.addAttribute("component", component);
		model.addAttribute("companyQties", component.getCompanyQties().stream().filter(cq->cq.getQty()>0).toArray(CompanyQty[]::new));
		return "modal/move_to_stock :: moveToStock";
	}

	@GetMapping("to_assembly/{componentId}")
	public String moveToAssembly(@PathVariable Long componentId, Model model){

		final List<PartNumber> pcas = bomRepository.findTopPartNumberByIdComponentId(componentId);
		model.addAttribute("pcas", pcas);

		final Component component = componentRepository.findById(componentId).get();
		model.addAttribute("component", component);

		model.addAttribute("companyQties", component.getCompanyQties().stream().filter(cq->cq.getQty()>0).toArray(CompanyQty[]::new));

		return "modal/move_to_assembly :: moveToAssembly";
	}

	@GetMapping("where_used/{componentId}")
	public String whereUsed(@PathVariable Long componentId, Model model){

		final List<BomComponent> bomComponents = bomRepository.findByIdComponentId(componentId);
		Component component = bomComponents.stream().findAny().map(BomComponent::getComponent).orElse(componentRepository.findById(componentId).orElse(null));

		model.addAttribute("component", component);
		model.addAttribute("bomComponents", bomComponents);

		return "modal/where_used :: whereUsed";
	}
}
