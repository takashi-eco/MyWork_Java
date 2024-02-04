package com.example.app.domain;

import com.example.app.validation.LoginGroup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class User {

	private Integer id;

	private Integer role;
	
	private String roleName;
	
	@NotBlank(groups={LoginGroup.class})
	@Size(max = 100)
	@Email
	private String loginId;
	
	@NotBlank(groups={LoginGroup.class})
	@Size(min=6, max=16)
	private String loginPass;
	
	@NotBlank
	@Size(max = 10)
	private String name;
	
	@Size(min=7, max=7)
	private String zipcode;
	
	@Size(max=255)
	private String address;
	

}
