/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.configuration.database;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * Hibernate configuration here - nothing special
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@Configuration
@PropertySource("classpath:database.properties")
public class HibernateConfiguration {

    @Value("${jdbc.driverClassName}")
    private String driverClassName;
    @Value("${jdbc.url}")
    private String jdbcURL;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    @Bean
    @Profile("production_only")
    public SessionFactory getSessionFactory() {
        LocalSessionFactoryBuilder builder
                = new LocalSessionFactoryBuilder(dataSource());
        builder.scanPackages("pl.arek.motoappserver.domain.entities")
                .addProperties(getHibernateProperties());
        return builder.buildSessionFactory();
    }

    private Properties getHibernateProperties() {
        Properties prop = null;
        try {
            prop = PropertiesLoaderUtils.loadAllProperties("database.properties");
            return prop;
        } catch (IOException ex) {
            Logger.getLogger(HibernateConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
        return prop;
    }

    @Bean
    @Profile("production_only")
    public HibernateTransactionManager txManager() {
        HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
        hibernateTransactionManager.setSessionFactory(this.getSessionFactory());
        return hibernateTransactionManager;
    }

    @Bean
    @Profile("production_only")
    public TransactionInterceptor transactionInterceptor() {
        TransactionInterceptor interceptor = new TransactionInterceptor();
        
        interceptor.setTransactionManager(txManager());

        
        return interceptor;
    }

    @Bean(name = "dataSource")
    @Profile("production_only")
    public BasicDataSource dataSource() {

        BasicDataSource dataSources = new BasicDataSource();
        dataSources.setDriverClassName(this.driverClassName);
        dataSources.setUrl(this.jdbcURL);
        dataSources.setUsername(this.username);
        dataSources.setPassword(this.password);

        return dataSources;
    }
}
