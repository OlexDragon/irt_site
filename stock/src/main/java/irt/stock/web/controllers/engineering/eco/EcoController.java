package irt.stock.web.controllers.engineering.eco;

import static irt.stock.data.jpa.beans.engineering.eco.EcoSpecifications.hasCategories;
import static irt.stock.data.jpa.beans.engineering.eco.EcoSpecifications.hasNumber;
import static irt.stock.data.jpa.beans.engineering.eco.EcoSpecifications.hasStatus;
import static irt.stock.data.jpa.beans.engineering.eco.EcoSpecifications.sentTo;

import java.util.List;

import javax.servlet.MultipartConfigElement;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import irt.stock.data.jpa.beans.User;
import irt.stock.data.jpa.beans.engineering.eco.Eco;
import irt.stock.data.jpa.beans.engineering.eco.EcoCategory;
import irt.stock.data.jpa.beans.engineering.eco.EcoOption;
import irt.stock.data.jpa.beans.engineering.eco.EcoRepository;
import irt.stock.data.jpa.repositories.UserRepository;
import irt.stock.data.jpa.services.UserPrincipal;
import irt.stock.data.jpa.services.UserRoles;

@Controller
@RequestMapping("engineering/eco")
public class EcoController {
	private final static Logger logger = LogManager.getLogger();

	@Autowired private MultipartConfigElement multipartConfigElement;
	@Autowired private UserRepository userRepository;
	@Autowired private EcoRepository ecoRepository;

	@GetMapping
	public String eco() {
		return "engineering/eco/ecos";
	}

	@GetMapping("new")
	public String newECO( Model model) {

		Iterable<User> engineeringTop = userRepository.findByPermission(UserRoles.ENGINEERING_TOP.getPermission());
		model.addAttribute("engineeringTop", engineeringTop);

		model.addAttribute("maxSize", multipartConfigElement.getMaxFileSize());
		model.addAttribute("maxRequest", multipartConfigElement.getMaxRequestSize());

		return "engineering/eco/new";
	}

	@PostMapping @Transactional
	public String ecoSearch(Authentication principal, int start, int size, EcoOption option, String search, @RequestParam(name = "categories[]", required = false) EcoCategory[] categories, Model model) {
		logger.error("{}; {}; {}; {}; {}", start, size, option, search, categories);

		User user = null;
		if(option == EcoOption.FOR_APPROVAL)
			user = ((UserPrincipal) principal.getPrincipal()).getUser();

		// Prepare page to load
		int page = start/size;
		Sort sort = Sort.by(Direction.DESC, "number");
		Pageable pageable = PageRequest.of(page, size, sort);

		List<Eco> ecos = ecoRepository

				.findAll(
						hasStatus(option.getEcoStatus())
						.and(hasNumber(search))
						.and(hasCategories(categories))
						.and(sentTo(user)),
						pageable)
				.getContent();

		int toRemove = start % size;
		if(toRemove>0)
			ecos = ecos.subList(toRemove, ecos.size());

		model.addAttribute("ecos", ecos);
		return "engineering/eco/ecos :: eco_card";
	}
}
