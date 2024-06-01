package com.pms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pms.dao.ParkingDataHistoryDao;
import com.pms.dao.ParkingSlotDao;
import com.pms.service.ParkingService;


@RestController
@RequestMapping("/carparking/slot/")
public class CarParkingController {
	
	@Autowired
	ParkingService parkingService;
	
	@GetMapping("info/{slotId}")
	@ResponseBody ParkingSlotDao specificParkingSlotInfo(@PathVariable Long slotId) {
		return parkingService.checkSpecificParkingSlot(slotId);
	}
	
	@GetMapping("check/{slotId}")
	String specificParkingSlotAvailablityCheck(@PathVariable Long slotId) {
		boolean isAvailable = parkingService.checkSpecificParkingSlotIsAvailable(slotId);
		if(isAvailable) 
			return( "The Parking slot "+slotId+" is not Available"); 
		else 
			return ("The Parking slot "+slotId+" is Available") ;
	}
	
	@PostMapping("parking")
	@ResponseBody ParkingSlotDao parkVehicle(@RequestBody ParkingSlotDao parkingSlotDao) {
		return parkingService.parkVehicle(parkingSlotDao);
		
	}
	
	@DeleteMapping("leave/{slotId}")
	String leaveVehicle(@PathVariable Long slotId) {
		boolean status =  parkingService.leaveVehicle(slotId);
		
		if(!status) 
			return("The Parking slot "+slotId+" is removed successfully"); 
		else 
			return ("Something problem with parking slot "+slotId) ;
		
	}
	
	@GetMapping("parking/history")
	@ResponseBody List<ParkingDataHistoryDao> getParkingSlotHistory() {
		return parkingService.getAllParkingHistory();
	}
	
	
}
