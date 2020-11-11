package com.epam.esm.database;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.generator.CertificateGenerator;
import com.epam.esm.generator.TagGenerator;
import com.epam.esm.generator.UserGenerator;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Configuration class for datasource beans: developer and test
 */
@Configuration
@ComponentScan("com.epam.esm")
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
    CommandLineRunner initDatabase(UserRepository repository, CertificateRepository certificateRepository, TagRepository tagRepository) {
        List<Tag> tags = TagGenerator.getTags(1000);
        List<GiftCertificate> certificates = CertificateGenerator.getTags(1000);
        List<User> users = UserGenerator.getTags(1000);

        return args -> {
            repository.saveAll(users);
            tagRepository.saveAll(tags);
            for(int i =1;i<1001;i++) {
                GiftCertificate giftCertificate = certificates.get(i-1);
                User savedUSer = users.get(i-1);
                savedUSer.setId(i);
                giftCertificate.setUser(savedUSer);
                int startIndex = ThreadLocalRandom.current().nextInt(1, 1000);
                Set<Tag> tagsSet = new HashSet<>();
                Tag tag1 = tags.get(startIndex-1);
                Tag tag2 = tags.get(startIndex);
                tag1.setId(startIndex);
                tag2.setId(startIndex+1);
                tagsSet.add(tag1);
                tagsSet.add(tag2);
                giftCertificate.setTags(tagsSet);
                certificateRepository.save(giftCertificate);
            }
        };
    }
}