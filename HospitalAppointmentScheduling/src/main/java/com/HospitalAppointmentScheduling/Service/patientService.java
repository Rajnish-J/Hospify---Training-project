package com.HospitalAppointmentScheduling.Service;

import java.time.LocalDate;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.HospitalAppointmentScheduling.BO.patientBO;
import com.HospitalAppointmentScheduling.CustomExceptions.AppointmentBookingDateException;
import com.HospitalAppointmentScheduling.CustomExceptions.AppointmentException;
import com.HospitalAppointmentScheduling.CustomExceptions.DateException;
import com.HospitalAppointmentScheduling.CustomExceptions.DateOfBirthException;
import com.HospitalAppointmentScheduling.CustomExceptions.EmailException;
import com.HospitalAppointmentScheduling.CustomExceptions.IdException;
import com.HospitalAppointmentScheduling.CustomExceptions.PasswordException;
import com.HospitalAppointmentScheduling.CustomExceptions.PhoneNumberException;
import com.HospitalAppointmentScheduling.CustomExceptions.patientException;
import com.HospitalAppointmentScheduling.DAO.PatientProjection;
import com.HospitalAppointmentScheduling.Entity.patientVO;
import com.HospitalAppointmentScheduling.Response.ResponseHandle;

import jakarta.transaction.Transactional;

@Service
public class patientService {
	@Autowired
	private patientBO patientBO;

	@Autowired
	private ResponseHandle response;

	Logger log = Logger.getLogger(patientService.class);

	// insert method
	@Transactional
	public ResponseHandle insertPatientDetails(patientVO vo)
			throws patientException, PhoneNumberException, EmailException, PasswordException, DateOfBirthException {
		log.info("Insert method triggered...");
		patientVO flag = patientBO.insertPatientDetails(vo);
		if (flag != null) {
			response.setSucessmessage("patient Details added successfully");
			response.setPatient(flag);
		} else {
			response.setFailuremessage("Error in fetching...");
		}
		return response;
	}

	// find by Id method:
	@Transactional
	public ResponseHandle fetchById(long id) throws IdException {
		log.info("Fetch by ID method triggered...");
		patientVO vo = patientBO.fetchByID(id);
		if (vo != null) {
			response.setPatient(vo);
			response.setId(vo.getPatientId());
			response.setSucessmessage("patient details fetched by Patient ID: ");
		} else {
			response.setFailuremessage("Error in fetching...");
		}
		return response;
	}

	// fetch all method:
	@Transactional
	public ResponseHandle fetchAll() {
		log.info("fetch all method triggered...");
		List<patientVO> list = patientBO.fetchAll();
		if (list.size() > 0) {
			response.setListpatient(list);
			response.setSucessmessage("Fetching all the patiend details is successfully executed");
		} else {
			response.setFailuremessage("Error in fetching...");
		}

		return response;
	}

	// update method
	@Transactional
	public ResponseHandle updatePatientDetails(long id) throws IdException {
		log.info("Update method triggered...");
		patientVO flag = patientBO.updatePatientDetails(id);
		if (flag != null) {
			response.setSucessmessage("updated the patient details successfully for the patient ID: ");
			response.setPatient(flag);
		} else {
			response.setFailuremessage("error in updating patient details");
		}

		return response;
	}

	// Associate method:
	@Transactional
	public ResponseHandle associate(patientVO vo) throws patientException, PhoneNumberException, EmailException,
			PasswordException, AppointmentException, AppointmentBookingDateException, DateOfBirthException {
		log.info("Asscoiate method triggered...");
		patientVO inserted = patientBO.Associate(vo);

		if (inserted != null) {
			response.setSucessmessage("Appointment added successfully");
			response.setPatient(inserted);
			response.setId(inserted.getPatientId());
		} else {
			response.setFailuremessage("Failed to add data");
		}

		return response;
	}

	// find by patient phone number:
	public ResponseHandle findbyphone(String ph) throws PhoneNumberException {
		log.info("Fetch patient details by phone number method triggered...");
		patientVO vo = patientBO.fetchbyPhoneNumber(ph);
		if (vo != null) {
			response.setPatient(vo);
			response.setSucessmessage("patient details fetched by phone number");
		} else {
			response.setFailuremessage("Error in fetching...");
		}
		return response;

	}

	// fetch by day appointments:
	public ResponseHandle findapptDay(LocalDate td) throws AppointmentException {
		log.info("Find the appointments by the Date method triggered...");
		List<patientVO> list = patientBO.fetchapptDay(td);
		if (list.size() > 0) {
			response.setListpatient(list);
			response.setSucessmessage("fetching the appoinment details within the day is successfully executed");
		} else {
			response.setFailuremessage("There is no appointments on the day...");
		}
		return response;
	}

	// fetch by more appointments
	public ResponseHandle findAppointmentsByNumber(long n) throws AppointmentException {
		log.info("Find the patients having more appointments compared to given number method triggered...");
		List<patientVO> list = patientBO.fetchappointByNumber(n);
		if (list.size() > 0) {
			response.setListpatient(list);
			response.setSucessmessage("fetching the appoinment details with the patient ID is successfully executed");
		} else {
			response.setFailuremessage("There are no patients having more than the number you provided...");
		}
		return response;
	}

	// fetch first name and last name:
	public ResponseHandle findName(long n) throws IdException {
		log.info("Fetching patient name by ID method triggered...");
		PatientProjection p = patientBO.findname(n);
		if (p != null) {
			response.setPro(p);
			response.setSucessmessage("fetching patient first name and last name is successfully executed");
		} else {
			response.setFailuremessage("Error in fetching...");
		}
		return response;
	}

	// Appointment by between two days:
	public ResponseHandle betweenTwoDOBpat(LocalDate sd, LocalDate ld) throws DateException {
		log.info(
				"Fetching the patient details by two date who all are having Date Of Birth between the two dates method triggered...");
		List<patientVO> list = patientBO.betweenTwoDOBpat(sd, ld);
		if (list.size() > 0) {
			response.setListpatient(list);
			response.setSucessmessage(
					"fetching patient details between the two dates with respect to DOB is successfully executed");
		} else {
			response.setFailuremessage("Error in fetching...");
		}
		return response;
	}

	// ascending order:
	public ResponseHandle acending() throws AppointmentException {
		log.info("Fetching the patient details in ascending order method triggered...");
		List<patientVO> list = patientBO.ascending();
		if (list.size() > 0) {
			response.setListpatient(list);
			response.setSucessmessage("fetching the paitent details in ascending order is successfully executed");
		} else {
			response.setFailuremessage("Error in fetching...");
		}
		return response;
	}
}
