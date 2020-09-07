package org.example.meetingcounter.config;


import org.example.meetingcounter.jwt.JwtFilter;
import org.example.meetingcounter.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtFilter jwtFilter;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(8);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .antMatcher("/**").authorizeRequests()
//                .antMatchers(new String[]{"/", "/not-restricted"}).permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .oauth2Login();
      http
                    .cors()
                .and()
                    .httpBasic().disable()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                    .antMatchers(/*"/", "/not-restricted",*/"/registration",/*"/static/**","validation.js", "/validation.js",*/ "/login", "/form-login","/user/hello").permitAll()
//                    .antMatchers("/**")/*.authorizeRequests()*/
              .anyRequest().authenticated()
                .and()
                    .logout()
                    .permitAll()
                .and()
                    .exceptionHandling()
                    .accessDeniedHandler((request, response, accessDeniedException) -> response.sendRedirect("/access-denied"))
//                .and()
//                    .oauth2Login()
              .and()
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
