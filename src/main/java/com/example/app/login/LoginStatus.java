package com.example.app.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginStatus {

	private Integer id;
	private String name;
	private String loginId;
	private int authority;
}
