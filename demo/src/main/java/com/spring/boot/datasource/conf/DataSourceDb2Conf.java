package com.spring.boot.datasource.conf;



import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "db2EntityManagerFactory", basePackages = {
		"com.sitael.bike.repository.db2" })
public class DataSourceDb2Conf {
	
	
	@Bean(name = "db2DataSourceProperties")
	@ConfigurationProperties("db2.datasource")
	public DataSourceProperties firstDataSourceProperties() {
	    return new DataSourceProperties();
	}

	@Bean(name = "db2DataSource")
	public HikariDataSource dataSource(@Qualifier("db2DataSourceProperties") DataSourceProperties dataSource) {
		return dataSource.initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}

	@Bean(name = "db2EntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("db2DataSource") HikariDataSource dataSource) {
		return builder.dataSource(dataSource).packages("com.sitael.bike.model.entities.db2")
				.persistenceUnit("db2").build();
	}

	
	   @Bean
	   public PlatformTransactionManager db2TransactionManager(@Qualifier("db2EntityManagerFactory") LocalContainerEntityManagerFactoryBean db2EntityManagerFactory){
	      JpaTransactionManager db2TransactionManager
	        = new JpaTransactionManager();
	      db2TransactionManager.setEntityManagerFactory(db2EntityManagerFactory.getObject());
	      return db2TransactionManager;
	   }
}
