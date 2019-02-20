package com.hcl.bss.data.migration.parsers;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hcl.bss.dao.ProductDAO;
import com.hcl.bss.dto.ProductDto;
import com.hcl.bss.validator.DataMigrationFieldValidator;

@Component
public class XLSXDataMigrationParser {
	@Autowired
	DataMigrationFieldValidator dataMigrationFieldValidator;
	@Autowired
	ProductDAO productDAO;

	public List<ProductDto> parseData(String path) throws IOException, ParseException {
		List<ProductDto> listProduct = new ArrayList<>();
		
		
		Workbook workbook = new XSSFWorkbook(path);
		Sheet firstSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = firstSheet.iterator();
		
		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();
			ProductDto product = new ProductDto();
			
			while (cellIterator.hasNext()) {
				Cell nextCell = cellIterator.next();
				int columnIndex = nextCell.getColumnIndex();
				
				switch (columnIndex) {
				case 1:
					product.setProductId((String) getCellValue(nextCell));
					
					break;
				case 2:
					product.setSku((String) getCellValue(nextCell));
					
					break;
				case 3:
					product.setProductDispName((String) getCellValue(nextCell));
					
					break;
				case 4:	
					product.setProductExpDate((String) getCellValue(nextCell));
					break;
					
				case 5:	
					product.setProductDescription((String) getCellValue(nextCell));
					break;
				}
			}
			listProduct.add(product);
			//dataMigrationFieldValidator.validateField(listProduct);
			
		}
		return listProduct;
		
	}
		private Object getCellValue(Cell cell) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				return cell.getStringCellValue();
				
			case Cell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue();

			case Cell.CELL_TYPE_NUMERIC:
				return cell.getNumericCellValue();
			}
			
			return null;
		}

}
