package com.eazybytes.eazystore.security;


import com.eazybytes.eazystore.filter.JWTTokenValidatorFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class EazyStoreSecurityConfig {
   private final PublicPathConfig publicPathConfig;

   @Value("${eazystore.cors.allowed.origins}")
   private String allowedOrigins;

    @Bean

    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
       CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
       tokenRepository.setCookiePath("/"); // Ensure cookie is sent for all backend paths
       return  http.cors(corsConfig -> corsConfig.configurationSource(corsConfigurationSource()))
               .csrf(csrfConfig->csrfConfig.csrfTokenRepository(tokenRepository)
                       .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
               .authorizeHttpRequests((requests) ->{
                   publicPathConfig.publicPaths().forEach(path ->
                   requests.requestMatchers(path).permitAll());
                  requests.requestMatchers("/eazystore/actuator/**").hasRole("OPS_ENG");
                  requests.requestMatchers("/swagger-ui.html","/swagger-ui/**","/v3/api-docs/**").hasAnyRole("DEV_ENG","QA_ENG");
                   requests.requestMatchers("/api/v1/admin/**").hasRole("ADMIN");
                   requests.requestMatchers("/api/v1/stickers/**").permitAll();

           requests.anyRequest().hasAnyRole("USER","ADMIN");
       })
               .addFilterBefore(new JWTTokenValidatorFilter(publicPathConfig.publicPaths()), BasicAuthenticationFilter.class)
               .formLogin(withDefaults())
               .httpBasic(withDefaults()).build();

    }


//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//        var user1=User.builder().username("sameer").password("$2a$12$QQFV.yyAKDICaMXlpPJiy.cIHgDa7RfU2Q/QZslBLq1JPba5QFJk2").roles("USER").build();
//        var user2=User.builder().username("admin").password("$2a$12$UedZH2ZGYptv5jH5W8YydeQQLTpSlyoG176COKy6aFSzTl31T/o42").roles("ADMIN","USER").build();
//
//        return new InMemoryUserDetailsManager(user1,user2);
//    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker(){
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }




    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider){




        var providerManager=new ProviderManager(authenticationProvider);
        return providerManager;
    }



    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Allow specific origins (update with your frontend URL)
        config.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));

        // Allow all HTTP methods
        config.setAllowedMethods(Collections.singletonList("*"));

        // Allow all headers
        config.setAllowedHeaders(Collections.singletonList("*"));

        // Allow credentials (cookies, authorization headers, etc.)
        config.setAllowCredentials(true);

        // Set max age of the CORS pre-flight request
         config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
