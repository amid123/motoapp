/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.configuration.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import pl.arek.motoappserver.advices.AdviceAccountCleaner;
import pl.arek.motoappserver.advices.AdviceRepositories;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@Configuration
@EnableAspectJAutoProxy
public class AdvicesConfiguration {

    @Bean
    AdviceRepositories adviceRepositories() {
        return new AdviceRepositories();

    }

    @Bean
    AdviceAccountCleaner adviceAccountCleaner() {
        return new AdviceAccountCleaner();
    }

}
