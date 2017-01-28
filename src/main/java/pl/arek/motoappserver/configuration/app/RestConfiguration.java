/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.configuration.app;

import pl.arek.motoappserver.rest.controllers.AccountController;
import pl.arek.motoappserver.rest.controllers.ProfileController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import pl.arek.motoappserver.rest.controllers.MembersListController;
import pl.arek.motoappserver.rest.controllers.MyPosiotionController;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@Configuration
@EnableWebMvc
@Profile("main")
public class RestConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    @Scope("request")
    AccountController accountController() {
        return new AccountController();
    }

    @Bean
    @Scope("request")
    ProfileController profileController() {
        return new ProfileController();
    }

    @Bean
    @Scope("request")
    MyPosiotionController myPosiotionController() {
        return new MyPosiotionController();
    }

    @Bean
    @Scope("session")
    MembersListController memberListController() {
        return new MembersListController();
    }

    @Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
        internalResourceViewResolver.setPrefix("/WEB-INF/views/");
        internalResourceViewResolver.setSuffix(".jsp");
        return internalResourceViewResolver;
    }
}
