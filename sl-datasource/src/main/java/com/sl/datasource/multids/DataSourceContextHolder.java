package com.sl.datasource.multids;

public class DataSourceContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();


    public static void setDataSource(String ds) { contextHolder.set(ds); }



    public static String getDataSource() { return contextHolder.get(); }



    public static void clearDataSource() { contextHolder.remove(); }
}
