package irt.web.controllers.managent;

import irt.web.entities.all.UsersEntity;
import irt.web.entities.all.UsersEntity.Permission;
import irt.web.entities.all.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("management/users")
public class UsersController {

	private final Logger logger = LogManager.getLogger();

	@Autowired
	private UserRepository userRepository;

	@ModelAttribute("users")
	public List<UsersEntity>  attrUsers(){
		return userRepository.findAll();
	}

	@PreAuthorize("hasRole('USER')")
	@RequestMapping({"","permissions"})
	public String users() {
		logger.entry();

		return "management/users";
	}

	@PreAuthorize("hasRole('USER_EDIT')")
	@RequestMapping(value="permissions", method=RequestMethod.POST)
	public String usersPermissions(@RequestParam Map<String, Boolean> params, @RequestParam("id") long userId) {

		UsersEntity usersEntity = userRepository.findOne(userId);
		logger.entry(userId, params, usersEntity);

		if(usersEntity!=null){
			List<Permission> permissions = new ArrayList<>();
			for(String s:params.keySet()){
				if(!s.equals("id")){
					try{
						Permission p = Permission.valueOf(s);
						permissions.add(p);
					}catch(IllegalArgumentException e){
						logger.catching(e);
					}
				}
			}
			long newPermissions = 0;
			for(Permission p:permissions)
				newPermissions |= p.toLong();

			if(!usersEntity.getPermission().equals(newPermissions)){
				usersEntity.setPermission(newPermissions);
				userRepository.save(usersEntity);
				logger.info("\n\tpermissions for {} has been chenged to {}", usersEntity.getUsername(), permissions);
			}
		}

		return "management/users";
	}
}
