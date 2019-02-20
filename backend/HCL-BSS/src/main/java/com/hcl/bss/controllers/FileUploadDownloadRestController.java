package com.hcl.bss.controllers;

import static com.hcl.bss.constants.ApplicationConstants.EXTERNAL_FILE_PATH;
import static com.hcl.bss.constants.ApplicationConstants.BLANK;
import static com.hcl.bss.constants.ApplicationConstants.FILE_NOT_EXIST_MSG;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hcl.bss.dto.FileUploadResponse;
import com.hcl.bss.services.data.migration.UploadService;

@RestController
public class FileUploadDownloadRestController {
	

	@Autowired
	UploadService uploadService;

	@RequestMapping(value = "/uploadProductData", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

	public ResponseEntity<FileUploadResponse> uploadProductData(@RequestParam("file") MultipartFile file)
			throws IOException, ParseException {
		FileUploadResponse fileUploadResponse = new FileUploadResponse();
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		if ("csv".equalsIgnoreCase(extension)) {
			fileUploadResponse = uploadService.csvFileUpload(file);
		}
		return new ResponseEntity<FileUploadResponse>(fileUploadResponse, HttpStatus.OK);
	}

	@GetMapping(path = "/downloadErrorFile/{fileName}", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public ResponseEntity<String> downloadErrorFile(HttpServletResponse response, @PathVariable("fileName") String fileName)
			throws IOException {
		String responseMessage = BLANK;
		responseMessage = uploadService.downloadCsv(response,fileName);
		
		
		return new ResponseEntity<String>(responseMessage, HttpStatus.OK);
		}
	}

