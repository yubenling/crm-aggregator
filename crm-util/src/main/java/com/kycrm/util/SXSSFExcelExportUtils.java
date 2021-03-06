package com.kycrm.util;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class SXSSFExcelExportUtils {
              
    /***
     * 导出Excel表格 
     * @param title 表格sheet名称
     * @param rowName 表头
     * @param dataList 表中数据数组每个元素代表一列，每一个List元素代表一行
     * @param response
     * @throws Exception
     * @author forChina
     */
	public static SXSSFWorkbook export(String title,String[] rowName,List<Object[]>  dataList) throws Exception{  
        try{  
            SXSSFWorkbook workbook = new SXSSFWorkbook();// 创建工作簿对象  
            Sheet sheet = workbook.createSheet(title);// 创建工作表  
            // 产生表格标题行  
            SXSSFRow rowm = (SXSSFRow) sheet.createRow(0);  
            Cell cellTiltle = rowm.createCell((short)0);  
              
            //sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展】  
            CellStyle columnTopStyle = SXSSFExcelExportUtils.getColumnTopStyle(workbook);//获取列头样式对象  
            CellStyle style = SXSSFExcelExportUtils.getStyle(workbook);//单元格样式对象  
              
            //sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (rowName.length-1)));    
            cellTiltle.setCellStyle(columnTopStyle);  
            cellTiltle.setCellValue(title);  
              
            // 定义所需列数  
            int columnNum = rowName.length;  
            Row rowRowName = sheet.createRow(2);// 在索引2的位置创建行(最顶端的行开始的第二行)  
              
            // 将列头设置到sheet的单元格中  
            for(int n = 0; n < columnNum; n ++){  
                Cell  cellRowName = rowRowName.createCell(n);//创建列头对应个数的单元格  
                cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING);//设置列头单元格的数据类型  
	            HSSFRichTextString text = new HSSFRichTextString(rowName[n]);  
	            cellRowName.setCellValue(text);//设置列头单元格的值  
	            cellRowName.setCellStyle(columnTopStyle);//设置列头单元格样式  
               
            }  
              
            //将查询出的数据设置到sheet对应的单元格中  
            for(int i=0;i<dataList.size();i++){  
                  
                Object[] obj = dataList.get(i);//遍历每个对象  
                Row row = sheet.createRow(i+3);//创建所需的行数  
                  
                for(int j=0; j<obj.length; j++){  
                    Cell  cell = null;   //设置单元格的数据类型  
                    if(j == 0){  
//                        cell = row.createCell(j,HSSFCell.CELL_TYPE_NUMERIC);  
//                        cell.setCellValue(i+1);  
                    	 cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);  
                         if(!"null".equals(obj[j]) && obj[j] != null){  
                             cell.setCellValue(obj[j].toString());//设置单元格的值  
                         }else{
                        	 cell.setCellValue("");
                         }
                    }else{  
                        cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);  
                        if(!"null".equals(obj[j])&& obj[j] != null){  
                            cell.setCellValue(obj[j].toString());//设置单元格的值  
                        }else{
                        	 cell.setCellValue("");
                        }
                    }  
                    cell.setCellStyle(style);//设置单元格样式  
                }  
            }  
            //让列宽随着导出的列长自动适应  
            for (int colNum = 0; colNum < columnNum; colNum++) {  
                int columnWidth = sheet.getColumnWidth(colNum) / 256;  
                for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {  
                    Row currentRow;  
                    //当前行未被使用过  
                    if (sheet.getRow(rowNum) == null) {  
                        currentRow = sheet.createRow(rowNum);  
                    } else {  
                        currentRow = sheet.getRow(rowNum);  
                    }  
                    if (currentRow.getCell(colNum) != null) {  
                        Cell currentCell = currentRow.getCell(colNum);  
                        if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {  
	                		try {
								int length = currentCell.getStringCellValue().getBytes().length;  
								if (columnWidth < length) {  
								    columnWidth = length;  
								}
							} catch (Exception e) {
								e.printStackTrace();
							}   
                        
                        }  
                    }  
                }  
                if(colNum == 0){  
                	if(columnWidth>150){
                		sheet.setColumnWidth(colNum, 150 * 256);
                	}else{
                		sheet.setColumnWidth(colNum, (columnWidth-2) * 256);  
                	}
                }else{  
                	if(columnWidth>150){
                		sheet.setColumnWidth(colNum, 150 * 256);
                	}else{
                		sheet.setColumnWidth(colNum, (columnWidth+4) * 256);
                	}
                }  
            }  
              
            if(workbook !=null){  
                return workbook;
            }  
  
        }catch(Exception e){  
            e.printStackTrace();  
        }
        return null;
          
    }  
      
    /***
     * 设置Excel表格表头样式     
     * @param workbook
     * @return
     * @author forChina
     */
    public static CellStyle getColumnTopStyle(SXSSFWorkbook workbook) {  
          
          // 设置字体  
          Font font = workbook.createFont();  
          //设置字体大小  
          font.setFontHeightInPoints((short)11);  
          //字体加粗  
          font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
          //设置字体名字   
          font.setFontName("Courier New");  
          //设置样式;   
          CellStyle style = workbook.createCellStyle();  
          //设置底边框;   
          style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
          //设置底边框颜色;    
          style.setBottomBorderColor(HSSFColor.BLACK.index);  
          //设置左边框;     
          style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
          //设置左边框颜色;   
          style.setLeftBorderColor(HSSFColor.BLACK.index);  
          //设置右边框;   
          style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
          //设置右边框颜色;   
          style.setRightBorderColor(HSSFColor.BLACK.index);  
          //设置顶边框;   
          style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
          //设置顶边框颜色;    
          style.setTopBorderColor(HSSFColor.BLACK.index);  
          //在样式用应用设置的字体;    
          style.setFont(font);  
          //设置自动换行;   
          style.setWrapText(false);  
          //设置水平对齐的样式为居中对齐;    
          style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
          //设置垂直对齐的样式为居中对齐;   
          style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
            
          return style;  
            
    }  
      
    /***
     * 列数据信息单元格样式     
     * @param workbook
     * @return
     * @author forChina
     */
    public static CellStyle getStyle(SXSSFWorkbook workbook) {  
          // 设置字体  
          Font font = workbook.createFont();  
          //设置字体大小  
          //font.setFontHeightInPoints((short)10);  
          //字体加粗  
          //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
          //设置字体名字   
          font.setFontName("Courier New");  
          //设置样式;   
          CellStyle style = workbook.createCellStyle();  
          //设置底边框;   
          style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
          //设置底边框颜色;    
          style.setBottomBorderColor(HSSFColor.BLACK.index);  
          //设置左边框;     
          style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
          //设置左边框颜色;   
          style.setLeftBorderColor(HSSFColor.BLACK.index);  
          //设置右边框;   
          style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
          //设置右边框颜色;   
          style.setRightBorderColor(HSSFColor.BLACK.index);  
          //设置顶边框;   
          style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
          //设置顶边框颜色;    
          style.setTopBorderColor(HSSFColor.BLACK.index);  
          //在样式用应用设置的字体;    
          style.setFont(font);  
          //设置自动换行;   
          style.setWrapText(false);  
          //设置水平对齐的样式为居中对齐;    
          style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
          //设置垂直对齐的样式为居中对齐;   
          style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
           
          return style;  
      
    }  

}
