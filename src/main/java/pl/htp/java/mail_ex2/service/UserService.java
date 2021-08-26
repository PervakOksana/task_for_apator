package pl.htp.java.mail_ex2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.htp.java.mail_ex2.model.User;
import pl.htp.java.mail_ex2.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public void saveUser(User user) {
		userRepository.save(user);
	}

}
