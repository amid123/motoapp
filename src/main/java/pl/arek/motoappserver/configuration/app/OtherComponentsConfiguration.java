/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.configuration.app;

import com.google.gson.Gson;
import pl.arek.motoappserver.services.account.impl.AccountActivationMessageCreatorImpl;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pl.arek.motoappserver.configuration.security.LogedInChecker;
import pl.arek.motoappserver.services.systemresponse.SystemResponseFactory;
import pl.arek.motoappserver.services.systemresponse.impl.SystemResponseFactoryImpl;
import pl.arek.motoappserver.services.account.AccountActivationMessageCreator;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@Configuration
@EnableScheduling
@EnableTransactionManagement
@PropertySource({
    "classpath:application.properties",
    "classpath:system_response.properties"
})
public class OtherComponentsConfiguration {

    private static final Logger logger = Logger.getLogger(OtherComponentsConfiguration.class.getName());

    Properties emailProperties;

    public OtherComponentsConfiguration() {
        try {
            this.emailProperties = PropertiesLoaderUtils.loadAllProperties("email.properties");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, "Error while loading email properties file!");
        }
    }

    /**
     *
     * task scheduling here
     *
     * @return
     */
    @Bean
    @Profile("main")
    TaskScheduler taskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    @Bean
    @Profile("main")
    AccountActivationMessageCreator activationMessage() {
        return new AccountActivationMessageCreatorImpl();
    }

    @Bean
    @Profile("main")
    SystemResponseFactory systemResponseFactory() {
        return new SystemResponseFactoryImpl();
    }

    @Bean
    @Scope("prototype")
    @Profile("main")
    protected PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Bean
    @Profile("main")
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    @Scope("prototype")
    @Profile("main")
    public Gson gson() {
        return new Gson();
    }

    @Bean
    @Scope("prototype")
    @Profile("production_only")
    public MailSender mailSender() {
        JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
        mailSenderImpl.setJavaMailProperties(this.emailProperties);
        mailSenderImpl.setUsername(this.emailProperties.getProperty("mail.login"));
        mailSenderImpl.setPassword(this.emailProperties.getProperty("mail.password"));
        mailSenderImpl.setHost(this.emailProperties.getProperty("mail.host"));
        mailSenderImpl.setPort(Integer.parseInt(this.emailProperties.getProperty("mail.port")));
        return mailSenderImpl;
    }

    @Bean
    @Scope("prototype")
    @Profile("main")
    LogedInChecker logedInChecker() {
        return new LogedInChecker();
    }
}
