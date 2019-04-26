package com.hcl.bss.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.bss.domain.Product;
import com.hcl.bss.dto.CSVRecordDataDto;
import com.hcl.bss.dto.ProductDataDto;
import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.dto.StatusDto;
import com.hcl.bss.services.DownloadRecordService;

import io.swagger.annotations.ApiOperation;

@RestController
public class DownloadRecordController {

	@Autowired
	DownloadRecordService downloadRecordService;
	@GetMapping(path = "/download/downloadRecord", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public ResponseEntity<StatusDto> downloadErrorFile(HttpServletResponse response,
			@RequestParam(value="tabName", required = true) String tabName) throws IOException {
		StatusDto status = new StatusDto();
		String responseMessage = downloadRecordService.downloadRecordCsv(response, tabName);
		status.setMsg(responseMessage);
		return new ResponseEntity<StatusDto>(status, HttpStatus.OK);
	}
	@ApiOperation(value = "DownLoad Records Based on Search Criteria", response = Product.class)
	@PostMapping(value = "/download/downloadSearchRecords")
	public ResponseEntity<StatusDto> downloadSearchRecords(@RequestBody CSVRecordDataDto csvRecordData,HttpServletResponse response) {
		StatusDto status = new StatusDto();
		Pageable reqCount = null;
		try {
			String msg = downloadRecordService.downloadSearchRecords(csvRecordData,reqCount,response);
			status.setMsg(msg);
			return new ResponseEntity<StatusDto>(status, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<StatusDto>(status, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
}
