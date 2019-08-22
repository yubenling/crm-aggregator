package com.kycrm.member.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.core.queue.LogAccessQueueService;
import com.kycrm.member.domain.entity.eco.log.LogAccessDTO;
import com.kycrm.member.domain.entity.eco.log.LogType;
import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.trade.TradeMessageVO;
import com.kycrm.member.domain.vo.trade.TradeResultVO;
import com.kycrm.member.domain.vo.trade.TradeVO;
import com.kycrm.member.service.item.IItemService;
import com.kycrm.member.service.message.IMsgSendRecordService;
import com.kycrm.member.service.message.ITradeMsgSendService;
import com.kycrm.member.service.order.IOrderDTOService;
import com.kycrm.member.service.trade.ITradeDTOService;
import com.kycrm.member.service.traderate.ITradeRatesService;
import com.kycrm.member.service.user.IUserAccountService;
import com.kycrm.util.TaoBaoClientUtil;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.ConstantUtils;
import com.kycrm.util.DateUtils;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.SmsCalculateUtil;
import com.kycrm.util.TradesInfo;
import com.kycrm.util.thread.MyFixedThreadPool;

@Controller
@RequestMapping("/marketing")
public class TradeController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(TradeController.class);

	@Autowired
	private ITradeDTOService tradeDTOService;

	@Autowired
	private IOrderDTOService orderDTOService;

	@Autowired
	private IMsgSendRecordService msgSendRecordService;

	@Autowired
	private SessionProvider sessionProvider;

	@Autowired
	private IUserAccountService userAccountService;

	@Autowired
	private ITradeMsgSendService tradeMsgSendService;

	@Autowired
	private ITradeRatesService tradeRatesService;

	@Autowired
	private IItemService itemService;

	/**
	 * 营销中心--订单短信群发根据条件筛选订单 ztk2018年1月9日下午5:57:43
	 */
	@RequestMapping(value = "/listTrade", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String listTrade(@RequestBody String params, HttpServletRequest request, HttpServletResponse response) {
		TradeVO tradeVO = null;
		try {
			tradeVO = JsonUtil.paramsJsonToObject(params, TradeVO.class);
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(102, "操作失败,请重新操作或联系系统管理员!").put("status", false).toJson();
		}
		if (tradeVO == null) {
			return rsMap(101, "操作失败").put("status", false).toJson();
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (userInfo != null && userInfo.getId() != null) {
			tradeVO.setUid(userInfo.getId());
			this.createTradeParams(tradeVO);
			tradeVO.setStartRows(0L);
			tradeVO.setPageSize(100L);
			String queryKey = null;
			if (tradeVO.getQueryKey() != null) {
				queryKey = tradeVO.getQueryKey();
			} else {
				queryKey = getKey();
				tradeVO.setQueryKey(queryKey);
			}
			// tradeRatesService.
			List<TradeResultVO> tradeResultVOs = null;
			try {
				tradeResultVOs = tradeDTOService.listMarketingCenterOrder(userInfo.getId(), tradeVO,
						userInfo.getAccessToken(), queryKey);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 日志上传
			Set<Long> tidSet = new HashSet<>();
			if (tradeResultVOs != null && !tradeResultVOs.isEmpty()) {
				for (TradeResultVO tradeResultVOOOOO : tradeResultVOs) {
					tidSet.add(tradeResultVOOOOO.getTid());
				}
				LogAccessDTO log = new LogAccessDTO();
				log.setAti(RequestUtil.getAtiValue(request));
				log.setUserIp("118.178.185.208");
				String userId = userInfo.getTaobaoUserNick();
				if (userId == null || "".equals(userId)) {
					userId = userInfo.getId() + "";
				}
				log.setUserId(userId);
				log.setOperation("订单查询");
				log.setUrl("http://crm.kycrm.com/msgSend/index#/marketingCenter/orderSMSGroup");
				log.setTid(StringUtils.join(tidSet, ","));
				log.setTradeIds(StringUtils.join(tidSet, ","));
				logger.info("*******************tids:" + StringUtils.join(tidSet, ","));
				this.AsyncSendOrderLog(log);
			}
			Long tradeCount = 0L;
			try {
				tradeCount = tradeDTOService.countMarketingCenterOrder(userInfo.getId(), tradeVO,
						userInfo.getAccessToken(), queryKey);
			} catch (Exception e) {
				e.printStackTrace();
			}
			ResultMap<String, Object> resultMap = rsMap(100, "操作成功").put("status", true);
			resultMap.put("data", tradeResultVOs);
			resultMap.put("totalCount", tradeCount);
			resultMap.put("queryKey", tradeVO.getQueryKey());
			return resultMap.toJson();
		}
		return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
	}

	/**
	 * exportTrade(导出筛选的订单) @Title: exportTrade @param @param
	 * params @param @param request @param @param response @param @return
	 * 设定文件 @return String 返回类型 @throws
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/exportTrade", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String exportTrade(@RequestBody String params, HttpServletRequest request, HttpServletResponse response) {
		if (params != null && !"".equals(params)) {
			params = "{" + (URLDecoder.decode(params).replace("=", ":")) + "}";
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		TradeVO tradeVO = null;
		try {
			tradeVO = JsonUtil.paramsJsonToObject(params, TradeVO.class);
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(102, "操作失败,请重新操作或联系系统管理员!").put("status", false).toJson();
		}
		if (tradeVO == null || userInfo == null) {
			return rsMap(101, "操作失败").put("status", false).toJson();
		}
		tradeVO.setUid(userInfo.getId());
		this.createTradeParams(tradeVO);
		long totalCount = 0l;
		try {
			totalCount = tradeDTOService.countMarketingCenterOrder(userInfo.getId(), tradeVO, userInfo.getAccessToken(),
					null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		long start = 0, end = 0;
		long pageSize = ConstantUtils.PROCESS_PAGE_SIZE_MAX;
		if (totalCount % pageSize == 0) {
			end = totalCount / pageSize;
		} else {
			end = (totalCount + pageSize) / pageSize;
		}
		List<TradeResultVO> tradeResultVOs = new ArrayList<>();
		String fileName = "document";
		try {
			while (start < end) {
				if ((start + 1) == end) {
					pageSize = totalCount - ConstantUtils.PROCESS_PAGE_SIZE_MAX * start;
				}
				tradeVO.setStartRows(start * ConstantUtils.PROCESS_PAGE_SIZE_MAX);
				tradeVO.setPageSize(pageSize);
				List<TradeResultVO> limitTradeResultVOs = tradeDTOService.listMarketingCenterOrder(userInfo.getId(),
						tradeVO, userInfo.getAccessToken(), null);
				start++;
				tradeResultVOs.addAll(limitTradeResultVOs);
			}

			fileName = new String("订单短信群发.xlsx".getBytes(), "ISO-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * List<Object[]> resultList = new ArrayList<>(); if(orderDTOs != null
		 * && !orderDTOs.isEmpty()){ for (int i = 0; i < orderDTOs.size(); i++)
		 * { OrderDTO orderDTO = orderDTOs.get(i); resultList.add(new
		 * Object[]{orderDTO.getTradeCreated()==null?null:DateUtils.
		 * dateToStringHMS(orderDTO.getTradeCreated()),orderDTO.getTid(),
		 * orderDTO.getOid(), orderDTO.getNumIid(),orderDTO.getTitle(),orderDTO.
		 * getSkuPropertiesName(),orderDTO.getPrice(),
		 * orderDTO.getPayment(),orderDTO.getBuyerNick(),orderDTO.
		 * getReceiverName(),orderDTO.getReceiverAddress(),
		 * orderDTO.getStatus(),orderDTO.getOrderFrom(),orderDTO.getSellerRate()
		 * ,orderDTO.getBuyerRate(),
		 * orderDTO.getSellerFlag(),orderDTO.getNum(),orderDTO.getRefundStatus()
		 * }); } } String[] rowNames = new
		 * String[]{"创建时间","主订单编号","子订单编号","商品id","宝贝名称","规格","单价","实收款","买家昵称",
		 * "收货人姓名",
		 * "收货人地址","交易状态","订单来源","卖家评价状态","买家评价状态","卖家标识","商品数量","退款状态"};
		 */

		SXSSFWorkbook resultWorkBook = this.createWorkBook(tradeResultVOs);
		// HSSFWorkbook workbook = null;
		// try {
		// workbook = ExcelExportUtils.export(fileName, rowNames, resultList);
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// }
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			resultWorkBook.write(outputStream);
			// workbook.write(outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 创建导出 createWorkBook(这里用一句话描述这个方法的作用) @Title:
	 * createWorkBook @param @return 设定文件 @return SXSSFWorkbook 返回类型 @throws
	 */
	public SXSSFWorkbook createWorkBook(List<TradeResultVO> tradeResultVOs) {
		// 创建一个工作簿
		SXSSFWorkbook workBook = new SXSSFWorkbook();
		Sheet sheet = workBook.createSheet();// 工作表
		Row titleRow = sheet.createRow(0);// 创建第一行，标题行
		titleRow.createCell(0).setCellValue("序号");
		titleRow.createCell(1).setCellValue("订单编号");
		titleRow.createCell(2).setCellValue("下单时间");
		titleRow.createCell(3).setCellValue("买家信息");
		titleRow.createCell(4).setCellValue("交易状态");
		titleRow.createCell(5).setCellValue("实付金额");
		titleRow.createCell(6).setCellValue("标识状态");
		titleRow.createCell(7).setCellValue("商品名称");
		titleRow.createCell(8).setCellValue("单价");
		titleRow.createCell(9).setCellValue("数量");
		int flag = 0;
		TradeResultVO tradeVO = null;
		for (int i = 0; i < tradeResultVOs.size(); i++) {
			tradeVO = tradeResultVOs.get(i);
			if (tradeVO != null) {
				titleRow = sheet.createRow(flag + 1);
				flag++;
				titleRow.createCell(0).setCellValue(i + 1);
				titleRow.createCell(1).setCellValue(tradeVO.getTid() + "");
				if (tradeVO.getCreated() != null) {
					titleRow.createCell(2).setCellValue(DateUtils.dateToStringHMS(tradeVO.getCreated()));
				}
				titleRow.createCell(3).setCellValue(tradeVO.getBuyerNick() + ":" + tradeVO.getReceiverMobile());
				if ("TRADE_NO_CREATE_PAY".equals(tradeVO.getTradeStatus())) {
					titleRow.createCell(4).setCellValue("没有创建支付宝交易");
				} else if ("WAIT_BUYER_PAY".equals(tradeVO.getTradeStatus())) {
					titleRow.createCell(4).setCellValue("等待买家付款");
				} else if ("WAIT_SELLER_SEND_GOODS".equals(tradeVO.getTradeStatus())) {
					titleRow.createCell(4).setCellValue("买家已付款");
				} else if ("WAIT_BUYER_CONFIRM_GOODS".equals(tradeVO.getTradeStatus())) {
					titleRow.createCell(4).setCellValue("卖家已发货");
				} else if ("TRADE_BUYER_SIGNED".equals(tradeVO.getTradeStatus())) {
					titleRow.createCell(4).setCellValue("买家已签收");
				} else if ("TRADE_FINISHED".equals(tradeVO.getTradeStatus())) {
					titleRow.createCell(4).setCellValue("交易成功");
				} else if ("TRADE_CLOSED".equals(tradeVO.getTradeStatus())) {
					titleRow.createCell(4).setCellValue("付款后,交易关闭");
				} else if ("TRADE_CLOSED_BY_TAOBAO".equals(tradeVO.getTradeStatus())) {
					titleRow.createCell(4).setCellValue("付款前,交易关闭");
				} else if ("PAY_PENDING".equals(tradeVO.getTradeStatus())) {
					titleRow.createCell(4).setCellValue("国际信用卡支付付款确认中");
				}
				titleRow.createCell(5).setCellValue(tradeVO.getTradePayment());
				titleRow.createCell(6).setCellValue(tradeVO.getSellerFlag());
				titleRow.createCell(7).setCellValue(tradeVO.getItemTitle());
				titleRow.createCell(8).setCellValue(tradeVO.getItemPrice() == null ? "0.00" : tradeVO.getItemPrice());
				titleRow.createCell(9).setCellValue(tradeVO.getItemNum() == null ? 0 : tradeVO.getItemNum());
				if (tradeVO.getOrderDTOs() != null && !tradeVO.getOrderDTOs().isEmpty()) {
					List<OrderDTO> orderDTOs = tradeVO.getOrderDTOs();
					for (OrderDTO orderDTO : orderDTOs) {
						if (orderDTO != null) {
							titleRow = sheet.createRow(flag + 1);
							flag++;
							titleRow.createCell(7).setCellValue(orderDTO.getTitle());
							titleRow.createCell(8).setCellValue(
									orderDTO.getPrice() == null ? "0.00" : orderDTO.getPrice().toString());
							titleRow.createCell(9).setCellValue(orderDTO.getNum() == null ? 0 : orderDTO.getNum());
						}
					}
					sheet.addMergedRegion(new CellRangeAddress(flag - orderDTOs.size(), flag, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(flag - orderDTOs.size(), flag, 1, 1));
					sheet.addMergedRegion(new CellRangeAddress(flag - orderDTOs.size(), flag, 2, 2));
					sheet.addMergedRegion(new CellRangeAddress(flag - orderDTOs.size(), flag, 3, 3));
					sheet.addMergedRegion(new CellRangeAddress(flag - orderDTOs.size(), flag, 4, 4));
					sheet.addMergedRegion(new CellRangeAddress(flag - orderDTOs.size(), flag, 5, 5));
					sheet.addMergedRegion(new CellRangeAddress(flag - orderDTOs.size(), flag, 6, 6));
				}
			}
		}
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 10000);
		sheet.setColumnWidth(4, 5000);
		sheet.setColumnWidth(7, 10000);

		return workBook;
	}

	/**
	 * @Title: createTaobaoLink
	 * @Description: (生成短链接,订单中心获取短链接)
	 * @return String 返回类型
	 * @author:jackstraw_yu
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/getLink", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String createTaobaoLink(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestBody String params) {
		Map<String, String> map = null;
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (userInfo == null) {
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		String token = userInfo.getAccessToken();
		try {
			JsonUtil.paramsJsonToObject(params, Map.class);
			// 获取token用于生成短链接
		} catch (Exception e) {
			return rsMap(102, "操作失败,参数转化异常!").put("status", false).toJson();
		}
		if (map == null || token == null)
			return rsMap(101, "操作失败").put("status", false).toJson();
		try {
			String link = TaoBaoClientUtil.creatLink(token, map.get("type"), map.get("value"));
			if (link == null)
				return rsMap(101, "操作失败").put("status", false).put("data", null).toJson();
			return rsMap(100, "操作成功").put("status", true).put("data", " " + link + " ").toJson();
		} catch (Exception e) {
			return rsMap(102, "操作失败,请重新操作或联系系统管理员!").put("status", false).toJson();
		}
	}

	/**
	 * groupTradeSms(群发短信) @Title: groupTradeSms @param @return 设定文件 @return
	 * String 返回类型 @throws
	 */
	@RequestMapping("/groupSms")
	@ResponseBody
	public String groupTradeSms(HttpServletRequest request, HttpServletResponse response, @RequestBody String params) {
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));

		TradeMessageVO messageVO = null;
		try {
			messageVO = JsonUtil.paramsJsonToObject(params, TradeMessageVO.class);
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(102, "操作失败,参数转化异常!").put("status", false).toJson();
		}
		if (userInfo == null || userInfo.getId() == null || messageVO == null) {
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		messageVO.setUid(userInfo.getId());
		if (messageVO.getActivityName() != null && !"".equals(messageVO.getActivityName())) {
			messageVO.setActivityName(messageVO.getActivityName().trim());
		}
		if (messageVO.getSendSchedule() != null && !"".equals(messageVO.getSendSchedule())) {
			if ("0".equals(messageVO.getSendSchedule())) {
				messageVO.setSchedule(false);
				messageVO.setSendTime(new Date());
			} else if ("1".equals(messageVO.getSendSchedule())) {
				messageVO.setSchedule(true);
				messageVO.setSendTime(DateUtils.parseDate(messageVO.getSendTimeStr(), "yyyy-MM-dd HH:mm:ss"));
			} else {
				messageVO.setSchedule(false);
				messageVO.setSendTime(new Date());
			}
		}
		logger.info("订单短信群发开始验证用户信息,当前时间:" + new Date() + "^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
		Map<String, Object> oMap = validateUserOpterate(messageVO);
		if ((Boolean) oMap.get("flag") == false) {
			return rsMap(101, oMap.get("message") == null ? "操作失败,请重新操作或联系系统管理员!" : (String) oMap.get("message"))
					.put("status", false).toJson();
		}
		logger.info("订单短信群发开始验证用户余额,当前时间:" + new Date() + "^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
		// 验证余额
		Map<String, Object> mMap = validateMessageCount(messageVO);
		if ((Boolean) mMap.get("flag") == false) {
			String json = rsMap(101, mMap.get("message") == null ? "操作失败,请重新操作或联系系统管理员!" : (String) mMap.get("message"))
					.put("status", false).toJson();
			return json;
		}
		logger.info("订单短信群发开始验证用户信息完成,开始发送,当前时间:" + new Date() + "^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");

		ResultMap<String, Object> resultMap = rsMap(100, "").put("status", true);
		try {
			messageVO.setIsDBQueryParam(false);
			// TODO 发送短信的逻辑
			tradeDTOService.doSendSms(messageVO.getUid(), messageVO);
			if (messageVO.getSchedule() != null && !messageVO.getSchedule()) {
				resultMap = rsMap(100, "订单发送成功!").put("status", true);
			} else if (messageVO.getSchedule() != null && messageVO.getSchedule()) {
				resultMap = rsMap(100, "订单定时保存成功!").put("status", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (messageVO.getSchedule() != null && !messageVO.getSchedule()) {
				resultMap = rsMap(101, "订单发送异常!").put("status", false);
			} else if (messageVO.getSchedule() != null && messageVO.getSchedule()) {
				resultMap = rsMap(100, "订单定时保存异常!").put("status", false);
			}
		}
		return resultMap.toJson();
	}

	/**
	 * validateUserOpterate(订单短信群发:验证用户的一些选择上的操作) @Title:
	 * validateUserOpterate @param @param sendMsgVo @param @return 设定文件 @return
	 * Map<String,Object> 返回类型 @throws
	 */
	private Map<String, Object> validateUserOpterate(TradeMessageVO messageVO) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flag = true;
		if (messageVO.getContent() == null || "".equals(messageVO.getContent())) {
			map.put("message", "短信内容不能为空!");
			flag = false;
		}
		if (messageVO.getSignVal() == null || "".equals(messageVO.getSignVal())) {
			String signValRe = messageVO.getSignVal().replace("【", "").replace("】", "").replace(" ", "");
			if (signValRe == null || "".equals(signValRe)) {
				map.put("message", "短信签名不能为空或者空格");
				flag = false;
			}
		}
		if (messageVO.getSendSchedule() == null || "".equals(messageVO.getSendSchedule())) {
			map.put("message", "请选择立即发送或者定时发送!");
			flag = false;
		}
		if ("1".equals(messageVO.getSendSchedule())
				&& (messageVO.getSendTimeStr() == null || "".equals(messageVO.getSendTimeStr()))) {
			map.put("message", "定时发送的时间不能为空!");
			flag = false;
		}
		if ("1".equals(messageVO.getSendSchedule()) && messageVO.getSendTime() != null
				&& !"".equals(messageVO.getSendTime())) {
			Date sTime = null;
			try {
				sTime = DateUtils.parseDate(messageVO.getSendTimeStr(), "yyyy-MM-dd HH:mm:ss");
				if (sTime != null && (sTime.getTime() - System.currentTimeMillis()) < 3 * 60 * 1000) {
					map.put("message", "定时发送时间与当前时间间隔不能小于3分钟!");
					flag = false;
				}
			} catch (Exception e) {
				logger.error("订单短信群发定时发送时间转换异常!!^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
				map.put("message", "时间转换异常,请重新操作或者联系系统管理员!");
				flag = false;
			}
		}
		map.put("flag", flag);
		return map;
	}

	/**
	 * validateMessageCount(订单短信群发:验证用户短信条数与用户余额) @Title:
	 * validateMessageCount @param @param sendMsgVo @param @return 设定文件 @return
	 * Map<String,Object> 返回类型 @throws
	 */
	private Map<String, Object> validateMessageCount(TradeMessageVO messageVO) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flag = true;
		// 订单短信剩余条数
		long smsNum = userAccountService.findUserAccountSms(messageVO.getUid());
		// 订单集合长度*每条短信大致要扣除的条数,即为要发送的短信的总条数
		Integer actualDeduction = SmsCalculateUtil.getActualDeduction(messageVO.getContent());
		if (0 == smsNum) {
			map.put("message", "短信剩余条数不足,请先充值后再发送,谢谢!");
			flag = false;
		} else if (smsNum < messageVO.getTotalCount() * actualDeduction) {
			map.put("message", "短信剩余条数小于要发送的总条数!");
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}

	/**
	 * 将页面查询参数重新填充到TreadeVO ztk2018年1月10日上午10:23:27
	 */
	private void createTradeParams(TradeVO tradeVO) {
		// 拍下订单时段
		if (tradeVO.getMinCreatedHourStr() != null && !"".equals(tradeVO.getMinCreatedHourStr())) {
			try {
				tradeVO.setMinCreatedHour(
						DateUtils.stringToDate(tradeVO.getMinCreatedHourStr(), DateUtils.SHORT_HOUR_FORMAT));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (tradeVO.getMaxCreatedHourStr() != null && !"".equals(tradeVO.getMaxCreatedHourStr())) {
			try {
				tradeVO.setMaxCreatedHour(
						DateUtils.stringToDate(tradeVO.getMaxCreatedHourStr(), DateUtils.SHORT_HOUR_FORMAT));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		// 下单时间
		if (tradeVO.getMinCreatedTimeStr() != null && !"".equals(tradeVO.getMinCreatedTimeStr())) {
			tradeVO.setMinCreatedTime(DateUtils.convertDate(tradeVO.getMinCreatedTimeStr()));
		}
		if (tradeVO.getMaxCreatedTimeStr() != null && !"".equals(tradeVO.getMaxCreatedTimeStr())) {
			tradeVO.setMaxCreatedTime(DateUtils.convertDate(tradeVO.getMaxCreatedTimeStr()));
		}
		// 发货时间
		if (tradeVO.getMinConsignTimeStr() != null && !"".equals(tradeVO.getMinConsignTimeStr())) {
			tradeVO.setMinConsignTime(DateUtils.convertDate(tradeVO.getMinConsignTimeStr()));
		}
		if (tradeVO.getMaxConsignTimeStr() != null && !"".equals(tradeVO.getMaxConsignTimeStr())) {
			tradeVO.setMaxConsignTime(DateUtils.convertDate(tradeVO.getMaxConsignTimeStr()));
		}
		// 确认时间
		if (tradeVO.getMinEndTimeStr() != null && !"".equals(tradeVO.getMinEndTimeStr())) {
			tradeVO.setMinEndTime(DateUtils.convertDate(tradeVO.getMinEndTimeStr()));
		}
		if (tradeVO.getMaxEndTimeStr() != null && !"".equals(tradeVO.getMaxEndTimeStr())) {
			tradeVO.setMaxEndTime(DateUtils.convertDate(tradeVO.getMaxEndTimeStr()));
		}
		// 将页面拼接的字符串转成list
		if (tradeVO.getCityStr() != null && !"".equals(tradeVO.getCityStr())) {
			List<String> cityList = this.splitStrToList(tradeVO.getCityStr(), ",");
			tradeVO.setCityList(cityList);
		}
		if (tradeVO.getIsTitle() != null && "1".equals(tradeVO.getIsTitle())) {
			/*
			 * if(tradeVO.getItemTitle() != null &&
			 * !"".equals(tradeVO.getItemTitle())){ List<Long> numIid =
			 * itemService.listAllItemIdByTitle(tradeVO.getUid(),
			 * tradeVO.getItemTitle()); if(numIid == null || numIid.isEmpty()){
			 * tradeVO.setNumIidList(null); }else {
			 * tradeVO.setNumIidList(numIid); } }
			 */
			tradeVO.setNumIidList(null);
		} else {
			if (tradeVO.getNumIidStr() != null && !"".equals(tradeVO.getNumIidStr())) {
				List<Long> numList = this.splitStrToLongList(tradeVO.getNumIidStr(), ",");
				tradeVO.setNumIidList(numList);
			}
		}
		if (tradeVO.getStateStr() != null && !"".equals(tradeVO.getStateStr())) {
			List<String> stateList = this.splitStrToList(tradeVO.getStateStr(), ",");
			tradeVO.setStateList(stateList);
		}
		if (tradeVO.getFlagStr() != null && !"".equals(tradeVO.getFlagStr())) {
			List<Integer> flagList = this.splitStrToIntList(tradeVO.getFlagStr(), ",");
			tradeVO.setSellerFlagList(flagList);
		}
		if (tradeVO.getSmsFileDays() != null && !"".equals(tradeVO.getSmsFileDays())
				&& !"0".equals(tradeVO.getSmsFileDays())) {
			int fileDay = Integer.parseInt(tradeVO.getSmsFileDays());
			Date beginTime = DateUtils.nDaysAgo(fileDay, new Date());
			Date endTime = new Date();
			List<Long> msgList = msgSendRecordService.listMsgId(tradeVO.getUid(), beginTime, endTime);
			tradeVO.setMsgIdList(msgList);
		}
		// 订单状态和评价状态封装到tradeVO
		if (tradeVO.getTradeStatus() != null && !"".equals(tradeVO.getTradeStatus())) {
			if (tradeVO.getTradeStatus().split(",").length > 1) {
				List<String> statusList = new ArrayList<String>();
				String[] statusArr = tradeVO.getTradeStatus().split(",");
				for (String status : statusArr) {
					statusList.add(status);
				}
				tradeVO.setTradeStatusList(statusList);
			}
		}
		/*
		 * if(tradeVO.getRateStatus() != null &&
		 * !"".equals(tradeVO.getRateStatus())){ if(tradeVO.getTradeStatusList()
		 * == null || tradeVO.getTradeStatusList().isEmpty()){
		 * if(tradeVO.getTradeStatus() == null ||
		 * "".equals(tradeVO.getTradeStatus())){ List<String> statusList = new
		 * ArrayList<>(); statusList.add(TradesInfo.WAIT_SELLER_SEND_GOODS);
		 * statusList.add(TradesInfo.WAIT_BUYER_CONFIRM_GOODS);
		 * statusList.add(TradesInfo.TRADE_BUYER_SIGNED);
		 * statusList.add(TradesInfo.TRADE_FINISHED);
		 * statusList.add(TradesInfo.TRADE_CLOSED);
		 * tradeVO.setTradeStatusList(statusList); } }else { List<String>
		 * tradeStatusList = tradeVO.getTradeStatusList();
		 * if(tradeStatusList.contains(TradesInfo.WAIT_BUYER_PAY)){
		 * tradeStatusList.remove(TradesInfo.WAIT_BUYER_PAY); }
		 * if(tradeStatusList.contains(TradesInfo.TRADE_CLOSED_BY_TAOBAO)){
		 * tradeStatusList.remove(TradesInfo.TRADE_CLOSED_BY_TAOBAO); }
		 * if(tradeStatusList.contains(TradesInfo.TRADE_NO_CREATE_PAY)){
		 * tradeStatusList.remove(TradesInfo.TRADE_NO_CREATE_PAY); }
		 * tradeVO.setTradeStatusList(tradeStatusList); }
		 */
		if ("6".equals(tradeVO.getRateStatus())) {
			tradeVO.setBuyerRate(true);
			tradeVO.setSellerRate(true);
		} else if ("2".equals(tradeVO.getRateStatus())) {
			tradeVO.setBuyerRate(false);
		} else if ("3".equals(tradeVO.getRateStatus())) {
			tradeVO.setSellerRate(false);
		} else if ("4".equals(tradeVO.getRateStatus())) {
			tradeVO.setBuyerRate(true);
			tradeVO.setSellerRate(false);
		} else if ("5".equals(tradeVO.getRateStatus())) {
			tradeVO.setBuyerRate(false);
			tradeVO.setSellerRate(true);
		}
		// }
		// 预售订单参数 TODO预售时提交
		if (tradeVO.getType() != null) {
			if ("fixed".equals(tradeVO.getType())) {
				tradeVO.setStepTradeStatus(null);
			} else if ("step".equals(tradeVO.getType())) {
				if (TradesInfo.FRONT_NOPAID_FINAL_NOPAID.equals(tradeVO.getStepTradeStatus())
						|| TradesInfo.FRONT_PAID_FINAL_NOPAID.equals(tradeVO.getStepTradeStatus())
						|| TradesInfo.FRONT_PAID_FINAL_PAID.equals(tradeVO.getStepTradeStatus())) {
					List<String> statusList = new ArrayList<String>();
					// statusList.add(TradesInfo.TRADE_FINISHED);
					// statusList.add(TradesInfo.WAIT_BUYER_CONFIRM_GOODS);
					statusList.add(TradesInfo.WAIT_BUYER_PAY);
					statusList.add(TradesInfo.WAIT_SELLER_SEND_GOODS);
					// statusList.add(TradesInfo.TRADE_BUYER_SIGNED);
					statusList.add(TradesInfo.PAY_PENDING);
					tradeVO.setTradeStatusList(statusList);
				}
			}
		} else {
			tradeVO.setStepTradeStatus(null);
		}
	}

	private List<String> splitStrToList(String oldStr, String splitStr) {
		if (oldStr != null && !"".equals(oldStr)) {
			String[] arr = oldStr.split(splitStr);
			List<String> list = new ArrayList<>();
			for (String string : arr) {
				list.add(string);
			}
			return list;
		}
		return null;
	}

	private List<Integer> splitStrToIntList(String oldStr, String splitStr) {
		if (oldStr != null && !"".equals(oldStr)) {
			String[] arr = oldStr.split(splitStr);
			List<Integer> list = new ArrayList<>();
			for (String string : arr) {
				list.add(Integer.parseInt(string));
			}
			return list;
		}
		return null;
	}

	private List<Long> splitStrToLongList(String oldStr, String splitStr) {
		if (oldStr != null && !"".equals(oldStr)) {
			String[] arr = oldStr.split(splitStr);
			List<Long> list = new ArrayList<>();
			for (String string : arr) {
				list.add(Long.parseLong(string));
			}
			return list;
		}
		return null;
	}

	/**
	 * @Title: getKey
	 * @Description: (每次搜索订单 生成一个key)
	 * @date 2017年5月17日 下午3:17:45
	 */
	private String getKey() {
		long id = System.currentTimeMillis();
		String key = id + "-";
		Random random = new Random();
		for (int i = 0; i < 3; i++) {
			key += random.nextInt(10);
		}
		return key;
	}

	/**
	 * @Description 上传订单日志
	 * @param userId
	 *            设定文件
	 * @return void 返回类型
	 * @author jackstraw_yu
	 * @date 2018年2月2日 下午5:22:43
	 */
	private void AsyncSendOrderLog(final LogAccessDTO log) {
		MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread() {
			@Override
			public void run() {
				try {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put(LogAccessDTO.class.getName(), log);
					map.put(LogType.class.getName(), LogType.ORDER_TYPE);
					LogAccessQueueService.getQueue().put(map);
					logger.info(
							"LogAccessQueueService.getQueue().size:" + LogAccessQueueService.getQueue().size() + "ge数");
				} catch (Exception e) {
					logger.error("##################### AsyncSendOrderLog() Exception:" + e.getMessage());
				}
			}
		});
	}

}
