package com.epam.esm.database;

import com.epam.esm.dao.GiftDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.generator.TagGenerator;
import com.epam.esm.service.GiftService;
import com.epam.esm.service.TagService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.List;

/**
 * Configuration class for datasource beans: developer and test
 */
@Configuration
@ComponentScan(basePackages = "com.epam.esm")
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
    }

    @Bean
    CommandLineRunner initDatabase(GiftService certificateRepository, TagService tagRepository) {
        List<Tag> tags = TagGenerator.getTags(1000);
        return args -> tagRepository.saveAll(tags);
    }
}