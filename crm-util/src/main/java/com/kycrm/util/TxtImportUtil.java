package com.kycrm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TxtImportUtil {
	/**
	 * txt导入
	 * @author HL
	 * @time 2018年8月2日 下午4:18:07 
	 * @param inputStream
	 * @return
	 */
	public static List<String> gainTxtData(InputStream inputStream){
	    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	    List<String> list = new ArrayList<String>();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	        	if(null == line || "".equals(line))
					continue;
	        	
	        	list.add(line.trim());
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            inputStream.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return list;
	}

}
