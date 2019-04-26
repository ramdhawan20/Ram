package com.hcl.bss.controllers;

import static com.hcl.bss.constants.ApplicationConstants.BLANK;
import static com.hcl.bss.constants.ApplicationConstants.CSV_WRONG_DATA;
import static com.hcl.bss.constants.ApplicationConstants.INVALID_FILE_TYPE;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hcl.bss.dto.ErrorResponseDTO;
import com.hcl.bss.dto.FileUploadResponse;
import com.hcl.bss.exceptions.ImportParseException;
import com.hcl.bss.services.datamigration.UploadService;

@RestController
public class FileUploadDownloadRestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadDownloadRestController.class);

	@Autowired
	UploadService uploadService;

	@RequestMapping(value = "/upload/uploadProductData", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

	public ResponseEntity<FileUploadResponse> uploadProductData(@RequestParam("file") MultipartFile file)
			throws IOException, ParseException {
		FileUploadResponse fileUploadResponse = new FileUploadResponse();
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		if ("csv".equalsIgnoreCase(extension)) {
			try {
				fileUploadResponse = uploadService.csvFileUpload(file);
			} catch (ImportParseException ipe) {
				LOGGER.error("Error", new ImportParseException(CSV_WRONG_DATA));
				throw new ImportParseException(CSV_WRONG_DATA);

			}
		} else {
			LOGGER.error("Error", new ImportParseException(INVALID_FILE_TYPE));
			throw new ImportParseException(INVALID_FILE_TYPE);
		}
		return new ResponseEntity<FileUploadResponse>(fileUploadResponse, HttpStatus.OK);
	}

	
	
	@ExceptionHandler(ImportParseException.class)
	public ResponseEntity<ErrorResponseDTO> exceptionHandler(Exception ex) {
		ErrorResponseDTO error = new ErrorResponseDTO();
		error.setErrorCode(HttpStatus.PRECONDITION_FAILED.value());
		error.setMessage(ex.getMessage());
		return new ResponseEntity<ErrorResponseDTO>(error, HttpStatus.CONFLICT);
	}
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponseDTO> sqlexceptionHandler(DataIntegrityViolationException ex) {
		ErrorResponseDTO error = new ErrorResponseDTO();
             if(ex.getMessage().contains("constraint")) {
                 if (ex.getMessage().contains("SKU_UNIQUE")) {
                	 error.setErrorCode(1062);
                	 error.setMessage("Duplicate SKU");
                 } 
                 else {
                	 error.setErrorCode(0);
                	 error.setMessage("Database Issue");
                 }
             }
		return new ResponseEntity<ErrorResponseDTO>(error, HttpStatus.CONFLICT);
	}

	@GetMapping(path = "/upload/downloadFile/{fileName}", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public ResponseEntity<String> downloadErrorFile(HttpServletResponse response,
			@PathVariable("fileName") String fileName) throws IOException {
		String responseMessage = BLANK;
		responseMessage = uploadService.downloadCsv(response, fileName);
		return new ResponseEntity<String>(responseMessage, HttpStatus.OK);
	}

	/*@GetMapping(path = "/createSampleFile", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public ResponseEntity<String> createSampleFile() throws IOException {
		String responseMessage = BLANK;
		responseMessage = uploadService.createSampleCsv();
		return new ResponseEntity<String>(responseMessage, HttpStatus.OK);
	}*/
}
