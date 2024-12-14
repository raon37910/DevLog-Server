package com.raon.devlog.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan(basePackages = "com.raon.devlog.mapper")
public class MySqlConfig {

	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);

		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sessionFactory.setMapperLocations(resolver.getResources("classpath:mappers/*.xml"));
		// dto 패키지에 있는 클래스에 별칭 등록하여 이름 사용
		sessionFactory.setTypeAliasesPackage("com.raon.devlog.dto");
		Resource myBatisConfig = resolver.getResource("classpath:mybatis-config.xml");
		sessionFactory.setConfigLocation(myBatisConfig);

		return sessionFactory.getObject();
	}
}
