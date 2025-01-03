package com.HospitalAppointmentScheduling.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.HospitalAppointmentScheduling.DTO.AppointmentDTO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class ExportService {

	// Export to PDF
	public ByteArrayInputStream exportToPDF(List<AppointmentDTO> appointments) {
		Document docs = new Document();
		ByteArrayOutputStream docsOutput = new ByteArrayOutputStream();

		try {
			PdfWriter.getInstance(docs, docsOutput);
			docs.open();
			docs.add(new Paragraph("Appointment Report"));
			for (AppointmentDTO obj : appointments) {
				docs.add(new Paragraph("ID: " + obj.getAppointmentID() + ", Date: " + obj.getAppointmentDate()
						+ ", Reason: " + obj.getReason() + ", Doctor: " + obj.getDoctor().getFirstName() + " "
						+ obj.getDoctor().getLastName()));
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			docs.close();
		}
		return new ByteArrayInputStream(docsOutput.toByteArray());
	}

	// Export to CSV
	public ByteArrayInputStream exportToCSV(List<AppointmentDTO> appointments) {
		StringBuilder csv = new StringBuilder("Appointment ID, Appointment Date, Reason, Doctor Name\n");
		for (AppointmentDTO obj : appointments) {
			csv.append(obj.getAppointmentID()).append(", ").append(obj.getAppointmentDate()).append(", ")
					.append(obj.getReason()).append(", ")
					.append(obj.getDoctor().getFirstName() + " " + obj.getDoctor().getLastName()).append("\n");
		}
		return new ByteArrayInputStream(csv.toString().getBytes());
	}

	// Export to Excel
	public ByteArrayInputStream exportToExcel(List<AppointmentDTO> appointments) throws IOException {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet("Appointment Details");

			// Header row
			Row headerRow = sheet.createRow(0);
			String[] headers = { "Appointment ID", "Appointment Date", "Reason", "Doctor Name" };
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
			}

			// Data rows
			int rowIdx = 1;
			for (AppointmentDTO obj : appointments) {
				Row row = sheet.createRow(rowIdx++);
				row.createCell(0).setCellValue(obj.getAppointmentID());
				row.createCell(1).setCellValue(obj.getAppointmentDate().toString());
				row.createCell(2).setCellValue(obj.getReason());
				row.createCell(3).setCellValue(obj.getDoctor().getFirstName() + " " + obj.getDoctor().getLastName());
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}
}
