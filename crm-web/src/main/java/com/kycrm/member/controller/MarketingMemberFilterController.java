package com.kycrm.member.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.filterrecord.FilterRecord;
import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.entity.usermanagement.SellerGroupRule;
import com.kycrm.member.domain.vo.member.MemberFilterRecordVO;
import com.kycrm.member.domain.vo.member.MemberFilterVO;
import com.kycrm.member.domain.vo.message.BatchSendBaseFilterMemberMessageVO;
import com.kycrm.member.domain.vo.message.BatchSendPremiumFilterMemberMessageVO;
import com.kycrm.member.domain.vo.message.SendMsgVo;
import com.kycrm.member.domain.vo.premiummemberfilter.PremiumMemberFilterVO;
import com.kycrm.member.service.item.IGroupedGoodsService;
import com.kycrm.member.service.marketing.IFilterRecordService;
import com.kycrm.member.service.marketing.IMarketingMemberFilterService;
import com.kycrm.member.service.marketing.IPremiumMarketingMemberFilterService;
import com.kycrm.member.service.message.IBatchSendMemberMessageService;
import com.kycrm.member.service.message.IMsgSendRecordService;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.member.service.user.IUserAccountService;
import com.kycrm.member.service.usermanagement.ISellerGroupRuleService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.member.utils.UserInfoUtil;
import com.kycrm.util.AssembleFilterConditionUtil;
import com.kycrm.util.DateUtils;
import com.kycrm.util.GzipUtil;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.SmsCalculateUtil;
import com.taobao.api.SecretException;

/**
 * 营销中心-会员筛选
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年7月17日下午2:28:21
 * @Tags
 */
@Controller
@RequestMapping("/marketing")
public class MarketingMemberFilterController extends BaseController {

	private static final Log logger = LogFactory.getLog(MarketingMemberFilterController.class);

	@Autowired
	private UserInfoUtil userInfoUtil;

	@Autowired
	private SessionProvider sessionProvider;

	@Autowired
	private ICacheService cacheService;

	@Autowired
	private IMarketingMemberFilterService marketingMemberFilterService;

	@Autowired
	private IPremiumMarketingMemberFilterService premiumMarketingMemberFilterService;

	@Autowired
	private IMsgSendRecordService msgSendRecordService;

	@Autowired
	private IUserAccountService userAccountService;

	@Autowired
	private ISellerGroupRuleService sellerGroupRuleService;

	@Autowired
	private IGroupedGoodsService groupedGoodsService;

	@Autowired
	private IFilterRecordService filterRecordService;

