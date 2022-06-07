package com.share.image.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalDetailsService principalDetailsService;
    private final AuthenticationFailureHandler loginFailureHandler;


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/image/**", "/templates/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/admin/**")
                    .access("hasRole('ROLE_ADMIN')")
                .antMatchers("/user/**")
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/**", "/auth/**").permitAll()
                .and()
                .formLogin()
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .failureHandler(loginFailureHandler)
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/user")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true);
    }


}

