package com.kycrm.syn.service.syn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.syn.service.member.MemberDTOServiceImpl;

@Service("updateMemberNumService")
public class UpdateMemberNumService {
	
	
	private static final Logger logger=LoggerFactory.getLogger(UpdateMemberNumService.class);
	
	@Resource(name = "memberDTOServiceImpl")
	private MemberDTOServiceImpl memberDTOServiceImpl;
    
	/**
	 * 根据订单处理会员拍下订单数量和拍下商品数量
	 * @param tradeList
	 */
	public void updateNum(List<TradeDTO> tradeList) {
		//分割成不同的用户
	    Map<Long, List<TradeDTO>> map=splitTradetoMap(tradeList);
		for(Long uid:map.keySet()){
			try {
				List<TradeDTO> list = map.get(uid);
				memberDTOServiceImpl.updateMemberNum(uid,list);
				Thread.sleep(100L);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("批量修改会员的拍下订单数和拍下商品数量出错"+e);
				continue;
			}
		}
	}
     /**
      * 
      * @param tradeList
      * @return
      */
	private Map<Long, List<TradeDTO>> splitTradetoMap(List<TradeDTO> tradeList) {
		Map<Long,List<TradeDTO>> map=new HashMap<Long, List<TradeDTO>>();
		for(TradeDTO td:tradeList){
			Long uid=td.getUid();
			if(uid==null){
				logger.info("uid为null订单tid为"+td.getTid());
				continue;
			}
			if(map.containsKey(uid)){
				map.get(uid).add(td);
			}else{
				List<TradeDTO> list=new ArrayList<TradeDTO>();
				list.add(td);
				map.put(uid, list);
			}
		}
		return map;
	}
	
	

}
