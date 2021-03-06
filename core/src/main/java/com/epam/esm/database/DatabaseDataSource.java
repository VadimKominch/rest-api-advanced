package com.epam.esm.database;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.generator.CertificateGenerator;
import com.epam.esm.generator.OrderGenerator;
import com.epam.esm.generator.TagGenerator;
import com.epam.esm.generator.UserGenerator;
import com.epam.esm.service.GiftService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.UserService;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Configuration class for datasource beans: developer and test
 */
@Configuration
@ComponentScan(basePackages = "com.epam.esm")
public class DatabaseDataSource {
    /**
     * Bean,defined to get access to developers database.Used c3p0 connection pool.
     * @return @{DataSource} for developer base
     */

    @Primary
    @Bean
    @Profile("dev")
    public DataSource getSource() throws PropertyVetoException {
        ComboPooledDataSource builder = new ComboPooledDataSource();
        builder.setDriverClass("com.mysql.cj.jdbc.Driver");
        builder.setJdbcUrl("jdbc:mysql://localhost:3306/module3_base");
        builder.setUser("root");
        builder.setPassword("root");
        return builder;
    }

    @Bean
    @Profile("test")
    public DataSource getTestSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .addScript("initBase.sql")
                .build();
    }

    @Bean
    @Transactional
    CommandLineRunner initDatabase(GiftService certificateRepository, TagService tagRepository, UserService repository, OrderService orderService) {
        List<Tag> tags = TagGenerator.getTags(1000);
        List<GiftCertificate> certificates = CertificateGenerator.getTags(1000);
        List<User> users = UserGenerator.getUsers(1000);
        List<Order> orders = OrderGenerator.getOrders(100);
        return args -> {
            repository.saveAll(users);
            tagRepository.saveAll(tags);
            for(int i =1;i<1001;i++) {
                GiftCertificate giftCertificate = certificates.get(i-1);
                User savedUSer = users.get(i-1);
                savedUSer.setId(i);
                int startIndex = ThreadLocalRandom.current().nextInt(1, 1000);
                List<Tag> tagsSet = new ArrayList<>();
                Tag tag1 = tags.get(startIndex-1);
                Tag tag2 = tags.get(startIndex);
                tag1.setId(startIndex);
                tag2.setId(startIndex+1);
                tagsSet.add(tag1);
                tagsSet.add(tag2);
                giftCertificate.setTags(tagsSet);
            }
            certificateRepository.saveAll(certificates);
            for (int i = 1; i < 100; i++) {
                Order order = orders.get(i-1);
                User user = users.get(i-1);
                GiftCertificate giftCertificate = certificates.get(i-1);
                GiftCertificate giftCertificate2 = certificates.get(2*i);
                List<GiftCertificate> certificateList = new ArrayList<>();
                for(int j = 0; j< i;j++)
                    certificateList.add(giftCertificate);
                certificateList.add(giftCertificate2);
                order.setCertificate(certificateList);
                order.getCertificate().forEach(el->el.getOrderList().add(order));
                order.setUser(user);
                user.getOrders().add(order);
                orderService.save(order);
            }

        };
    }
}