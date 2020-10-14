package com.spring.boot.datasource.conf;



import org.springframework.beans.factory.annotation.Qualifier;
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

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "db1EntityManagerFactory", basePackages = {
		"com.sitael.bike.repository.db1" })
public class DataSourceDb1Conf {

	@Primary
	@Bean(name = "db1DataSourceProperties")
	@ConfigurationProperties("db1.datasource")
	public DataSourceProperties firstDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Primary
	@Bean(name = "db1DataSource")
	public HikariDataSource dataSource(@Qualifier("db1DataSourceProperties") DataSourceProperties dataSource) {
		return dataSource.initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}

	@Primary
	@Bean(name = "db1EntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("db1DataSource") HikariDataSource dataSource) {
		return builder.dataSource(dataSource).packages("com.sitael.bike.model.entities.db1")
				.persistenceUnit("db1").build();
	}

	@Primary
	@Bean
	public PlatformTransactionManager transactionManager(
			@Qualifier("db1EntityManagerFactory") LocalContainerEntityManagerFactoryBean db1EntityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(db1EntityManagerFactory.getObject());
		return transactionManager;
	}
}
