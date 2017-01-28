/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.configuration.app;

import org.hibernate.SessionFactory;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.MailSender;
import pl.arek.motoappserver.domain.repositories.PersonalSettingsRepository;
import pl.arek.motoappserver.domain.repositories.UserProfileRepository;
import pl.arek.motoappserver.domain.repositories.UserRepository;
import pl.arek.motoappserver.domain.repositories.impl.PersonalSettingsRepositoryImpl;
import pl.arek.motoappserver.domain.repositories.impl.UserProfileRepositoryImpl;
import pl.arek.motoappserver.domain.repositories.impl.UserRepositoryImpl;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@Configuration
@Profile("test")
public class TestConfiguration {

    @Bean
    @Profile("test")
    UserRepository userRepository() {
        return Mockito.mock(UserRepositoryImpl.class);

    }

    @Bean
    @Profile("test")
    UserProfileRepository userProfileRepository() {

        return Mockito.mock(UserProfileRepositoryImpl.class);
    }

    @Bean
    @Profile("test")
    PersonalSettingsRepository personalSettingsRepository() {

        return Mockito.mock(PersonalSettingsRepositoryImpl.class);
    }

    @Bean
    @Scope("prototype")
    @Profile("test")
    public MailSender mailSender() {
        return Mockito.mock(MailSender.class);
    }

    @Bean
    public SessionFactory getSessionFactory() {
        return Mockito.mock(SessionFactory.class);

    }
}
