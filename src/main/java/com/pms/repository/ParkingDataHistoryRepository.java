package com.pms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.model.ParkingDataHistory;


public interface ParkingDataHistoryRepository extends JpaRepository<ParkingDataHistory, Long>{

}
