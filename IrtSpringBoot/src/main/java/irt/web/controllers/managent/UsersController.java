package irt.web.controllers.managent;

import irt.web.entities.all.repository.UserRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("management/users")
public class UsersController {

	private final Logger logger = LogManager.getLogger();

	@Autowired
	private UserRepository userRepository;

	@PreAuthorize("hasRole('USER')")
	@RequestMapping
	public String users(Model model) {
		logger.entry();

		model.addAttribute("users", userRepository.findAll());

		return "management/users";
	}
}
