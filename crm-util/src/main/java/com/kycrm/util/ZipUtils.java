package com.kycrm.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
	public class ZipUtils {
		private static final Log log = LogFactory.getLog(ZipUtils.class);
	        
	    private ZipUtils(){};
	   /**
	     * 创建ZIP文件
	     * @param sourcePath 文件或文件夹路径
	     * @param zipPath 生成的zip文件存在路径（包括文件名）
	     */
	    public static boolean createZip(String sourcePath, String zipPath) {
	        FileOutputStream fos = null;
	        ZipOutputStream zos = null;
	        try {
	            fos = new FileOutputStream(zipPath);
	            zos = new ZipOutputStream(fos);
	            writeZip(new File(sourcePath), "", zos);
	        } catch (Exception e) {
	            log.error("创建ZIP文件失败",e);
	            return false;
	        } finally {
	            try {
	                if (zos != null) {
	                    zos.close();
	                }
	            } catch (Exception e) {
	                log.error("创建ZIP文件失败",e);
	            }

	        }
			return true;
	    }
	    
	    private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
	        if(file.exists()){
	            if(file.isDirectory()){//处理文件夹
	                parentPath+=file.getName()+File.separator;
	                File [] files=file.listFiles();
	                for(File f:files){
	                    writeZip(f, parentPath, zos);
	                }
	            }else{
	                FileInputStream fis=null;
	                DataInputStream dis=null;
	                try {
	                    fis=new FileInputStream(file);
	                    dis=new DataInputStream(new BufferedInputStream(fis));
	                    ZipEntry ze = new ZipEntry(parentPath + file.getName());
	                    zos.putNextEntry(ze);
	                    byte [] content=new byte[1024];
	                    int len;
	                    while((len=fis.read(content))!=-1){
	                        zos.write(content,0,len);
	                        zos.flush();
	                    }
	                } catch (FileNotFoundException e) {
	                    log.error("创建ZIP文件失败",e);
	                } catch (IOException e) {
	                    log.error("创建ZIP文件失败",e);
	                }finally{
	                    try {
	                        if(dis!=null){
	                            dis.close();
	                        }
	                    }catch(IOException e){
	                        log.error("创建ZIP文件失败",e);
	                    }
	                }
	            }
	        }
	    }    
	}
