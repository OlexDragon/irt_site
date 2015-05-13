package irt.web.workers;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;

public class IrtPasswordEncoder implements PasswordEncoder{

	private static final Logger logger = LogManager.getLogger();

	@Override
	public String encode(CharSequence rawPassword) {
		logger.entry(rawPassword);
		String encode;

		if(rawPassword==null || (encode = rawPassword.toString()).equals("?"))
			encode = "?";
		else
			encode = Base64.encodeBase64String(encode.getBytes());

		return logger.exit(encode);
	}

	@Override
	public boolean matches(CharSequence rawPassword,  String encodedPassword) {
		logger.entry(rawPassword, encodedPassword );
		try {

			boolean matches = false;
			if(rawPassword!=null){
				encodedPassword = decrypt(encodedPassword);
				if(rawPassword.toString().trim().equals(encodedPassword))
					matches = true;
			}

			return logger.exit(matches);

		} catch (GeneralSecurityException | IOException e) {
			logger.catching(e);
			return false;
		}
	}

    private static String decrypt(String encodedPassword) throws GeneralSecurityException, IOException { 
    	logger.entry(encodedPassword);
        return encodedPassword==null || encodedPassword.equals("?")
        		? "?"
        				:  new String(Base64.decodeBase64(encodedPassword)); 
    } 
}
