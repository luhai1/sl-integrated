package com.sl.datasource.multids;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultiDataSource
        extends AbstractRoutingDataSource {
    protected Object determineCurrentLookupKey() {
        System.out.println("---------------: " + DataSourceContextHolder.getDataSource());
        return DataSourceContextHolder.getDataSource();
    }
}