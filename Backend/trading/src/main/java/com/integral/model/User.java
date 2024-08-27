package com.integral.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.integral.domain.USER_ROLE;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data // for getter setter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "fullname")
	private String fullName;
	private String email;
	// only write able whenever fetch user from client side
   // password will not come there it will be ignore
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) 
	private String password;
	// @Embedded annotation ka use hota hai jab aap kisi entity mein kisi doosre
	// class ke fields ko directly usi entity ke table mein columns ke roop mein map
	// karna chahte ho. Matlab, TwoFactorAuth class ke fields, jaise ki enabled aur
	// code, entity ke table mein alag columns ke taur par save honge, bina alag
	// table banaye.
	@Embedded
	private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
	private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;

}
