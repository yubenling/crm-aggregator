package com.kycrm.syn.thread;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.member.TempEntity;
import com.kycrm.member.service.trade.ITradeDTOService;
import com.kycrm.syn.service.member.MemberDTOServiceImpl;

public class SynchronizeBuyNumber implements Runnable {

	private Logger logger = LoggerFactory.getLogger(SynchronizeBuyNumber.class);

	private Long uid;

	private String column;

	private ITradeDTOService tradeDTOService;

	private MemberDTOServiceImpl memberDTOService;

	public SynchronizeBuyNumber(Long uid, String column, ITradeDTOService tradeDTOService,
			MemberDTOServiceImpl memberDTOService) {
		super();
		this.uid = uid;
		this.column = column;
		this.tradeDTOService = tradeDTOService;
		this.memberDTOService = memberDTOService;
	}

	@Override
	public void run() {
		try {
			List<MemberInfoDTO> memberInfoDTOList = null;
			Map<String, Long> tempMap = null;
			List<TempEntity> list = null;
			List<MemberInfoDTO> memberInfoList = null;
			MemberInfoDTO memberInfoDTO = null;
			while (true) {
				memberInfoDTOList = this.memberDTOService.getBuyerNickList(uid, column, 0, 1000);
				if (memberInfoDTOList != null && memberInfoDTOList.size() == 0) {
					logger.info("crm_member_info_dto" + uid + " 的buy_number同步完成");
					break;
				}
				tempMap = new HashMap<String, Long>(memberInfoDTOList.size());
				for (int j = 0; j < memberInfoDTOList.size(); j++) {
					tempMap.put(memberInfoDTOList.get(j).getBuyerNick(), memberInfoDTOList.get(j).getId());
				}
				list = this.tradeDTOService.getBuyNumberFromTradeTable(uid, tempMap.keySet());
				if (list != null && list.size() > 0) {
					memberInfoList = new ArrayList<MemberInfoDTO>(list.size());
					for (int j = 0; j < list.size(); j++) {
						for (Entry<String, Long> map : tempMap.entrySet()) {
							if (list.get(j).getBuyerNick() == null || "".equals(list.get(j).getBuyerNick())) {
								continue;
							}
							if (map.getKey() == null || "".equals(map.getKey())) {
								continue;
							}
							if (list.get(j).getBuyerNick().equals(map.getKey())) {
								memberInfoDTO = new MemberInfoDTO();
								memberInfoDTO.setId(map.getValue());
								memberInfoDTO.setBuyNumber(list.get(j).getNum());
								memberInfoDTO.setBuyerNick(list.get(j).getBuyerNick());
								memberInfoList.add(memberInfoDTO);
							}
						}
					}
					this.memberDTOService.batchUpdate(uid, column, memberInfoList);
					int hour = GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY);
					if (hour >= 0 && hour <= 8) {
						Thread.sleep(2000);
					} else {
						Thread.sleep(5000);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
