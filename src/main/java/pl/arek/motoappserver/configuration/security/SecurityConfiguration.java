/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.test.context.ActiveProfiles;

/**
 * We will register here all dependencies with are associated with security
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@Profile("security")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * custom authentication succes handler
     *
     * @return
     */
    @Bean
    public AuthSuccessHandler authSuccessHandler() {
        return new AuthSuccessHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new MyAuthenticationEntryPoint();
    }

    /**
     * custom authentication failure handler
     *
     * @return
     */
    @Bean
    public AuthFailureHandler authFailureHandler() {
        return new AuthFailureHandler();
    }

    /**
     * custom UserDetailService
     *
     * @return
     */
    @Bean
    UserDetailsService appUserDetailsService() {
        return new SecurityUserDetailsService();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(appUserDetailsService());
        authenticationProvider.setPasswordEncoder(new ShaPasswordEncoder());

        return authenticationProvider;
    }

    @Bean
    public LogoutSuccessHandler successLogoutHandler() {
        return new MySuccessLogoutHandler();
    }

    @Bean
    public AccessDeniedHandler accesssDeniedHandler() {
        return new MyAccesssDeniedHandler();
    }

    /**
     * Fabrykujemy własny filtr autentykacji
     *
     * @return
     * @throws Exception
     */
    @Bean
    public UsernamePasswordAuthenticationFilter authenticationFilter() throws Exception {
        CustomUsernamePasswordAuthenticationFilter authenticationFilter
                = new CustomUsernamePasswordAuthenticationFilter();

        authenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/account/login", "POST"));
        authenticationFilter.setAuthenticationManager(this.authenticationManagerBean());

        authenticationFilter.setAuthenticationSuccessHandler(authSuccessHandler());
        authenticationFilter.setAuthenticationFailureHandler(authFailureHandler());

        // authenticationFilter.setPostOnly(false);
        authenticationFilter.setUsernameParameter("username");
        authenticationFilter.setPasswordParameter("password");

        return authenticationFilter;
    }

    /**
     * Fabrykowanie enkodera hasel BCryptPasswordEncoder
     *
     * @return
     */
    @Bean
    protected PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    /**
     * Zwracamy managera autentykacji
     *
     * @return
     * @throws Exception
     */
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
        auth.userDetailsService(appUserDetailsService()).passwordEncoder(passwordEncoder());
    }

    /**
     * konfiguracja mudulu security
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().accessDeniedHandler(this.accesssDeniedHandler());

        // dodajemy własny filtr autentykacji
        http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class).exceptionHandling().accessDeniedHandler(accesssDeniedHandler());

        // konfiguracja logoutu dodajemy wlasny successfull logout handler aby wysylac status po logoucie
        http.csrf().disable().logout().logoutRequestMatcher(
                new AntPathRequestMatcher("/account/logout", "GET")).logoutSuccessHandler(successLogoutHandler());

        http.authorizeRequests()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/main/**", "/main*").permitAll()
                .antMatchers(HttpMethod.POST, "/account").permitAll()
                .antMatchers(HttpMethod.GET, "/account/activation/**").permitAll()
                .antMatchers("/test").permitAll().anyRequest().authenticated();

    }

}
