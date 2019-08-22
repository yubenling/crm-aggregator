package com.kycrm.member.service.message;

import java.util.Map;

import com.kycrm.member.domain.entity.message.BatchSmsData;

/** 
* @author wy
* @version 创建时间：2018年1月15日 下午12:05:47
*/
public interface IMultithreadBatchSmsService {
    /**
     * 群发批量短信
     * @author: wy
     * @time: 2018年1月15日 下午12:06:27
     * @param obj
     */
    public Map<String, Integer> batchOperateSms(final BatchSmsData obj);
   
}
