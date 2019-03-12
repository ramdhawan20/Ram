package com.hcl.bss.datamigration.parsers;


import static com.hcl.bss.constants.ApplicationConstants.BLANK;
import static com.hcl.bss.constants.ApplicationConstants.COMMA_DELIMITER;
import static com.hcl.bss.constants.ApplicationConstants.EXP_DATE_IDX;
import static com.hcl.bss.constants.ApplicationConstants.PRODUCT_DISPLAY_NAME_IDX;
import static com.hcl.bss.constants.ApplicationConstants.PRODUCT_TYPE_CODE_IDX;
import static com.hcl.bss.constants.ApplicationConstants.PROD_DESCRIPTION_IDX;
import static com.hcl.bss.constants.ApplicationConstants.SKU_IDX;
import static com.hcl.bss.constants.ApplicationConstants.START_DATE_IDX;
import static com.hcl.bss.constants.ApplicationConstants.HYPHEN;

import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.exceptions.ImportParseException;
import com.hcl.bss.validator.DataMigrationFieldValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.hcl.bss.constants.ApplicationConstants.*;

@Component
public class CSVDataMigrationParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVDataMigrationParser.class);
	@Autowired
	DataMigrationFieldValidator dataMigrationFieldValidator;
	@Value("${upload.file.header}")
	String prescribedFileHeader;

	public List<ProductDto> parseCsvData(String fileName) throws IOException, ParseException {
		List<ProductDto> listProduct = new ArrayList<>();
		BufferedReader fileReader = null;

		try {

			String line = BLANK;

			// Create the file reader
			fileReader = new BufferedReader(new FileReader(fileName));

			// Read the CSV file header
			String header = getCSVHeader(fileReader);

			if (prescribedFileHeader.equalsIgnoreCase(header)) {

				// Read the file line by line starting from the second line
				while ((line = fileReader.readLine()) != null) {
					// TO do  if (line.length < COLUMN_COUNT) {
					// Get all tokens available in line
					String[] token = line.split(COMMA_DELIMITER, -1);
					//String[] tokens = test(token);
					

					
					
					
					
					if (0 != token.length) {
						if (token.length == header.split(COMMA_DELIMITER).length) {
							// Create a new product object and fill this data
							// TODO - Use Builder Pattern. ProductDto.......build()
							ProductDto product = new ProductDto(token[PRODUCT_TYPE_CODE_IDX],
									token[PRODUCT_DISPLAY_NAME_IDX], token[SKU_IDX],token[START_DATE_IDX], token[EXP_DATE_IDX],
									token[PROD_DESCRIPTION_IDX]);

							listProduct.add(product);

						}
					} else {
						throw new ImportParseException();
					}
					// TODO - Remove after UT. Print the new product list
					for (ProductDto prod : listProduct) {
						LOGGER.info(prod.toString());

					}

				}
			} else {
				throw new ImportParseException();
			}
		}

		finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();

			}
		}
		return listProduct;

	}

	/*private String[] test(String[] tokens) {
		 for(int i = 0; i < tokens.length; i++)
		    {
		        if(tokens[i].isEmpty())
		        {
		        	tokens[i] = HYPHEN;
		        }
		    }
		return tokens;
	}*/

	private String getCSVHeader(BufferedReader fileReader) {
		String header = BLANK;
		try {
			header = fileReader.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return header;
	}

}
