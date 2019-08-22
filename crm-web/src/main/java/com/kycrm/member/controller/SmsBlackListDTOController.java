package com.kycrm.member.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.message.SmsBlackListDTO;
import com.kycrm.member.domain.entity.message.SmsBlackListImport;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.message.SmsBlackListVO;
import com.kycrm.member.service.member.IMemberDTOService;
import com.kycrm.member.service.message.ISmsBlackListImportService;
import com.kycrm.member.service.message.ISmsBlackListDTOService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.ExcelImportUtil;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.MobileRegEx;
import com.kycrm.util.TxtImportUtil;
import com.kycrm.util.thread.MyFixedThreadPool;

@Controller
@RequestMapping(value = "/member")
public class SmsBlackListDTOController extends BaseController {
	private static final Log logger = LogFactory.getLog(SmsBlackListDTOController.class);
	@Autowired
	private SessionProvider sessionProvider;
	@Autowired
	private ISmsBlackListDTOService smsBlackListDTOService;
	@Autowired
	private IMemberDTOService memberDTOService;
	@Autowired
	private ISmsBlackListImportService smsBlackListImportService;
	@Autowired
	private IUserInfoService userInfoService;
	
	//导入文件格式
	private final String IS_TXT = "txt";
	private final String IS_XLSX = "xlsx";
	private final String IS_XLS = "xls";
	/**
	 * 查询黑名单分页数据
	 * @author HL
	 * @time 2018年3月6日 下午3:45:14 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/findSmsBlackPage",produces="text/html;charset=UTF-8")
	public String findSmsBlackListPage(@RequestBody String params,
			HttpServletRequest request, HttpServletResponse response){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		
		SmsBlackListVO vo = new SmsBlackListVO();
		if (null != params && !"".equals(params)) {
			try {
				vo = JsonUtil.paramsJsonToObject(params, SmsBlackListVO.class);
			} catch (Exception e) {
				e.printStackTrace();
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
		}
		vo.setUid(user.getId());
		String accessToken = userInfoService.findUserTokenById(user.getId());
		Map<String, Object> map= smsBlackListDTOService.findSmsBlackListPage(user.getId(),vo,accessToken);
		map.put("pageNo", vo.getPageNo());
		return successReusltMap(null).put(ApiResult.API_RESULT, map).toJson();
	}
	/**
	 * 删除该用户的所以黑名单号码
	 * @author HL
	 * @time 2018年3月6日 下午3:45:38 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value="/deleteSmsBlack",produces="text/html;charset=UTF-8")
	public String deleteSmsBlackAll(@RequestBody String params,
			HttpServletRequest request, HttpServletResponse response){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		
		SmsBlackListVO vo = new SmsBlackListVO();
		if (null != params && !"".equals(params)) {
			try {
				vo = JsonUtil.paramsJsonToObject(params, SmsBlackListVO.class);
			} catch (Exception e) {
				e.printStackTrace();
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
		}
		vo.setUid(user.getId());
		Map<String,Object> map = smsBlackListDTOService.deleteSmsBlack(user.getId(),vo);
		if((Boolean) map.get("status")){
			List<String > buyerNicks = (List<String>) map.get("content");
			if(null != buyerNicks && buyerNicks.size()>0){
				memberDTOService.threadDisposeMemberStatus(vo.getUid(),buyerNicks, "normal");
			}
		}
		return successReusltMap(ApiResult.DEL_SUCCESS).toJson();
	}
	
	/**
	 * 手动添加黑名单
	 * @author HL
	 * @time 2018年3月8日 下午2:12:13 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/insertSmsBlack",produces="text/html;charset=UTF-8")
	public String insertSmsBlackList(@RequestBody String params,
			HttpServletRequest request, HttpServletResponse response){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		
		SmsBlackListVO vo = new SmsBlackListVO();
		if (null != params && !"".equals(params)) {
			try {
				vo = JsonUtil.paramsJsonToObject(params, SmsBlackListVO.class);
			} catch (Exception e) {
				e.printStackTrace();
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
		}
		vo.setUid(user.getId());
		vo.setAddSource("1");
		
		//验证内容
		if("1".equals(vo.getType())){
			List<String> newList = new ArrayList<String>();
			List<String> blackLists = vo.getBlackLists();
			for (String str : blackLists) {
				if(MobileRegEx.validateMobile(str)){
					newList.add(str);
				}
			}
			vo.setBlackLists(newList);
		}
		
		if(null == vo.getBlackLists() || vo.getBlackLists().size()==0)
			return failureReusltMap(ApiResult.CONTENT_ERROR_ISNULL).toJson();
		
		boolean result = this.insertSmsBlackList(user.getId(),vo,user);
		if(result){
			return successReusltMap(ApiResult.INSERT_SUCCESS).toJson();
		}
		return failureReusltMap(ApiResult.INSERT_FAILURE).toJson();
	}
	
	/**
	 * 导入黑名单数据
	 * @author HL
	 * @time 2018年3月8日 下午2:29:51 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/uploadBlackList",produces="text/html;charset=UTF-8")
	public String uploadBlackList(@RequestParam MultipartFile file,
			String type, HttpServletRequest request,
			HttpServletResponse response) {     
		UserInfo user = sessionProvider.getDefaultAttribute(requestToken());
		if(null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		
		try {
			//手机号码
			List<String> list = new ArrayList<String>();
			
			//文件名称
			String filename = file.getOriginalFilename();
			
			//总数
			int sumNum = 0;
			
			if(filename.contains(IS_TXT)){
				list = TxtImportUtil.gainTxtData(file.getInputStream());
			}else if(filename.contains(IS_XLSX) || filename.contains(IS_XLS)) {
				// 读取整个excel文档
				List<List<String>> dataLists = ExcelImportUtil.gainExcelData(file);
				for (List<String> data : dataLists) {
					if(null != data.get(0) && !"".equals(data.get(0))){
						sumNum++;
						if("1".equals(type) && MobileRegEx.validateMobile(data.get(0))){
							list.add(data.get(0).trim());
						}else{
							list.add(data.get(0).trim());
						}
					}
				}
			}else{
				return failureReusltMap(ApiResult.IMPORT_FILE_FORMATERROR).toJson();
			}
			
			if (null == list || list.size() == 0)
				return failureReusltMap(ApiResult.CONTENT_ERROR_ISNULL).toJson();
			
			// 判断文件是否过大
			if (list.size() > 150000)
				return failureReusltMap(ApiResult.IMPORT_FILE_OVERSIZE).toJson();

			//创建导入记录
			SmsBlackListImport blackListImport = new SmsBlackListImport();
			blackListImport.setUid(user.getId());
			blackListImport.setFileName(filename);
			blackListImport.setSumNum(sumNum);
			blackListImport.setSuccessNum(0);
			blackListImport.setStatus(2);
			blackListImport.setCreatedBy(user.getTaobaoUserNick());
			blackListImport.setLastModifiedBy(user.getTaobaoUserNick());
			Long rid = smsBlackListImportService.insertSmsBlackListImport(blackListImport);
			
			//封装vo
			SmsBlackListVO vo = new SmsBlackListVO();
			vo.setType(type);
			vo.setUid(user.getId());
			vo.setAddSource("2");
			vo.setBlackLists(list);
			this.threadInsertSmsBlackList(user.getId(),vo,user,rid);
			
			return successReusltMap(ApiResult.IMPORT_DISPOSE).toJson();
		} catch (Exception e) {
			logger.info("===========黑名单管理==============用户:"+user.getTaobaoUserNick()+"导入黑名单异常!!!====="+e.getMessage());
		}
		return failureReusltMap(ApiResult.IMPORT_FAILURE).toJson();
	}
	
	
	/**
	 * 查询黑名单导入记录数据
	 * @author HL
	 * @time 2018年3月6日 下午3:45:14 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/findSmsBlackImport",produces="text/html;charset=UTF-8")
	public String findSmsBlackImport(HttpServletRequest request, HttpServletResponse response){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		
		Map<String, Object> map= smsBlackListImportService.findSmsBlackListImport(user.getId());
		return successReusltMap(null).put(ApiResult.API_RESULT, map).toJson();
	}
	
	/**
	 * 删除黑名单导入记录数据
	 * @author HL
	 * @time 2018年3月6日 下午3:45:14 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/deleteBlackImport",produces="text/html;charset=UTF-8")
	public String deleteSmsBlackImport(@RequestBody String params,
			HttpServletRequest request, HttpServletResponse response){
		SmsBlackListVO vo = new SmsBlackListVO();
		if (null != params && !"".equals(params)) {
			try {
				vo = JsonUtil.paramsJsonToObject(params, SmsBlackListVO.class);
			} catch (Exception e) {
				e.printStackTrace();
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
		}
		smsBlackListImportService.deleteSmsBlackImport(vo.getId());
		return successReusltMap(ApiResult.DEL_SUCCESS).toJson();
	}
	
	
	
	
	
	public boolean insertSmsBlackList(Long uid,SmsBlackListVO vo,UserInfo user) {
			List<SmsBlackListDTO> list = this.packagingSmsBlack(vo, user);
			try {
				if(null != list && list.size()>0 ){
					long i = 0;//处理条数
					int dataSize = list.size();//总条数
					int start = 0,//开始次数
						end =0,//结束次数
						node = 1000;//每次处理多少条
					if(dataSize%node==0){
						end=dataSize/node;
					}else{
						end=(dataSize+node)/node;
					}
					while (start<end) {
						List<SmsBlackListDTO> subList = new ArrayList<SmsBlackListDTO>();
						if(start==(end-1)){
							subList = list.subList(start*node, dataSize);
						}else{
							subList = list.subList(start*node, (start+1)*node);
						}
						start++;
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("list", subList);
						map.put("uid", uid);
						i += smsBlackListDTOService.insertSmsBlackList(uid,map);
					}
					if(i>0){
						return true;
					}
				}
			} catch (Exception e) {
				logger.error("==========黑名单管理==========手动添加失败==="+vo.getUid()+"====="+e.getMessage());
			}
		return false;
	}
	
	
	
	private void threadInsertSmsBlackList(Long uid, SmsBlackListVO vo,
			UserInfo user, Long rid) {
			MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread(){
				@Override
				public void run() {
					try {
						List<SmsBlackListDTO> list = packagingSmsBlack(vo, user);
						if(null != list && list.size()>0 ){
							long i = 0;//处理条数
							int dataSize = list.size();//总条数
					    	int start = 0,//开始次数
					    		end =0,//结束次数
					    		node = 1000;//每次处理多少条
					    	if(dataSize%node==0){
					    		end=dataSize/node;
					    	}else{
					    		end=(dataSize+node)/node;
					    	}
					    	
					    	Map<String, Object> rMap = new HashMap<String, Object>();
					    	rMap.put("id", rid);
					    	rMap.put("status", 2);
					    	
					    	while (start<end) {
					    		List<SmsBlackListDTO> subList = new ArrayList<SmsBlackListDTO>();
					    		if(start==(end-1)){
					    			subList = list.subList(start*node, dataSize);
					    		}else{
					    			subList = list.subList(start*node, (start+1)*node);
					    		}
					    		start++;
					    		Map<String, Object> map = new HashMap<String, Object>();
								map.put("list", subList);
								map.put("uid", uid);
								i += smsBlackListDTOService.insertSmsBlackList(uid,map);
								if(i>0){
									rMap.put("successNum", i);
									smsBlackListImportService.updateSmsBlackListImport(rMap);
								}
							}
					    	rMap.put("successNum", i);
					    	if(i==0){
					    		rMap.put("status", 1);
					    	}else{
					    		rMap.put("status", 0);
					    	}
					    	smsBlackListImportService.updateSmsBlackListImport(rMap);
						}
					} catch (Exception e) {
						logger.error("==========黑名单管理==========导入添加失败==="+vo.getUid()+"====="+e.getMessage());
					}
				}
			});
	}
	
	
	/**
	 * 封装黑名单数据
	 * @author HL
	 * @time 2018年3月7日 下午5:06:53 
	 * @param vo
	 * @param user
	 * @return
	 */
	private List<SmsBlackListDTO> packagingSmsBlack(SmsBlackListVO vo,UserInfo user) {
		
		List<SmsBlackListDTO> list = new ArrayList<SmsBlackListDTO>();
		List<String> buyerNicks = new ArrayList<String>();
		List<String> blacks = vo.getBlackLists();
		String accessToken = user.getAccessToken();
		
		for (String black : blacks) {
			if(null !=black && !"".equals(black)){
				SmsBlackListDTO smsBlack = new SmsBlackListDTO();
				smsBlack.setUid(vo.getUid());
				smsBlack.setAddSource(vo.getAddSource());
				smsBlack.setType(vo.getType());
				smsBlack.setCreatedBy(user.getTaobaoUserNick());
				smsBlack.setLastModifiedBy(user.getTaobaoUserNick());
				
				if("1".equals(vo.getType())){
					if(!MobileRegEx.validateMobile(black))
						continue;
						
					try {
						String encryptPhone = EncrptAndDecryptClient.getInstance().encryptData(black, EncrptAndDecryptClient.PHONE,accessToken);
						smsBlack.setPhone(encryptPhone);
						smsBlack.setNickOrPhone(encryptPhone);
					} catch (Exception e) {
						try {
							accessToken = userInfoService.findUserTokenById(vo.getUid());
							String encryptPhone = EncrptAndDecryptClient.getInstance().encryptData(black, EncrptAndDecryptClient.PHONE,accessToken);
							smsBlack.setPhone(encryptPhone);
							smsBlack.setNickOrPhone(encryptPhone);
						} catch (Exception e1) {
							logger.error("*********黑名单管理**********加密出错，********************"+e.getMessage());
							smsBlack.setPhone(black);
							smsBlack.setNickOrPhone(black);
							continue;
						}
					}
				}
				
				if("2".equals(vo.getType())){
					try {
						String encryptName = EncrptAndDecryptClient.getInstance().encryptData(black, EncrptAndDecryptClient.SEARCH, accessToken);
						smsBlack.setNick(encryptName);
						smsBlack.setNickOrPhone(encryptName);
						buyerNicks.add(encryptName);
					} catch (Exception e) {
						try {
							accessToken = userInfoService.findUserTokenById(vo.getUid());
							String encryptName = EncrptAndDecryptClient.getInstance().encryptData(black, EncrptAndDecryptClient.SEARCH, accessToken);
							smsBlack.setNick(encryptName);
							smsBlack.setNickOrPhone(encryptName);
							buyerNicks.add(encryptName);
						} catch (Exception e1) {
							logger.error("********黑名单管理***********加密出错，********************"+e.getMessage());
							smsBlack.setNick(black);
							smsBlack.setNickOrPhone(black);
							continue;
						}
					}
				}
				list.add(smsBlack);
			}
		}
		memberDTOService.threadDisposeMemberStatus(vo.getUid(),buyerNicks,"blacklist");
		return list;
	}

}
