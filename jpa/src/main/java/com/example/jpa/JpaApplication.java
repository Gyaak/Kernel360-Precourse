package com.example.jpa;

import com.example.jpa.user.db.UserEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;

@SpringBootApplication
public class JpaApplication {

	public interface UserRepository extends JpaRepository<UserEntity, Long> {
	}

}
