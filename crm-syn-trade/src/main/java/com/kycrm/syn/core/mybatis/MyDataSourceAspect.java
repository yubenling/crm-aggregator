package com.kycrm.syn.core.mybatis;

import java.lang.reflect.Method;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.kycrm.member.domain.entity.base.BaseListEntity;
import com.kycrm.member.exception.KycrmApiException;
import com.kycrm.member.exception.NoSellernickByDataSourceException;

/**
 * @author wy
 * @version 创建时间：2018年1月10日 上午11:43:53
 */
@Aspect
@Component
public class MyDataSourceAspect {

	/**
	 * 主业务库
	 */
	public static final String MASTER = "defaultDataSource";
	/**
	 * sysInfo推送库
	 */
	public static final String SYSINFO = "sysinfoDataSource";
	/**
	 * 订单，会员，短信一库
	 */
	public static final String TRADE_FIRST = "dataSource1";
	/**
	 * 订单，会员，短信二库
	 */
	public static final String TRADE_SECOND = "dataSource2";

	private Logger logger = org.slf4j.LoggerFactory.getLogger(MyDataSourceAspect.class);

	@Before("execution(* com.kycrm.syn.service..*(..))")
	public void beforeMethod(JoinPoint point) throws Exception {
		Object target = point.getTarget();
		String method = point.getSignature().getName();
		Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getMethod().getParameterTypes();
		try {
			Class<? extends Object> cls = target.getClass();
			Method m = cls.getMethod(method, parameterTypes);
//			System.out.println("**** " + m.getName());
			if (m != null && m.isAnnotationPresent(MyDataSource.class)) {
				MyDataSource data = m.getAnnotation(MyDataSource.class);
				setDateSource(data, point);
			} else if (cls.isAnnotationPresent(MyDataSource.class)) {
				MyDataSource data = cls.getAnnotation(MyDataSource.class);
				setDateSource(data, point);
			}
		} catch (KycrmApiException e) {
			// 若出现自定义异常，强制停止方法执行
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			// 若出现异常，手动设为主库
			e.printStackTrace();
			throw e;
			// MyDataSourceHolder.setDataSourceType(MASTER);
		}
	}

	private void setDateSource(MyDataSource data, JoinPoint point) {
		String dataSourceType = data.value();
		if (dataSourceType == null || "".equals(dataSourceType) || "null".equalsIgnoreCase(dataSourceType)) {
			setDataSoruceByMethodParam(point);
		} else {
			setDataSoruceByValue(dataSourceType);
		}
	}

	/**
	 * 指定设置数据源
	 * 
	 * @author: wy
	 * @time: 2018年1月10日 下午12:09:21
	 * @param dataSourceType
	 */
	private void setDataSoruceByValue(String dataSourceType) {

		this.logger.debug("指定切换数据源：" + dataSourceType);
		switch (dataSourceType) {
		case MASTER: {
			MyDataSourceHolder.setDataSourceType(MASTER);
			break;
		}
		case SYSINFO: {
			MyDataSourceHolder.setDataSourceType(SYSINFO);
			break;
		}
		case TRADE_FIRST: {
			MyDataSourceHolder.setDataSourceType(TRADE_FIRST);
			break;
		}
		case TRADE_SECOND: {
			MyDataSourceHolder.setDataSourceType(TRADE_SECOND);
			break;
		}
		default: {
			MyDataSourceHolder.setDataSourceType(MASTER);
			break;
		}
		}
	}

	/**
	 * 通过方法的第一个字符串参数，动态设置数据源（非公用，只为订单库使用）
	 * 
	 * @author: wy
	 * @time: 2018年1月10日 下午12:10:14
	 * @param joinPoint
	 */
	private void setDataSoruceByMethodParam(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		if (args == null || args.length < 1) {
			throw new NoSellernickByDataSourceException("动态切换数据源时，方法没有参数，无法切换。");
		}
		if (!(args[0] instanceof Long)) {
			throw new NoSellernickByDataSourceException("动态切换数据源时，方法第一个参数不是用户主键id，无法动态切换。");
		}
		if (args[0] == null) {
			throw new NoSellernickByDataSourceException("动态切换数据源时，不允许用户主键为空");
		}
		String uid = String.valueOf(args[0]);
		this.logger.debug("动态切换数据源：" + uid);
		if ("".equals(uid) || "null".equalsIgnoreCase(uid)) {
			throw new NoSellernickByDataSourceException("动态切换数据源时卖家昵称不存在。");
		}
		if (new Integer(uid) % 2 == 0) {
			MyDataSourceHolder.setDataSourceType(TRADE_FIRST);
		} else if (new Integer(uid) % 2 != 0) {
			MyDataSourceHolder.setDataSourceType(TRADE_SECOND);
		} else {
			throw new KycrmApiException("错误的数据源切换，用户主键id：" + uid);
		}
	}

	/**
	 * 通过反射截取方法的参数，动态为实体类或map设置用户主键id
	 * 
	 * @author: wy
	 * @time: 2018年1月16日 上午11:09:35
	 * @param sellerNick
	 *            卖家昵称
	 * @param args
	 *            方法传递的参数
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private void saveUidToEntity(long uid, Object[] args) {
		if (args == null || args.length == 0) {
			return;
		}
		for (Object object : args) {
			if (object instanceof BaseListEntity) {
				BaseListEntity entity = (BaseListEntity) object;
				entity.setUid(uid);
				break;
			} else if (object instanceof Map) {
				Map map = (Map) object;
				map.put("uid", uid);
				break;
			}
		}
	}
}
