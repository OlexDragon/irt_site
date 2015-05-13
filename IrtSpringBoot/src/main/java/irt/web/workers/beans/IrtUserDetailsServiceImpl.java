package irt.web.workers.beans;

import irt.web.entities.all.UsersEntity;
import irt.web.entities.all.repository.UserRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class IrtUserDetailsServiceImpl implements UserDetailsService {

	private final Logger logger = LogManager.getLogger();

	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.entry(username);
		UsersEntity userEntity = repository.findOneByUsername(username);
		if(userEntity==null)
			throw new UsernameNotFoundException("No user found with username: " + username);

		return logger.exit(LoginDetails
							.getBuilder()
							.setId(userEntity.getId())
							.setUsername(userEntity.getUsername())
							.setPassword(userEntity.getPassword())
							.setPermissions(userEntity.getPermission())
							.build());
	}

}
