//package com.denyskozii.meetingcounter.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//@Configuration
//@Order(1)
//public class GoogleSecurityConfig extends WebSecurityConfigurerAdapter {
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http
//                .antMatcher("/**").authorizeRequests()
//                .antMatchers("/", "/not-restricted").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .oauth2Login();
//    }
//}