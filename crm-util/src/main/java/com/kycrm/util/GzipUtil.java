package com.kycrm.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Gzip压缩解压工具类
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年8月23日上午10:58:12
 * @Tags
 */
public class GzipUtil {

	public static final String GZIP_ENCODE_UTF_8 = "UTF-8";
	public static final String GZIP_ENCODE_ISO_8859_1 = "ISO-8859-1";

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 压缩
	 * @Date 2018年8月23日上午11:00:25
	 * @param str
	 * @param encoding
	 * @return
	 * @ReturnType byte[]
	 */
	public static byte[] compress(String str, String encoding) {
		if (str == null || str.length() == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes(encoding));
			gzip.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 压缩
	 * @Date 2018年8月23日上午11:00:36
	 * @param str
	 * @return
	 * @throws IOException
	 * @ReturnType byte[]
	 */
	public static byte[] compress(String str) throws IOException {
		return compress(str, GZIP_ENCODE_UTF_8);
	}

	public static byte[] uncompress(byte[] bytes) throws IOException {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		try {
			GZIPInputStream ungzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = ungzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
			out.close();
		}
		return out.toByteArray();
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 解压
	 * @Date 2018年8月23日上午11:00:42
	 * @param bytes
	 * @param encoding
	 * @return
	 * @throws IOException
	 * @ReturnType String
	 */
	public static String uncompressToString(byte[] bytes, String encoding) throws IOException {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		try {
			GZIPInputStream ungzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = ungzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
			return out.toString(encoding);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
			out.close();
		}
		return out.toString(encoding);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 解压
	 * @Date 2018年8月23日上午11:00:53
	 * @param bytes
	 * @return
	 * @throws IOException
	 * @ReturnType String
	 */
	public static String uncompressToString(byte[] bytes) throws IOException {
		return uncompressToString(bytes, GZIP_ENCODE_UTF_8);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 测试
	 * @Date 2018年8月23日上午10:59:36
	 * @param args
	 * @throws IOException
	 * @ReturnType void
	 */
	public static void main(String[] args) throws IOException {
		String s = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		System.out.println("字符串长度：" + s.length());
		System.out.println("压缩后：：" + compress(s).length);
		System.out.println("解压后：" + new String(uncompress(compress(s))));
		System.out.println("解压后长度：" + uncompress(compress(s)).length);
		System.out.println("解压字符串后：：" + uncompressToString(compress(s)));
		System.out.println("解压字符串后长度：：" + uncompressToString(compress(s)).length());
	}
}
