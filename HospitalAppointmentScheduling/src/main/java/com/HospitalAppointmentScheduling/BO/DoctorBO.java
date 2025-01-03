package com.HospitalAppointmentScheduling.BO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.HospitalAppointmentScheduling.CustomExceptions.IdException;
import com.HospitalAppointmentScheduling.DAO.DoctorDetailsProjection;
import com.HospitalAppointmentScheduling.DAO.DoctorRepo;

@Component
public class DoctorBO {
	@Autowired
	DoctorRepo doctorRepo;

	public List<DoctorDetailsProjection> fetch() {
		List<DoctorDetailsProjection> list = doctorRepo.findDoctorDetailsWithSpecialization();
		return list;
	}

	// validations

	// check ID present in the DB
	public boolean validateDocID(Long id) throws IdException {
		boolean flag = false;
		List<Long> dID = doctorRepo.fetchDoctorId();
		for (Long obj : dID) {
			if (obj == id) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			throw new IdException("ERROR: Doctor ID not exist in the database");
		}
		if (id == null) {
			throw new IdException("ERROR: Doctor Id field could not be null");
		} else if (id <= 0) {
			throw new IdException("ERROR: Doctor ID could not be negative or zero");
		}

		return id != null && id > 0;
	}

}
