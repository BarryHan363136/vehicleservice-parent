package com.bmw.vehicleservice.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * 描述: 数据源实现
 *
 * @outhor hants
 * @create 2018-04-26 上午11:13
 */
@Configuration
public class DataSourceConfiguration implements EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfiguration.class);
    public static final String PARAS_CONFIG_FILENAME = "application.properties";
    protected static final Properties properties = new Properties();
    private Environment environment;

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
    /**
     * mybatis扫描ORM映射文件路径、实体类路径、dao层路径
     * */
    private String mapperLocations;
    private String typeAliasesPackage;
    private String daoBasePackage;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        logger.info("初始化数据源参数配置...");
        try {
        /**
         * 1.从系统环境变量中获取数据源配置参数，如果获取不到就从配置文件中获取
         * */
        this.driverClassName = environment.getProperty("jdbc.driverClassName");
        this.driverUrl = environment.getProperty("jdbc.url");
        this.driverUsername = environment.getProperty("jdbc.username");
        this.driverPassword = environment.getProperty("jdbc.password");
        this.initialSize = StringUtils.isNotBlank(environment.getProperty("jdbc.initialsize")) ? Integer.parseInt(environment.getProperty("jdbc.initialsize")) : 10;
        this.minIdle = StringUtils.isNotBlank(environment.getProperty("jdbc.minidle")) ? Integer.parseInt(environment.getProperty("jdbc.minidle")) : 10;
        this.maxActive = StringUtils.isNotBlank(environment.getProperty("jdbc.maxactive")) ? Integer.parseInt(environment.getProperty("jdbc.maxactive")) : 100;
        this.typeAliasesPackage = StringUtils.isNotBlank(environment.getProperty("mybatis.entityScannerPath")) ? environment.getProperty("mybatis.entityScannerPath") : "com.bmw.vehicleservice.entity";
        this.mapperLocations = StringUtils.isNotBlank(environment.getProperty("mybatis.mappingsScannerPath")) ? environment.getProperty("mybatis.mappingsScannerPath") : "classpath:com/bmw/vehicleservice/mappings/*.xml";
        this.daoBasePackage = StringUtils.isNotBlank(environment.getProperty("mybatis.daoScannerPath")) ? environment.getProperty("mybatis.daoScannerPath") : "com.bmw.vehicleservice.mapper";
        /**
         * 2.从配置文件中获取数据源配置参数
         * */
        if (StringUtils.isBlank(driverClassName) ||
                StringUtils.isBlank(driverUrl) ||
                StringUtils.isBlank(driverUsername) ||
                StringUtils.isBlank(driverPassword) || StringUtils.isBlank(typeAliasesPackage) ||
                StringUtils.isBlank(mapperLocations) || StringUtils.isBlank(daoBasePackage)){
            logger.warn("<=====从配置文件中获取数据源配置参数,系统环境变量未设置数据源参数!!!=====>");
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(PARAS_CONFIG_FILENAME);
            if (in!=null){
                properties.load(in);
            }
            this.driverClassName = properties.getProperty("jdbc.driverClassName");
            this.driverUrl = properties.getProperty("jdbc.url");
            this.driverUsername = properties.getProperty("jdbc.username");
            this.driverPassword = properties.getProperty("jdbc.password");
            }
            logger.info("初始化数据源参数配置完成...");
        } catch (IOException e) {
            logger.error("DataSourceConfiguration参数初始化失败 {} ", e);
        }
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
        sqlSessionFactoryBean.setTypeAliasesPackage(this.typeAliasesPackage);
        /** 设置mybatis configuration 扫描路径 */
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(this.mapperLocations));
        return sqlSessionFactoryBean;
    }

    @Bean
    public MapperScannerConfigurer getMapperScannerConfigurer() throws Exception {
        MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
        scannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        /** 设置dao层扫描路径 */
        scannerConfigurer.setBasePackage(this.daoBasePackage);
        return scannerConfigurer;
    }



}