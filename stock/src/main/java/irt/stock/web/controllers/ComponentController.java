package irt.stock.web.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import irt.stock.data.jpa.beans.ComponentObsolete;
import irt.stock.data.jpa.beans.PartNumber;
import irt.stock.data.jpa.repositories.ComponentObsoleteRepository;
import irt.stock.data.jpa.repositories.ComponentRepository;
import irt.stock.data.jpa.repositories.PartNumberRepository;

@Controller
@RequestMapping("/component")
@Transactional
public class ComponentController {
//	private final static Logger logger = LogManager.getLogger();

	@Autowired private ComponentRepository componentRepository;
    @Autowired private PartNumberRepository partNumberRepository;
    @Autowired private ComponentObsoleteRepository componentObsoleteRepository;

	@PostMapping("details/{componentId}")
	public String saveComponentQty(@PathVariable Long componentIdn, Model model) {
		model.addAttribute("component", componentRepository.findById(componentIdn));
		return null;
	}

    @GetMapping(value={"/obsolete"})
    public String obsolete() {

    	partNumberRepository.findByPartNumberLikeOrderByPartNumber("%R__").stream().filter(pn -> pn.getComponentObsolete() == null)
    	.collect(
    			Collectors.groupingBy(
    					pn -> {
    						String partNumber = pn.getPartNumber();
    						return partNumber.substring(0, partNumber.length() - 3);
    					}))

    	.entrySet().stream().filter(es -> es.getValue().size() > 1)
        .forEach(
        		entrySet -> {

        			List<PartNumber> pns = entrySet.getValue();
        			pns.remove(pns.size() - 1);
        			pns.forEach(
        					pn -> {
        						ComponentObsolete toSave = Optional.ofNullable(pn.getComponentObsolete()).map(co -> co.setStatus(ComponentObsolete.Status.OBSOLETE)).orElse(new ComponentObsolete(pn.getId(), ComponentObsolete.Status.OBSOLETE));
        						ComponentObsolete saved = (ComponentObsolete)componentObsoleteRepository.save(toSave);
        						pn.setComponentObsolete(saved);
        					});
        		});

        return "redirect:/";
    }
}
