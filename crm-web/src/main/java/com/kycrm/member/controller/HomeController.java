package com.kycrm.member.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.effect.EffectStandardRFM;
import com.kycrm.member.domain.entity.effect.MarketingCenterEffect;
import com.kycrm.member.domain.entity.effect.TradeCenterEffect;
import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.domain.entity.message.SmsRecordDTO;
import com.kycrm.member.domain.entity.notice.Notice;
import com.kycrm.member.domain.entity.other.TaskNode;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.effect.TradeCenterEffectVO;
import com.kycrm.member.domain.vo.member.MemberInfoVO;
import com.kycrm.member.domain.vo.receive.ReceiveInfoVO;
import com.kycrm.member.domain.vo.trade.TradeVO;
import com.kycrm.member.service.effect.IItemDetailService;
import com.kycrm.member.service.effect.IMarketingCenterEffectService;
import com.kycrm.member.service.effect.ITradeCenterEffectService;
import com.kycrm.member.service.member.IMemberDTOService;
import com.kycrm.member.service.message.IMsgSendRecordService;
import com.kycrm.member.service.message.IMsgTempTradeService;
import com.kycrm.member.service.message.ISmsReceiveInfoService;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.notice.INoticeService;
import com.kycrm.member.service.order.IOrderDTOService;
import com.kycrm.member.service.other.IShortLinkService;
import com.kycrm.member.service.other.ITaskNodeService;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.member.service.trade.ITradeDTOService;
import com.kycrm.member.service.tradecenter.ITradeSetupService;
import com.kycrm.member.service.traderate.ITradeRatesService;
import com.kycrm.member.service.user.IUserAccountService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.service.util.IMsgTempTradeQueueService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.DateUtils;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.MsgType;
import com.kycrm.util.NumberUtils;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.TradesInfo;
import com.kycrm.util.XSSFDateUtil;

/**
 * 首页Controller
 * @ClassName: HomeController  
 * @author ztk
 * @date 2018年1月18日 下午4:02:21 
 */
@Controller
@RequestMapping("/home")
public class HomeController extends BaseController {
	
	private Logger logger = LoggerFactory.getLogger(HomeController.class);
	public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";  
    public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";  
    public static final String EMPTY = "";  
    public static final String POINT = ".";  
    public static SimpleDateFormat sdf =   new SimpleDateFormat("yyyy/MM/dd");

	@Autowired
    private ITaskNodeService taskNodeService;
	@Autowired
	private IMemberDTOService memberService;
	
	@Autowired
	private ITradeCenterEffectService tradeCenterEffectService;
	
	@Autowired
	private IMarketingCenterEffectService marketingCenterEffectService;
	
	@Autowired
	private ISmsReceiveInfoService receiveInfoService;
	
	@Autowired
	private ITradeDTOService tradeDTOService;
	
	@Autowired
	private ISmsRecordDTOService smsRecordDTOService;
	
	@Autowired
	private SessionProvider sessionProvider;
	
	@Autowired
	private IMsgSendRecordService msgSendRecordService;
	
	@Autowired
	private ITradeRatesService tradeRatesService;
	
	@Autowired
	private IUserAccountService userAccountService;
	
	@Autowired
	private IMsgTempTradeQueueService tempTradeQueueService;
	
	@Autowired
	private IItemDetailService itemDetailService;
	
	@Autowired
	private IOrderDTOService orderDTOService;
	
	@Autowired
	private IMsgTempTradeService tempTradeService;
	
	@Autowired
	private IUserInfoService userInfoService;
	
	@Autowired
	private INoticeService noticeService;
	
	@Autowired
	private ITradeSetupService tradeSetupService;
	
	@Autowired
	private IShortLinkService shortLinkService;
	
	@Autowired
	private ICacheService cacheService;
	
/*	@Autowired
	private IEffectStandardRFMService effectStandardRFMService;
*/	
	
	@RequestMapping("/index")
	public String homeIndex(){
		return "index";
	}
	
