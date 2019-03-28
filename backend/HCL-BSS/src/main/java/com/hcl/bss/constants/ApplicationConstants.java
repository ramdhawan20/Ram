package com.hcl.bss.constants;

public final class ApplicationConstants {

		

	// Error CSV file header
	public static final String FILE_HEADER = "ROW_NO,ERROR";
	//Delimiter used in CSV file 
    public static final String COMMA_DELIMITER = ",";
     
    //Product attributes index
    public static final int PRODUCT_TYPE_CODE_IDX = 0;
    public static final int PRODUCT_DISPLAY_NAME_IDX = 1;
    public static final int SKU_IDX = 2;
    public static final int START_DATE_IDX = 3;
    public static final int EXP_DATE_IDX = 4; 
    public static final int PROD_DESCRIPTION_IDX = 5;
    public static final String BLANK = "";
    public static final String DATE_FORMAT_DDMMYYYY = "dd/MM/yyyy";
    public static final String DATE_FORMAT_DD_MM_YYYY = "dd-MM-yyyy";
    public static final String STATUS_PARTIAL_SUCCESS = "Partial Success";
    public static final String STATUS_SUCCESS = "Success";
    public static final String STATUS_FAIL = "Fail";
    public static final String EXTERNAL_FILE_PATH = "/PROJECTS/SUBSCRIPTION/files/";//"C://Users//Public//temp//";
   // public static final String EXTERNAL_FILE_PATH = "C://Users//Public//temp//";
    public static final String FILE_NOT_EXIST_MSG = "Sorry. The file you are looking for does not exist";
    public static final String ERROR_FILE_NAME_SUFFIX = "yyyy-MM-dd-HH_mm_ss";
    public static final String UPLOADED_FOLDER = "/PROJECTS/SUBSCRIPTION/files/";//C://Users//Public//temp//";
    public static final String ERROR_FILE = "/PROJECTS/SUBSCRIPTION/files/error_log_";
    public static final String CSV_EXTENTION = ".csv";
    public static final String TEXT_EXTENTION = ".txt";
    public static final String NEW_LINE_SEPARATOR = "\n";
    public static final String CSV_WRONG_DATA = "Incorrect CSV Header or No Records in file ! Download Sample CSV!";
    public static final String INVALID_FILE_TYPE = "Invalid File Type! Only CSV Allowed.";
    public static final String DD_MM_YYYY_HH_MM = "dd-MM-yyyy.HH.mm";
    public static final String DD_MM_YYYY = "dd/MM/yyyy";
    public static final String ROW_NO = "Row No: ";
    public static final String TAB_SPACE = "\t";
    public static final String ERROR = "Error : ";
    public static final String LINE_SEPERATOR = "line.separator";
    public static final String UTF_8 = "UTF-8";
    public static final String DUPLICATE_SKU = "Duplicate SKU in File: ";
    public static final String DUPLICATE_SKU_DB = " SKU in Database exists: ";
    public static final String DEFAULT_EXP_DATE = "31/12/9999";
    public static final String PRODUCT_TYPE_CODE_NOT_DEFIENED_IN_MASTER = "ProductTypeCode Value is not defiened in ProductTypeMaster DB Constraint Violation: ";

    //subscription batch
    public static final String PASS_STATUS = "Pass";
    public static final String FAIL_STATUS = "Fail";
    public static final String BILL_TO = "billTo";
    public static final String SOLD_TO = "soldTo";
    public static final String COMPANY_ID = "companyId";
    public static final String VOLUME = "VOLUME";
    public static final String RECORD_NO = "Record No : ";
    public static final String UPLOAD = "UPLOAD";
    public static final String HYPHEN = "-";
    public static final String UNIT = "UNIT";

    public static final String PRODUCT_DATA = "/PROJECTS/SUBSCRIPTION/files/PRODUCT_DATA.CSV";
    public static final String USER_DATA = "/PROJECTS/SUBSCRIPTION/files/USER_DATA.CSV";
    public static final String SUBSCRIPTION_DATA = "/PROJECTS/SUBSCRIPTION/files/SUBSCRIPTION_DATA.CSV";

    //User Management
    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_NORMAL = 2;
    public static final String ADMIN = "Admin";
    public static final String NORMAL = "Normal";
    public static final String ACTIVE = "Active";
    public static final String INACTIVE = "Inactive";
       
     public enum BillingInvoicing{
        ACTIVE(1),
        INACTIVE(0);

        private int active;

        private BillingInvoicing(int number){
            this.active  = number;
        }

        public int getActiveStatus(){
            return this.active;
        }
    }

}

