package com.hcl.bss.services;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Pageable;

import com.hcl.bss.dto.CSVRecordDataDto;
import com.hcl.bss.dto.ProductDto;

public interface DownloadRecordService {

	String downloadRecordCsv(HttpServletResponse response, String tabName) throws IOException;

	String downloadSearchRecords(CSVRecordDataDto csvRecordData, Pageable reqCount, HttpServletResponse response) throws IOException;


}
