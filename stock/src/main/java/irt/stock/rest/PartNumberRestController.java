package irt.stock.rest;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
//	private final static Logger logger = LogManager.getLogger();

	@Autowired private PartNumberRepository partNumberRepository;
	@Autowired private ComponentRepository componentRepository;

	@PostMapping
	public List<Component> postPartNambers(@RequestParam(name="desiredPN", required=false, defaultValue="")String desiredPN){

		String pn = desiredPN.toUpperCase().replaceAll("[^A-Z0-9_%]", "");
		return componentRepository.findDistinctByPartNumberContainingOrManufPartNumberContainingOrAlternativeComponentsAltMfrPartNumberContainingOrderByPartNumber(pn, desiredPN, pn);
	}

	@PostMapping("/limit15")
	public List<PartNumber> postPartNambersWithLimit15(@RequestParam(name="desiredPN", required=false, defaultValue="")String desiredPN){

		String pn = desiredPN.toUpperCase().replaceAll("[^A-Z0-9_%]", "");
		return partNumberRepository.findFirst15ByPartNumberContainingOrderByPartNumber(pn);
	}

	@PostMapping("/limit20")
	public List<PartNumber> postPartNambersWithLimit20(@RequestParam(name="desiredPN", required=false, defaultValue="")String desiredPN){

		String pn = desiredPN.toUpperCase().replaceAll("[^A-Z0-9_%]", "");
		return partNumberRepository.findFirst20ByPartNumberContainingOrderByPartNumber(pn);
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

	@PostMapping("testConnection")
	public Boolean testConnection(){
		return true;
	}

	@RequestMapping("new/revision")
	public Integer getNewRevision(@RequestParam String partNumber){

		String upperCase = partNumber.toUpperCase();
		final int length = upperCase.length();
		final int revIndex = length-3;

		if(upperCase.length()<3 || upperCase.charAt(revIndex)!='R')
			return null;

		OptionalInt max = partNumberRepository.findByPartNumberLikeOrderByPartNumber(upperCase.substring(0, revIndex) + '%').parallelStream()
		.map(PartNumber::getPartNumber)
		.map(pn->pn.substring(revIndex+1))
		.mapToInt(Integer::parseInt)
		.max();

		return max.orElse(0) + 1;
	}
}
