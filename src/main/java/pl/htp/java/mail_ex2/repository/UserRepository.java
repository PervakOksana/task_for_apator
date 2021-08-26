package pl.htp.java.mail_ex2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.htp.java.mail_ex2.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
    List<User> findByUserName(String userName);

}
