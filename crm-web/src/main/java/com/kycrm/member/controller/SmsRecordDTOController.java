package com.kycrm.member.controller;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.message.SmsRecordVO;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.SXSSFExcelExportUtils;


@Controller
@RequestMapping(value = "/backstage")
public class SmsRecordDTOController extends BaseController{
	private static final Log logger = LogFactory.getLog(SmsRecordDTOController.class);
	@Autowired
	private SessionProvider sessionProvider;
	
	@Autowired
	private ISmsRecordDTOService smsRecordDTOService;
	
	@Autowired
	private IUserInfoService userInfoService;
	

	/**
	 * @Description: 短信发送记录查询
	 * @author HL
	 * @date 2018年1月26日 下午4:58:42
	 */
	@ResponseBody
	@RequestMapping(value="smsSendRecord",produces="text/html;charset=UTF-8")
	public String findSmsSendRecord(@RequestBody String params,
			HttpServletRequest request, HttpServletResponse response) {
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		
		SmsRecordVO vo = new SmsRecordVO();
		if (null != params && !"".equals(params)) {
			try {
				vo = JsonUtil.paramsJsonToObject(params, SmsRecordVO.class);
			} catch (Exception e) {
				e.printStackTrace();
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
		}
		vo.setUid(user.getId());
		String accessToken = userInfoService.findUserTokenById(user.getId());
		Map<String, Object> map = smsRecordDTOService.findSmsSendRecordPage(user.getId(),vo,accessToken);
		map.put("pageNo", vo.getPageNo());
		return successReusltMap(null).put(ApiResult.API_RESULT, map).toJson();
	}
	
	
	/**
	 * 短信发送记录导出
	 * @author HL
	 * @time 2018年2月5日 下午3:23:00 
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value="sendRecordExport",method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	public void smsSendRecordExport(@RequestBody String params,
			HttpServletRequest request, HttpServletResponse response) {
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null != user){
			SmsRecordVO vo = new SmsRecordVO();
			if (null != params && !"".equals(params)) {
				params = "{"+(URLDecoder.decode(params).replace("=", ":"))+"}";
				try {
					vo = JsonUtil.paramsJsonToObject(params, SmsRecordVO.class);
				} catch (Exception e) {
					logger.error("************短信发送记录********导出失败******参数错误！！！"+e.getMessage());
				}
			}
			
			vo.setUid(user.getId());
			
			//表头列名
			String[] rowName = new String[]{"短信类型","手机号  ","买家昵称","发送时间","短信内容","发送状态","计费（条数）"};
			List<Object[]> dataList = new ArrayList<Object[]>();//数据集合
			
			//防止传送过大，分页查询
			Long count = smsRecordDTOService.findSmsSendRecordAndExportCount(user.getId(),vo);
			logger.info("************短信发送记录********导出**********查询出条数:"+count);
			if(null == count || count==0 || count > 100000) return; 
			long end = 0,start = 0,page=10000;
			if(count<page){
				end	= 1;
			}else if(count%page==0){
				end  = count/page;
			}else{
				end  = (count+page)/page;
			}
			while(start<end){
				start++;
				Integer pageNo = (int) start;
				Integer currentRows = (int) page;
				vo.setCurrentRows(currentRows);
				vo.setPageNo(pageNo);
				String accessToken = userInfoService.findUserTokenById(user.getId());
				List<Object[]> export = smsRecordDTOService.findSmsSendRecordAndExport(user.getId(),vo,accessToken);
				dataList.addAll(export);
			}
			
			
			try {
				String filename = new String("短信记录报表.xlsx".getBytes(),"ISO-8859-1");
				//使用工具生产Excel表格
				SXSSFWorkbook workbook = SXSSFExcelExportUtils.export("短信记录报表", rowName, dataList);
				response.setHeader("Content-Disposition","attachment;filename=" + filename);
				workbook.write(response.getOutputStream());
			} catch (Exception e) {
				logger.error("************短信发送记录********导出失败******文件输出错误！！！"+e.getMessage());
			}finally{
				try {
					response.getOutputStream().flush();
					response.getOutputStream().close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else{
			logger.error("************短信发送记录********导出失败******用户为空！！！");
		}
	}
}
