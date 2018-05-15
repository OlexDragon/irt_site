package irt.stock.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import irt.stock.data.jpa.beans.Component;
import irt.stock.data.jpa.beans.PartNumber;
import irt.stock.data.jpa.repositories.ComponentRepository;
import irt.stock.data.jpa.repositories.PartNumberRepository;

@RestController
@RequestMapping("/pn")
public class PartNumberRestController {

	@Autowired private PartNumberRepository partNumberRepository;
	@Autowired private ComponentRepository componentRepository;

	@RequestMapping
	public List<? extends PartNumber> postPartNambers(@RequestParam(name="desiredPN", required=false, defaultValue="")String desiredPN){

		desiredPN = desiredPN.toUpperCase().replaceAll("[^A-Z0-9_%]", "");
		return partNumberRepository.findByPartNumberContainingOrManufPartNumberContainingOrderByPartNumber(desiredPN, desiredPN);
	}

	@RequestMapping("like")
	public List<? extends PartNumber> postPartNambersLike(@RequestParam(name="desiredPN", required=false, defaultValue="")String desiredPN){

		desiredPN = desiredPN.toUpperCase().replaceAll("[^A-Z0-9_%]", "");
		return partNumberRepository.findByPartNumberLikeOrderByPartNumber(desiredPN);
	}

	@RequestMapping("details")
	public Optional<Component> postPartNamber(@RequestParam("pnId")Long pnId){
		return componentRepository
				.findById(pnId)
				.map(c->{
					final String partNumber = c.getPartNumber();

					if(c.getManufPartNumber()==null && partNumber.startsWith("M")) {
						c.setManufPartNumber(partNumber);

						if(c.setManufPartNumber(partNumber)) 
							return componentRepository.save(c);
					}

					return c;
				});
	}
}
