package com.integral.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class PaymentDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String accountHolderName;
	private String ifsc;
	private String bankName;
	private String accountNumber;
	@OneToOne
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private User user;

}
