package com.hcl.bss.services.data.migration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hcl.bss.dao.ProductDAO;
import com.hcl.bss.data.migration.parsers.CSVDataMigrationParser;
import com.hcl.bss.domain.ErrorCsvFile;
import com.hcl.bss.dto.ErrorCsvFileDto;
import com.hcl.bss.dto.FileUploadResponse;
import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.dto.ProductErrorLogDetails;
import com.hcl.bss.dto.ProductUploadDetails;
import com.hcl.bss.fieldValidator.DataMigrationFieldValidator;
import static com.hcl.bss.constants.ApplicationConstants.STATUS_FAIL;
import static com.hcl.bss.constants.ApplicationConstants.STATUS_PARTIAL_SUCCESS;
import static com.hcl.bss.constants.ApplicationConstants.STATUS_SUCCESS;

@Service
public class UploadServiceImpl implements UploadService {
	//@Autowired
	//XLSXDataMigrationParser xLSXDataMigrationParser;
	@Autowired
	CSVDataMigrationParser cSVDataMigrationParser;
	@Autowired
	DataMigrationFieldValidator dataMigrationFieldValidator;
	@Autowired
	ProductDAO productDAO;

	private static String UPLOADED_FOLDER = "C://Users//Public//temp//";
	// Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";

	// CSV file header
	private static final String FILE_HEADER = "ROW_NO,ERROR";

	/*
	 * @Override public List<ProductDto> fileUpload(MultipartFile file) throws
	 * IOException, ParseException { List<ProductDto> listProduct = new
	 * ArrayList<>(); ProdUploadDTO errormessages = new ProdUploadDTO();
	 * 
	 * TO DO CHECK FILE TYPE FIRST BEFORE PROCEEDING
	 * 
	 * 
	 * String path = UPLOADED_FOLDER+file.getOriginalFilename(); File
	 * tempConvertFile = new File(path);
	 * System.out.println(file.getOriginalFilename());
	 * tempConvertFile.createNewFile(); FileOutputStream fout = new
	 * FileOutputStream(tempConvertFile); fout.write(file.getBytes()); fout.close();
	 * 
	 * listProduct = xLSXDataMigrationParser.parseData(path);
	 * 
	 * errormessages = dataMigrationFieldValidator.validateFields(listProduct);
	 * if(null == errormessages) { listProduct =
	 * productDAO.saveProduct(listProduct); } return listProduct; }
	 */
	@Override
	public FileUploadResponse csvFileUpload(MultipartFile file) throws IOException, ParseException {

		List<ProductErrorLogDetails> errorLogDetailsList = new ArrayList<>();
		List<ProductDto> productList = new ArrayList<>();
		List<ProductDto> successListProduct = new ArrayList<>();
		ProductUploadDetails productUploadDetails = new ProductUploadDetails();

		String uploadFileName = file.getOriginalFilename();
		String path = UPLOADED_FOLDER + file.getOriginalFilename();
		
		String errorFileNameSuffix = new SimpleDateFormat("yyyy-MM-dd-HH_mm_ss").format(new Date());
		String errorFileNameFullPath = "C:/Users/Public/temp/error_log_" + errorFileNameSuffix + ".csv";
		String errorFileName = errorFileNameFullPath.substring(errorFileNameFullPath.lastIndexOf("/") + 1).trim();
		
		File tempConvertFile = new File(path);
		System.out.println(file.getOriginalFilename());
		tempConvertFile.createNewFile();

		FileOutputStream fout = new FileOutputStream(tempConvertFile);
		fout.write(file.getBytes());

		// TODO Catch ImportParseException
		productList = cSVDataMigrationParser.parseCsvData(path);

		//Retrieve error details and success product records 
		productUploadDetails = dataMigrationFieldValidator.validateFields(productList);
		
		//Save success products to DB
		successListProduct = productDAO.saveProduct(productUploadDetails.getValidProductList());
		
		//Retrieve Error log detail list
		errorLogDetailsList = productUploadDetails.getErrorLogDetailsList();
		
		//Write error details to csv
		writeErrorDetailsInCsv(errorFileNameFullPath, errorLogDetailsList);
		
		FileUploadResponse fileUploadResponse = prepareFileUploadResponse(errorLogDetailsList, successListProduct, productList, errorFileName, uploadFileName);

		return fileUploadResponse;
	}

	private FileUploadResponse prepareFileUploadResponse(List<ProductErrorLogDetails> errorLogDetailsList,
			List<ProductDto> successListProduct, List<ProductDto> productList, String errorFileName, String uploadFileName) {
		
		FileUploadResponse response = new FileUploadResponse();
		if (errorLogDetailsList.size() == 0) {
			response.setStatus(STATUS_SUCCESS);
		} else if (successListProduct.size() == 0) {
			response.setStatus(STATUS_FAIL);
		} else {
			response.setStatus(STATUS_PARTIAL_SUCCESS);
		}
		response.setNoOfSuccessRecords(successListProduct.size());
		
		int noOfFailRecords = productList.size() - successListProduct.size();
		response.setNoOfFailRecords(noOfFailRecords);
		response.setNoOfRecords(productList.size());

		String timeStamp = new SimpleDateFormat("dd-MM-yyyy.HH.mm").format(new Date());
		response.setDateAdded(timeStamp);
		response.setErrorLogFileName(errorFileName);
		response.setUploadFileName(uploadFileName);
		
		return response;
	}

	private void writeErrorDetailsInCsv(String errorFileNameFullPath, List<ProductErrorLogDetails> errors) {
		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(errorFileNameFullPath);

			// Write the CSV file header
			fileWriter.append(FILE_HEADER.toString());

			// Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);

			// Write a new student object list to the CSV file
			for (ProductErrorLogDetails err : errors) {
				fileWriter.append(String.valueOf(err.getRowNo()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(err.getErrorMsg()));
				fileWriter.append(NEW_LINE_SEPARATOR);
			}

			System.out.println("CSV file was created successfully !!!");

		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}
		}

	}

	@Override
	public ErrorCsvFile getFile(Long fileId) {
		// TODO Auto-generated method stub
		return null;
	}
}
