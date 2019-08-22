package com.kycrm.syn.service.syn;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.kycrm.member.domain.entity.item.TbItem;
import com.kycrm.member.service.item.ITbItemService;
import com.kycrm.syn.core.redis.CacheService;
import com.kycrm.syn.init.InitManangeItem;
import com.kycrm.syn.queue.ItemQueueService;
import com.kycrm.syn.util.RedisLock;
import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.ValidateUtil;

/**
 * @author wy
 * @version 创建时间：2018年1月29日 下午3:31:50
 */
@Service
public class ItemSysInfoService {

	@Autowired
	private CacheService cacheService;

	@Autowired
	private ITbItemService tbItemService;

	@Resource(name = "redisTemplate")
	private StringRedisTemplate redisTemplate;

	private Logger logger = LoggerFactory.getLogger(ItemSysInfoService.class);

	// 节点同步跨度时间自己调节
	int ITEM_SYN_TIME_SLEEP_MINUTE = Constants.ITEM_SYN_TIME_SLEEP_MINUTE /*+ 95*/;

	/**
	 * 相同间隔时间的订单同步只允许一个job执行
	 * 
	 * @author: sungk
	 * @time: 2018年1月30日 下午4:16:00
	 */
	public void startSynItemBySysInfo() {
		String startTimeStr = cacheService.getJsonStr(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE,
				RedisConstant.RediskeyCacheGroup.ITEM_NODE_START_TIME_KEY);
		Optional<String> optional = Optional.ofNullable(startTimeStr); 
		String startTime = optional.orElse(Constants.DEFAULT_NODE_SYNC_TIME);

		String totalCountStr = cacheService.getJsonStr(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE,
				RedisConstant.RediskeyCacheGroup.TOTAL_ITEM_DATA_COUNT_KEY);
		Optional<String> optional2 = Optional.ofNullable(totalCountStr);
		String totalCount = optional2.orElse(optional2.orElse("0"));
		logger.info("item节点数据同步开始时间:{} 同步时长:{}分钟  历史已处理数据量:{}", 
				startTime, ITEM_SYN_TIME_SLEEP_MINUTE, totalCount);

		if (ValidateUtil.isEmpty(startTime) ) {
			return;
		}
		Date startDate = DateUtils.parseTime(startTime);
		//计算同步end时间
		Date endDate = DateUtils.addMinute(startDate, ITEM_SYN_TIME_SLEEP_MINUTE);
//		System.out.println(startDate+"----"+endDate);
		RedisLock lock = new RedisLock(redisTemplate,
				RedisConstant.RedisCacheGroup.ITEM_SYN_LOCK + startDate.getTime() + "_" + endDate.getTime(),
				InitManangeItem.SLEEP_TIME);
		try {
			if (lock.lock()) {
				this.startSyncItem(startDate, endDate, totalCount);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 分页定时查询订单，同步淘宝推送库的订单数据
	 * 
	 * @author: sungk
	 * @time: 2018年7月4日 下午2:21:05
	 */
	private void startSyncItem(Date startDate, Date endDate, String totalCount) {
		if (startDate == null || endDate == null) {
			return;
		}
		Long rowsCount = this.tbItemService.findCountByDate(startDate, endDate);
		Long pageNum = 0L;
		Long pageSize = Constants.PROCESS_PAGE_SIZE_MIDDLE;
		if (rowsCount / Constants.PROCESS_PAGE_SIZE_MIDDLE == 0) {
			pageNum = 1L;
		} else if (rowsCount % Constants.PROCESS_PAGE_SIZE_MIDDLE == 0) {
			pageNum = rowsCount / Constants.PROCESS_PAGE_SIZE_MIDDLE;
		} else {
			pageNum = (rowsCount + Constants.PROCESS_PAGE_SIZE_MIDDLE) / Constants.PROCESS_PAGE_SIZE_MIDDLE;
		}
		if (rowsCount == 0) {
			pageNum = 0l;
		}
		Integer syncAddTime=0;
		if(rowsCount>0){
			syncAddTime=(int)(ITEM_SYN_TIME_SLEEP_MINUTE*60/pageNum);
		}
		List<TbItem> rspTbItemList = new ArrayList<>();
		int start = 0;
		this.logger.info("**** item本次数据总量{}条 共{}页 每页{}条", rowsCount, pageNum, pageSize);
		Date SearchEedDate=null;
		while (start < pageNum) {
			try {
				if (start == pageNum - 1) {
					 SearchEedDate = endDate;
				}else{
					 SearchEedDate =DateUtils.addSecond(startDate, syncAddTime);	
				}
				logger.info("item开始时间"+DateUtils.formatTime(startDate)+"结束时间"+DateUtils.formatTime(SearchEedDate));
				// 分页查询item数据
				rspTbItemList = this.tbItemService.findItemByLimit(startDate, SearchEedDate)
						.stream()
						// TODO 测试过滤 上线注释
						//.filter(x -> x.getNick().equals("北京冰点零度") 
						//		|| x.getNick().equals("溜溜梅旗舰店")
						//		|| x.getNick().equals("小白你什么都没看见哦"))
						.collect(Collectors.toList());
            
				start ++;
				if (!ValidateUtil.isEmpty(rspTbItemList)) {
					this.logger.info("========>>>>>>item数据同步， 第{}次分发  本次数据{}条 <<<<<<=======", (start + 1), rspTbItemList.size());
					logger.debug("**** item过滤后数据量：{}", rspTbItemList.size());
					ItemQueueService.ITEM_QUEUE.put(rspTbItemList);
				}
				startDate=SearchEedDate;
				int size = ItemQueueService.ITEM_QUEUE.size();
				if (size > 10000) {
					int sleep = (int) size / 2000;
					Thread.sleep(Constants.TRADE_SYN_SLEEP_TIME * sleep);
				} else {
					Thread.sleep(Constants.TRADE_SYN_SLEEP_TIME);
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logger.info("item处理结束，下次同步节点时间：{}", sdf.format(endDate));
		rowsCount = Long.valueOf(totalCount) + rowsCount;
		cacheService.putNoTime(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE,
				RedisConstant.RediskeyCacheGroup.ITEM_NODE_START_TIME_KEY,
				sdf.format(endDate));
		cacheService.putNoTime(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE,
				RedisConstant.RediskeyCacheGroup.TOTAL_ITEM_DATA_COUNT_KEY, rowsCount);
	}
}
