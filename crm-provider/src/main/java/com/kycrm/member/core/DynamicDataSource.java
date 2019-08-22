package com.kycrm.member.core;

import java.util.logging.Logger;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.kycrm.member.core.annotation.MyDataSourceHolder;

/**
 * Function:
 * @author zlp
 * Date: 2017/4/2 上午12:22
 * @since JDK 1.7
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return MyDataSourceHolder.getDataSourceType();
    }

    public Logger getParentLogger() {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }
}

