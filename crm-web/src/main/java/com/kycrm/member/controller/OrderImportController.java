package com.kycrm.member.controller;

import java.io.File;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.OrderImportProvider;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.orderimport.OrderImportRecord;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.orderimport.OrderImportRecordVO;
import com.kycrm.member.service.orderimport.IOrderImportRecordService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.CsvReaderUtils;
import com.kycrm.util.DateUtils;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.OrderImportUtil;
import com.kycrm.util.PinYin4jUtil;
import com.kycrm.util.UploadPathUtil;
import com.kycrm.util.ZipUtils;
import com.kycrm.util.thread.MyFixedThreadPool;

@Controller
@RequestMapping(value = "/backstage")
public class OrderImportController extends BaseController {
	private static final Log logger = LogFactory.getLog(OrderImportController.class);
	
	@Autowired
	private SessionProvider sessionProvider;
	@Autowired
	private IOrderImportRecordService orderImportRecordService;
	@Autowired
	private OrderImportProvider orderImportProvider;
	
	/**
	 * 查询订单导入分页记录
	 * @author HL
	 * @time 2018年8月30日 下午3:10:49 
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="findImportRecord",produces="text/html;charset=UTF-8")
	public String findOrderImportRecord(@RequestBody String params,
			HttpServletRequest request, HttpServletResponse response){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		
		OrderImportRecordVO vo	= new OrderImportRecordVO(); 
		if (null != params && !"".equals(params)) {
			try {
				vo = JsonUtil.paramsJsonToObject(params, OrderImportRecordVO.class);
			} catch (Exception e) {
				e.printStackTrace();
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
		}
		
		vo.setUid(user.getId());
		Map<String, Object> map= orderImportRecordService.findOrderImportRecord(vo);
		map.put("pageNo", vo.getPageNo());
		return successReusltMap(null).put(ApiResult.API_RESULT, map).toJson();
	}
	
	/**
	 * 删除订单导入记录
	 * @author HL
	 * @time 2018年8月30日 下午4:26:59 
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/deleteImportRecord",produces="text/html;charset=UTF-8")
	public String deleteOrderImportRecord(@RequestBody String params,
			HttpServletRequest request, HttpServletResponse response){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		
		OrderImportRecordVO vo	= new OrderImportRecordVO(); 
		if (null != params && !"".equals(params)) {
			try {
				vo = JsonUtil.paramsJsonToObject(params, OrderImportRecordVO.class);
			} catch (Exception e) {
				e.printStackTrace();
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
		}
		vo.setUid(user.getId());
		boolean result = orderImportRecordService.deleteOrderImportRecord(vo);
		if(result){
			return successReusltMap(ApiResult.DEL_SUCCESS).toJson();
		}
		return failureReusltMap(ApiResult.DEL_FAILURE).toJson();
	}
	
	
	
	/**
	 * 订单上传
	 * @author HL
	 * @time 2018年5月24日 下午2:32:36 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/orderUpload",method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	public String orderUploadFile(@RequestParam MultipartFile[] files,
			HttpServletResponse response, HttpServletRequest request)
			{
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();

		try {
			//验证文件
			Map<String, Object> map = validateFiles(files);
			if(!(Boolean)map.get("status"))
				return failureReusltMap((String)map.get("msgKey")).toJson();
			
			//获取对应文件
			String msg = (String) map.get("msgKey");
			int o = Integer.parseInt(msg);
			int i = o==0?1:0;
			
			MultipartFile oFile = files[o];
			MultipartFile iFile = files[i];
			
			if(oFile.getSize()>52428800 || iFile.getSize()>52428800)
				return failureReusltMap(ApiResult.IMPORT_FILE_OVERSIZE).toJson();
			
			CsvReaderUtils oCsv = new CsvReaderUtils(oFile.getInputStream(), ',', Charset.forName("GBK"));
			CsvReaderUtils iCsv = new CsvReaderUtils(iFile.getInputStream(), ',', Charset.forName("GBK"));
			
			oCsv.readHeaders();
			iCsv.readHeaders();
			// 获取每个头对应的索引
			Map<String, Integer> oHeaderMap = OrderImportUtil.makeHeaderMap(oCsv.getHeaders());
			Map<String, Integer> iHeaderMap = OrderImportUtil.makeHeaderMap(iCsv.getHeaders());
			//检验头信息
			if (!oHeaderMap.keySet().containsAll(OrderImportUtil.getOrderHeaders())
					|| !iHeaderMap.keySet().containsAll(OrderImportUtil.getItemHeaders()))
				return failureReusltMap(ApiResult.IMPORT_CONTENT_ERROR).toJson();
			
			
			CsvReaderUtils oCsvCount = new CsvReaderUtils(oFile.getInputStream(), ',', Charset.forName("GBK"));
			CsvReaderUtils iCsvCount = new CsvReaderUtils(iFile.getInputStream(), ',', Charset.forName("GBK"));
			int iCount = -1;//商品总数
			int oCount = -1;//订单总数
			while(iCsvCount.readRecord()){
				iCount++;
			}
			while (oCsvCount.readRecord()) {
				oCount++;
			}
			String oName = oFile.getOriginalFilename();//订单文件名字
			String iName = iFile.getOriginalFilename();//商品文件名字
			Long rId = createOrderImport(user,oName,iName,oCount,iCount);
			if(null == rId)
				return failureReusltMap(ApiResult.SAVE_FAILURE).toJson();
			
			//异步处理上传文件
			this.disposeFilesData(oCsv, oHeaderMap, iCsv, iHeaderMap, user, rId);
			
			// 保存文件
			this.saveFile(oFile, oName);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("*****订单上传，userNick："+user.getTaobaoUserNick()+"*****异常******************"+e.getMessage());
			return failureReusltMap(ApiResult.IMPORT_FAILURE).toJson();
		}
		
		return successReusltMap(ApiResult.IMPORT_DISPOSE).toJson();
	}	

	/**
	 * 将商品文件对订单文件进行补充---并处理订单数据
	 * @throws Exception 
	 * @time 2018年9月13日 上午11:02:45
	 */
	private void disposeFilesData(CsvReaderUtils oCsv,
			Map<String, Integer> oHeaderMap, CsvReaderUtils iCsv,
			Map<String, Integer> iHeaderMap, UserInfo user, Long rId){
		MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread(){
			@Override
			public void run() {
				//订单数据
				List<String[]> datasList = new ArrayList<String[]>();
				try {
					//封装商品map，key---订单id  value---标题
					Map<String,String> iTitleMap = new HashMap<String,String>();
					while(iCsv.readRecord()){
						String[] iValues = iCsv.getValues();
						String itid = OrderImportUtil.disposeTid(OrderImportUtil.getValFromArray(iHeaderMap, iValues,"订单编号"));
						if(null==itid || "".equals(itid))
							continue;
						
						String iTitle = OrderImportUtil.getValFromArray(iHeaderMap, iValues,"标题");
						if(null==iTitle || "".equals(iTitle))
							continue;
						
						iTitleMap.put(itid,iTitle);
					}
					
					//封装订单文件内容
					Set<String> repOid = new HashSet<String>();//放入订单判断订单是否存在
					while (oCsv.readRecord()) {
						String[] oValues = oCsv.getValues();
						String otid = OrderImportUtil.disposeTid(OrderImportUtil.getValFromArray(oHeaderMap, oValues,"订单编号"));
						if(null == otid || "".equals(otid)){
							logger.error("*****订单上传，userNick："+user.getTaobaoUserNick()+"*****订单号为空不记录******************");
							continue;
						}
						
						if(repOid.contains(otid)){
							logger.error("*****订单上传，userNick："+user.getTaobaoUserNick()+"*****订单号重复不记录******************");
							continue;
						}
						
						repOid.add(otid);
						
						String oTitle = OrderImportUtil.getValFromArray(oHeaderMap, oValues,"宝贝标题");
						if(null == oTitle || "".equals(oTitle)){
							String iTitle = iTitleMap.get(otid);
							Integer index = OrderImportUtil.getIndexFromArray(oHeaderMap, oValues,"宝贝标题");
							if(null != iTitle && !"".equals(iTitle) && null != index){
								oValues[index]=iTitle;
							}else{
								logger.error("*****订单上传，userNick："+user.getTaobaoUserNick()+"*****宝贝标题为空不记录******************");
								continue;
							}
						}
						
						datasList.add(oValues);
					}
				} catch (Exception e) {
					logger.error("*****订单上传，userNick："+user.getTaobaoUserNick()+"*****文件补充异常*********"+e.getMessage());
				}finally{
					//----------处理订单数据------------
					orderImportProvider.disposeOrderImportData(datasList, oHeaderMap,user,rId);
				}
			}
		});
	}

	/**
	 * 添加订单上传记录
	 */
	private Long createOrderImport(UserInfo user, String oName, String iName,
			int oCount, int iCount) {
		OrderImportRecord orderImport = new OrderImportRecord();
		orderImport.setUid(user.getId());
		orderImport.setUserId(user.getTaobaoUserNick());
		orderImport.setOrderName(oName);
		orderImport.setOrderNumber(oCount);
		orderImport.setCommodityName(iName);
		orderImport.setCommodityNumber(iCount);
		orderImport.setState("1");
		orderImport.setCreatedBy(user.getTaobaoUserNick());
		orderImport.setLastModifiedBy(user.getTaobaoUserNick());
		// 执行保存返回id
		Long rid = orderImportRecordService.insertOrderImportRecord(orderImport);
		return rid;
	}

	
	
	/**
	 * 保存导入的订单文件到指定的目录下
	 */
	private void saveFile(MultipartFile file, String fileName) {
		try {
			// 创建时间，并格式
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String formatStr = format.format(date);

			//每天创建一个文件夹
			String day = DateUtils.dateToString(new Date(), DateUtils.FORMAT_YYYYMMDD)+"/";
			// 获取保存路径
			String filePath = UploadPathUtil.ORDER_FILE_PATH+day;
			StringBuffer fileBuffer = new StringBuffer(filePath);
			fileBuffer.append(PinYin4jUtil.hanyu2pinyin(fileName.substring(0,
					fileName.lastIndexOf("."))));
			fileBuffer.append("_" + formatStr);
			fileBuffer.append(fileName.substring(fileName.lastIndexOf("."),
					fileName.length()));

			// 是否创建文件
			File fileP = new File(filePath);
			if (!fileP.exists()) {
				fileP.mkdirs();
			}
			// 上传文件
			file.transferTo(new File(fileBuffer.toString()));
			logger.info("*****订单上传*************文件保存成功，路径：" + fileBuffer.toString());
			// 将文件打包为zip
			String sourcePath = fileBuffer.toString();
			String zipPath = sourcePath.substring(0,
					sourcePath.lastIndexOf("."))
					+ ".zip";
			boolean createZip = ZipUtils.createZip(sourcePath, zipPath);
			// 判断是否创建zip,如果创建成功就删除原文件
			if (createZip) {
				File delFile = new File(sourcePath);
				delFile.delete();
				logger.info("*****订单上传*************文件压缩成功，路径：" + zipPath);
			}

		} catch (Exception e) {
			logger.error("*****订单上传*************保存文件异常:" + e.getMessage());
		}
	}
	
	/**
	 * 验证上传文件 
	 */
	private Map<String, Object> validateFiles(MultipartFile[] files) throws Exception {
		
		if(null == files || files.length == 0)//文件为空
			return resultMap(false,ApiResult.IMPORT_ORDERANDITEM_ISNULL);
		
		if(files.length != 2 || null == files[0] || null == files[1])//不是两个文件
			return resultMap(false,ApiResult.IMPORT_ORDERANDITEM_ISNULL);
		
		String name1 = files[0].getOriginalFilename().replaceAll("\\d+","");//截取文件名字
		String name2 = files[1].getOriginalFilename().replaceAll("\\d+","");
		if (!OrderImportUtil.getFileName().contains(name1)//不能修改文件名字
				|| !OrderImportUtil.getFileName().contains(name2))
			return resultMap(false,ApiResult.IMPORT_NAME_ERROR);
		
		if(name1.equals(name2))//两个文件不能相同
			return resultMap(false,ApiResult.IMPORT_ORDERANDITEM_ISNULL);
			
		if (!name1.equals(OrderImportUtil.ORDER_LIST)//必须一个为订单文件
				&& !name2.equals(OrderImportUtil.ORDER_LIST))
			return resultMap(false,ApiResult.IMPORT_ORDERANDITEM_ISNULL);
		
		if(name1.equals(OrderImportUtil.ORDER_LIST)){//返回订单文件索引
			return resultMap(true,"0");
		}else{
			return resultMap(true,"1");
		}
	}
	
	/**
	 * 返回结果集
	 */
	private Map<String,Object> resultMap(Boolean status,String msgKey){
 		Map<String,Object> map = new HashMap<String,Object>();
 		map.put("status", status);
 		map.put("msgKey", msgKey);
 		return map;
 	}
}
