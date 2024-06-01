package com.pms.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.dao.ParkingDataHistoryDao;
import com.pms.dao.ParkingSlotDao;
import com.pms.helper.ParkingHelper;
import com.pms.model.ParkingDataHistory;
import com.pms.model.ParkingSlot;
import com.pms.repository.ParkingDataHistoryRepository;
import com.pms.repository.ParkingSlotRepository;
import com.pms.util.ParkingFare;

@Service
public class ParkingService {

	@Autowired
	ParkingSlotRepository parkingSlotRepository;
	@Autowired ParkingDataHistoryRepository dataHistoryRepository;
	 
	@Autowired
	ParkingHelper helper;
	
	public ParkingSlotDao parkVehicle(ParkingSlotDao parkingSlotDao) {
		ParkingSlot entityObj = helper.getParkingSlotfromDao(parkingSlotDao);
		if(entityObj.getEntryTime() == null)
			entityObj.setEntryTime(LocalDateTime.now());
		entityObj = parkingSlotRepository.save(entityObj);
		parkingSlotDao.setSlotId(entityObj.getSlotId());
		parkingSlotDao.setEntryTime(entityObj.getEntryTime());
		return parkingSlotDao;
		
	}
	
	public ParkingSlotDao unparkVehicle(ParkingSlotDao parkingSlotDao) {
		long diffInHours;
		ParkingSlot entityObj = helper.getParkingSlotfromDao(parkingSlotDao);
		if(entityObj.getExitTime() == null)
			entityObj.setExitTime(LocalDateTime.now());
		entityObj = parkingSlotRepository.save(entityObj);
		if(entityObj != null) {
			diffInHours = java.time.Duration.between(entityObj.getEntryTime(), entityObj.getExitTime()).toHours();
			if(diffInHours >= 9)
				entityObj.setFare((double)(diffInHours * ParkingFare.PerDayFare.getFare()));
			else 
				entityObj.setFare((double)(diffInHours * ParkingFare.PerHourFare.getFare()));
			entityObj = parkingSlotRepository.save(entityObj);
			ParkingDataHistory dataHistory = helper.getParkingHistoryFromParkingSlot(entityObj);
			dataHistoryRepository.save(dataHistory);
			parkingSlotDao.setEntryTime(entityObj.getEntryTime());
			parkingSlotDao.setExitTime(entityObj.getExitTime());
			parkingSlotDao.setFare(entityObj.getFare());
			entityObj = helper.unParkVehicle(entityObj);
			parkingSlotRepository.save(entityObj);
		}
		return parkingSlotDao;
		
	}
	
	public ParkingSlotDao checkSpecificParkingSlot(Long slotId) {
		Optional<ParkingSlot> parkingSlot = parkingSlotRepository.findById(slotId);
		if(parkingSlot.isPresent())
			return helper.getParkingSlotDaofromEntity(parkingSlot.get());
			else return null;
		
	}
	
	public boolean checkSpecificParkingSlotIsAvailable(Long slotId) {
		Optional<ParkingSlot> parkingSlot = parkingSlotRepository.findById(slotId);
		
		return parkingSlot.isPresent();
	}
	
	public List<ParkingDataHistoryDao> getAllParkingHistory() {
		List<ParkingDataHistory> parkingHistory = dataHistoryRepository.findAll();
		return helper.getParkingDataHistoryDao(parkingHistory);
	 }

	public boolean leaveVehicle(Long slotId) {
		parkingSlotRepository.deleteById(slotId);
		return  parkingSlotRepository.findById(slotId).isPresent();
	}
	 
}
