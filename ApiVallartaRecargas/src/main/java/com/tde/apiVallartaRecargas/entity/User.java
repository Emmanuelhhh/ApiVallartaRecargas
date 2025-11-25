package com.tde.apiVallartaRecargas.entity;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "ope_user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "user", nullable = false, unique = true)	
	private String  user;
	
	@Column(name= "password", nullable = false)
	private byte[] password;
	
	
}
