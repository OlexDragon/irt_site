package irt.stock.web.controllers;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import irt.stock.data.jpa.repositories.ComponentRepository;

@Controller
@RequestMapping("/component")
@Transactional
public class ComponentController {
//	private final static Logger logger = LogManager.getLogger();

	@Autowired private ComponentRepository componentRepository;

	@PostMapping("details/{componentId}")
	public String saveComponentQty(@PathVariable Long componentIdn, Model model) {
		model.addAttribute("component", componentRepository.findById(componentIdn));
		return null;
	}
}
