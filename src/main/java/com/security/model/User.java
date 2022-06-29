package com.security.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@NoArgsConstructor
public class User {
	
	@Builder
    public User(String email, String username, String password, String role, String delyn, String provider,
			String providerId, Timestamp createDate) {
		this.email = email;
		this.username = username;
		this.password = password;
		this.role = role;
		this.delyn = delyn;
		this.provider = provider;
		this.providerId = providerId;
		this.createDate = createDate;
	}

	@Id // primary key
    @GeneratedValue( strategy = GenerationType.IDENTITY )
     private int id;
    private String email;
    private String username;
    private String password;
    private String role; // ROLE_USER., ROLE_ADMIN
    private String delyn;
    
    private String provider;    // 
    private String providerId; // 구글 로그인시 
    
    @CreationTimestamp
    private Timestamp createDate;
    
}
