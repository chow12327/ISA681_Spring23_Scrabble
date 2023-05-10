package com.isa681.scrabble.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class JwtRequestFilter extends OncePerRequestFilter {
    private final AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver;
    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private AuthenticationEntryPoint authenticationEntryPoint = new BearerTokenAuthenticationEntryPoint();
    private AuthenticationFailureHandler authenticationFailureHandler = new AuthenticationEntryPointFailureHandler((request, response, exception) -> {
        this.authenticationEntryPoint.commence(request, response, exception);
    });
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();
    private BearerTokenResolver bearerTokenResolver ;
    @Autowired
    private final JwtDecoder jwtDecoder;


    public JwtRequestFilter(AuthenticationManager authenticationManager, JwtDecoder jwtDecoder) {
        Assert.notNull(authenticationManager, "authenticationManager cannot be null");
        this.authenticationManagerResolver = (request) -> {
            return authenticationManager;
        };
        this.jwtDecoder =  jwtDecoder;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

            Cookie[] cookies = request.getCookies();
            String token = null;

            try {
                if(cookies!=null) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals("isa681_scrabble")) {
                            token = cookie.getValue();
                        }
                    }
                }
            } catch (OAuth2AuthenticationException var10) {
                //this.logger.trace("Sending to authentication entry point since failed to resolve bearer token", var10);
                //this.authenticationEntryPoint.commence(request, response, var10);
                return;
            }

            if (token == null) {
                this.logger.trace("Did not process request since did not find bearer token");
                filterChain.doFilter(request, response);
            } else {
                BearerTokenAuthenticationToken authenticationRequest = new BearerTokenAuthenticationToken(token);
                authenticationRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));

                try {
                    AuthenticationManager authenticationManager = this.authenticationManagerResolver.resolve(request);
                    Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);
                    SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
                    context.setAuthentication(authenticationResult);
                    this.securityContextHolderStrategy.setContext(context);
                    this.securityContextRepository.saveContext(context, request, response);
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s", authenticationResult));
                    }

                    filterChain.doFilter(request, response);
                } catch (AuthenticationException var9) {
                    this.securityContextHolderStrategy.clearContext();
                    this.logger.trace("Failed to process authentication request", var9);
                    this.authenticationFailureHandler.onAuthenticationFailure(request, response, var9);
                }

            }
    }
}
