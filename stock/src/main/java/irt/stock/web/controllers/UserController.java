package irt.stock.web.controllers;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import irt.stock.data.jpa.beans.User;
import irt.stock.data.jpa.repositories.UserRepository;
import irt.stock.data.jpa.services.UserRoles;

@Controller
@RequestMapping("user")
public class UserController {
//	private final static Logger logger = LogManager.getLogger();

	@Autowired private UserRepository userRepository;

	@RequestMapping
	public String userList(Model model){
		final Iterable<User> findAll = userRepository.findAllByOrderByStatusDescFirstnameAsc();
		model.addAttribute("users", findAll);
		return "user";
	}

	@RequestMapping("edit")
	public String editUser(@RequestParam(required=false) Long userId, Model model){

		final UserRoles[] roles = UserRoles.values();
		Arrays.sort(roles, (a, b)->a.name().compareTo(b.name()));
		model.addAttribute( "roles", roles);

		Optional.ofNullable(userId).flatMap(userRepository::findById).ifPresent(u->model.addAttribute( "user", u));

		return "modal/user_edit :: edit_user";
	}
}
