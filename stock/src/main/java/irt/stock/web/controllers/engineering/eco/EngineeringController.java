package irt.stock.web.controllers.engineering.eco;

import static irt.stock.data.jpa.beans.engineering.ecr.EcrSpecifications.hasStatus;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import irt.stock.data.jpa.beans.User;
import irt.stock.data.jpa.beans.engineering.eco.EcoRepository;
import irt.stock.data.jpa.beans.engineering.ecr.Ecr;
import irt.stock.data.jpa.beans.engineering.ecr.EcrCategory.Category;
import irt.stock.data.jpa.beans.engineering.ecr.EcrRepository;
import irt.stock.data.jpa.beans.engineering.ecr.EcrStatus.Status;
import irt.stock.data.jpa.repositories.UserRepository;
import irt.stock.data.jpa.services.UserRoles;

@Controller
@RequestMapping("engineering")
public class EngineeringController {
	private final static Logger logger = LogManager.getLogger();

	@Autowired private MultipartConfigElement multipartConfigElement;
	@Autowired private UserRepository userRepository;
	@Autowired private EcrRepository ecrRepository;
	@Autowired private EcoRepository ecoRepository;

	@ModelAttribute
	public void modelAttribute(Model model, HttpServletRequest request) throws MalformedURLException {
		
		URL url = new URL(request.getScheme(), request.getServerName(), request.getServerPort(), "");
		model.addAttribute("url", url);
	}

	@GetMapping
	public String eco() {
		return "engineering/eco/ecos";
	}

	@GetMapping("ecr/new")
	public String newECO( Model model) {

		Iterable<User> engineeringTop = userRepository.findByPermission(UserRoles.ENGINEERING_TOP.getPermission());
		model.addAttribute("engineeringTop", engineeringTop);

		model.addAttribute("maxSize", multipartConfigElement.getMaxFileSize());
		model.addAttribute("maxRequest", multipartConfigElement.getMaxRequestSize());

		Iterable<User> engineers = userRepository.findByPermission(UserRoles.ENGINEERING.getPermission());
		model.addAttribute("engineers", engineers);

		return "engineering/eco/new";
	}

	@GetMapping("ecr/{number}")
	public String getEcr(@PathVariable long number, Model model) throws MalformedURLException {

		ecrRepository.findById(number)
		.ifPresent(
				ecr->{
					List<Ecr> ecrs = new ArrayList<>();
					ecrs.add(ecr);
					model.addAttribute("ecrs", ecrs);

					Iterable<User> engineers = userRepository.findByPermission(UserRoles.ENGINEERING.getPermission());
					model.addAttribute("engineers", engineers);
				});

		return "engineering/eco/ecr";
	}

	@GetMapping("eco/{number}")
	public String getEco(@PathVariable long number, Model model) throws MalformedURLException {

		ecoRepository.findById(number)
		.ifPresent(
				eco->{
					model.addAttribute("eco", eco);
				});

		return "engineering/eco/eco";
	}

	// for AJAX
	@PostMapping("ecr/ajax") @Transactional
	public String getEcrs(int start, int size, @RequestParam(name = "status[]", required = false) Status[] status, Model model) {
//		logger.error("{}; {}; {};", start, size, status);

		List<Ecr> ecrs = ecrRepository.findAll(hasStatus(status), getPage(start, size)).getContent();
		int toRemove = start % size;
		if(toRemove>0)
			ecrs = ecrs.subList(toRemove, ecrs.size());
		model.addAttribute("ecrs", ecrs);

		Iterable<User> engineers = userRepository.findByPermission(UserRoles.ENGINEERING.getPermission());
		model.addAttribute("engineers", engineers);

		return "engineering/eco/ecr :: ecr_card";
	}

	@PostMapping("eco") @Transactional
	public String ecoSearch(Authentication principal, int start, int size, String search, @RequestParam(name = "categories[]", required = false) Category[] categories, Model model) {
		logger.error("{}; {}; {}; {}; {}", start, size, search, categories);

		getPage(start, size);

//		List<Eco> ecos = ecoRepository
//
//				.findAll(
//						hasStatus(option.getEcoStatus())
//						.and(hasNumber(search))
//						.and(hasCategories(categories))
//						.and(sentTo(user)),
//						pageable)
//				.getContent();
//
//		int toRemove = start % size;
//		if(toRemove>0)
//			ecos = ecos.subList(toRemove, ecos.size());
//
//		model.addAttribute("ecos", ecos);
		return "engineering/eco/ecos :: eco_card";
	}

	private PageRequest getPage(int start, int size) {
		// Prepare page to load
		int page = start/size;
		Sort sort = Sort.by(Direction.DESC, "number");
		return PageRequest.of(page, size, sort);
	}
}
