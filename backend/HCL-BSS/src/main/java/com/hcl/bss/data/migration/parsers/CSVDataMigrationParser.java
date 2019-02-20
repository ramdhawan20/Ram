package com.hcl.bss.data.migration.parsers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hcl.bss.dao.ProductDAO;
import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.exceptions.ImportParseException;
import com.hcl.bss.validator.DataMigrationFieldValidator;

import static com.hcl.bss.constants.ApplicationConstants.COMMA_DELIMITER;
import static com.hcl.bss.constants.ApplicationConstants.PRODUCT_ID_IDX;
import static com.hcl.bss.constants.ApplicationConstants.PRODUCT_DISPLAY_NAME_IDX;
import static com.hcl.bss.constants.ApplicationConstants.SKU_IDX;
import static com.hcl.bss.constants.ApplicationConstants.EXP_DATE_IDX;
import static com.hcl.bss.constants.ApplicationConstants.PROD_DESCRIPTION_IDX;
import static com.hcl.bss.constants.ApplicationConstants.BLANK;

@Component
public class CSVDataMigrationParser {
	@Autowired
	DataMigrationFieldValidator dataMigrationFieldValidator;
	@Autowired
	ProductDAO productDAO;

	public List<ProductDto> parseCsvData(String fileName) throws IOException, ParseException {
		List<ProductDto> listProduct = new ArrayList<>();
		BufferedReader fileReader = null;

		try {

			String line = BLANK;

			// Create the file reader
			fileReader = new BufferedReader(new FileReader(fileName));

			// Read the CSV file header to skip it
			fileReader.readLine();

			// Read the file line by line starting from the second line
			while ((line = fileReader.readLine()) != null) {
				// Get all tokens available in line
				String[] tokens = line.split(COMMA_DELIMITER);
				if (tokens.length == 5) {
					// Create a new product object and fill his data
					// TODO - Use Builder Pattern. ProductDto.......build()
					ProductDto product = new ProductDto(tokens[PRODUCT_ID_IDX], tokens[PRODUCT_DISPLAY_NAME_IDX],
							tokens[SKU_IDX], tokens[EXP_DATE_IDX], tokens[PROD_DESCRIPTION_IDX]);
					listProduct.add(product);
				} else {
					throw new ImportParseException("Column is more than expected");//TODO - Add Message
				}
			}

			// TODO - Remove after UT. Print the new product list
			for (ProductDto prod : listProduct) {
				System.out.println(prod.toString());
			}
		} catch (ImportParseException ipe) {// TODO - Proper Handling
			throw new ImportParseException("Error in Parsing csv Data");
		} catch (Exception e) {
			throw new ImportParseException("Error in Parsing csv Data");
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				throw new ImportParseException("Error while closing fileReader !!!");
				
			}
		}
		return listProduct;

	}

}
