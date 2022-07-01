package com.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.model.User;

// CRUD 함수를 들고 있음
// @Repository 가 없어도 Ioc가 됨 JpaRepository 상속

public interface UserRepository extends JpaRepository<User,Integer> {

	// findBy규칙 --> Username 문법
	// select * from user where email 1?
	// jpa query method 보면 됨
	public User findByEmail( String email );
}