	@Autowired
	private IBatchSendMemberMessageService batchSendMemberMessageService;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 会员筛选
	 * @Date 2018年7月21日上午10:03:23
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	@RequestMapping("/memberFilter")
	@ResponseBody
	public String memberFilter(HttpServletRequest request, HttpServletResponse response, @RequestBody String params)
			throws Exception {
		// 返回结果集
		Map<String, Object> resultMap = null;
		Long uid = null;
		try {
			resultMap = new HashMap<String, Object>(4);
			// 查询条件实体
			MemberFilterVO memberFilterVO = null;
			if (null != params && !"".equals(params)) {
				memberFilterVO = JsonUtil.paramsJsonToObject(params, MemberFilterVO.class);
			}
			// 从Redis中获取uid
			UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
			if (user == null) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			uid = user.getId();
			String accessToken = user.getAccessToken();
			memberFilterVO.setUid(uid);
			logger.info("用户UID = " + uid + " 使用【会员筛选】功能入参 = " + JsonUtil.toJson(memberFilterVO));
			Map<String, Object> memberInfoResultMap = this.marketingMemberFilterService.findMembersByCondition(uid,
					user.getTaobaoUserNick(), accessToken, memberFilterVO);
			String queryKey = userInfoUtil.getKey();
			this.cacheService.putString(
					RedisConstant.RediskeyCacheGroup.BASE_MEMBER_FILTER + "-" + queryKey + "-" + uid,
					JsonUtil.toJson(memberFilterVO));
			resultMap.put("memberInfoList", memberInfoResultMap.get("memberInfoList"));
			resultMap.put("memberCount", memberInfoResultMap.get("memberCount"));
			resultMap.put("memberFilterType", 1);// 1为基础会员筛选
			resultMap.put("queryKey", queryKey);
			return successReusltMap("1011").put(ApiResult.API_RESULT, resultMap).toJson();
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = new HashMap<String, Object>(2);
			logger.error("用户UID = " + uid + " 使用【会员筛选】出错 " + e);
			resultMap.put("success", false);
			resultMap.put("info", e.getMessage());
			if (e instanceof SecretException) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				return failureReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 高级会员筛选
	 * @Date 2018年10月24日下午5:19:52
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	@RequestMapping("/premiumMemberFilter")
	@ResponseBody
	public String premiumMemberFilter(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String params) throws Exception {
		// 返回结果集
		Map<String, Object> resultMap = null;
		Long uid = null;
		try {
			resultMap = new HashMap<String, Object>(3);
			// 查询条件实体
			PremiumMemberFilterVO premiumMemberFilterVO = null;
			if (null != params && !"".equals(params)) {
				premiumMemberFilterVO = JsonUtil.paramsJsonToObject(params, PremiumMemberFilterVO.class);
			}
			// 从Redis中获取uid
			UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
			if (user == null) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			uid = user.getId();
			// 设置uid
			premiumMemberFilterVO.setUid(uid);
			logger.info("用户UID = " + uid + " 使用【高级会员筛选】功能入参 = " + JsonUtil.toJson(premiumMemberFilterVO));
			// 【分组筛选】会员分组ID
			Long memberGroupId = premiumMemberFilterVO.getMemberGroupId();
			MemberFilterVO memberFilterVO = null;
			if (memberGroupId != null) {
				// 获取指定会员分组条件
				SellerGroupRule sellerGroupRule = this.sellerGroupRuleService.findSellerGroupRule(uid, memberGroupId);
				if (sellerGroupRule != null) {
					memberFilterVO = AssembleFilterConditionUtil.assembleMemberFilterCondition(uid, sellerGroupRule);
				} else {
					resultMap.put("success", false);
					resultMap.put("info", "高级会员筛选 - 会员分组不存在");
					return failureReusltMap("2067").put(ApiResult.API_RESULT, resultMap).toJson();
				}
			}
			// 【分组筛选】商品分组ID
			Long goodsGroupId = premiumMemberFilterVO.getGoodsGroupId();
			byte[] compress = null;
			if (goodsGroupId != null) {
				// 获取指定商品分组编号对应的商品
				List<Long> groupedNumIidList = this.groupedGoodsService.listGroupedNumIid(uid, goodsGroupId);
				String json = JsonUtil.toJson(groupedNumIidList);
				compress = GzipUtil.compress(json);
			}
			Long memberCount = this.premiumMarketingMemberFilterService.findMembersCountByCondition(uid,
					premiumMemberFilterVO, memberFilterVO, compress);
			String queryKey = userInfoUtil.getKey();
			this.cacheService.putString(
					RedisConstant.RediskeyCacheGroup.PREMIUM_MEMBER_FILTER + "-" + queryKey + "-" + uid,
					JsonUtil.toJson(premiumMemberFilterVO));
			resultMap.put("memberCount", memberCount);
			resultMap.put("memberFilterType", 2);// 2为高级会员筛选
			resultMap.put("queryKey", queryKey);
			return successReusltMap("1011").put(ApiResult.API_RESULT, resultMap).toJson();
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = new HashMap<String, Object>(2);
			logger.error("用户UID = " + uid + " 使用【高级会员筛选】出错 " + e);
			resultMap.put("success", false);
			resultMap.put("info", e.getMessage());
			if (e instanceof SecretException) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				return failureReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 会员筛选 - 下载数据
	 * @Date 2018年8月11日下午3:07:29
	 * @param request
	 * @param response
	 * @param params
	 * @throws IOException
	 * @ReturnType void
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	@RequestMapping(value = "/downloadMemberInfo")
	public void downloadMemberInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody String params)
			throws IOException {
		// 从Redis中获取uid
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (user == null) {
			logger.error("请求日期 = " + DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + " 会员筛选-下载数据 : user为空");
			return;
		}
		Long uid = user.getId();
		MemberFilterVO memberFilterVO = null;
		if (params != null && !"".equals(params)) {
			params = URLDecoder.decode(params);
			String[] split = params.split("=");
			split[0] = "\"" + split[0] + "\"";
			params = "{" + split[0] + ":" + split[1] + "}";
			memberFilterVO = JsonUtil.paramsJsonToObject(params, MemberFilterVO.class);
			logger.info("用户UID = " + uid + " 使用【会员筛选 - 下载数据】功能入参 = " + JsonUtil.toJson(memberFilterVO));
		} else {
			logger.error("UID = " + uid + "请求日期 = " + DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss")
					+ " 会员筛选-下载数据 : JSON转换为memberFilterVO失败");
			return;
		}
		if (memberFilterVO == null) {
			logger.error("UID = " + uid + "请求日期 = " + DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss")
					+ " 会员筛选-下载数据 : 会员筛选条件对象memberFilterVO为空");
			return;
		}
		memberFilterVO.setUid(uid);
		String taoBaoUserNick = user.getTaobaoUserNick();
		String accessToken = user.getAccessToken();
		ServletOutputStream outputStream = null;
		// 设置导出报表文件名
		String filename = new String("会员信息.xlsx".getBytes(), "ISO-8859-1");
		// 创建一个工作簿
		SXSSFWorkbook book = new SXSSFWorkbook();
		// 使用工作簿创建一个工作表
		Sheet sheet = book.createSheet();
		// 创建一行
		Row row = sheet.createRow(0);
		// 创建单元格
		Cell cell = row.createCell(0);
		// 设置第一行的内容
		cell.setCellValue("序号");
		cell = row.createCell(1);
		cell.setCellValue("客户昵称");
		cell = row.createCell(2);
		cell.setCellValue("会员等级");
		cell = row.createCell(3);
		cell.setCellValue("最后交易时间");
		cell = row.createCell(4);
		cell.setCellValue("成功交易笔数");
		cell = row.createCell(5);
		cell.setCellValue("累计消费金额");
		cell = row.createCell(6);
		cell.setCellValue("平均客单价");
		cell = row.createCell(7);
		// cell.setCellValue("收货地址");
		// cell = row.createCell(8);
		int currentPage = 1;
		int pageSize = 5000;
		int rowNum = 0;
		Map<String, Object> memberInfoResultMap = null;
		while (true) {
			try {
				memberInfoResultMap = new HashMap<String, Object>(2);
				if (memberFilterVO != null && uid != null && !"".equals(uid)) {
					memberInfoResultMap = this.marketingMemberFilterService.findMembersByCondition(uid, taoBaoUserNick,
							accessToken, memberFilterVO, currentPage, pageSize, true, false);
				}
				List<MemberInfoDTO> memberInfoList = (List<MemberInfoDTO>) memberInfoResultMap.get("memberInfoList");
				if (memberInfoList != null && memberInfoList.size() > 0) {
					sheet = this.exportExcel(memberInfoList, sheet, row, cell, rowNum);
					rowNum = rowNum + pageSize;
					currentPage++;
				} else {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);

			}
		}
		if (memberInfoResultMap != null) {
			// 设置短信类型、短信条数与时间一列的宽度
			sheet.setColumnWidth(1, 4000);
			sheet.setColumnWidth(2, 4000);
			sheet.setColumnWidth(3, 4000);
			sheet.setColumnWidth(4, 4000);
			sheet.setColumnWidth(5, 4000);
			sheet.setColumnWidth(6, 4000);
			sheet.setColumnWidth(7, 4000);
			// sheet.setColumnWidth(8, 10000);
			response.setHeader("Content-Disposition", "attachment;filename=" + filename);
			try {
				outputStream = response.getOutputStream();
				book.write(outputStream);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			logger.error("UID = " + uid + " 请求日期 = " + DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss")
					+ " 查询会员集合为空");
		}
	}

	private Sheet exportExcel(List<MemberInfoDTO> list, Sheet sheet, Row row, Cell cell, int rowNum) throws Exception {
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(rowNum + 1 + i);
			// 序号
			row.createCell(0).setCellValue(rowNum + i + 1);
			// 客户昵称
			if (list.get(i).getBuyerNick() != null) {
				row.createCell(1).setCellValue(list.get(i).getBuyerNick());
			}
			// 会员等级
			row.createCell(2).setCellValue("店铺会员");
			// 最后交易时间
			if (list.get(i).getLastTradeTime() != null) {
				row.createCell(3)
						.setCellValue(DateUtils.formatDate(list.get(i).getLastTradeTime(), "yyyy-MM-dd hh:mm:ss"));
			}
			// 成功交易笔数
			if (list.get(i).getTradeNum() != null) {
				row.createCell(4).setCellValue(list.get(i).getTradeNum() + "");
			}
			// 累计消费金额
			if (list.get(i).getTradeAmount() != null) {
				row.createCell(5).setCellValue(list.get(i).getTradeAmount() + "");
			}
			// 平均客单价
			if (list.get(i).getAvgTradePrice() != null) {
				row.createCell(6).setCellValue(list.get(i).getAvgTradePrice().doubleValue());
			}
			// // 收货地址
			// if (list.get(i).getOrderDTO().getReceiverAddress() != null) {
			// row.createCell(7).setCellValue(list.get(i).getOrderDTO().getReceiverAddress());
			// }
		}
		return sheet;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 会员筛选 - 下载数据
	 * @Date 2018年8月11日下午3:07:29
	 * @param request
	 * @param response
	 * @param params
	 * @throws IOException
	 * @ReturnType void
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	@RequestMapping(value = "/downloadPremiumFilterMemberInfo")
	public void downloadPremiumFilterMemberInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String params) throws IOException {
		// 从Redis中获取uid
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (user == null) {
			logger.error("请求日期 = " + DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + " 会员筛选-下载数据 : user为空");
			return;
		}
		Long uid = user.getId();
		PremiumMemberFilterVO premiumMemberFilterVO = null;
		if (params != null && !"".equals(params)) {
			params = URLDecoder.decode(params);
			String[] split = params.split("=");
			split[0] = "\"" + split[0] + "\"";
			params = "{" + split[0] + ":" + split[1] + "}";
			premiumMemberFilterVO = JsonUtil.paramsJsonToObject(params, PremiumMemberFilterVO.class);
			logger.info("用户UID = " + uid + " 使用【高级会员筛选 - 下载数据】功能入参 = " + JsonUtil.toJson(premiumMemberFilterVO));
		} else {
			logger.error("UID = " + uid + "请求日期 = " + DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss")
					+ " 会员筛选-下载数据 : JSON转换为memberFilterVO失败");
			return;
		}
		if (premiumMemberFilterVO == null) {
			logger.error("UID = " + uid + "请求日期 = " + DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss")
					+ " 会员筛选-下载数据 : 会员筛选条件对象memberFilterVO为空");
			return;
		}
		premiumMemberFilterVO.setUid(uid);
		String taoBaoUserNick = user.getTaobaoUserNick();
		String accessToken = user.getAccessToken();
		ServletOutputStream outputStream = null;
		// 设置导出报表文件名
		String filename = new String("会员信息.xlsx".getBytes(), "ISO-8859-1");
		// 创建一个工作簿
		SXSSFWorkbook book = new SXSSFWorkbook();
		// 使用工作簿创建一个工作表
		Sheet sheet = book.createSheet();
		// 创建一行
		Row row = sheet.createRow(0);
		// 创建单元格
		Cell cell = row.createCell(0);
		// 设置第一行的内容
		cell.setCellValue("序号");
		cell = row.createCell(1);
		cell.setCellValue("会员昵称");
		cell = row.createCell(2);
		cell.setCellValue("手机号");
		cell = row.createCell(3);
		cell.setCellValue("最新购买时间");
		cell = row.createCell(4);
		cell.setCellValue("订单金额");
		cell = row.createCell(5);
		cell.setCellValue("拍下金额");
		cell = row.createCell(6);
		cell.setCellValue("付款金额");
		cell = row.createCell(7);
		cell.setCellValue("付款商品数量");
		cell = row.createCell(8);
		cell.setCellValue("拍下商品数量");
		cell = row.createCell(9);
		cell.setCellValue("购买次数");
		cell = row.createCell(10);
		cell.setCellValue("拍下次数");
		cell = row.createCell(11);
		cell.setCellValue("拍下未付款次数");
		cell = row.createCell(12);
		int currentPage = 1;
		int pageSize = 5000;
		int rowNum = 0;
		Map<String, Object> memberInfoResultMap = null;
		while (true) {
			try {
				memberInfoResultMap = new HashMap<String, Object>(2);
				if (premiumMemberFilterVO != null && uid != null && !"".equals(uid)) {
					// 【分组筛选】会员分组ID
					Long memberGroupId = premiumMemberFilterVO.getMemberGroupId();
					MemberFilterVO memberFilterVO = null;
					if (memberGroupId != null) {
						// 获取指定会员分组条件
						SellerGroupRule sellerGroupRule = this.sellerGroupRuleService.findSellerGroupRule(uid,
								memberGroupId);
						if (sellerGroupRule != null) {
							memberFilterVO = AssembleFilterConditionUtil.assembleMemberFilterCondition(uid,
									sellerGroupRule);
						} else {
							throw new Exception("高级会员筛选 - 会员分组不存在");
						}
					}
					// 【分组筛选】商品分组ID
					Long goodsGroupId = premiumMemberFilterVO.getGoodsGroupId();
					byte[] compress = null;
					if (goodsGroupId != null) {
						// 获取指定商品分组编号对应的商品
						List<Long> groupedNumIidList = this.groupedGoodsService.listGroupedNumIid(uid, goodsGroupId);
						String json = JsonUtil.toJson(groupedNumIidList);
						compress = GzipUtil.compress(json);
					}
					memberInfoResultMap = this.premiumMarketingMemberFilterService.findMembersByCondition(uid,
							taoBaoUserNick, accessToken, premiumMemberFilterVO, memberFilterVO, compress, currentPage,
							pageSize, true, false);
				}
				List<MemberInfoDTO> memberInfoList = (List<MemberInfoDTO>) memberInfoResultMap.get("memberInfoList");
				if (memberInfoList != null && memberInfoList.size() > 0) {
					sheet = this.exportPremiumExcel(memberInfoList, sheet, row, cell, rowNum);
					rowNum = rowNum + pageSize;
					currentPage++;
				} else {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);

			}
		}
		if (memberInfoResultMap != null) {
			// 设置短信类型、短信条数与时间一列的宽度
			sheet.setColumnWidth(1, 4000);
			sheet.setColumnWidth(2, 4000);
			sheet.setColumnWidth(3, 5800);
			sheet.setColumnWidth(4, 4000);
			sheet.setColumnWidth(5, 4000);
			sheet.setColumnWidth(6, 4000);
			sheet.setColumnWidth(7, 4000);
			sheet.setColumnWidth(8, 4000);
			sheet.setColumnWidth(9, 4000);
			sheet.setColumnWidth(10, 3000);
			sheet.setColumnWidth(11, 5000);
			response.setHeader("Content-Disposition", "attachment;filename=" + filename);
			try {
				outputStream = response.getOutputStream();
				book.write(outputStream);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			logger.error("UID = " + uid + " 请求日期 = " + DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss")
					+ " 查询会员集合为空");
		}
	}

	private Sheet exportPremiumExcel(List<MemberInfoDTO> list, Sheet sheet, Row row, Cell cell, int rowNum)
			throws Exception {
		MemberInfoDTO memberInfoDTO = null;
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(rowNum + 1 + i);
			// 序号
			row.createCell(0).setCellValue(rowNum + i + 1);
			memberInfoDTO = list.get(i);
			// 会员昵称
			if (memberInfoDTO.getBuyerNick() != null) {
				row.createCell(1).setCellValue(memberInfoDTO.getBuyerNick());
			}
			// 手机号
			if (memberInfoDTO.getMobile() != null) {
				row.createCell(2).setCellValue(memberInfoDTO.getMobile());
			}
			// 最后交易时间
			if (memberInfoDTO.getLastTradeTime() != null) {
				row.createCell(3)
						.setCellValue(DateUtils.formatDate(memberInfoDTO.getLastTradeTime(), "yyyy-MM-dd HH:mm:ss"));
			}
			OrderDTO orderDTO = memberInfoDTO.getOrderDTO();
			if (orderDTO != null) {
				// 订单金额
				if (orderDTO.getPayment() != null) {
					row.createCell(4).setCellValue(orderDTO.getPayment() + "");
				}
				// 拍下金额
				if (orderDTO.getTradeTotalFee() != null) {
					row.createCell(5).setCellValue(orderDTO.getTradeTotalFee() + "");
				}
				// 付款金额
				if (orderDTO.getTradePayment() != null) {
					row.createCell(6).setCellValue(orderDTO.getTradePayment() + "");
				}
			}
			// 付款商品数量
			if (memberInfoDTO.getItemNum() != null) {
				row.createCell(7).setCellValue(memberInfoDTO.getItemNum() + "");
			}
			// 拍下商品数量
			if (memberInfoDTO.getAddItemNum() != null) {
				row.createCell(8).setCellValue(memberInfoDTO.getAddItemNum() + "");
			}
			// 购买次数
			if (memberInfoDTO.getBuyNumber() != null) {
				row.createCell(9).setCellValue(memberInfoDTO.getBuyNumber() + "");
			}
			// 拍下次数
			if (memberInfoDTO.getAddNumber() != null) {
				row.createCell(10).setCellValue(memberInfoDTO.getAddNumber() + "");
			}
			// 拍下未付款次数
			if (memberInfoDTO.getAddNotPayNumber() != null) {
				row.createCell(11).setCellValue(memberInfoDTO.getAddNotPayNumber() + "");
			}
		}
		return sheet;
	}

	/**
	 *
	 * @Author ZhengXiaoChen
	 * @Description 营销中心 - 会员筛选 - 短信群发
	 * @Date 2018年8月27日下午1:39:57
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	@RequestMapping("/sendMessageForMultiMembers")
	@ResponseBody
	public String sendMessageForMultiMembers(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String params) throws Exception {
		// 返回结果集
		Map<String, Object> resultMap = null;
		Long msgId = 0L;
		StopWatch watch = new StopWatch();
		watch.start();
		try {
			resultMap = new HashMap<String, Object>(3);
			SendMsgVo sendMsgVo = null;
			if (params != null && !"".equals(params)) {
				sendMsgVo = JsonUtil.paramsJsonToObject(params, SendMsgVo.class);
			} else {
				watch.stop();
				resultMap.put("success", false);
				resultMap.put("info", "入参为空");
				return failureReusltMap(null).put(ApiResult.API_RESULT, resultMap).toJson();
			}
			// 从Redis中获取uid
			UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
			if (user == null) {
				resultMap.put("success", false);
				resultMap.put("info", "用户的UID为空");
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			Long uid = user.getId();
			sendMsgVo.setUid(uid);
			logger.info("UID = " + uid + " 会员短信群发入参 = " + JsonUtil.toJson(sendMsgVo));
			String queryKey = sendMsgVo.getQueryKey();
			// 卖家编号
			String taoBaoUserNick = this.userInfoUtil.getTaoBaoUserNick(request, response);
			sendMsgVo.setUserId(taoBaoUserNick);
			sendMsgVo.setIpAddress(this.getIPAddress(request));
			Long memberCount = null;
			Integer deductionNum = null;
			Long smsNum = null;
			MemberFilterVO memberFilterVO = null;
			PremiumMemberFilterVO premiumMemberFilterVO = null;
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			Long filterRecordId = null;
			SellerGroupRule sellerGroupRule = null;
			byte[] compress = null;
			// 会员筛选类型【1：基础会员筛选; 2：高级会员筛选】
			Integer memberFilterType = sendMsgVo.getMemberFilterType();
			if (memberFilterType != null && !"".equals(memberFilterType)) {
				if (memberFilterType == 1) {
					memberFilterVO = this.cacheService.getString(
							RedisConstant.RediskeyCacheGroup.BASE_MEMBER_FILTER + "-" + queryKey + "-" + uid,
							MemberFilterVO.class);
					if (memberFilterVO != null) {
						memberFilterVO.setUid(uid);
						logger.info("UID = " + uid + " UUID = " + uuid + " querykey = " + queryKey + " 【基础会员筛选】条件 = "
								+ memberFilterVO.toString());
						memberCount = this.marketingMemberFilterService.findMemberCountByCondition(uid, memberFilterVO);
						logger.info("UID = " + uid + " UUID = " + uuid + " 将要发送短信的会员总数 = " + memberCount);
						// 记录本次发送短信所使用的基础会员筛选条件
						filterRecordId = this.insertFilterRecord(uid, taoBaoUserNick, 1, uuid, memberFilterVO);
					} else {
						watch.stop();
						resultMap.put("success", false);
						resultMap.put("info", "基础会员筛选条件缓存失效");
						return failureReusltMap("2064").put(ApiResult.API_RESULT, resultMap).toJson();
					}
				} else if (memberFilterType == 2) {
					premiumMemberFilterVO = this.cacheService.getString(
							RedisConstant.RediskeyCacheGroup.PREMIUM_MEMBER_FILTER + "-" + queryKey + "-" + uid,
							PremiumMemberFilterVO.class);
					if (premiumMemberFilterVO != null) {
						premiumMemberFilterVO.setUid(uid);
						logger.info("UID = " + uid + " UUID = " + uuid + " querykey = " + queryKey + " 【高级会员筛选】条件 = "
								+ premiumMemberFilterVO.toString());
						// 【分组筛选】会员分组ID
						Long memberGroupId = premiumMemberFilterVO.getMemberGroupId();
						if (memberGroupId != null) {
							// 获取指定会员分组条件
							sellerGroupRule = this.sellerGroupRuleService.findSellerGroupRule(uid, memberGroupId);
							if (sellerGroupRule != null) {
								memberFilterVO = AssembleFilterConditionUtil.assembleMemberFilterCondition(uid,
										sellerGroupRule);
							} else {
								watch.stop();
								resultMap.put("success", false);
								resultMap.put("info", "高级会员筛选 - 会员分组不存在");
								return failureReusltMap("2067").put(ApiResult.API_RESULT, resultMap).toJson();
							}
						}
						// 【分组筛选】商品分组ID
						Long goodsGroupId = premiumMemberFilterVO.getGoodsGroupId();
						if (goodsGroupId != null) {
							// 获取指定商品分组编号对应的商品
							List<Long> groupedNumIidList = this.groupedGoodsService.listGroupedNumIid(uid,
									goodsGroupId);
							String json = JsonUtil.toJson(groupedNumIidList);
							compress = GzipUtil.compress(json);
						}
						memberCount = this.premiumMarketingMemberFilterService.findMembersCountByCondition(uid,
								premiumMemberFilterVO, memberFilterVO, compress);
						logger.info("UID = " + uid + " UUID = " + uuid + " 将要发送短信的会员总数 = " + memberCount);
						// 记录本次发送短信所使用的高级会员筛选条件
						filterRecordId = this.insertFilterRecord(uid, taoBaoUserNick, 2, uuid, premiumMemberFilterVO);
					} else {
						watch.stop();
						resultMap.put("success", false);
						resultMap.put("info", "高级会员筛选条件缓存失效");
						return failureReusltMap("2065").put(ApiResult.API_RESULT, resultMap).toJson();
					}
				} else {
					watch.stop();
					resultMap.put("success", false);
					resultMap.put("info", "未知筛选类型");
					return failureReusltMap("2033").put(ApiResult.API_RESULT, resultMap).toJson();
				}
				// 获取单条短信发送条数
				deductionNum = SmsCalculateUtil.getActualDeduction(sendMsgVo.getContent());
				// 查询用户的短信条数
				smsNum = this.userAccountService.findUserAccountSms(uid);
				// 判断当前短信条数是否大于发送条数
				if (smsNum < memberCount * deductionNum) {
					resultMap.put("success", false);
					resultMap.put("info", "短信余额不足");
					return failureReusltMap(ApiResult.SMS_NOT_ENOUGH).toJson();
				} else {
					Boolean schedule = sendMsgVo.getSchedule();
					if (schedule != null) {
						if (memberFilterType == 1) {
							BatchSendBaseFilterMemberMessageVO batchSendBaseFilterMemberMessageVO = new BatchSendBaseFilterMemberMessageVO();
							batchSendBaseFilterMemberMessageVO.setUser(user);
							batchSendBaseFilterMemberMessageVO.setUuid(uuid);
							batchSendBaseFilterMemberMessageVO.setMemberCount(memberCount);
							batchSendBaseFilterMemberMessageVO.setFilterRecordId(filterRecordId);
							batchSendBaseFilterMemberMessageVO.setSendMsgVo(sendMsgVo);
							batchSendBaseFilterMemberMessageVO.setMemberFilterVO(memberFilterVO);
							batchSendBaseFilterMemberMessageVO.setMemberFilterType(memberFilterType);
							if (memberFilterVO.getSentFilter() != null && !"".equals(memberFilterVO.getSentFilter())) {
								batchSendBaseFilterMemberMessageVO
										.setShieldDay(Integer.valueOf(memberFilterVO.getSentFilter()));
							}
							this.batchSendMemberMessageService.batchSendMemberMessageBaseMethod(uid,
									batchSendBaseFilterMemberMessageVO, false);
						} else {
							BatchSendPremiumFilterMemberMessageVO batchSendPremiumFilterMemberMessageVO = new BatchSendPremiumFilterMemberMessageVO();
							batchSendPremiumFilterMemberMessageVO.setUser(user);
							batchSendPremiumFilterMemberMessageVO.setUuid(uuid);
							batchSendPremiumFilterMemberMessageVO.setMemberCount(memberCount);
							batchSendPremiumFilterMemberMessageVO.setFilterRecordId(filterRecordId);
							batchSendPremiumFilterMemberMessageVO.setSendMsgVo(sendMsgVo);
							batchSendPremiumFilterMemberMessageVO.setPremiumMemberFilterVO(premiumMemberFilterVO);
							batchSendPremiumFilterMemberMessageVO.setMemberFilterType(memberFilterType);
							batchSendPremiumFilterMemberMessageVO.setMemberFilterVO(memberFilterVO);
							batchSendPremiumFilterMemberMessageVO.setCompress(compress);
							if (premiumMemberFilterVO.getSentFilter() != null
									&& !"".equals(premiumMemberFilterVO.getSentFilter())) {
								batchSendPremiumFilterMemberMessageVO
										.setShieldDay(Integer.valueOf(premiumMemberFilterVO.getSentFilter()));
							}
							this.batchSendMemberMessageService.batchSendMemberMessageBaseMethod(uid,
									batchSendPremiumFilterMemberMessageVO, false);
						}
					} else {
						watch.stop();
						resultMap.put("success", false);
						resultMap.put("info", "发送短信类型为空");
						return failureReusltMap("2063").put(ApiResult.API_RESULT, resultMap).toJson();
					}
				}
				resultMap.put("success", true);
				return successReusltMap("1015").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				watch.stop();
				resultMap.put("success", false);
				resultMap.put("info", "筛选类型为空");
				return failureReusltMap("2062").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("会员短信群发出错" + e);
			resultMap.put("success", false);
			resultMap.put("info", e.getMessage());
			this.updateMsgSendRecord(msgId);
			if (e instanceof SecretException) {
				return failureReusltMap("4001").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				return failureReusltMap("2061").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 更新短信总记录表状态
	 * @Date 2018年9月12日下午1:50:19
	 * @param msgId
	 * @throws Exception
	 * @ReturnType void
	 */
	private void updateMsgSendRecord(Long msgId) throws Exception {
		MsgSendRecord msg = new MsgSendRecord();
		msg.setId(msgId);
		msg.setStatus("2");// 2 表示全部失败
		this.msgSendRecordService.updateMsgRecordByMsgId(msg);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 获取客户端IP
	 * @Date 2018年9月12日下午1:50:06
	 * @param request
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	private String getIPAddress(HttpServletRequest request) throws Exception {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 *
	 * @Author ZhengXiaoChen
	 * @Description 记录筛选条件
	 * @Date 2018年11月14日下午4:26:37
	 * @param uid
	 * @param taoBaoUserNick
	 * @param filterType
	 * @param uuid
	 * @param object
	 * @throws Exception
	 * @ReturnType void
	 */
	private Long insertFilterRecord(Long uid, String taoBaoUserNick, Integer filterType, String uuid, Object object)
			throws Exception {
		FilterRecord filterRecord = new FilterRecord();
		filterRecord.setUid(uid);
		filterRecord.setFilterType(filterType);
		if (filterType == 1) {
			MemberFilterVO memberFilterVO = (MemberFilterVO) object;
			filterRecord.setParameter(JsonUtil.toJson(memberFilterVO));
		} else {
			PremiumMemberFilterVO premiumMemberFilterVO = (PremiumMemberFilterVO) object;
			filterRecord.setParameter(JsonUtil.toJson(premiumMemberFilterVO));

		}
		filterRecord.setUuid(uuid);
		filterRecord.setCreatedBy(taoBaoUserNick);
		filterRecord.setCreatedDate(new Date());
		logger.info("UID = " + uid + "创建一条筛选记录 = " + filterRecord.toString());
		return this.filterRecordService.insertFilterRecord(uid, filterRecord);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 再次营销
	 * @Date 2018年12月26日上午10:34:46
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	@RequestMapping("/findFilterRecord")
	@ResponseBody
	public String findFilterRecord(HttpServletRequest request, HttpServletResponse response, @RequestBody String params)
			throws Exception {
		// 返回结果集
		Map<String, Object> resultMap = null;
		MemberFilterRecordVO memberFilterRecordVO = null;
		Long uid = null;
		try {
			resultMap = new HashMap<String, Object>(4);
			// 查询条件实体
			if (null != params && !"".equals(params)) {
				memberFilterRecordVO = JsonUtil.paramsJsonToObject(params, MemberFilterRecordVO.class);
			}
			// 从Redis中获取uid
			UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
			if (user == null) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			uid = user.getId();
			logger.info("UID = " + uid + " 使用再次营销接口入参 = " + JsonUtil.toJson(memberFilterRecordVO));
			if (uid == null) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			Long id = memberFilterRecordVO.getId();
			if (id == null) {
				return failureReusltMap("2071").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			FilterRecord filterRecord = this.filterRecordService.getFilterRecordById(uid, id);
			Integer filterType = filterRecord.getFilterType();
			if (filterType == 1) {
				filterRecord.setMemberFilterVO(JsonUtil.fromJson(filterRecord.getParameter(), MemberFilterVO.class));
			} else {
				filterRecord.setPremiumMemberFilterVO(
						JsonUtil.fromJson(filterRecord.getParameter(), PremiumMemberFilterVO.class));
			}
			resultMap.put("success", true);
			resultMap.put("info", "查询成功");
			resultMap.put("filterRecord", filterRecord);
			return successReusltMap("1011").put(ApiResult.API_RESULT, resultMap).toJson();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("会员短信群发出错" + e);
			resultMap.put("success", false);
			resultMap.put("info", e.getMessage());
			return successReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
		}
	}
}
