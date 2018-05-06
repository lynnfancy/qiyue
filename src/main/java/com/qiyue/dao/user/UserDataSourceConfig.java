package com.qiyue.dao.user;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

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
@EnableJpaRepositories( basePackages = "com.qiyue.dao.user",
						entityManagerFactoryRef = "userEntityManagerFactory",
						transactionManagerRef="userTransactionManager")
public class UserDataSourceConfig {
	
	/**
	 * user 数据源
	 * @return
	 */
	@Bean
	@ConfigurationProperties("spring.datasource.user")
	public DataSourceProperties userDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	@ConfigurationProperties("spring.datasource.user")
	public DataSource userDataSource() {
		return userDataSourceProperties().initializeDataSourceBuilder().build();
	}
	
    /**
     * 实体管理对象
     * @param builder  由spring注入这个对象，首先根据type注入（多个就取声明@Primary的对象），否则根据name注入
     * @return
     */
	@Bean
	public LocalContainerEntityManagerFactoryBean userEntityManagerFactory(
			EntityManagerFactoryBuilder builder) {
		return builder
				.dataSource(userDataSource())
				.packages(UserEntity.class)
				.persistenceUnit("user")
				.build();
	}
	
    /**
     * 事务管理对象
     * @return
     */
    @Bean
    public PlatformTransactionManager userTransactionManager(EntityManagerFactory emf){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
}
