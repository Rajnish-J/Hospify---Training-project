package com.HospitalAppointmentScheduling.Controller;

//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.HospitalAppointmentScheduling.CustomExceptions.AppointmentException;
import com.HospitalAppointmentScheduling.CustomExceptions.DateOfBirthException;
import com.HospitalAppointmentScheduling.CustomExceptions.EmailException;
import com.HospitalAppointmentScheduling.CustomExceptions.IdException;
import com.HospitalAppointmentScheduling.CustomExceptions.PasswordException;
import com.HospitalAppointmentScheduling.CustomExceptions.PatientException;
import com.HospitalAppointmentScheduling.CustomExceptions.PhoneNumberException;
import com.HospitalAppointmentScheduling.CustomExceptions.genderException;
import com.HospitalAppointmentScheduling.DTO.AppointmentDTO;
import com.HospitalAppointmentScheduling.DTO.PatientDTO;
import com.HospitalAppointmentScheduling.Entity.AppointmentsVO;
import com.HospitalAppointmentScheduling.Entity.DoctorVO;
import com.HospitalAppointmentScheduling.Entity.PatientVO;
import com.HospitalAppointmentScheduling.Response.ResponseHandle;
import com.HospitalAppointmentScheduling.Service.PatientService;

@RestController
@RequestMapping("/patient")
public class PatientController {

	@Autowired
	private PatientService pservice;

	@Autowired
	private ResponseHandle res;

//	Logger log = Logger.getLogger(PatientController.class);

