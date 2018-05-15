package irt.stock.web.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import irt.stock.data.jpa.beans.BomComponent;
import irt.stock.data.jpa.beans.PartNumber;
import irt.stock.data.jpa.repositories.BomRepository;
import irt.stock.data.jpa.repositories.ComponentRepository;
import irt.stock.data.jpa.repositories.PartNumberRepository;

@Controller
@RequestMapping("bom")
public class BomController {
	private final static Logger logger = LogManager.getLogger();

	@Autowired private PartNumberRepository partNumberRepository;
	@Autowired private ComponentRepository componentRepository;
	@Autowired private BomRepository bomRepository;

	@GetMapping
	public String bomHome(@CookieValue(name="desiredPCA", defaultValue="") String desiredPN, Model model){

		desiredPN = desiredPN.toUpperCase().replaceAll("[^A-Z0-9_%]", "");

		model.addAttribute("desiredPN", desiredPN);

		if(desiredPN.startsWith("PCA"))
			desiredPN = desiredPN + "%";
		else
			desiredPN = "PCA%" + desiredPN + "%";

		final List<? extends PartNumber> partNumbers = partNumberRepository.findByPartNumberLikeOrderByPartNumber(desiredPN);
		model.addAttribute("partNumbers", partNumbers);

		return "bom/bom";
	}

	@GetMapping("details/{componentId}")
	public String pcaDetails(@PathVariable Long componentId, Model model) {

		componentRepository.findById(componentId)
		.ifPresent(component->{
			
			logger.debug("componentId:{}; {}", componentId, component);

			model.addAttribute("component", component);

			final List<BomComponent> components = bomRepository.findByIdTopComponentId(componentId);
			Collections.sort(components, (c1,c2)->{

				int result = Optional
							.ofNullable(c1.getComponent().getSchematicLetter())
							.map(sl1->Optional
									.ofNullable(c2.getComponent().getSchematicLetter())
									.map(sl2->sl1.compareTo(sl2))
									.orElse(1))
							.orElse(-1);

				if(result!=0)
					return result;
	
				Pattern p = Pattern.compile("[ -]");  // insert your pattern here

				String reference1 = c1.getReference().getReferences();
				String reference2 = c2.getReference().getReferences();

	
				Matcher m = p.matcher(reference1);
				if (m.find()) {
					int position = m.start();
					reference1 = reference1.substring(0, position);
				}

	
				m = p.matcher(reference2);
				if (m.find()) {
					int position = m.start();
					reference2 = reference2.substring(0, position);
				}

				int i1 = Integer.parseInt(reference1);
				int i2 = Integer.parseInt(reference2);

				return Integer.compareUnsigned(i1, i2);
			});
			model.addAttribute("components", components);
		});

		return "bom/pca_details :: pcaDetails";
	}


	@GetMapping("modal/add_component/{componentId}")
	public String addComponentToBOM(@PathVariable Long componentId) {
		return "modal/bom_add_component :: add_component";
	}

	@GetMapping("modal/edit_references/{componentId}")
	public String editComponentReferences(@PathVariable Long componentId, Model model) {
		logger.entry(componentId);

		partNumberRepository.findById(componentId).ifPresent(component->model.addAttribute("component", component));

		return "modal/bom_edit_reference :: edit_reference";
	}

	@GetMapping("modal/delete_component/{componentId}")
	public String removeComponentFromBOM(@PathVariable Long componentId, Model model) {
		logger.entry(componentId);

		partNumberRepository.findById(componentId).ifPresent(component->model.addAttribute("component", component));

		return "modal/bom_remove_component :: remove_component";
	}

	@GetMapping("modal/replace_component/{componentId}")
	public String replaceComponent(@PathVariable Long componentId, Model model) {
		logger.entry(componentId);

		partNumberRepository.findById(componentId).ifPresent(component->model.addAttribute("component", component));

		return "modal/bom_replace_component :: replace_component";
	}

	@GetMapping("modal/delete_bom/{componentId}")
	public String removeBomFromBOM(@CookieValue Long bomComponentId, Model model) {
		logger.entry(bomComponentId);

		partNumberRepository.findById(bomComponentId).ifPresent(component->model.addAttribute("component", component));

		return "modal/bom_remove_bom :: remove_bom";
	}
}
