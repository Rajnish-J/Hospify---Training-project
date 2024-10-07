package com.HospitalAppointmentScheduling.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.HospitalAppointmentScheduling.Entity.CityVO;

@Repository
public interface CityRepo extends JpaRepository<CityVO, Integer> {

}