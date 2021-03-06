package com.example.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.beetl.sql.core.Interceptor;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.UnderlinedNameConversion;
import org.beetl.sql.core.db.MySqlStyle;
import org.beetl.sql.core.loader.MarkdownClasspathLoader;
import org.beetl.sql.ext.spring.BeetlSqlScannerConfigurer;
import org.beetl.sql.ext.spring.SpringConnectionSource;
import org.beetl.sql.ext.spring.SqlManagerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class MyDataSourceConfig {

	@Bean
	public DataSource master(@Autowired DataSourceProperty dataSourceProperty) {
		final HikariDataSource hikariDataSource = new HikariDataSource();
		hikariDataSource.setJdbcUrl(dataSourceProperty.getMaster().getUrl());
		hikariDataSource.setUsername(dataSourceProperty.getMaster().getUsername());
		hikariDataSource.setPassword(dataSourceProperty.getMaster().getPassword());
		return hikariDataSource;
	}

	@Bean
	public List<DataSource> slaves(@Autowired DataSourceProperty dataSourceProperty) {
		List<DataSource> list = new ArrayList<>();
		for (DataSourceProperty.Prop pro : dataSourceProperty.getSlave()) {
			final HikariDataSource hikariDataSource = new HikariDataSource();
			hikariDataSource.setJdbcUrl(pro.getUrl());
			hikariDataSource.setUsername(pro.getUsername());
			hikariDataSource.setPassword(pro.getPassword());
			list.add(hikariDataSource);
		}
		return list;
	}

	@Bean("tx")
	public TransactionManager tx(@Qualifier("master") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean
	public SqlManagerFactoryBean sqlManagerFactoryBean(@Qualifier("master") DataSource dataSource,
			@Qualifier("slaves") List<DataSource> slaves) {
		SqlManagerFactoryBean sqlManagerFactoryBean = new SqlManagerFactoryBean();
		sqlManagerFactoryBean.setDbStyle(new MySqlStyle());
		sqlManagerFactoryBean.setSqlLoader(new MarkdownClasspathLoader("sql"));
		sqlManagerFactoryBean.setNc(new UnderlinedNameConversion());
		sqlManagerFactoryBean.setInterceptors(new Interceptor[]{new PrintSqlInterceptor()});
		SpringConnectionSource springConnectionSource = new SpringConnectionSource();
		springConnectionSource.setMasterSource(dataSource);
		springConnectionSource.setSlaveSource(slaves.toArray(new DataSource[0]));
		sqlManagerFactoryBean.setCs(springConnectionSource);
		return sqlManagerFactoryBean;
	}

	@Bean
	public SQLManager sqlManager(@Autowired SqlManagerFactoryBean sqlManagerFactoryBean) throws Exception {
		return sqlManagerFactoryBean.getObject();
	}

	@Bean
	public static BeetlSqlScannerConfigurer beetlSqlScannerConfigurer() {
		BeetlSqlScannerConfigurer beetlSqlScannerConfigurer = new BeetlSqlScannerConfigurer();
		// ???????????????BeanDefinitionRegistryPostProcessor???????????????????????????????????????
		beetlSqlScannerConfigurer.setDaoSuffix("Dao");
		beetlSqlScannerConfigurer.setBasePackage("com.example.dao");
		beetlSqlScannerConfigurer.setSqlManagerFactoryBeanName("sqlManagerFactoryBean");
		return beetlSqlScannerConfigurer;
	}
}
