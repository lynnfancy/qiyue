package com.qiyue.dao.annouce;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.qiyue.dao.annouce.entity.AnnouceCollectorResult;
import com.qiyue.dao.user.entity.UserEntity;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories( basePackages = "com.qiyue.dao.annouce",
						entityManagerFactoryRef = "annouceEntityManagerFactory",
						transactionManagerRef="annouceTransactionManager")
public class AnnouceDataSourceConfig {
	
	/**
	 * annouce 数据源
	 * @Primary 默认选项
	 * @return
	 */
	@Bean
	@Primary
	@ConfigurationProperties("spring.datasource.annouce")
	public DataSourceProperties annouceDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	@Primary
	@ConfigurationProperties("spring.datasource.annouce")
	public DataSource annouceDataSource() {
		return annouceDataSourceProperties().initializeDataSourceBuilder().build();
	}
	
    /**
     * 实体管理对象
     * @param builder  由spring注入这个对象，首先根据type注入（多个就取声明@Primary的对象），否则根据name注入
     * @return
     */
	@Bean
	@Primary
	public LocalContainerEntityManagerFactoryBean annouceEntityManagerFactory(
			EntityManagerFactoryBuilder builder) {
		return builder
				.dataSource(annouceDataSource())
				.packages(AnnouceCollectorResult.class)
				.persistenceUnit("annouce")
				.build();
	}
	
    /**
     * 事务管理对象
     * @return
     */
    @Bean
    @Primary
    public PlatformTransactionManager annouceTransactionManager(EntityManagerFactory emf){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
}