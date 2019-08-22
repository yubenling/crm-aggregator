package com.kycrm.util;

/**
 * @author wy
 */
public class GetCurrentPageUtil {
	private GetCurrentPageUtil(){}
	/**
	 * 取得默认的每页显示数量
	 * @param lineSize 要显示的每页数量
	 * @return
	 */
	public static int getLineSize(Integer lineSize){
		if(lineSize==null || lineSize<0){
			lineSize = 10;
		}
		return lineSize;
	}
	
	/**
	 * 取得默认的每页显示数量
	 * @param lineSize 要显示的每页数量
	 * @return
	 */
	public static long getLineSize(Long lineSize){
		if(lineSize==null || lineSize<0){
			lineSize = 10L;
		}
		return lineSize;
	}
	
	/**
	 * 查询要显示的总页数
	 * @param totalNum 总记录数
	 * @param lineSize 每页显示数量
	 * @return 总页数
	 */
	public static int getTotalPage(Integer totalNum,Integer lineSize){
		if(totalNum==null || totalNum<0){
			return 0;
		}
		lineSize = getLineSize(lineSize);
		int totalPage = (totalNum+(lineSize-1))/lineSize;
		return totalPage;
	}
	
	/**
	 * 查询要显示的总页数
	 * @param totalNum 总记录数
	 * @param lineSize 每页显示数量
	 * @return 总页数
	 */
	public static long getTotalPage(Long totalNum,Long lineSize){
		if(totalNum==null || totalNum<0){
			return 0;
		}
		lineSize = getLineSize(lineSize);
		long totalPage = (totalNum+(lineSize-1))/lineSize;
		return totalPage;
	}
	
	/**
	 * 取得要显示的页数，如果页数超过了总页数，默认显示最后一页
	 * @param page 显示的页数
	 * @param totalNum 总记录数
	 * @param lineSize 每页显示数量
	 * @return 要显示的页数
	 */
	public static int getPage(Integer page,Integer totalNum,Integer lineSize){
		if(page==null || page <1){
			return 1;
		}
		int totalPage = getTotalPage(totalNum,lineSize);
		if(page>totalPage){
			page = totalPage;
		}
		return page;
	}
	/**
	 * mysql 分页取得开始的显示数量，100条数据，10条一页    则第三页开始数为 limit 29,10
	 * @param page 要显示的页数
	 * @param totalNum 总记录数
	 * @param lineSize 每页显示数
	 * @return 返回每页开始数量位置
	 */
	public static int getStartNum(Integer page,Integer totalNum,Integer lineSize){
		lineSize = getLineSize(lineSize);
		page = getPage(page, totalNum, lineSize);
		if(page==0){
		    return 0;
		}
		return (page-1)*lineSize;
	}
}
