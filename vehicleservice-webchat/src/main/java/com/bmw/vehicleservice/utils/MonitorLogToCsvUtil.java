package com.bmw.vehicleservice.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 将监控点日志记录至csv文件中
 * 
 * @author Administrator
 *
 */
public class MonitorLogToCsvUtil {
  /** 日志属性 **/
  private static final Logger LOG = LoggerFactory.getLogger("MONITORLOGTOCSV");

  public static void writeMonitorLogToCsv(String msg) {
    LOG.info("weather|main|"+msg);
  }
}
