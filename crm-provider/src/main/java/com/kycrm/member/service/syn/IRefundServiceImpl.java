package com.kycrm.member.service.syn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.dao.syn.IRefundDao;
import com.kycrm.member.domain.entity.refund.RefundDTO;


@Service("refundService")
@MyDataSource
public class IRefundServiceImpl implements IRefundService {
	
	
	private static final Logger logger=LoggerFactory.getLogger(IRefundServiceImpl.class);
	
	
	@Autowired
	private IRefundDao refundDao;
	
    

	@Override
	public void saveRefund(Long uid,List<RefundDTO> saveList) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("list", saveList);
		refundDao.saveRefund(map);
	}

	@Override
	public void updateRefund(Long uid,List<RefundDTO> updatelist) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("list", updatelist);
		refundDao.updateRefund(map);
	}
    /**
     * 检查表是否存在，如果不存在则创建表，并添加索引
     */
	@Override
	public void doCreateTableByNewUser(Long uid) {
		String  isExist=refundDao.isExistsTable(uid);
		if(isExist==null){
			logger.info("用户"+uid+"创建退款表");
			//创建表，并添加索引（暂时没有添加索引）
			refundDao.doCreateTableByNewUser(uid);
			//添加索引
			refundDao.addIndex(uid);
		}else{
			logger.info("用户"+uid+"退款表已经存在");
		}
	}
    /**
     * 判断该退款订单是否存在
     */
	@Override
	public boolean isExit(Long uid, Long refundId) {
		logger.info("判断退款订单是否存在,uid为"+uid+"退款订单为"+refundId);
		Map<String, Object> map=new  HashMap<String, Object>();
		map.put("uid", uid);
		map.put("refundId", refundId);
		Long exit = refundDao.isExit(map);
		if(exit>0){
			return true;
		}
		return false;
	}

	
	
    
}
