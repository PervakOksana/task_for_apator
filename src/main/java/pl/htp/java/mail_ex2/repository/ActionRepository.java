package pl.htp.java.mail_ex2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.htp.java.mail_ex2.model.Action;

public interface ActionRepository extends JpaRepository<Action, Long> {
    
    List<Action> findAll();
}
