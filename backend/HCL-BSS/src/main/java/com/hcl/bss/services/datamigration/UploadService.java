package com.hcl.bss.services.data.migration;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hcl.bss.domain.ErrorCsvFile;
import com.hcl.bss.dto.FileUploadResponse;

@Service
public interface UploadService {

	FileUploadResponse csvFileUpload(MultipartFile file) throws IOException, ParseException;

	ErrorCsvFile getFile(Long fileId);

	String downloadCsv(HttpServletResponse response, String fileName) throws IOException;

}
