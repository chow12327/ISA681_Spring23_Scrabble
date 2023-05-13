package com.isa681.scrabble.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class ScrabbleSecurityConfig{
    //@Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery("select username, password, enabled from player where username=?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select username, role from player where username=?");
        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        XorCsrfTokenRequestAttributeHandler delegate = new XorCsrfTokenRequestAttributeHandler();

        delegate.setCsrfRequestAttributeName("_csrf");
        CsrfTokenRequestHandler requestHandler = delegate::handle;

        httpSecurity
                .securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/gameDetails").hasAuthority("SCOPE_ROLE_USER")
                        .requestMatchers(HttpMethod.GET, "/api/historicgames").hasAuthority("SCOPE_ROLE_USER")
                        .requestMatchers(HttpMethod.GET, "/api/activegames").hasAuthority("SCOPE_ROLE_USER")
                        .requestMatchers(HttpMethod.GET, "/api/players").hasAuthority("SCOPE_ROLE_USER")
                        .requestMatchers(HttpMethod.POST,"/api/timeout").hasAuthority("SCOPE_ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/creategame").hasAuthority("SCOPE_ROLE_USER")
                        .requestMatchers(HttpMethod.POST, "/api/joinGame").hasAuthority("SCOPE_ROLE_USER")
                        .requestMatchers(HttpMethod.POST, "/api/submitMove").hasAuthority("SCOPE_ROLE_USER")
                        .requestMatchers(HttpMethod.OPTIONS, "/api/gameDetails").hasAuthority("SCOPE_ROLE_USER")
                        .requestMatchers(HttpMethod.OPTIONS, "/api/historicgames").hasAuthority("SCOPE_ROLE_USER")
                        .requestMatchers(HttpMethod.OPTIONS, "/api/activegames").hasAuthority("SCOPE_ROLE_USER")
                        .requestMatchers(HttpMethod.OPTIONS, "/api/players").hasAuthority("SCOPE_ROLE_USER")
                        .requestMatchers(HttpMethod.OPTIONS,"/api/timeout").hasAuthority("SCOPE_ROLE_ADMIN")
                        .requestMatchers(HttpMethod.OPTIONS, "/api/creategame").hasAuthority("SCOPE_ROLE_USER")
                        .requestMatchers(HttpMethod.OPTIONS, "/api/joinGame").hasAuthority("SCOPE_ROLE_USER")
                        .requestMatchers(HttpMethod.OPTIONS, "/api/submitMove").hasAuthority("SCOPE_ROLE_USER")
                        .anyRequest()
                        .authenticated())
                .csrf((csrf) -> csrf
                        .csrfTokenRepository(tokenRepository)
                        .csrfTokenRequestHandler(requestHandler)
                )
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .httpBasic(
                        Customizer.withDefaults())
                .headers(header -> {
                    header.frameOptions().sameOrigin();
                    header.httpStrictTransportSecurity().requestMatcher(AnyRequestMatcher.INSTANCE).includeSubDomains(true).maxAgeInSeconds(31536000);
                });

        return httpSecurity.build();
    }

    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
