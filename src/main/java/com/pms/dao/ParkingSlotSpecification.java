package com.pms.dao;

import org.springframework.data.jpa.domain.Specification;

import com.pms.model.ParkingSlot;


public class ParkingSlotSpecification {

	public static Specification<ParkingSlot> availableParkingSlot() {
		return (parkingslot, cq, cb) -> cb.equal(parkingslot.get("carNumber"), "");
	}
}
