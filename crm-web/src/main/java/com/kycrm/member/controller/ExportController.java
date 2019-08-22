package com.kycrm.member.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.member.MemberFilterVO;
import com.kycrm.member.service.export.IExportService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.util.DateUtils;
import com.kycrm.util.JsonUtil;

@Controller
@RequestMapping("/export")
public class ExportController {

	private static final Log logger = LogFactory.getLog(ExportController.class);

	@Autowired
	private IExportService exportService;

	@Autowired
	private IUserInfoService userInfoService;

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
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/exportMemberInfo")
	public void exportMemberInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody String params)
			throws IOException {
		MemberFilterVO memberFilterVO = null;
		Long uid = null;
		if (params != null && !"".equals(params)) {
			memberFilterVO = JsonUtil.paramsJsonToObject(params, MemberFilterVO.class);
			uid = memberFilterVO.getUid();
			logger.info("用户UID = " + uid + " 使用【会员筛选 - 下载数据】功能入参 = " + JsonUtil.toJson(memberFilterVO));
		} else {
			logger.error("UID = " + uid + "请求日期 = " + DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss")
					+ " 会员筛选-下载数据 : JSON转换为memberFilterVO失败");
			return;
		}
		UserInfo user = userInfoService.findUserInfo(uid);
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
		cell.setCellValue("客户名称");
		cell = row.createCell(2);
		cell.setCellValue("旺旺ID");
		cell = row.createCell(3);
		cell.setCellValue("手机号");
		cell = row.createCell(4);
		cell.setCellValue("交易时间");
		cell = row.createCell(5);
//		cell.setCellValue("累计消费金额");
//		cell = row.createCell(6);
//		cell.setCellValue("购买次数");
//		cell = row.createCell(7);
		int startRows = 0;
		int pageSize = 2000;
		int rowNum = 0;
		Map<String, Object> memberInfoResultMap = null;
		Long limitId = null;
		while (true) {
			try {
				memberInfoResultMap = new HashMap<String, Object>(2);
				if (memberFilterVO != null && uid != null && !"".equals(uid)) {
					memberInfoResultMap = this.exportService.findMembersByCondition(uid, taoBaoUserNick, accessToken,
							memberFilterVO, startRows, pageSize, limitId);
				}
				List<MemberInfoDTO> memberInfoList = (List<MemberInfoDTO>) memberInfoResultMap.get("memberInfoList");
				limitId = (Long) memberInfoResultMap.get("limitId");
				if (memberInfoList != null && memberInfoList.size() > 0) {
					sheet = this.exportExcel(memberInfoList, sheet, row, cell, rowNum);
					rowNum = rowNum + pageSize;
				} else {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);
				break;
			}
		}
		if (memberInfoResultMap != null) {
			// 设置短信类型、短信条数与时间一列的宽度
			sheet.setColumnWidth(1, 4000);
			sheet.setColumnWidth(2, 4000);
			sheet.setColumnWidth(3, 4000);
			sheet.setColumnWidth(4, 4000);
			sheet.setColumnWidth(5, 4000);
//			sheet.setColumnWidth(6, 4000);
//			sheet.setColumnWidth(7, 4000);
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
			// 收货人姓名
			if (list.get(i).getReceiverName() != null) {
				row.createCell(1).setCellValue(list.get(i).getReceiverName());
			}
			// 旺旺ID
			if (list.get(i).getBuyerNick() != null) {
				row.createCell(2).setCellValue(list.get(i).getBuyerNick());
			}
			// 手机号
			if (list.get(i).getMobile() != null) {
				row.createCell(3).setCellValue(list.get(i).getMobile());
			}
			// 最后交易时间
			if (list.get(i).getLastTradeTime() != null) {
				row.createCell(4).setCellValue(DateUtils.formatDate(list.get(i).getLastTradeTime(), "yyyy-MM-dd HH:mm:ss"));
			}
//			// 累计消费金额
//			if (list.get(i).getTradeAmount() != null) {
//				row.createCell(5).setCellValue(list.get(i).getTradeAmount().doubleValue());
//			}
//			// 购买次数
//			if (list.get(i).getTradeNum() != null) {
//				row.createCell(6).setCellValue(list.get(i).getTradeNum());
//			}
		}
		return sheet;
	}
}