	// insert:
	@PostMapping("/insert")
	public ResponseEntity<?> insertPatient(@RequestBody PatientDTO dto) {
//		log.info("Patient chooses create account option...");

		// converting DTO to entity
		PatientVO vo = new PatientVO();
		vo.setFirstName(dto.getFirstName());
		vo.setLastName(dto.getLastName());
		vo.setPatientEmail(dto.getPatientEmail());
		vo.setPatientPassword(dto.getPatientPassword());
		vo.setPatientPhone(dto.getPatientPhone());
		vo.setDob(dto.getDob());
		vo.setGender(dto.getGender());
		String pass = "Patient details being processed for insertion: " + vo;
//		log.info(pass);

		try {
			res = pservice.insertPatientDetails(vo);
			dto.setUpdatedAt(res.getPatient().getUpdatedAt());
			dto.setCreatedAt(res.getPatient().getCreatedAt());
			dto.setPatientId(res.getPatient().getPatientId());
			String pass1 = "Patient details successfully inserted with ID: " + res.getPatient().getPatientId();
//			log.info(pass1);
			return ResponseEntity.ok("Patient Details successfully saved: " + res.getPatient().getPatientId());
		} catch (PatientException e) {
//			log.error("Patient Details details records not in the format", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (PhoneNumberException e) {
//			log.error("Phone number format is wrong", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (EmailException e) {
//			log.error("email format is not valid", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (PasswordException e) {
//			log.error("password format is not valid", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (DateOfBirthException e) {
//			log.error("Date of Birth format is not in the pattern", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (genderException e) {
//			log.error("Gender you entered is not valid", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	// fetch by id:
	@GetMapping("patientId/{id}")
	public ResponseEntity<?> findBypatientId(@PathVariable("id") long id) {
//		log.info("patient chooses fetch Details by their ID...");
		try {
			res = pservice.fetchById(id);
			String pass = "Patient details successfully fetched for ID: " + id;
//			log.info(pass);
			return ResponseEntity.ok(mapToDTO(res.getPatient()));
		} catch (IdException e) {
//			log.error("ID not found in the DateBase", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	// fetch all:
	@GetMapping("/fetchallPatient")
	public List<PatientDTO> fetchall() {
//		log.info("fetch all the details option...");
		res = pservice.fetchAll();
		List<PatientVO> list = res.getListPatient();
		List<PatientDTO> listd = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			PatientVO vo = list.get(i);
			PatientDTO getDto = mapToDTO(vo);
			listd.add(getDto);
		}
//		log.info("All patient details successfully fetched.");
		return listd;
	}

	// update method
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updatePatientDetails(@RequestBody PatientDTO dto, @PathVariable long id) {
//		log.info("patient chooses Update their information by their ID...");
		PatientVO vo = new PatientVO();

		vo.setFirstName(dto.getFirstName());
		vo.setLastName(dto.getLastName());
		vo.setDob(dto.getDob());
		vo.setPatientPhone(dto.getPatientPhone());
		vo.setPatientEmail(dto.getPatientEmail());
		vo.setPatientPassword(dto.getPatientPassword());
		vo.setGender(dto.getGender());
		try {
			res = pservice.updatePatientDetails(vo, id);
			String pass = "Patient details updated successfully for ID: " + id;
//			log.info(pass);
			return ResponseEntity.ok(mapToDTO(res.getPatient()));
		} catch (IdException e) {
//			log.error("ID not found in the DataBase", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (PatientException e) {
//			log.error("Patient does not having the valid inputs", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (PhoneNumberException e) {
//			log.error("Phone number format is wrong", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (EmailException e) {
//			log.error("email format is not valid", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (PasswordException e) {
//			log.error("password format is not valid", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (genderException e) {
//			log.error("Gender you entered is not valid", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// delete method
	// DELETE request to delete a patient by ID
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deletePatient(@PathVariable("id") Long id) {

		try {
			res = pservice.deletePatient(id);
			return ResponseEntity.ok(res.getSucessMessage());
		} catch (IdException e) {
//			log.error("Patient does not exists in the database", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// find by patient phone number:
	@GetMapping("/fetchByPhoneNumber/{ph}")
	public ResponseEntity<?> findbyphone(@PathVariable("ph") String ph) {
//		log.info("patient chooses fetching their details by their phone number...");
		try {
			res = pservice.findbyphone(ph);
			String pass = "Patient details successfully fetched by phone number: " + ph;
//			log.info(pass);
			return ResponseEntity.ok(mapToDTO(res.getPatient()));
		} catch (PhoneNumberException e) {
//			log.error("phone number exception caught", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	// fetch by day appointments:
	@GetMapping("/appointmentDate/{td}")
	public ResponseEntity<?> findapptDay(@PathVariable("td") LocalDate td) {
//		log.info("patient chooses to fetch the appointment details by the day...");
		try {
			res = pservice.findapptDay(td);
			String pass = "Appointments successfully fetched for the date: " + td;
//			log.info(pass);
		} catch (AppointmentException e) {
//			log.error("Appointment exception caught", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		List<PatientVO> list = res.getListPatient();
		List<PatientDTO> listd = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			PatientVO vo = list.get(i);
			PatientDTO getDto = mapToDTO(vo);
			listd.add(getDto);
		}

		return ResponseEntity.ok(listd);
	}

	// fetch first name and last name:
	@GetMapping("/findFirstandLastNamebyPatientId/{id}")
	public ResponseEntity<?> findName(@PathVariable("id") long n) {
//		log.info("patient chooses Find their First and Last names in the records...");
		try {
			res = pservice.findName(n);
			String pass = "First and last names successfully fetched for patient ID: " + n;
//			log.info(pass);
			return ResponseEntity
					.ok("First name: " + res.getPro().getFirstName() + " Second name: " + res.getPro().getLastName());
		} catch (IdException e) {
//			log.error("Id Exception", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// ascending order:
	@GetMapping("/AscendingOrder")
	public ResponseEntity<?> acending() {
//		log.info("patient chooses fetching all the patient details in ascending order...");
		try {
			res = pservice.ascending();
//			log.info("Patient details successfully fetched in ascending order");
			List<PatientVO> list = res.getListPatient();
			List<PatientDTO> listd = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				PatientVO vo = list.get(i);
				PatientDTO getDto = mapToDTO(vo);
				listd.add(getDto);
			}
			return ResponseEntity.ok(listd);
		} catch (AppointmentException e) {
//			log.error("Id Exception", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	// find Latest Phone Number Entry
	@GetMapping("/findMostCommonDOB")
	public ResponseEntity<?> findMostCommonDOB() {
//		log.info("patient chooses fetching the recent number added in the controller layer");
		res = pservice.findMostCommonDOB();
//		log.info("patient phone retrieved");
		if (res.getSucessMessage() != null) {
			return ResponseEntity.ok(res.getListOfDates());
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res.getFailureMessage());
		}
	}

	// find Total Patients Count
	@GetMapping("/findTotalPatientsCount")
	public ResponseEntity<?> findTotalPatientsCount() {
//		log.info("find the total number of patient records method triggered in the controller layer");
		res = pservice.findTotalPatientsCount();
//		log.info("total number of patient records fetched" + res.getId());
		if (res.getSucessMessage() != null) {
			return ResponseEntity.ok("Total count of patient records: " + res.getId());
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res.getFailureMessage());
		}
	}

	// own method: converts entity to DTO
	public static PatientDTO mapToDTO(PatientVO patientVO) {
		// Map PatientVO to PatientDTO
		PatientDTO dto = new PatientDTO();
		dto.setPatientId(patientVO.getPatientId());
		dto.setFirstName(patientVO.getFirstName());
		dto.setLastName(patientVO.getLastName());
		dto.setDob(patientVO.getDob());
		dto.setPatientPhone(patientVO.getPatientPhone());
		dto.setPatientEmail(patientVO.getPatientEmail());
		dto.setPatientPassword(patientVO.getPatientPassword());
		dto.setGender(patientVO.getGender());
		dto.setCreatedAt(patientVO.getCreatedAt());
		dto.setUpdatedAt(patientVO.getUpdatedAt());

		// Map Appointments
		List<AppointmentDTO> ListappointmentDTOs = new ArrayList<>();
		if (patientVO.getAppointments() != null) {
			for (AppointmentsVO appointment : patientVO.getAppointments()) {
				AppointmentDTO appointmentDTO = new AppointmentDTO();
				appointmentDTO.setAppointmentID(appointment.getAppointmentID());
				appointmentDTO.setAppointmentDate(appointment.getAppointmentDate());
				appointmentDTO.setReason(appointment.getReason());
				appointmentDTO.setCreatedAt(appointment.getCreatedAt());
				appointmentDTO.setUpdatedAt(appointment.getUpdatedAt());
				appointmentDTO.setDoctorID(appointment.getDoctor().getDoctorId());

				DoctorVO docVO = new DoctorVO();
				docVO.setDoctorId(appointment.getDoctor().getDoctorId());
				docVO.setFirstName(appointment.getDoctor().getFirstName());
				docVO.setLastName(appointment.getDoctor().getLastName());

				appointmentDTO.setDoctor(docVO);

				ListappointmentDTOs.add(appointmentDTO);
			}
		}
		dto.setAppointments(ListappointmentDTOs);

		return dto;
	}

}

// unused code:

//association method
//	@PostMapping("/associatePatientsWithAppointments")
//	public ResponseEntity<?> associate(@RequestBody PatientDoctorDTO dto) {
//		log.info("Patient chooses to create an account with booking appointments...");
//
//		// Create PatientVO object
//		PatientVO vo = new PatientVO();
//		vo.setFirstName(dto.getPatient().getFirstName());
//		vo.setLastName(dto.getPatient().getLastName());
//		vo.setPatientEmail(dto.getPatient().getPatientEmail());
//		vo.setPatientPassword(dto.getPatient().getPatientPassword());
//		vo.setPatientPhone(dto.getPatient().getPatientPhone());
//		vo.setDob(dto.getPatient().getDob());
//		vo.setGender(dto.getPatient().getGender());
//		String pass = "Patient details to be associated with appointments: " + vo;
//		log.info(pass);
//
//		List<AppointmentDTO> list = dto.getPatient().getAppointments();
//		List<AppointmentsVO> listvo = new ArrayList<>();
//		for (AppointmentDTO obj : dto.getPatient().getAppointments()) {
//			AppointmentsVO avo = new AppointmentsVO();
//			avo.setAppointmentDate(obj.getAppointmentDate());
//			avo.setReason(obj.getReason());
//
//			DoctorVO dVO = new DoctorVO();
//			dVO.setDoctorId(dto.getDoctor().getDoctorId());
//
//			AppointmentStatusVO asVO = new AppointmentStatusVO();
//			asVO.setStatusName("Pending");
//
//			avo.setDoctor(dVO);
//			avo.setStatus(asVO);
//			avo.setPatient(vo);
//			listvo.add(avo);
//		}
//		vo.setAppointments(listvo);
//		String pass1 = "Patient appointments successfully associated: " + listvo;
//		log.info(pass1);
//
//		try {
//			res = pservice.associate(vo);
//			log.info("Patient and appointments successfully saved.");
//			return ResponseEntity.ok("Patient Details and Appointments added successfully. Patient ID: "
//					+ res.getPatient().getPatientId());
//
//		} catch (PatientException e) {
//			log.error("Patient details are not in the correct format", e);
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//
//		} catch (PhoneNumberException e) {
//			log.error("Phone number format is incorrect", e);
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//
//		} catch (EmailException e) {
//			log.error("Email format is invalid", e);
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//
//		} catch (PasswordException e) {
//			log.error("Password format is invalid", e);
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//
//		} catch (AppointmentException e) {
//			log.error("At least one appointment must be booked", e);
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//
//		} catch (AppointmentBookingDateException e) {
//			log.error("Appointment booking date cannot be in the past", e);
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//
//		} catch (DateOfBirthException e) {
//			log.error("Date of birth cannot be in the future", e);
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//		} catch (ReasonException e) {
//			log.error("Reason Exception caught: ", e);
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//		} catch (genderException e) {
//			log.error("Gender you entered is not valid", e);
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//		}
//	}

//patient details by between two days:
//	@GetMapping("/patientDetailsAmongTwoDate/{sd}/{ld}")
//	public ResponseEntity<?> betweenTwoDOBpat(@PathVariable("sd") LocalDate sd, @PathVariable("ld") LocalDate ld) {
//		String pass = "Fetching patient details between two dates: " + sd + " and " + ld;
//		log.info(pass);
//		try {
//			res = pservice.betweenTwoDOBpat(sd, ld);
//			log.info("Patient details successfully fetched between two dates.");
//			List<PatientVO> list = res.getListPatient();
//			List<PatientDTO> listd = new ArrayList<>();
//			for (int i = 0; i < list.size(); i++) {
//				PatientVO vo = list.get(i);
//				PatientDTO getDto = mapToDTO(vo);
//				listd.add(getDto);
//			}
//			return ResponseEntity.ok(listd);
//		} catch (DateException e) {
//			log.error("Id Exception", e);
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//		}
//
//	}

//find Patient With Most Appointments
//	@GetMapping("/findPatientWithMostAppointments")
//	public ResponseEntity<?> findPatientWithMostAppointments() {
//		log.info("fetching the patient having more appointment triggered in the controller layer");
//		res = pservice.findPatientWithMostAppointments();
//		List<PatientVO> list = res.getListPatient();
//		List<PatientDTO> listd = new ArrayList<>();
//		for (int i = 0; i < list.size(); i++) {
//			PatientVO vo = list.get(i);
//			PatientDTO getDto = mapToDTO(vo);
//			listd.add(getDto);
//		}
//		log.info("patient with more appointments fetched successfully");
//		if (res.getSucessMessage() != null) {
//			return ResponseEntity.ok(listd);
//		} else {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res.getFailureMessage());
//		}
//	}