	/**
	 * todayConsumeDetail(首页今日消费明细)
	 * @Title: todayConsumeDetail 
	 * @param @param request
	 * @param @param response
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping(value="/indexTodayConsume",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String todayConsumeDetail(HttpServletRequest request,HttpServletResponse response){
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		long l8 = System.currentTimeMillis();
		//今日发送客户数
		Integer sendCustomerCount = smsRecordDTOService.countRecordByTime(userInfo.getId(),DateUtils.getStartTimeOfDay(new Date()),new Date());
		long l9 = System.currentTimeMillis();
		logger.info("计算今日发送客户数时间：" + (l9 - l8) + "ms");
		//今日扣除短信条数
		Integer sendSmsCount = smsRecordDTOService.sumSmsNumByTime(userInfo.getId(),DateUtils.getStartTimeOfDay(new Date()),new Date());
		long l10 = System.currentTimeMillis();
		logger.info("计算今日发送客户数时间：" + (l10 - l9) + "ms");
		ResultMap<String,Object> resultMap = rsMap(100, "操作成功").put("status", true);
		resultMap.put("sendCustomerCount", NumberUtils.getResult(sendCustomerCount));
		resultMap.put("sendSmsCount", NumberUtils.getResult(sendSmsCount));
		return resultMap.toJson();
	}
	
	/**
	 * index(店铺会员数、昨日催付回款金额、昨日营销回款金额、昨日新增客户数、昨日客户回复数、昨日中差评订单数、昨日整体ROI)
	 * @Title: index 
	 * @param @param request
	 * @param @param response
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping(value="/indexConstant",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String index(HttpServletRequest request,HttpServletResponse response){
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		ResultMap<String, Object> resultMap = rsMap(100, "操作成功").put("status", true);
		//店铺会员数
		MemberInfoVO memberInfoVO = new MemberInfoVO();
		memberInfoVO.setUid(userInfo.getId());
		long memberCount = memberService.countMemberByParam(userInfo.getId(),memberInfoVO);
		try {
			String cacheMapStr = cacheService.get(RedisConstant.RedisCacheGroup.YESTERDAY_DATA_CACHE, userInfo.getId() + "", String.class);
			if(cacheMapStr != null && !"".equals(cacheMapStr)){
				@SuppressWarnings("unchecked")
				Map<String, Object> cacheMap = JsonUtil.fromJson(cacheMapStr, Map.class);
				if(cacheMap != null && !cacheMap.isEmpty()){
					resultMap.put("reminderFee", cacheMap.get("reminderFee") == null? 0 : cacheMap.get("reminderFee"));
					resultMap.put("yesterMemberMoney", cacheMap.get("yesterMemberMoney") == null? 0 : cacheMap.get("yesterMemberMoney"));
					resultMap.put("yesterdayCreateMember", cacheMap.get("yesterdayCreateMember") == null? 0 : cacheMap.get("yesterdayCreateMember"));
					resultMap.put("receiveCount", cacheMap.get("receiveCount") == null? 0 : cacheMap.get("receiveCount"));
					resultMap.put("badTradeCount", cacheMap.get("badTradeCount") == null? 0 : cacheMap.get("badTradeCount"));
					resultMap.put("yesterROI", cacheMap.get("yesterROI") == null? "0:0" : cacheMap.get("yesterROI"));
					resultMap.put("memberCount", memberCount);
					resultMap.put("userId", userInfo.getTaobaoUserNick());
					return resultMap.toJson();
				}
			}
		} catch (Exception e) {
			logger.info("从缓存中取出昨日数据异常：" + e.getMessage());
			e.printStackTrace();
		}
		//昨天0点
		Date yesterStart = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(1, new Date()));
		//昨天24点
		Date yesterEnd = DateUtils.getEndTimeOfDay(DateUtils.nDaysAgo(1, new Date()));
		long l1 = System.currentTimeMillis();
		long l2 = System.currentTimeMillis();
		logger.info("计算店铺会员数时间：" + (l2 - l1) + "ms");
		//昨日催付回款金额
		List<String> typeList = new ArrayList<String>();
		typeList.add(MsgType.MSG_CGCF);
		typeList.add(MsgType.MSG_ECCF);
		typeList.add(MsgType.MSG_JHSCF);
		double reminderFee = tradeCenterEffectService.sumRefundFee(userInfo.getId(), typeList, yesterStart, yesterEnd);
		long l3 = System.currentTimeMillis();
		logger.info("计算昨日催付回款金额时间：" + (l3 - l2) + "ms");
		//昨日营销回款金额
		double yesterMemberMoney = tempTradeService.sumPaymentByDate(userInfo.getId(), yesterStart, yesterEnd);
		long l4 = System.currentTimeMillis();
		logger.info("计算昨日营销回款金额时间：" + (l4 - l3) + "ms");
		//昨日新增客户数
		memberInfoVO.setBeginTime(yesterStart);
		memberInfoVO.setEndTime(yesterEnd);
		long yesterdayCreateMember = memberService.countMemberByParam(userInfo.getId(),memberInfoVO);
		long l5 = System.currentTimeMillis();
		logger.info("计算昨日新增客户数时间：" + (l5 - l4) + "ms");
		//昨日客户回复数
		ReceiveInfoVO receiveInfoVO = new ReceiveInfoVO();
		receiveInfoVO.setUid(userInfo.getId());
		receiveInfoVO.setbTime(DateUtils.dateToStringHMS(yesterStart));
		receiveInfoVO.seteTime(DateUtils.dateToStringHMS(yesterEnd));
		long receiveCount = receiveInfoService.countReceiveInfo(userInfo.getId(),receiveInfoVO,userInfo);
		long l6 = System.currentTimeMillis();
		logger.info("计算昨日客户回复数时间：" + (l6 - l5) + "ms");
		//昨日中差评订单数
		List<String> resultList = new ArrayList<>();
		resultList.add("neutral");
		resultList.add("bad");
		Long badTradeCount = tradeRatesService.countTradeByParam(userInfo.getId(), yesterStart, yesterEnd, resultList);
		long l7 = System.currentTimeMillis();
		logger.info("计算昨日中差评订单数时间：" + (l7 - l6) + "ms");
		//昨日整体ROI
		Integer yesterSmsNum = smsRecordDTOService.sumSmsNumByTime(userInfo.getId(), yesterStart, yesterEnd);
		String yesterROI = creatROI(NumberUtils.getResult(yesterSmsNum) * 0.05, yesterMemberMoney + reminderFee);
//		DecimalFormat decimalFormat = new DecimalFormat("0.00");
//		String yesterROI = decimalFormat.format(sendSmsMoney) + "";
		long l8 = System.currentTimeMillis();
		logger.info("计算昨日整体ROI时间：" + (l8 - l7) + "ms");
		
		
		resultMap.put("memberCount", memberCount);
		resultMap.put("userId", userInfo.getTaobaoUserNick());
		resultMap.put("reminderFee", reminderFee);
		resultMap.put("yesterMemberMoney", yesterMemberMoney);
		resultMap.put("yesterdayCreateMember", yesterdayCreateMember);
		
		resultMap.put("receiveCount", receiveCount);
		resultMap.put("badTradeCount", badTradeCount);
		resultMap.put("yesterROI", yesterROI);
		
		resultMap.put("yesterSmsNum", yesterSmsNum);
		return resultMap.toJson();
//		return "index";
	}
	
	/**
	 * 首页图表数据
	 * @Title: indexDataChart 
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping(value="/indexChart",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String indexDataChart(HttpServletRequest request,HttpServletResponse response){
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		//昨天0点
		Date yesterStart = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(1, new Date()));
		//七天前的0点
		Date weekBeforeDate = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(7, new Date()));
		//一个月前的0点
		Date monthBeforeDate = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(30, new Date()));
		//昨天24点
		Date yesterEnd = DateUtils.getEndTimeOfDay(DateUtils.nDaysAgo(1, new Date()));
		//昨日每小时下单数详情
		List<TradeDTO> tradeList = marketingCenterEffectService.queryTradeNumByHour(userInfo.getId(), yesterStart, yesterEnd);
		List<TradeDTO> weekTradeList = marketingCenterEffectService.queryTradeNumByHour(userInfo.getId(), weekBeforeDate, yesterEnd);
		List<TradeDTO> monthTradeList = marketingCenterEffectService.queryTradeNumByHour(userInfo.getId(), monthBeforeDate, yesterEnd);
		List<String> hourData = new ArrayList<String>();
		List<Object> tradeNum = new ArrayList<Object>();
		List<Object> weekTradeNum = new ArrayList<Object>();
		List<Object> monthTradeNum = new ArrayList<Object>();
		Map<String, Object> map = new LinkedHashMap<>();
		Map<String, Object> weekMap = new LinkedHashMap<>();
		Map<String, Object> monthMap = new LinkedHashMap<>();
		for(int i = 0; i < 24; i++){
			String hours = i + "";
			map.put(hours, 0);
			weekMap.put(hours, 0);
			monthMap.put(hours, 0);
		}
		if(tradeList != null && !tradeList.isEmpty()){
			for (TradeDTO tradeDTO : tradeList) {
				if(map.containsKey(tradeDTO.getTidStr())){
					map.put(tradeDTO.getTidStr(), tradeDTO.getTid());
				}
			}
		}
		if(weekTradeList != null && !weekTradeList.isEmpty()){
			for (TradeDTO tradeDTO : weekTradeList) {
				if(weekMap.containsKey(tradeDTO.getTidStr())){
					weekMap.put(tradeDTO.getTidStr(), tradeDTO.getTid());
				}
			}
		}
		if(monthTradeList != null && !monthTradeList.isEmpty()){
			for (TradeDTO tradeDTO : monthTradeList) {
				if(monthMap.containsKey(tradeDTO.getTidStr())){
					monthMap.put(tradeDTO.getTidStr(), tradeDTO.getTid());
				}
			}
		}
		if(map != null && !map.isEmpty()){
			Set<Entry<String,Object>> entrySet = map.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				hourData.add(entry.getKey() + ":00");
				tradeNum.add(entry.getValue());
			}
		}
		if(weekMap != null && !weekMap.isEmpty()){
			Set<Entry<String,Object>> entrySet = weekMap.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				weekTradeNum.add(entry.getValue());
			}
		}
		if(monthMap != null && !monthMap.isEmpty()){
			Set<Entry<String,Object>> entrySet = monthMap.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				monthTradeNum.add(entry.getValue());
			}
		}
		//近七天每日的下单金额金额
		/*Date beginTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(6, new Date()));*/
		List<String> dateList = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			String nDaysAgo = DateUtils.dateToString(DateUtils.nDaysAgo(i, new Date()));
			dateList.add(nDaysAgo);
		}
		List<Double> weekMoneyList = smsRecordDTOService.listDaysSmsMoney(userInfo.getId(), new Date());
		List<Double> weekPaymentList = tradeDTOService.listDaysPayment(userInfo.getId(), new Date());
		ResultMap<String, Object> resultMap = rsMap(100, "操作成功").put("status", true);
		resultMap.put("dateList", dateList);
		resultMap.put("weekMoneyList", weekMoneyList);
		resultMap.put("weekPaymentList", weekPaymentList);
		resultMap.put("hourData", hourData);
		resultMap.put("tradeNum", tradeNum);
		resultMap.put("weekTradeNum", weekTradeNum);
		resultMap.put("monthTradeNum", monthTradeNum);
		return resultMap.toJson();
	}
	
	/**
	 * 查询软件到期时间和短信剩余条数
	 * @Title: asd 
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("/indexExpirationTime")
	@ResponseBody
	public String indexExpirationTime(HttpServletRequest request,HttpServletResponse response){
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		logger.info("userinfo ========:" + JSONObject.toJSONString(userInfo));
		//软件到期时间
		if (userInfo != null && userInfo.getExpirationTime() != null) {
			ResultMap<String,Object> resultMap = rsMap(100, "操作成功").put("status", true);
			Long userSms = userAccountService.findUserAccountSms(userInfo.getId());
			resultMap.put("userSms", userSms);
			//小于一天显示小时,大于一天显示天,忽略整整一天的情况
			if((userInfo.getExpirationTime().getTime()-System.currentTimeMillis()) < 86400000){
				resultMap.put("hours", (userInfo.getExpirationTime().getTime()-System.currentTimeMillis())/(3600000));
				logger.info("用户id ：" + userInfo.getId() + "查询得出的到期时间:" + (userInfo.getExpirationTime().getTime()-System.currentTimeMillis())/(3600000) + "短信条数：" + userSms);
			}else{
				resultMap.put("days", (userInfo.getExpirationTime().getTime()-System.currentTimeMillis())/(86400000)+1);
				logger.info("用户id ：" + userInfo.getId() + "查询得出的到期时间" + (userInfo.getExpirationTime().getTime()-System.currentTimeMillis())/(86400000)+1  + "短信条数：" + userSms);
			}
			return resultMap.toJson();
		}else {
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
	}
	
	/**
	 * listHomeLink(查询首页用户设置的快捷入口)
	 * @Title: listHomeLink 
	 * @param @param request
	 * @param @param response
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("/listLink")
	@ResponseBody
	public String listHomeLink(HttpServletRequest request, HttpServletResponse response){
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo == null || userInfo.getId() == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		String linkStr = userInfoService.findLinkByUid(userInfo.getId());
		ResultMap<String,Object> resultMap = rsMap(100, "操作成功").put("status", true);
		resultMap.put("data", linkStr);
		return resultMap.toJson();
	}
	
	/**
	 * 更新快捷入口
	 * @Title: updateShortcut 
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("/updateLink")
	@ResponseBody
	public String updateShortcut(@RequestBody String params,HttpServletRequest request,
			HttpServletResponse response){
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo == null || userInfo.getId() == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		UserInfo userParam = null;
		try {
			userParam = JsonUtil.paramsJsonToObject(params, UserInfo.class);
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(102, "操作失败,请重新操作或联系系统管理员!").put("status", false).toJson();
		}
		if(userParam == null){
			return rsMap(101, "操作失败").put("status", false).toJson();
		}
		String linkStr = userParam.getShortcutLinkStr();
		if(linkStr != null && !"".equals(linkStr)){
			String[] split = linkStr.split(",");
			if(split.length > 4){
				return rsMap(101, "操作失败,链接最多可以保存4个,请重新操作").put("status", false).toJson();
			}
		}
		try {
			userInfoService.updateUserShortcutLinkStr(userInfo.getId(),linkStr);
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(101, "更新失败").put("status", false).toJson();
		}
		ResultMap<String,Object> resultMap = rsMap(100, "操作成功").put("status", true);
		return resultMap.toJson();
	}
	
	/**
	 * 计算ROI
	 * ZTK2017年7月7日上午9:54:08
	 */
	private String creatROI(Double smsMoney,Double returnMoney){
		double result = NumberUtils.getResult(smsMoney);
		double result2 = NumberUtils.getResult(returnMoney);
		if(result != 0.00){
			double roi = result2 / result;
			return "1:" + NumberUtils.getTwoDouble(roi);
		}else {
			return NumberUtils.getTwoDouble(result) + ":0";
		}
	}
	
	@SuppressWarnings("static-access")
	@RequestMapping("/importData")
	public String importExcelData(HttpServletRequest request,HttpServletResponse response,
			@RequestParam("filename") MultipartFile file){
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!----------------------------");
		if(userInfo == null || userInfo.getId() == null){
			return null;
		}
		int node = 1;
		long l1 = System.currentTimeMillis();
		memberService.doCreateTableByNewUser(userInfo.getId());
//		smsRecordDTOService.doCreateTableByNewUser(userInfo.getId());
//		orderDTOService.doCreateTableByNewUser(userInfo.getId());
//		tradeDTOService.doCreateTableByNewUser(userInfo.getId());
//		List<TradeDTO> tradeDTOs = new ArrayList<>();
//		List<OrderDTO> orderDTOs = new ArrayList<>();
		List<MemberInfoDTO> memberInfoDTOs = new ArrayList<>();
		XSSFWorkbook workbook = null;
		InputStream inputStream = null;
		try {
			inputStream = file.getInputStream();
			workbook = new XSSFWorkbook(inputStream);
			for(int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++ ){
				XSSFSheet sheet = workbook.getSheetAt(sheetNum);
				if(sheet == null){
					continue;
				}
				int totalRows = sheet.getLastRowNum();
				for(int rowNum = 0;rowNum < totalRows; rowNum++){
					XSSFRow row = sheet.getRow(rowNum);
					if(row != null){
//						OrderDTO orderDTO = new OrderDTO();
//						SmsRecordDTO recordDTO = new SmsRecordDTO();
//						TradeDTO tradeDTO = new TradeDTO();
						MemberInfoDTO memberDTO = new MemberInfoDTO();
						short totalCell = row.getLastCellNum();
						memberDTO.setUid(1243L);
						memberDTO.setBuyerNick(this.getXValue(row.getCell(0)));
						memberDTO.setTradeNum(Long.parseLong(this.getXValue(row.getCell(2))));
						memberDTO.setTradeAmount(new BigDecimal(Double.parseDouble(this.getXValue(row.getCell(3)))));
						memberDTO.setCloseTradeAmount(new BigDecimal(Double.parseDouble(this.getXValue(row.getCell(4)))));
						memberDTO.setCloseTradeNum(0L);
						memberDTO.setProvince("北京");
						memberDTO.setCity("北京");
						memberDTO.setItemNum(Long.parseLong(this.getXValue(row.getCell(8))));
						memberDTO.setAvgTradePrice(new BigDecimal(Double.parseDouble(this.getXValue(row.getCell(9)))));
						memberDTO.setLastTradeTime(new Date(Long.parseLong(this.getXValue(row.getCell(11)))));
						memberDTO.setCloseItemNum(0L);
						memberDTO.setMobile(this.getXValue(row.getCell(13)));
						memberDTO.setReceiverName(this.getXValue(row.getCell(21)));
						if(rowNum % 3 == 0){
							memberDTO.setLastMarketingTime(DateUtils.getTimeByDay(new Date(), -1));
							memberDTO.setLastSendTime(new Date());
							memberDTO.setPayItemNum(0L);
							memberDTO.setAddItemNum(2L);
							memberDTO.setFirstPayTime(DateUtils.getTimeByDay(new Date(), -6));
							memberDTO.setSecondPayTime(DateUtils.getTimeByDay(new Date(), -5));
							memberDTO.setLastPayTime(DateUtils.getTimeByDay(new Date(), -5));
							memberDTO.setBuyNumber(6);
							memberDTO.setAddNumber(8);
							memberDTO.setAddNotPayNumber(2);
							memberDTO.setTotalPayFee(new BigDecimal(32.5));
							memberDTO.setFirstPayItem(1000000000L);
							memberDTO.setLastPayItem(2000000000L);
							memberDTO.setRefundNumber(0);
							memberDTO.setRefundFee(new BigDecimal(0));
							memberDTO.setMarketingSmsNumber(1);
						}else if(rowNum % 3 == 1){
							memberDTO.setLastMarketingTime(DateUtils.getTimeByDay(new Date(), -2));
							memberDTO.setLastSendTime(DateUtils.getTimeByDay(new Date(), -2));
							memberDTO.setPayItemNum(3L);
							memberDTO.setAddItemNum(5L);
							memberDTO.setFirstPayTime(DateUtils.getTimeByDay(new Date(), -10));
							memberDTO.setSecondPayTime(DateUtils.getTimeByDay(new Date(), -7));
							memberDTO.setLastPayTime(DateUtils.getTimeByDay(new Date(), -6));
							memberDTO.setBuyNumber(10);
							memberDTO.setAddNumber(12);
							memberDTO.setAddNotPayNumber(2);
							memberDTO.setTotalPayFee(new BigDecimal(20.2));
							memberDTO.setFirstPayItem(3000000000L);
							memberDTO.setLastPayItem(4000000000L);
							memberDTO.setRefundNumber(2);
							memberDTO.setRefundFee(new BigDecimal(6));
							memberDTO.setMarketingSmsNumber(5);
						}else{
							memberDTO.setLastMarketingTime(new Date());
							memberDTO.setLastSendTime(DateUtils.getTimeByDay(new Date(), -1));
							memberDTO.setPayItemNum(6L);
							memberDTO.setAddItemNum(8L);
							memberDTO.setFirstPayTime(DateUtils.getTimeByDay(new Date(), -26));
							memberDTO.setSecondPayTime(DateUtils.getTimeByDay(new Date(), -20));
							memberDTO.setLastPayTime(DateUtils.getTimeByDay(new Date(), -8));
							memberDTO.setBuyNumber(2);
							memberDTO.setAddNumber(4);
							memberDTO.setAddNotPayNumber(2);
							memberDTO.setTotalPayFee(new BigDecimal(10.0));
							memberDTO.setFirstPayItem(5000000000L);
							memberDTO.setLastPayItem(6000000000L);
							memberDTO.setRefundNumber(3);
							memberDTO.setRefundFee(new BigDecimal(18));
							memberDTO.setMarketingSmsNumber(10);
						}
						if(row.getCell(23) != null){
							if(Integer.parseInt(this.getXValue(row.getCell(23))) == 0){
								memberDTO.setRefundFlag(false);
							}
						}
						memberDTO.setStatus("1");
						memberDTO.setNeutralBadRate(false);
						
//						orderDTO.setBuyerNick(this.getXValue(row.getCell(0)));
//						orderDTO.setCreatedDate(new Date(Long.parseLong(this.getXValue(row.getCell(1)))));
//						orderDTO.setNumIid(Long.parseLong(this.getXValue(row.getCell(2))));
//						orderDTO.setNum(Long.parseLong(this.getXValue(row.getCell(3))));
//						orderDTO.setPayment(new BigDecimal(Double.parseDouble(this.getXValue(row.getCell(4)))));
//						orderDTO.setPrice(new BigDecimal(Double.parseDouble(this.getXValue(row.getCell(5)))));
//						orderDTO.setTitle(this.getXValue(row.getCell(6)));
//						orderDTO.setOrderFrom(this.getXValue(row.getCell(7)));
//						orderDTO.setTid(Long.parseLong(this.getXValue(row.getCell(8))));
//						orderDTO.setOid(Long.parseLong(this.getXValue(row.getCell(9))));
//						orderDTO.setRefundStatus(this.getXValue(row.getCell(10)));
//						orderDTO.setStatus(this.getXValue(row.getCell(11)));
//						orderDTO.setTradeSource(1);
						/*recordDTO.setActualDeduction(Integer.parseInt(this.getXValue(row.getCell(0))));
						recordDTO.setContent(this.getXValue(row.getCell(2)));
						recordDTO.setMsgId(Long.parseLong(this.getXValue(row.getCell(3))));
						recordDTO.setRecNum(this.getXValue(row.getCell(4)));
						recordDTO.setSendTime(DateUtils.parseDate("2018-06-08 19:40:00"));
						recordDTO.setType("33");
						recordDTO.setUserId("溜溜梅旗舰店");
						recordDTO.setUid(userInfo.getId());*/
						
						
						/*if(row.getCell(1) != null && !"".equals(this.getXValue(row.getCell(1)))){
							tradeDTO.setConsignTime(new Date(Long.parseLong(this.getXValue(row.getCell(1)))));
						}
						tradeDTO.setCreated(new Date(Long.parseLong(this.getXValue(row.getCell(2)))));
						if(row.getCell(3) != null && !"".equals(this.getXValue(row.getCell(3)))){
							tradeDTO.setEndTime(new Date(Long.parseLong(this.getXValue(row.getCell(3)))));
						}
						if(row.getCell(5) != null && !"".equals(this.getXValue(row.getCell(5)))){
							tradeDTO.setPayment(new BigDecimal(Double.parseDouble(this.getXValue(row.getCell(5)))));
						}
						if(row.getCell(6) != null && !"".equals(this.getXValue(row.getCell(6)))){
							tradeDTO.setPayTime(new Date(Long.parseLong(this.getXValue(row.getCell(6)))));
						}
						if(row.getCell(7) != null && !"".equals(this.getXValue(row.getCell(7)))){
							tradeDTO.setReceiverAddress(this.getXValue(row.getCell(7)));
						}
						if(row.getCell(8) != null && !"".equals(this.getXValue(row.getCell(8)))){
							tradeDTO.setReceiverMobile(this.getXValue(row.getCell(8)));
						}
						if(row.getCell(9) != null && !"".equals(this.getXValue(row.getCell(9)))){
							tradeDTO.setSellerFlag(Long.parseLong(this.getXValue(row.getCell(9))));
						}
						if(row.getCell(10) != null && !"".equals(this.getXValue(row.getCell(10)))){
							tradeDTO.setStatus(this.getXValue(row.getCell(10)));
						}
						if(row.getCell(11) != null && !"".equals(this.getXValue(row.getCell(11)))){
							tradeDTO.setRefundFlag(Boolean.parseBoolean(this.getXValue(row.getCell(11))));
						}
						tradeDTO.setTid(Long.parseLong(this.getXValue(row.getCell(12))));
						tradeDTO.setType(this.getXValue(row.getCell(13)));
						if(row.getCell(14) != null && !"".equals(this.getXValue(row.getCell(14)))){
							tradeDTO.setModified(new Date(Long.parseLong(this.getXValue(row.getCell(14)))));
						}
						tradeDTO.setTradeSource(2);
						recordDTOs.add(recordDTO);*/
//						orderDTOs.add(orderDTO);
						memberInfoDTOs.add(memberDTO);
						System.err.println("memberInfoDTOs.size() =" + memberInfoDTOs.size() + ",node = " +node);
						if(memberInfoDTOs.size() >= 1000){
							System.err.println("memberInfoDTOs.size() =" + memberInfoDTOs.size() + "，dayu10000");
//							smsRecordDTOService.doCreaterSmsRecordDTOByList(userInfo.getId(), recordDTOs);
//							orderDTOService.batchInsertOrderList(userInfo.getId(), orderDTOs);
							memberService.batchSaveMemberData(userInfo.getId(), memberInfoDTOs);
							memberInfoDTOs.clear();
							node += 1;
						}
						if(node == 29 && memberInfoDTOs.size() >= 6){
							System.err.println("memberInfoDTOs.size() =" + memberInfoDTOs.size() + "，dayu10000 + node = " + node);
//							smsRecordDTOService.doCreaterSmsRecordDTOByList(userInfo.getId(), orderDTOs);
							memberService.batchSaveMemberData(userInfo.getId(), memberInfoDTOs);
							memberInfoDTOs.clear();
							node += 1;
						}
//						if(node == 3 && orderDTOs.size() >= 2){
//							System.err.println("orderDTOs.size() =" + orderDTOs.size() + "，dayu10000 + node = " + node);
////							smsRecordDTOService.doCreaterSmsRecordDTOByList(userInfo.getId(), orderDTOs);
//							orderDTOService.batchInsertOrderList(userInfo.getId(), orderDTOs);
//							orderDTOs.clear();
//							node += 1;
//						}
//						for(int cellNum = 0;cellNum < totalCell + 1; cellNum++){
//							XSSFCell cell = row.getCell(cellNum);
//							if(cell == null){
//								continue;
//							}
//						}
					}
				}
//				tradeDTOService.batchInsertTradeList(userInfo.getId(), tradeDTOs);
			}
			logger.info("导入完成，耗时：" + (System.currentTimeMillis() - l1) + "ms");
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/*// 创建csv接收对象
		CsvReaderUtils r = null;
		try {
			// 获取文件流
			InputStream inputStream = file.getInputStream();
			// 生成CsvReader对象，以，为分隔符，GBK编码方式
			r = new CsvReaderUtils(inputStream, ',', Charset.forName("GBK"));
//			r = new CsvReaderUtils(inputStream);
			// 读取表头
//			r.readHeaders();
//			int columnCount = r.getColumnCount();
			// 获取每一行数组放入List
			List<TradeDTO> datasList = new ArrayList<TradeDTO>();
			while (r.readRecord()) {
				String[] values = r.getValues();
				TradeDTO tradeDTO = new TradeDTO();
				tradeDTO.setBuyerNick(values[0]);
				if(values[1] != null && !"".equals(values[1])){
					tradeDTO.setConsignTime(new Date(Long.parseLong(values[1])));
				}
				tradeDTO.setCreated(new Date(Long.parseLong(values[2])));
				if(values[3] != null && !"".equals(values[3])){
					tradeDTO.setEndTime(new Date(Long.parseLong(values[3])));
				}
				if(values[5] != null && !"".equals(values[5])){
					tradeDTO.setPayment(new BigDecimal(Double.parseDouble(values[5])));
				}
				if(values[6] != null && !"".equals(values[6])){
					tradeDTO.setPayTime(new Date(Long.parseLong(values[6])));
				}
				if(values[7] != null && !"".equals(values[7])){
					tradeDTO.setReceiverAddress(values[7]);
				}
				if(values[8] != null && !"".equals(values[8])){
					tradeDTO.setReceiverMobile(values[8]);
				}
				if(values[9] != null && !"".equals(values[9])){
					tradeDTO.setSellerFlag(Long.parseLong(values[9]));
				}
				if(values[10] != null && !"".equals(values[10])){
					tradeDTO.setStatus(values[10]);
				}
				if(values[11] != null && !"".equals(values[11])){
					tradeDTO.setRefundFlag(Boolean.parseBoolean(values[11]));
				}
				tradeDTO.setTid(Long.parseLong(values[12]));
				tradeDTO.setType(values[13]);
				if(values[14] != null && !"".equals(values[14])){
					tradeDTO.setModified(new Date(Long.parseLong(values[14])));
				}
				datasList.add(tradeDTO);
			}
			tradeDTOService.batchInsertTradeList(userInfo.getId(), datasList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			r.close();
		}*/
		return null;
	}
	
	public static String getXValue(XSSFCell xssfCell){  
        if (xssfCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {  
            return String.valueOf(xssfCell.getBooleanCellValue());  
        } else if (xssfCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {  
            String cellValue = "";  
            if(XSSFDateUtil.isCellDateFormatted(xssfCell)){  
                Date date = XSSFDateUtil.getJavaDate(xssfCell.getNumericCellValue());  
                cellValue = sdf.format(date);  
            }else{  
                DecimalFormat df = new DecimalFormat("#.##");  
                cellValue = df.format(xssfCell.getNumericCellValue());  
                String strArr = cellValue.substring(cellValue.lastIndexOf(POINT)+1,cellValue.length());  
                if(strArr.equals("00")){  
                    cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));  
                }    
            }  
            return cellValue;  
        } else {  
           return String.valueOf(xssfCell.getStringCellValue());  
        }  
   }
	
	/**
	 * synchData(同步数据)
	 * @Title: synchData 
	 * @param @param request
	 * @param @param response
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("/synchData")
	@ResponseBody
	public String synchData(HttpServletRequest request,HttpServletResponse response){
		List<Long> uidList = msgSendRecordService.listUidBySendCreate(DateUtils.getTimeByDay(new Date(), -17), DateUtils.getTimeByDay(new Date(), 13),null);
		if(uidList != null && !uidList.isEmpty()){
			for (Long uid : uidList) {
				if(uid != 196L){
					continue;
				}
				List<Long> msgIds = marketingCenterEffectService.findAllMsgIdByTime(uid, DateUtils.getTimeByDay(new Date(), -5));
				if(msgIds == null){
					continue;
				}
				for (Long msgId : msgIds) {
					if(msgId == null){
						continue;
					}
					MsgSendRecord msgSendRecord = msgSendRecordService.queryRecordById(uid, msgId);
					if(msgSendRecord == null){
						continue;
					}
					try {
						marketingCenterEffectService.handleData(uid, msgSendRecord);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		MarketingCenterEffect resultEffect = marketingCenterEffectService.findEffectByDays(196L, 293L, "TOTAL", 1);
		return rsMap(100, "success").put("status", true).toJson();
	}
	
	@RequestMapping("/sumData")
	@ResponseBody
	public String sumData(HttpServletRequest request,HttpServletResponse response){
//		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
//		CustomerDetailVO itemVO = new CustomerDetailVO();
//		itemVO.setUid(userInfo.getId());
//		itemVO.setMsgId(47473L);
//		itemVO.setTradeStatus("create");
//		Map<String, Object> itemMap = itemDetailService.limitItemDetail(userInfo.getId(), itemVO);
//		System.out.println(itemMap);
//		CustomerDetailVO customerDetailVO = new CustomerDetailVO();
//		customerDetailVO.setCustomerType("create");
//		customerDetailVO.setUid(userInfo.getId());
//		customerDetailVO.setMsgId(47473L);
//		Map<String, Object> customerMap = tempTradeService.limitCustomerDetail(userInfo.getId(), customerDetailVO, userInfo);
//		System.err.println(customerMap);
		
//		return rsMap(100, "jisuan wancheng").put("data1", itemMap).put("data2", customerMap).toJson();
		try {
//			TradeCenterEffectVO tradeCenterEffect = new TradeCenterEffectVO();
//			tradeCenterEffect.setUid(196L);
//			tradeCenterEffect.setEffectTime(DateUtils.getStartTimeOfDay(new Date()));
//			tradeCenterEffect.setTaskId(1L);
//			TradeCenterEffect TresultradeEffect = tradeCenterEffectService.queryTradeEffect(tradeCenterEffect);
//			if(TresultradeEffect != null){
//				tradeCenterEffectService.updateTradeCenterEffect(tradeCenterEffect);
//			}else {
//				tradeCenterEffectService.saveTradeCenterEffect(tradeCenterEffect);
//			}
			Date synchTime = new Date();
			Date startTime = DateUtils.addMinute(synchTime, -180);
			TaskNode taskNode = taskNodeService.queryTaskNodeByType("tradeCenterEffect");
			if(taskNode != null && taskNode.getTaskEndTime() != null){
				startTime = taskNode.getTaskEndTime();
			}else {
				taskNode = new TaskNode();
				taskNode.setTaskEndTime(synchTime);
				taskNode.setType("tradeCenterEffect");
				taskNodeService.saveTaskNode(taskNode);
			}
			Date endTime = synchTime;
			List<Long> uIdList = userInfoService.listTokenNotNull();
			for (int i = 0; i < uIdList.size(); i++) {
				Long uid = uIdList.get(i);
				if(uid != 196L){
					continue;
				}
				logger.info("!~~~~~~~~~~~订单中心效果分析，账户为：196北京冰点零度");
				//催付，回款等都是3天未付款就自动取消
				for (int j = 0; j < 3; j++) {
					Date nDaysAgo = DateUtils.nDaysAgo(j, startTime);
					Date effectTimeStart = DateUtils.getStartTimeOfDay(nDaysAgo);
					Date effectTimeEnd = DateUtils.getEndTimeOfDay(nDaysAgo);
					List<Long> taskIdList = tradeSetupService.queryTaskIdIsUse(uid, true);
					if(taskIdList == null || taskIdList.isEmpty()){
						continue;
					}
					for (Long taskId : taskIdList) {
						List<SmsRecordDTO> smsRecordDTOs = smsRecordDTOService.listRecordByTaskId(uid, taskId, startTime, endTime);
						List<String> tids = new ArrayList<String>();//催付订单数
						int smsNum = 0;//短信消费条数
						int linkNum = 0;//短信内连接数
						int customerClick = 0;//客户点击量
						int pageClick = 0;//页面点击量
						if(null == smsRecordDTOs || smsRecordDTOs.isEmpty()){
							continue;
						}
						logger.info("~~~~taskId是："+taskId+"第："+j+"天之前的recordDTOList的个数："+smsRecordDTOs.size());
//						JSONObject allEffect = shortLinkService.getAllEffect(uid, smsRecordDTOs.get(0).getType(), taskId, effectTimeStart, effectTimeEnd);
						//统计0,1,2天前发送订单中心短信的订单数
						for (int k = 0; k < smsRecordDTOs.size(); k++) {
							SmsRecordDTO smsRecordDTO = smsRecordDTOs.get(k);
							if(smsRecordDTO != null && smsRecordDTO.getOrderId() != null){
								tids.add(smsRecordDTO.getOrderId());
								smsNum += NumberUtils.getResult(smsRecordDTO.getActualDeduction());
							}
						}
						//根据计算所有类型的回款数据，保存到tradeCenterEffect
						TradeCenterEffectVO effectVO = sumTradeCenterEffect(uid, smsRecordDTOs.get(0).getType(), tids, null, null);
						if(effectVO != null){
							logger.info("~~~~taskId是："+taskId+"第："+j+"天之前根据计算所有类型的回款数据不为null");
							effectVO.setUid(uid);
							effectVO.setTaskId(taskId);
							effectVO.setEffectTime(effectTimeStart);
							effectVO.setSmsNum(smsNum);
							effectVO.setSmsMoney(NumberUtils.getTwoDouble(smsNum * 0.05));
							//点击量(暂时不上)
//							if(allEffect != null){
//								linkNum = allEffect.getInteger("total");
//								customerClick = allEffect.getInteger("customerClickNum");
//								pageClick = allEffect.getInteger("pageClickNum");
//							}
							effectVO.setCustomerClick(customerClick);
							effectVO.setLinkNum(linkNum);
							effectVO.setPageClick(pageClick);
							effectVO.setCreatedBy(uid + "");
							effectVO.setCreatedDate(new Date());
							effectVO.setLastModifiedBy(uid +"");
							effectVO.setLastModifiedDate(new Date());
							logger.info("~~~~taskId是："+taskId+"第："+j+"天之前的催付订单数：" + effectVO.getTargetOrder());
							logger.info("~~~~taskId是："+taskId+"第："+j+"天之前的回款订单数：" + effectVO.getEarningFee());
							TradeCenterEffect tradeEffect = tradeCenterEffectService.queryTradeEffect(effectVO);
							if(tradeEffect != null){
								tradeCenterEffectService.updateTradeCenterEffect(effectVO);
							}else {
								tradeCenterEffectService.saveTradeCenterEffect(effectVO);
							}
						}else {
							logger.info("~~~~taskId是："+taskId+"第："+j+"天之前根据计算所有类型的回款数据为null");
						}
					}
				}
			}
			taskNode.setTaskEndTime(endTime);
			taskNodeService.updateTaskNode(taskNode);
			
			
			
			
			
			
//			tradeCenterEffectService.tradeCenterEffectJob(new Date(), null, null);
		
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(100, "操作异常").put("status", false).toJson();
		}
		return rsMap(100, "操作完成").put("status", true).toJson();
	}
	
	@RequestMapping("/oneData")
	@ResponseBody
	public String singleSynchData(){
		/**
		 * RFM标准分析
		 */
		List<Long> uidList = userInfoService.listTokenNotNull();
		Date startNodeDate = new Date();
		if(uidList != null && !uidList.isEmpty()){
			for (Long uid : uidList) {
				if(uid != 1243){
					continue;
				}
				try {
					/*List<EffectStandardRFM> customerRFMs = this.tradeDTOService.
							listCustomerRFMs(uid, startNodeDate);*/
					List<EffectStandardRFM> customerRFMs = null;
//					effectStandardRFMService.saveListStandardRFM(customerRFMs);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		/**
		 * 一次性同步ROI的job
		 */
		/*logger.info("开始执行一次性定时任务同步营销中心ROI到MsgSendRecord记录" + DateUtils.dateToStringHMS(new Date()));
		List<Long> uids = userInfoService.listTokenNotNull();
		if(uids != null && !uids.isEmpty()){
			for (Long uid : uids) {
				logger.info("一次性定时任务同步营销中心ROI,uid:" + uid + "总个数:" + uids.size());
				if(uid == null || (uid != 196L && uid != 1243L)){
					logger.info("一次性定时任务同步营销中心ROI,uid:" + uid + "为NULL");
					continue;
				}
				List<MsgSendRecord> msgSendRecords = msgSendRecordService.listAllEffectSendRecord(uid, null, null, true);
				if(msgSendRecords != null && !msgSendRecords.isEmpty()){
					for (MsgSendRecord msgSendRecord : msgSendRecords) {
						Long deduction = smsRecordDTOService.sumDeductionById(uid, msgSendRecord.getId());
						Integer totalSendNum = deduction.intValue();//消耗短信条数
						Double totalSendMoney = NumberUtils.getTwoDouble(NumberUtils.getResult(totalSendNum) * 0.05);//消费短信金额
						Double paidFee = marketingCenterEffectService.sumPaidByDate(uid, msgSendRecord, msgSendRecord.getSendCreat(), new Date());
						String msgROI = this.creatROI(totalSendMoney, paidFee);
						msgSendRecordService.updateROIById(msgSendRecord.getId(), msgROI, null);
					}
				}
				logger.info("一次性定时任务同步营销中心ROI,uid:" + uid + "执行完毕");
			}
		}
		logger.info("开始执行一次性定时任务同步营销中心ROI到MsgSendRecord记录，同步完毕" + DateUtils.dateToStringHMS(new Date()));*/
		
		
		/*List<Long> uidList = msgSendRecordService.listUidBySendCreate(DateUtils.getTimeByDay(new Date(), -120), new Date(), 7448L);
		if(uidList != null && !uidList.isEmpty()){
			logger.info("~~~~~~~~~!!!!最近30天发过营销短信的用户个数为：" + uidList.size());
		}else {
			logger.info("~~~~~~~~~!!!!最近30天发过营销短信的用户个数为NULL");
		}
		if(uidList != null && !uidList.isEmpty()){
			for (Long uid : uidList) {
				if(uid != 7449L){
					continue;
				}
				UserInfo userInfo = userInfoService.findUserInfo(uid);
				//查询近15天产生的所有的msg
				SmsRecordVO msgRecordVO = new SmsRecordVO();
				msgRecordVO.setBeginTime(DateUtils.getTimeByDay(new Date(), -120));
				msgRecordVO.setEndTime(new Date());
				msgRecordVO.setStatus(5);
				List<MsgSendRecord> msgRecords = msgSendRecordService.listAllEffectSendRecord(userInfo.getId(),msgRecordVO, true);
				if(msgRecords != null && !msgRecords.isEmpty()){
					for (MsgSendRecord msgSendRecord : msgRecords) {
						if(msgSendRecord.getId() != 49002L){
							continue;
						}
						//查询本次发送总记录下产生的订单
						try {
							Date bTime = msgSendRecord.getSendCreat(), eTime = new Date();
							SmsRecordVO smsRecordVO = new SmsRecordVO();
							smsRecordVO.setUid(userInfo.getId());
							smsRecordVO.setStatus(2);
							smsRecordVO.setType(msgSendRecord.getType());
							smsRecordVO.setMsgId(msgSendRecord.getId());
							marketingCenterEffectService.singleSynchEffectData(uid, msgSendRecord, bTime, eTime);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}else {
					logger.info("~~~~~~~~~~~!!!!!!!!!!用户：" + uid + "查询的发送批次个数为:NULL");
				}
			}
			
		}*/
		/**
		 * 创建表，创建索引
		 */
		/*memberService.doCreateTableByNewUser(11117L);
		// 会有多地址表(字段√主键↑ 索引√ 建表√建索引√)【4】
		memberService.doCreateMemberReceiveDetailTableByNewUser(11117L);
		// 评价表(字段√ 主键↑索引√ 建表√建索引√)【5】
		memberService.doCreateTradeRatesTableByNewUser(11117L);
		memberService.doCreateSmsBlacklistTableByNewUser(11117L);
		memberService.doCreateMemberItemAmountTableByNewUser(11117L);
		
		memberService.doCreateTableByNewUser(11118L);
		// 会有多地址表(字段√主键↑ 索引√ 建表√建索引√)【4】
		memberService.doCreateMemberReceiveDetailTableByNewUser(11118L);
		// 评价表(字段√ 主键↑索引√ 建表√建索引√)【5】
		memberService.doCreateTradeRatesTableByNewUser(11118L);
		memberService.doCreateSmsBlacklistTableByNewUser(11118L);
		memberService.doCreateMemberItemAmountTableByNewUser(11118L);*/
		
		return rsMap(100, "成功").put("status", true).toJson();
	}
	
	
	/**
	 * 展示首页公告 标题
	 * @time 2018年9月11日 下午6:45:53 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/showNoticeTitle")
	public String showNoticeTitle(HttpServletResponse response, HttpServletRequest request){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		
		List<Notice> title = noticeService.findNoticeTitle();
		return successReusltMap(null).put(ApiResult.API_RESULT, title).toJson();
	}
	
	/**
	 * 展示首页公告 内容
	 * @time 2018年9月11日 下午6:45:53 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/showNoticeContent")
	public String showNoticeContent(@RequestBody String params,
			HttpServletResponse response, HttpServletRequest request){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		Map<String, String> map = new HashMap<String, String>();
    	if (null != params && !"".equals(params)) {
    		try {
    			map = JsonUtil.paramsJsonToObject(params,Map.class);
			} catch (Exception e) {
				e.printStackTrace();
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
		}
    	String content  = "";
    	if(map != null && map.size()>0){
    		Long id = Long.parseLong(map.get("id"));
    		content = noticeService.findNoticeContent(id);
    	}
		return successReusltMap(null).put(ApiResult.API_RESULT, content).toJson();
	}
	
	
	/**
	 * 计算订单中心发送过短信的订单的回款数据
	 */
	public TradeCenterEffectVO sumTradeCenterEffect(Long uid,String type,List<String> tidList,
			Date startTime,Date endTime){
		TradeCenterEffectVO tradeCenterEffectVO = new TradeCenterEffectVO();
		List<String> statusList = new ArrayList<String>();
		int targetOrder = 0,earningOrder = 0;
		double targetFee = 0.00,earningFee = 0.00;
		if(MsgType.MSG_JHSCF.equals(type) || MsgType.MSG_ECCF.equals(type)){//聚划算
			targetOrder = tidList.size();
			TradeCenterEffect targetEffect = tradeDTOService.sumEarningOrder(uid, tidList, statusList, startTime, endTime);
			if(targetEffect != null){
				targetFee = NumberUtils.getResult(targetEffect.getTargetFee());
			}
			statusList.add(TradesInfo.WAIT_SELLER_SEND_GOODS);
			statusList.add(TradesInfo.WAIT_BUYER_CONFIRM_GOODS);
			statusList.add(TradesInfo.TRADE_BUYER_SIGNED);
			statusList.add(TradesInfo.TRADE_FINISHED);
			statusList.add(TradesInfo.TRADE_CLOSED);
			TradeCenterEffect earningEffect = tradeDTOService.sumEarningOrder(uid, tidList, statusList,startTime,endTime);
			if(earningEffect != null){
				earningFee = NumberUtils.getResult(earningEffect.getTargetFee());
				earningOrder = NumberUtils.getResult(earningEffect.getTargetOrder());
			}
		}else if(MsgType.MSG_CGCF.equals(type)){//常规或二次
			List<String> ecTidList = smsRecordDTOService.tradeCenterEffectRecordTid(uid, type,  startTime, endTime, null);
			targetOrder = tidList.size();
			TradeCenterEffect targetEffect = tradeDTOService.sumEarningOrder(uid, tidList,null,startTime,endTime);
			if(targetEffect != null){
				targetFee = NumberUtils.getResult(targetEffect.getTargetFee());
			}
			if(tidList != null){
				tidList.removeAll(ecTidList);
			}
			statusList.add(TradesInfo.WAIT_SELLER_SEND_GOODS);
			statusList.add(TradesInfo.WAIT_BUYER_CONFIRM_GOODS);
			statusList.add(TradesInfo.TRADE_BUYER_SIGNED);
			statusList.add(TradesInfo.TRADE_FINISHED);
			statusList.add(TradesInfo.TRADE_CLOSED);
			TradeCenterEffect earningEffect = tradeDTOService.sumEarningOrder(uid, tidList,statusList,startTime,endTime);
			if(earningEffect != null){
				earningFee = NumberUtils.getResult(earningEffect.getTargetFee());
				earningOrder = NumberUtils.getResult(earningEffect.getTargetOrder());
			}
		}else if(MsgType.MSG_HKTX.equals(type)){//回款提醒
			targetOrder = tidList.size();
			TradeCenterEffect targetEffect = tradeDTOService.sumEarningOrder(uid, tidList,null,startTime,endTime);
			if(targetEffect != null){
				targetFee = NumberUtils.getResult(targetEffect.getTargetFee());
			}
			statusList.add(TradesInfo.TRADE_FINISHED);
			statusList.add(TradesInfo.TRADE_CLOSED);
			TradeCenterEffect earningEffect = tradeDTOService.sumEarningOrder(uid, tidList, statusList, startTime,endTime);
			if(earningEffect != null){
				earningFee = NumberUtils.getResult(earningEffect.getTargetFee());
				earningOrder = NumberUtils.getResult(earningEffect.getTargetOrder());
			}
		}else if(MsgType.MSG_HPTX.equals(type)){//好评提醒
			targetOrder = tidList.size();
		}else if(MsgType.MSG_FHTX.equals(type) || MsgType.MSG_DDTCTX.equals(type) || MsgType.MSG_PJTX.equals(type) || 
				MsgType.MSG_QSTX.equals(type) || MsgType.MSG_BBGH.equals(type)){//物流提醒和宝贝关怀
			targetOrder = tidList.size();
		}
		tradeCenterEffectVO.setTargetFee(targetFee);
		tradeCenterEffectVO.setTargetOrder(targetOrder);
		tradeCenterEffectVO.setEarningFee(earningFee);
		tradeCenterEffectVO.setEarningOrder(earningOrder);
		tradeCenterEffectVO.setType(type);
		return tradeCenterEffectVO;
	}
}
