package com.epam.esm.database;

import com.epam.esm.entity.GiftSertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.hibernate5.SessionHolder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;
import static org.hibernate.cfg.Environment.*;

@Configuration
@PropertySource("classpath:db.properties")
@EnableTransactionManagement
@ComponentScan(value = "com.epam.esm")
public class HibernateConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public LocalSessionFactoryBean getSessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        Properties properties = new Properties();
        properties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
        properties.put("hibernate.hbm2ddl.auto",env.getProperty("spring.jpa.hibernate.ddl-auto"));
        factoryBean.setDataSource(dataSource);
        factoryBean.setHibernateProperties(properties);
        factoryBean.setAnnotatedClasses(Tag.class, GiftSertificate.class,User.class);
        return factoryBean;
    }

    @Bean
    public JpaTransactionManager getTransactionManager(SessionFactory sessionFactory) {
        return new JpaTransactionManager(sessionFactory);
    }

}
