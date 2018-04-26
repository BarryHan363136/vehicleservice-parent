package com.bmw.vehicleservice.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 相关参数获取实现类
 * 
 * @author hantongshan
 * @modify time 2018-04-26 16:49 PM
 */
public class PropertieUtil {

  private static final Log logger = LogFactory.getLog(PropertieUtil.class);

  public static final String PARAS_CONFIG_FILENAME = "application.properties";

  protected static final Properties properties = new Properties();

  protected static void init(String propertyFileName) {
    logger.debug("PropertieUtil begin init,propertyFileName:" + propertyFileName);
    InputStream in = null;
    try {
      in = PropertieUtil.class.getClassLoader().getResourceAsStream(propertyFileName);
      if (in != null)
        properties.load(in);
      logger.info("PropertieUtil init sucessfull.............");
    } catch (IOException e) {
      logger.error("PropertieUtil init error:", e);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          logger.error("PropertieUtil in finally error:", e);
        }
      }
    }
  }

  public static String getProperty(String key, String defaultValue) {
    String value = properties.getProperty(key, defaultValue);
    logger.info("PropertieUtil getProperty key:" + key + ",value:" + value + ",defaultValue:"
        + defaultValue);
    return value;
  }

  public static String getProperty(String key) {
    String value = properties.getProperty(key);
    logger.info("PropertieUtil getProperty key:" + key + ",value:" + value);
    return value;
  }

  static {
    init(PARAS_CONFIG_FILENAME);
  }

//  /** 消息指令超时时间设置 */
//  public static Integer COMMAND_TIMEOUT_SECONDS = new Integer(
//      getProperty("opts.content.command.timeout.seconds"));
//  /* Command历史数据存储路径 */
//  public static String EXCEL_DATA_PATH = new String(
//      getProperty("opts.content.excel.data.storge.path"));
//  public static Integer COMMAND_TIMEOUT_DAYS = new Integer(
//      getProperty("opts.content.command.timeout.days"));
//  public static void main(String args[]) {
//    System.out.println("**********为:" + PropertieUtil.COMMAND_TIMEOUT_DAYS);
//  }
}
