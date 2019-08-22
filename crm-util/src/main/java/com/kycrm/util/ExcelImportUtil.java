package com.kycrm.util;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ExcelImportUtil {
	
	
	/**
	 * 解析excel数据，只读取第一页的数据
	 * @param file
	 * @return
	 * @throws Exception 
	 */
	public static List<List<String>> gainExcelData(MultipartFile file) throws Exception {
		
		List<List<String>> dataList = new ArrayList<List<String>>();
		
		// 创建输入流
		InputStream inputStream = null;
		Workbook wb = null;
		try {
			inputStream = file.getInputStream();
			wb = new XSSFWorkbook(inputStream);
		} catch (Exception ex) {
			inputStream = file.getInputStream();
			wb = new HSSFWorkbook(inputStream);
		}

		//只读取第一页的数据
		dataList= ExcelImportUtil.read(dataList, wb, 0);
		return dataList;
	}

	/**
	 * 解析Excel返回数据
	 * @param dataList
	 * @param wb
	 * @param sheets
	 * @return
	 */
	public static List<List<String>> read(List<List<String>> dataList, Workbook wb,
			int sheets) {
		DecimalFormat df = new DecimalFormat("#");// 创建数字转换对象

		// 总行数
		int totalRows = 0;
		// 总列数
		int totalCells = 0;

		// 第一个shell页
		Sheet sheet = wb.getSheetAt(sheets);

		// Excel的行数

		totalRows = sheet.getPhysicalNumberOfRows();

		// Excel的列数

		if (totalRows >= 1 && sheet.getRow(0) != null) {

			totalCells = sheet.getRow(0).getPhysicalNumberOfCells();

		}
		// 遍历Excel的行
		for (int r = 0; r < totalRows; r++) {

			Row row = sheet.getRow(r);

			if (row == null) {

				continue;

			}

			List<String> rowLst = new ArrayList<String>();

			// 遍历Excel的列
			for (int c = 0; c < totalCells; c++) {

				Cell cell = row.getCell(c);

				String cellValue = "";

				if (null != cell) {
					// 以下是判断数据的类型
					switch (cell.getCellType()) {
					case HSSFCell.CELL_TYPE_NUMERIC: // 数字
						cellValue = df.format(cell.getNumericCellValue());
						break;

					case HSSFCell.CELL_TYPE_STRING: // 字符串
						cellValue = cell.getStringCellValue();
						break;

					case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
						cellValue = cell.getBooleanCellValue() + "";
						break;

					case HSSFCell.CELL_TYPE_FORMULA: // 公式
						cellValue = cell.getCellFormula() + "";
						break;

					case HSSFCell.CELL_TYPE_BLANK: // 空值
						cellValue = "";
						break;

					case HSSFCell.CELL_TYPE_ERROR: // 故障
						cellValue = "非法字符";
						break;

					default:
						cellValue = "未知类型";
						break;
					}
				}

				rowLst.add(cellValue);

			}
			// 保存第r行的第c列

			dataList.add(rowLst);

		}
		return dataList;

	}
}
