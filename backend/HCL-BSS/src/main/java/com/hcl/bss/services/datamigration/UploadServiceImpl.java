package com.hcl.bss.services.datamigration;

import static com.hcl.bss.constants.ApplicationConstants.BLANK;
import static com.hcl.bss.constants.ApplicationConstants.CSV_EXTENTION;
import static com.hcl.bss.constants.ApplicationConstants.DATE_FORMAT_DD_MM_YYYY;
import static com.hcl.bss.constants.ApplicationConstants.DD_MM_YYYY;
import static com.hcl.bss.constants.ApplicationConstants.DD_MM_YYYY_HH_MM;
import static com.hcl.bss.constants.ApplicationConstants.ERROR;
import static com.hcl.bss.constants.ApplicationConstants.ERROR_FILE;
import static com.hcl.bss.constants.ApplicationConstants.ERROR_FILE_NAME_SUFFIX;
import static com.hcl.bss.constants.ApplicationConstants.EXTERNAL_FILE_PATH;
import static com.hcl.bss.constants.ApplicationConstants.FILE_NOT_EXIST_MSG;
import static com.hcl.bss.constants.ApplicationConstants.LINE_SEPERATOR;
import static com.hcl.bss.constants.ApplicationConstants.NEW_LINE_SEPARATOR;
import static com.hcl.bss.constants.ApplicationConstants.ROW_NO;
import static com.hcl.bss.constants.ApplicationConstants.STATUS_FAIL;
import static com.hcl.bss.constants.ApplicationConstants.STATUS_PARTIAL_SUCCESS;
import static com.hcl.bss.constants.ApplicationConstants.STATUS_SUCCESS;
import static com.hcl.bss.constants.ApplicationConstants.TAB_SPACE;
import static com.hcl.bss.constants.ApplicationConstants.TEXT_EXTENTION;
import static com.hcl.bss.constants.ApplicationConstants.UPLOADED_FOLDER;
import static com.hcl.bss.constants.ApplicationConstants.UTF_8;

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
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.collections4.IterableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hcl.bss.datamigration.parsers.CSVDataMigrationParser;
import com.hcl.bss.domain.ErrorCsvFile;
import com.hcl.bss.domain.Product;
import com.hcl.bss.domain.ProductTypeMaster;
import com.hcl.bss.dto.FileUploadResponse;
import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.dto.ProductErrorLogDetails;
import com.hcl.bss.dto.ProductUploadDetails;
import com.hcl.bss.repository.ProductRepository;
import com.hcl.bss.validator.DataMigrationFieldValidator;
@Service
@Transactional
public class UploadServiceImpl implements UploadService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UploadServiceImpl.class);
	@Autowired
	CSVDataMigrationParser cSVDataMigrationParser;
	@Autowired
	DataMigrationFieldValidator dataMigrationFieldValidator;
	@Autowired
	ProductRepository productRepository;
	@Value("${sample.file.header}")
	 String sampleFileHeader;
	@Value("${sample.file.validations}")
	 String sampleFileValidations;
	
	
	@Override
	public FileUploadResponse csvFileUpload(MultipartFile file) throws IOException, ParseException{
		Set<String> skuSetDB = new HashSet<>();
		List<Product> productEntityList  = null;
		List<ProductErrorLogDetails> errorLogDetailsList = new ArrayList<>();
		List<ProductDto> productList = new ArrayList<>();
		Iterable<Product> successListProduct = new ArrayList<>();
		ProductUploadDetails productUploadDetails = new ProductUploadDetails();

		String uploadFileName = file.getOriginalFilename();
		String path = UPLOADED_FOLDER + file.getOriginalFilename();
		
		String errorFileNameSuffix = new SimpleDateFormat(ERROR_FILE_NAME_SUFFIX).format(new Date());
		String errorFileNameFullPath = ERROR_FILE + errorFileNameSuffix + TEXT_EXTENTION;
		String errorFileName = errorFileNameFullPath.substring(errorFileNameFullPath.lastIndexOf("/") + 1).trim();
		
		File tempConvertFile = new File(path);
		LOGGER.info(file.getOriginalFilename());
		tempConvertFile.createNewFile();
		FileOutputStream fout = null;

		try {
			fout = new FileOutputStream(tempConvertFile);
			fout.write(file.getBytes());
	
			// TODO Catch ImportParseException
			productList = cSVDataMigrationParser.parseCsvData(path);
			skuSetDB = productRepository.getSkus();
			//Retrieve error details and success product records 
			productUploadDetails = dataMigrationFieldValidator.validateFields(productList,skuSetDB);
			
			// convert product dto to entity
			productEntityList = convertProductDTOListToEntityList(productUploadDetails.getValidProductList());
			
		try {
			successListProduct = productRepository.saveAll(productEntityList);
		}catch(DataIntegrityViolationException ex) {
			throw new DataIntegrityViolationException("Error: ",ex);
		}
	        
			
			//Retrieve Error log detail list
			errorLogDetailsList = productUploadDetails.getErrorLogDetailsList();
			
			
			
			//Write error details to csv only if some errors present.
			if(!errorLogDetailsList.isEmpty()) {
			writeErrorDetailsInText(errorFileNameFullPath, errorLogDetailsList);
			}
		}/*catch(ImportParseException e) {
			//TODO
		}catch(Exception e) {
			//TODO
		}*/finally {
			fout.close();
		}
		
		FileUploadResponse fileUploadResponse = prepareFileUploadResponse(errorLogDetailsList, successListProduct, productList, errorFileName, uploadFileName);

		return fileUploadResponse;
	}

	private List<Product> convertProductDTOListToEntityList(List<ProductDto> validProductList) {
		List<Product> productEntityList  = new ArrayList<>();
		Date date1 = null;
		Date date2 = null;
		for(ProductDto prod : validProductList) {
			Product product = new Product();
			ProductTypeMaster ptm = new ProductTypeMaster();
			ptm.setProductTypeCode(prod.getProductTypeCode());
			product.setProductTypeCode(ptm);
			product.setSku(prod.getSku());
			product.setProductDescription(prod.getProductDescription());
			product.setProductDispName(prod.getProductDispName());
			product.setCreatedDate(new Date());
			String expDate = prod.getProductExpDate();
			try {
			date1=new SimpleDateFormat(DD_MM_YYYY).parse(expDate); 
			product.setProductExpDate(date1);
			}
			catch(Exception e) {
				try {
					date2=new SimpleDateFormat(DATE_FORMAT_DD_MM_YYYY).parse(expDate); 
					product.setProductExpDate(date2);
				}
				catch(Exception ex) {
					LOGGER.error("ERROR :", ex);
				}
			}
			productEntityList.add(product);
		}
		
		return productEntityList;
	}

	private FileUploadResponse prepareFileUploadResponse(List<ProductErrorLogDetails> errorLogDetailsList,
			Iterable<Product> successListProduct, List<ProductDto> productList, String errorFileName, String uploadFileName) {
		
		//get itrable size 
		Integer successListProductSize  = IterableUtils.size(successListProduct);
		Integer noOfFailedRecords = productList.size()- successListProductSize;
		FileUploadResponse response = new FileUploadResponse();
		if ((noOfFailedRecords == 0) && (productList.size() == successListProductSize)) {
			response.setStatus(STATUS_SUCCESS);
		} else if ((0 == successListProductSize) && (noOfFailedRecords == productList.size())) {
			response.setStatus(STATUS_FAIL);
		}
		else if((0 != successListProductSize) &&  (0 != noOfFailedRecords) )
		{
			response.setStatus(STATUS_PARTIAL_SUCCESS);
		}
		else {
			response.setStatus(BLANK);
		}
		response.setNoOfSuccessRecords(successListProductSize);

		response.setNoOfFailRecords(noOfFailedRecords);
		response.setNoOfRecords(productList.size());

		String timeStamp = new SimpleDateFormat(DD_MM_YYYY_HH_MM).format(new Date());
		response.setDateAdded(timeStamp);
		if(!errorLogDetailsList.isEmpty()) {
		response.setErrorLogFileName(errorFileName);
		}
		response.setUploadFileName(uploadFileName);
		
		return response;
	}

	private void writeErrorDetailsInText(String errorFileNameFullPath, List<ProductErrorLogDetails> errors) {
		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(errorFileNameFullPath);

			/*// Write the CSV file header
			fileWriter.append(FILE_HEADER.toString());

			// Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);

			// Write a new student object list to the CSV file
			for (ProductErrorLogDetails err : errors) {
				fileWriter.append(String.valueOf(err.getRowNo()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(err.getErrorMsg()));
				fileWriter.append(NEW_LINE_SEPARATOR);
			}*/
			// Write the CSV file header
						

						// Add a new line separator after the header
						//fileWriter.append(NEW_LINE_SEPARATOR);
						// next line only for text else comment below line
					//	

						for (ProductErrorLogDetails err : errors) {
							fileWriter.append(ROW_NO);
							fileWriter.append(String.valueOf(err.getRowNo()));
							fileWriter.append(TAB_SPACE);
							fileWriter.append(ERROR);
							fileWriter.append(String.valueOf(err.getErrorMsg()));
							fileWriter.write(System.getProperty(LINE_SEPERATOR));
							
						}
						LOGGER.info("Text file was created successfully !!!");
			

		} catch (Exception e) {
			LOGGER.info("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				LOGGER.info("Error while flushing/closing fileWriter !!!");
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
			LOGGER.info(FILE_NOT_EXIST_MSG);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName(UTF_8)));
			outputStream.close();
			return errorMessage;
			
		}
		else {
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		if (mimeType == null) {
			mimeType = "text/csv";
		}


		response.setContentType(mimeType);

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

	@Override
	public String downloadSampleCsv(String fileName) {
		String sampleFileNameFullPath = UPLOADED_FOLDER + fileName + CSV_EXTENTION;
		String response = BLANK;
		response = sampleCsvData(sampleFileNameFullPath);
		return response;
	}

	private String sampleCsvData(String sampleFileNameFullPath) {
		String response = BLANK;
		FileWriter fileWriter = null;
		

		try {
			fileWriter = new FileWriter(sampleFileNameFullPath,false);

			// Write the CSV file header
			fileWriter.append(sampleFileHeader);

			// Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);
			fileWriter.append(sampleFileValidations);
			
			response = "SampleCSV Created";
			LOGGER.info("CSV file was created successfully !!!");
			

		} catch (Exception e) {
			LOGGER.info("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				LOGGER.info("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}
		}
		return response;
	}
}
