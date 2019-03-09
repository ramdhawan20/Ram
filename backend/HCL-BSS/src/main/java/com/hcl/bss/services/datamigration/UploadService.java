package com.hcl.bss.services.datamigration;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.hcl.bss.domain.ErrorCsvFile;
import com.hcl.bss.dto.FileUploadResponse;

public interface UploadService {

	FileUploadResponse csvFileUpload(MultipartFile file) throws IOException, ParseException;

	

	String downloadCsv(HttpServletResponse response, String fileName) throws IOException;

	String createSampleCsv();

}
