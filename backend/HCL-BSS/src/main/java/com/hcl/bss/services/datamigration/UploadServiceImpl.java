package com.hcl.bss.services.data.migration;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hcl.bss.dao.ProductDAO;
import com.hcl.bss.data.migration.parsers.CSVDataMigrationParser;
import com.hcl.bss.domain.ErrorCsvFile;
import com.hcl.bss.dto.ErrorCsvFileDto;
import com.hcl.bss.dto.FileUploadResponse;
import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.dto.ProductErrorLogDetails;
import com.hcl.bss.dto.ProductUploadDetails;
import com.hcl.bss.exceptions.ImportParseException;
import com.hcl.bss.validator.DataMigrationFieldValidator;

import static com.hcl.bss.constants.ApplicationConstants.BLANK;
import static com.hcl.bss.constants.ApplicationConstants.EXTERNAL_FILE_PATH;
import static com.hcl.bss.constants.ApplicationConstants.FILE_NOT_EXIST_MSG;
import static com.hcl.bss.constants.ApplicationConstants.STATUS_FAIL;
import static com.hcl.bss.constants.ApplicationConstants.STATUS_PARTIAL_SUCCESS;
import static com.hcl.bss.constants.ApplicationConstants.STATUS_SUCCESS;
import static com.hcl.bss.constants.ApplicationConstants.ERROR_FILE;
import static com.hcl.bss.constants.ApplicationConstants.ERROR_FILE_NAME_SUFFIX;
import static com.hcl.bss.constants.ApplicationConstants.CSV_EXTENTION;
import static com.hcl.bss.constants.ApplicationConstants.UPLOADED_FOLDER;
import static com.hcl.bss.constants.ApplicationConstants.NEW_LINE_SEPARATOR;
import static com.hcl.bss.constants.ApplicationConstants.FILE_HEADER;
import static com.hcl.bss.constants.ApplicationConstants.COMMA_DELIMITER;
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
		
		String errorFileNameSuffix = new SimpleDateFormat(ERROR_FILE_NAME_SUFFIX).format(new Date());
		String errorFileNameFullPath = ERROR_FILE + errorFileNameSuffix + CSV_EXTENTION;
		String errorFileName = errorFileNameFullPath.substring(errorFileNameFullPath.lastIndexOf("/") + 1).trim();
		
		File tempConvertFile = new File(path);
		System.out.println(file.getOriginalFilename());
		tempConvertFile.createNewFile();
		FileOutputStream fout = null;

		try {
			fout = new FileOutputStream(tempConvertFile);//TODO check its use
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
		}catch(ImportParseException e) {
			//TODO
		}catch(Exception e) {
			//TODO
		}finally {
			fout.close();
		}
		
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

	@Override
	public String downloadCsv(HttpServletResponse response,String fileName) throws IOException {
		String responseMessage = BLANK;
	
		File file = null;
		file = new File(EXTERNAL_FILE_PATH + fileName);
		System.out.println(EXTERNAL_FILE_PATH + fileName);
		
		//TODO Move this logic in Service

		if (!file.exists()) {
			String errorMessage = FILE_NOT_EXIST_MSG;
			System.out.println(errorMessage);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
			return errorMessage;
			
		}
		else {
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		if (mimeType == null) {
			System.out.println("mimetype is not detectable, will take csv as default");
			mimeType = "text/csv";
		}

		System.out.println("mimetype : " + mimeType);

		response.setContentType(mimeType);

		/*
		 * "Content-Disposition : inline" will show viewable types [like
		 * images/text/pdf/anything viewable by browser] right on browser while
		 * others(zip e.g) will be directly downloaded [may provide save as popup, based
		 * on your browser setting.]
		 */
		// response.setHeader("Content-Disposition", String.format("inline; filename=\""
		// + file.getName() +"\""));

		/*
		 * "Content-Disposition : attachment" will be directly download, may provide
		 * save as popup, based on your browser setting
		 */
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));

		response.setContentLength((int) file.length());
	

		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

		// Copy bytes from source to destination(outputstream in this example), closes
		// both streams.
		FileCopyUtils.copy(inputStream, response.getOutputStream());
		responseMessage = "Download completed";
	}
		return responseMessage;
}
}
