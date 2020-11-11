package com.epam.esm.database;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * Configuration class for datasource beans: developer and test
 */
@Configuration
public class DatabaseDataSource {
    /**
     * Bean,defined to get access to developers database
     * @return @{DataSource} for developer base
     */

    @Primary
    @Bean
    @Profile("dev")
    public DataSource getSource() {
        DriverManagerDataSource builder = new DriverManagerDataSource();
        builder.setDriverClassName("com.mysql.cj.jdbc.Driver");
        builder.setUrl("jdbc:mysql://localhost:3306/module3_base");
        builder.setUsername("root");
        builder.setPassword("root");
        return builder;
    }

    @Bean
    @Profile("test")
    public DataSource getTestSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .addScript("init-h2.sql")
                .build();
    }}