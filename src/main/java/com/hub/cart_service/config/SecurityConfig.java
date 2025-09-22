package com.hub.cart_service.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    private static final String ACCESS_REALM_CLAIM = "realm_access";
    private static final String ROLE_CLAIM = "roles";

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .authorizeHttpRequests(
                author ->
                        author.anyRequest().authenticated()
            ).oauth2ResourceServer(
                    oauth2 ->
                            oauth2.jwt(Customizer.withDefaults())
            ).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = jwt -> {
            Map<String, Collection<String>> realmAccess = jwt.getClaim(ACCESS_REALM_CLAIM);
            return realmAccess.get(ROLE_CLAIM)
                    .stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        };

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        // convert in interface org.springframework.core.convert.converter.Converter<Jwt,AbstractAuthenticationToken>
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}
