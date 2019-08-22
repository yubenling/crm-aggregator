package com.kycrm.transferdata.service.base;

import java.util.Date;
import java.util.List;

public interface ITransferBaseService<T> {

	public List<T> getByRange(String collectionName, int startPosition, int endPosition, Date startDate, Date endDate)
			throws Exception;

	public Long getCount(String collectionName, Date startDate, Date endDate) throws Exception;

}
