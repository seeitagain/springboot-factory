package org.example.lc.config;

import org.ldaptive.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LdapConfig {
    @Value("${ldap.url}")
    private String ldapUrl;

    @Value("${ldap.base-dn}")
    private String baseDn;

    @Value("${ldap.user-dn}")
    private String userDn;

    @Value("${ldap.password}")
    private String password;

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionConfig connConfig = new ConnectionConfig(ldapUrl);
        connConfig.setConnectionInitializer(
                new BindConnectionInitializer(
                        userDn, new Credential(password)));
        return new DefaultConnectionFactory(connConfig);
    }
}
