/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.configuration.app;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.context.WebApplicationContext;
import pl.arek.motoappserver.configuration.database.HibernateConfiguration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import pl.arek.motoappserver.configuration.security.SecurityConfiguration;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
public class DispatcherInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * @return
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{
            HibernateConfiguration.class,
            OtherComponentsConfiguration.class,
            SecurityConfiguration.class,
            AdvicesConfiguration.class,
            RepositoriesConfiguration.class,
            ServicesConfiguration.class
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{RestConfiguration.class
        };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    /**
     * we nedd to get created contex and register our profiles;
     *
     * @return
     */
    @Override
    protected WebApplicationContext createRootApplicationContext() {

        WebApplicationContext context = super.createRootApplicationContext();
        ((ConfigurableEnvironment) context.getEnvironment()).setActiveProfiles("main", "production_only", "security");

        return context;
    }

}
