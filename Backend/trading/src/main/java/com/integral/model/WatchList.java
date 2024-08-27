package com.integral.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class WatchList {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long Id;
	@OneToOne
	private User user;
	@ManyToMany
	private List<Coin> coins = new ArrayList<>();

}
