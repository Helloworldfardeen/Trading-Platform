package com.integral.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integral.model.WatchList;

public interface WatchListRepository extends JpaRepository<WatchList, Long> {
	WatchList findByUserId(Long userId);
}
