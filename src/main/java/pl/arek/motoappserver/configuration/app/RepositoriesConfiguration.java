/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.configuration.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource("classpath:props/database.properties")
public class RepositoriesConfiguration {

    /**
     * Repositories here
     */
    @Bean
    @Profile("production_only")
    UserRepository userRepository() {
        return new UserRepositoryImpl();
    }

    @Bean
    @Profile("production_only")
    UserProfileRepository userProfileRepository() {
        return new UserProfileRepositoryImpl();
    }

    @Bean
    @Profile("production_only")
    PersonalSettingsRepository personalSettingsRepository() {
        return new PersonalSettingsRepositoryImpl();
    }

}
