package com.hcl.bss.services;

import static com.hcl.bss.constants.ApplicationConstants.BLANK;
import static com.hcl.bss.constants.ApplicationConstants.DD_MM_YYYY;
import static com.hcl.bss.constants.ApplicationConstants.EXTERNAL_FILE_PATH;
import static com.hcl.bss.constants.ApplicationConstants.NEW_LINE_SEPARATOR;
import static com.hcl.bss.constants.ApplicationConstants.PRODUCT_DATA;
import static com.hcl.bss.constants.ApplicationConstants.USER_DATA;
import static com.hcl.bss.constants.ApplicationConstants.SUBSCRIPTION_DATA;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.hcl.bss.domain.Customer;
import com.hcl.bss.domain.Product;
import com.hcl.bss.domain.RatePlan;
import com.hcl.bss.domain.Subscription;
import com.hcl.bss.domain.SubscriptionRatePlan;
import com.hcl.bss.domain.User;
import com.hcl.bss.dto.CSVRecordDataDto;
import com.hcl.bss.dto.MultipleRatePlanDto;
import com.hcl.bss.dto.ProductDataDto;
import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.dto.RatePlanProductDto;
import com.hcl.bss.dto.SubscriptionDto;
import com.hcl.bss.dto.SubscriptionInOut;
import com.hcl.bss.repository.CustomerRepository;
import com.hcl.bss.repository.ProductRepository;
import com.hcl.bss.repository.SubscriptionRepository;
import com.hcl.bss.repository.UserRepository;
import com.hcl.bss.repository.specification.ProductSpecification;
@Service
@Transactional
public class DownloadRecordServiceImpl implements DownloadRecordService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadRecordServiceImpl.class);
	  private static final String CSV_SEPARATOR = ",";
	@Autowired
	ProductRepository productRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ProductServiceImpl productServiceImpl;
	@Autowired
	SubscriptionRepository subscriptionRepository;
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	SubscriptionServiceImpl subscriptionServiceImpl;
	@Value("${download.csv.product.header}")
	String downloadProductCsvHeader;
	@Value("${download.csv.user.header}")
	String downloadUserCsvHeader;
	@Value("${download.csv.subscription.header}")
	String downloadSubscriptionCsvHeader;
	
	@Override
	public String downloadRecordCsv(HttpServletResponse response, String tabName) throws IOException {
		String responseMessage = BLANK;
		String fileName = null;
		List<ProductDto> productDtoList = new ArrayList<ProductDto>();
		List<Product> productList = new ArrayList<Product>();
		List<User> userList = new ArrayList<User>();
		List<Subscription> subscriptionList = new ArrayList<Subscription>();
		List<SubscriptionDto> subscriptionDtoList = new ArrayList<SubscriptionDto>();
		if("PRODUCTLANDINGPAGE".equalsIgnoreCase(tabName)) {
			fileName = "PRODUCT_DATA.CSV";
			productList = productRepository.findAll();
		}
		else if("USERLANDINGPAGE".equalsIgnoreCase(tabName)) {
			fileName = "USER_DATA.CSV";
			userList = userRepository.findAll();
		}
		else if("SUBSCRIPTIONLANDINGPAGE".equalsIgnoreCase(tabName)) {
			fileName = "SUBSCRIPTION_DATA.CSV";
			subscriptionList = subscriptionRepository.findAll();
		}
			if(!productList.isEmpty()) {
			productDtoList = convertProductEntityToDto(productList);
			}
			else if(!subscriptionList.isEmpty()) {
				subscriptionDtoList = convertSUbscriptionEntityToDto(subscriptionList);
				}
		
			File file = null;
			file = new File(EXTERNAL_FILE_PATH + fileName);
			System.out.println(EXTERNAL_FILE_PATH + fileName);
			if(!productDtoList.isEmpty()) {
				writeToProductCSV(productDtoList);
			}
			else if(!userList.isEmpty()) {
				writeToUserCSV(userList);
			}
			else if(!subscriptionDtoList.isEmpty()) {
				writeToSubscriptionCSV(subscriptionDtoList);
			}
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				mimeType = "text/csv";
			}


			response.setContentType(mimeType);

			response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));

			response.setContentLength((int) file.length());
		

			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

			FileCopyUtils.copy(inputStream, response.getOutputStream());
			responseMessage = "Download completed";
			return responseMessage;
		
	}
	private void writeToSubscriptionCSV(List<SubscriptionDto> subscriptionDtoList) {
FileWriter fileWriter = null;
		

		try {
			fileWriter = new FileWriter(SUBSCRIPTION_DATA,false);

			// Write the CSV file header
			fileWriter.append(downloadSubscriptionCsvHeader);
			fileWriter.append(NEW_LINE_SEPARATOR);
			 for (SubscriptionDto subscription : subscriptionDtoList)
	            {
	                StringBuffer oneLine = new StringBuffer();
	                oneLine.append(subscription.getSubscriptionId());
	                oneLine.append(CSV_SEPARATOR);
	                oneLine.append(subscription.getCustomerName());
	                oneLine.append(CSV_SEPARATOR);
	                oneLine.append(subscription.getEmail()); 
	                oneLine.append(CSV_SEPARATOR);
	                if(null != subscription.getRatePlanList()) {
	                oneLine.append(subscription.getRatePlanList().get(0).getPlanName()); 
	                }else {
	                	oneLine.append(BLANK); 
	                }
	                oneLine.append(CSV_SEPARATOR);
	                oneLine.append(subscription.getStatus()); 
	                oneLine.append(CSV_SEPARATOR);
	                if(null != subscription.getRatePlanList()) {
	                oneLine.append(subscription.getRatePlanList().get(0).getPrice()); 
	                }else {
	                	  oneLine.append(BLANK); 
	                }
	                oneLine.append(CSV_SEPARATOR);
	                oneLine.append(subscription.getCreatedDate()); 
	                oneLine.append(CSV_SEPARATOR);
	                oneLine.append(subscription.getActivatedDate()); 
	                oneLine.append(CSV_SEPARATOR);
	                oneLine.append(subscription.getLastBillDate()); 
	                oneLine.append(CSV_SEPARATOR);
	                oneLine.append(subscription.getNextBillDate()); 
	                fileWriter.write(oneLine.toString());
	                fileWriter.append(NEW_LINE_SEPARATOR);
	            }
			
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
		
		
	}
	private List<SubscriptionDto> convertSUbscriptionEntityToDto(List<Subscription> subscriptionList) {
		LOGGER.info("<--------------Start convertSubscriptionsToDto() method in SubscriptionServiceImpl.java---------------------->");
		List<SubscriptionDto> subscriptionDtoList = new ArrayList<>();
		SubscriptionDto subscriptionDto = null;
		List <MultipleRatePlanDto> ratePlanList = null;
		MultipleRatePlanDto multipleRatePlanDto = null;
		DateFormat dateFormat = new SimpleDateFormat(DD_MM_YYYY);
		for(Subscription subscription : subscriptionList) {
			subscriptionDto = new SubscriptionDto();
			subscriptionDto.setSubscriptionId(subscription.getSubscriptionId());
			
			Optional<Customer> customerOpt = customerRepository.findById(subscription.getCustomerId());
			Customer customer = customerOpt.get();
			
			subscriptionDto.setCustomerName(customer.getFirstName());
			subscriptionDto.setEmail(customer.getEmail());
			
			for(SubscriptionRatePlan susRatePlan : subscription.getSubscriptionRatePlan()) {
				multipleRatePlanDto = new MultipleRatePlanDto();
				ratePlanList = new ArrayList<>();
				
				multipleRatePlanDto.setPlanName(susRatePlan.getRatePlan().getRatePlanDescription());
				multipleRatePlanDto.setPrice(susRatePlan.getPrice());
				
				ratePlanList.add(multipleRatePlanDto);
				multipleRatePlanDto = null;
			}
			
			subscriptionDto.setRatePlanList(ratePlanList);
			subscriptionDto.setStatus(subscription.getStatus());
/*			subscriptionDto.setCreatedDate(subscription.getCreatedDate());
			subscriptionDto.setActivatedDate(subscription.getActivationDate());
			subscriptionDto.setLastBillDate(subscription.getSubscriptionStartDate());
			subscriptionDto.setNextBillDate(subscription.getSubscriptionEndDate());
*/
			if(subscription.getCreatedDate() != null)
				subscriptionDto.setCreatedDate(dateFormat.format(subscription.getCreatedDate()));
			if(subscription.getActivationDate() != null)
				subscriptionDto.setActivatedDate(dateFormat.format(subscription.getActivationDate()));
			if(subscription.getSubscriptionStartDate() != null)
				subscriptionDto.setLastBillDate(dateFormat.format(subscription.getSubscriptionStartDate()));
			if(subscription.getSubscriptionEndDate() != null)
				subscriptionDto.setNextBillDate(dateFormat.format(subscription.getSubscriptionEndDate()));
			
			subscriptionDtoList.add(subscriptionDto);
			subscriptionDto = null;
			ratePlanList = null;
		}
		ratePlanList = null;
		subscriptionList = null;
		LOGGER.info("<--------------End convertSubscriptionsToDto() method in SubscriptionServiceImpl.java---------------------->");

		return subscriptionDtoList;
	}
	private void writeToUserCSV(List<User> userList) {
FileWriter fileWriter = null;
		

		try {
			fileWriter = new FileWriter(USER_DATA,false);

			// Write the CSV file header
			fileWriter.append(downloadUserCsvHeader);
			fileWriter.append(NEW_LINE_SEPARATOR);
			 for (User user : userList)
	            {
	                StringBuffer oneLine = new StringBuffer();
	                oneLine.append(user.getId());
	                oneLine.append(CSV_SEPARATOR);
	                oneLine.append(user.getUserFirstName());
	                oneLine.append(CSV_SEPARATOR);
	                oneLine.append(user.getUserLastName());               
	                fileWriter.write(oneLine.toString());
	                fileWriter.append(NEW_LINE_SEPARATOR);
	            }
			
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
		
		
	}
	private void writeToProductCSV(List<ProductDto> productDtoList) {
		FileWriter fileWriter = null;
		

		try {
			fileWriter = new FileWriter(PRODUCT_DATA,false);

			// Write the CSV file header
			fileWriter.append(downloadProductCsvHeader);
			fileWriter.append(NEW_LINE_SEPARATOR);
			 for (ProductDto product : productDtoList)
	            {
	                StringBuffer oneLine = new StringBuffer();
	                oneLine.append(product.getProductDispName());
	                oneLine.append(CSV_SEPARATOR);
	                oneLine.append(product.getProductTypeCode());
	                oneLine.append(CSV_SEPARATOR);
	                String desc = product.getProductDescription();
	                if(null == desc) {
	                	desc = BLANK;
	                }
	                oneLine.append(desc);
	                oneLine.append(CSV_SEPARATOR);
	                oneLine.append(product.getSku());               
	                oneLine.append(CSV_SEPARATOR);
	                oneLine.append(product.getProductStartDate());
	                oneLine.append(CSV_SEPARATOR);
	                oneLine.append(product.getProductExpDate());
	                oneLine.append(CSV_SEPARATOR); 
	                String status = product.getStatus();
	                if(null == status) {
	                	status = BLANK;
	                }
	                oneLine.append(status);
	                oneLine.append(CSV_SEPARATOR); 
	                if(product.getRatePlans().iterator().hasNext()) {
	                	String ratePlans = product.getRatePlans().iterator().next().getRatePlanId();
	                	if(null == ratePlans)
	                		ratePlans = BLANK;
		                oneLine.append(ratePlans);
		                }
		                
	                fileWriter.write(oneLine.toString());
	                fileWriter.append(NEW_LINE_SEPARATOR);
	            }
			
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
		
	}
		
	
	private List<ProductDto> convertProductEntityToDto(Iterable<Product> productEntityList) {
		List<ProductDto> ProductDtoList = new ArrayList<>();
		
		Set<RatePlan> ratePlanSet = new HashSet<RatePlan>();
		
		for(Product product :productEntityList) {
			Set<RatePlanProductDto> ratePlans = new HashSet<RatePlanProductDto>();
			ProductDto prod = new ProductDto();
			String eDate = BLANK;
			String sDate = BLANK;
			Date startDate = product.getProductStartDate();
			Date expDate = product.getProductExpDate();
			DateFormat dateFormat = new SimpleDateFormat(DD_MM_YYYY);
			if(expDate != null) {
			eDate = dateFormat.format(expDate); 
			}
			if(startDate != null) {
			sDate = dateFormat.format(startDate);
			}
			prod.setUidpk(product.getUidpk());
			prod.setProductDispName(product.getProductDispName());
			prod.setProductTypeCode(product.getProductTypeCode().getProductType());
			prod.setProductDescription(product.getProductDescription());
			prod.setSku(product.getSku());
			prod.setProductStartDate(sDate);
			prod.setProductExpDate(eDate);
			ratePlanSet = product.getRatePlans();
			for(RatePlan ratePlan : ratePlanSet) {
				RatePlanProductDto rpDto = new RatePlanProductDto();
				
				if(ratePlan.getIsActive() == 0) {
				rpDto.setIsActive("InActive");
				}
				else {
					rpDto.setIsActive("Active");
				}
				rpDto.setUidpk(ratePlan.getId());
				rpDto.setRatePlanId(ratePlan.getRatePlanId());
				rpDto.setName(ratePlan.getRatePlanDescription());
				rpDto.setBillEvery(ratePlan.getBillingFrequency());
				rpDto.setPrice(ratePlan.getPrice());
				if(null != ratePlan.getUom()) {
				rpDto.setUnitOfMesureId(ratePlan.getUom().getUnitOfMeasure());
				}
				ratePlans.add(rpDto);
			}
			
			prod.setRatePlans(ratePlans);
			if(!prod.getRatePlans().isEmpty()) {
				prod.setAssociatedWithPlan(true);
			}
			ProductDtoList.add(prod);
			
		}
		return ProductDtoList;
	}
	@Override
	public String downloadSearchRecords(CSVRecordDataDto csvRecordData, Pageable reqCount, HttpServletResponse response) throws IOException {
		String responseMessage = BLANK;
		String fileName = null;
		List<SubscriptionDto> subscriptionDtoList = new ArrayList<SubscriptionDto>();
		
		ProductDataDto productList = new ProductDataDto();
		if("PRODUCTSEARCHPAGE".equalsIgnoreCase(csvRecordData.getPageName())) {
			fileName = "PRODUCT_DATA.CSV";
			ProductDto product = csvRecordData.getProductData();
			productList = productServiceImpl.searchProducts(product, reqCount, response);
		}
		else if("SUBSCRIPTIONSEARCHPAGE".equalsIgnoreCase(csvRecordData.getPageName())) {
			fileName = "SUBSCRIPTION_DATA.CSV";
			SubscriptionInOut subscription = csvRecordData.getSubscriptionData();
			subscriptionDtoList = subscriptionServiceImpl.findAllSubscription(subscription, reqCount, response);
		}
			File file = null;
			file = new File(EXTERNAL_FILE_PATH + fileName);
			System.out.println(EXTERNAL_FILE_PATH + fileName);
			if(null != productList.getProductList() && !productList.getProductList().isEmpty()) {
			writeToProductCSV(productList.getProductList());
			}
			else if(!subscriptionDtoList.isEmpty()) {
				writeToSubscriptionCSV(subscriptionDtoList);
				}
			
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				mimeType = "text/csv";
			}


			response.setContentType(mimeType);

			response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));

			response.setContentLength((int) file.length());
		

			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

			FileCopyUtils.copy(inputStream, response.getOutputStream());
			responseMessage = "Download completed";
			return responseMessage;
		
	}
	
	        }
