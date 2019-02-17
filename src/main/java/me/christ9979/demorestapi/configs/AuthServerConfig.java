package me.christ9979.demorestapi.configs;

import me.christ9979.demorestapi.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
/**
 * @EnableAuthorizationServer, AuthorizationServerConfigurerAdapter 상속으로
 * OAuth 인증 서버 설정을 한다.
 */
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AccountService accountService;

    @Autowired
    TokenStore tokenStore;

    /**
     * 인증 서버 시큐리티 설정을 설정한다.
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

        /**
         * Client Secret을 passwordEncoder를 통해 인증한다.
         */
        security.passwordEncoder(passwordEncoder);
    }

    /**
     * 클라이언트 인증 정보를 설정한다.
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /**
         * jdbc로도 설정 가능
         */
        clients.inMemory()
                .withClient("myApp")
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read", "write")
                .secret(this.passwordEncoder.encode("pass"))
                .accessTokenValiditySeconds(10 * 60)
                .refreshTokenValiditySeconds(6 * 10 * 60);
    }

    /**
     * 인증 api 호출 endpoint 설정을 한다.
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        /**
         * 우리의 AuthenticationManager, userDetailsService, tokenStore를 사용하도록 한다.
         */
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(accountService)
                .tokenStore(tokenStore);
    }
}
