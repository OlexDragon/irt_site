package irt.stock.web.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import irt.stock.data.jpa.beans.Component;
import irt.stock.data.jpa.repositories.ComponentRepository;
import irt.stock.data.jpa.repositories.PartNumberRepository;
import irt.stock.web.PartNumber;

@RestController
@RequestMapping("/pn")
public class PartNumberController {

	@Autowired private PartNumberRepository partNumberRepository;

	@Autowired private ComponentRepository componentRepository;

	@RequestMapping
	public List<? extends PartNumber> postPartNambers(@RequestParam(name="desiredPN", required=false, defaultValue="")String desiredPN){
		return partNumberRepository.findByPartNumberContaining(desiredPN);
	}

	@RequestMapping("details")
	public Optional<Component> postPartNamber(@RequestParam("pnId")Long pnId){
		return componentRepository.findById(pnId);
	}
}
