package org.example.lc.config;

import org.springframework.cglib.proxy.NoOp;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth    //使用内存中的InMemoryUserDetailsManager
                .inMemoryAuthentication()
                //不适用密码编码器
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                //配置admin用户
                .withUser("admin").password("admin").roles("ADMIN")
                //配置normal用户
                .and().withUser("normal").password("normal").roles("NORMAL");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/test/echo").permitAll()
                .antMatchers("/test/admin").hasRole("ADMIN")
                .antMatchers("/test/normal").access("hasRole('ROLE_NORMAL')")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .permitAll();
    }
}
