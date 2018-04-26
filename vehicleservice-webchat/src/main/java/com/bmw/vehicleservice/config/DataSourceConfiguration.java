package com.bmw.vehicleservice.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.bmw.vehicleservice.utils.PropertieUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 描述: 数据源实现
 *
 * @outhor hants
 * @create 2018-04-26 上午11:13
 */
@Configuration
public class DataSourceConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfiguration.class);

    /**
     * 数据源配置相关参数设置
     * */
    private String driverClassName;
    private String driverUrl;
    private String driverUsername;
    private String driverPassword;
    private Integer initialSize;
    private Integer minIdle;
    private Integer maxActive;

    public DataSourceConfiguration(){
        logger.info("初始化数据源服务配置...");
        this.driverClassName = new String(PropertieUtil.getProperty("jdbc.driverClassName"));
        this.driverUrl = new String(PropertieUtil.getProperty("jdbc.url"));
        this.driverUsername = new String(PropertieUtil.getProperty("jdbc.username"));
        this.driverPassword = new String(PropertieUtil.getProperty("jdbc.password"));
        this.initialSize = StringUtils.isNotBlank(PropertieUtil.getProperty("jdbc.initialsize")) ? Integer.parseInt(PropertieUtil.getProperty("jdbc.initialsize")) : 10;
        this.minIdle = StringUtils.isNotBlank(PropertieUtil.getProperty("jdbc.minidle")) ? Integer.parseInt(PropertieUtil.getProperty("jdbc.minidle")) : 10;
        this.maxActive = StringUtils.isNotBlank(PropertieUtil.getProperty("jdbc.maxactive")) ? Integer.parseInt(PropertieUtil.getProperty("jdbc.maxactive")) : 100;
    }

    @Bean(name = "dataSource", initMethod = "init", destroyMethod = "close")
    @Primary
    public DruidDataSource dataSource() {
        try {
            DruidDataSource dataSource = new DruidDataSource();
            logger.info("数据源配置参数,driverClassName:"+driverClassName+",driverUrl:"+driverUrl+",driverUsername:"+driverUsername+",driverPassword:"+driverPassword);
            dataSource.setDriverClassName(driverClassName);
            dataSource.setUrl(driverUrl);
            dataSource.setUsername(driverUsername);
            dataSource.setPassword(driverPassword);
            dataSource.setInitialSize(initialSize);
            dataSource.setMinIdle(minIdle);
            dataSource.setMaxActive(maxActive);
            dataSource.setMaxWait(60000);
            dataSource.setMaxWait(1000);
            dataSource.setTimeBetweenEvictionRunsMillis(60000);
            dataSource.setMinEvictableIdleTimeMillis(300000);
            dataSource.setValidationQuery("select 1 from dual");
            dataSource.setTestWhileIdle(true);
            dataSource.setTestOnBorrow(false);
            dataSource.setTestOnReturn(false);
            dataSource.setPoolPreparedStatements(true);
            dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
            dataSource.setFilters("stat");
            logger.info("数据源初始化完成...");
            return dataSource;
        } catch (Exception e) {
            logger.error("DruidDataSource初始化失败", e);
            return null;
        }
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     *创建sqlSessionFactoryBean 实例
     * 并且设置configtion 如驼峰命名.等等
     * 设置mapper 映射路径
     * 设置datasource数据源
     * sqlSessionFactoryBean.setConfigLocation(resolver.getResource("classpath:mybatis/mybatis-config.xml"));
     *
     * @return
     * @throws Exception
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean createSqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        /** 设置datasource */
        sqlSessionFactoryBean.setDataSource(dataSource());
        /** 设置typeAlias 包扫描路径即实体类路径 */
        sqlSessionFactoryBean.setTypeAliasesPackage("com.bmw.vehicleservice.entity");
        /** 设置mybatis configuration 扫描路径 */
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:com/bmw/vehicleservice/mappings/*.xml"));
        return sqlSessionFactoryBean;
    }

    @Bean
    public MapperScannerConfigurer getMapperScannerConfigurer() throws Exception {
        MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
        scannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        scannerConfigurer.setBasePackage("com.bmw.vehicleservice.mapper");
        return scannerConfigurer;
    }


}