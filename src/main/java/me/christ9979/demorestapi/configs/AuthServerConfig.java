package me.christ9979.demorestapi.configs;

import me.christ9979.demorestapi.accounts.AccountService;
import me.christ9979.demorestapi.common.AppProperties;
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
 * OAuth2 인증 서버 설정을 한다.
 * Resource Server와 달리 원래는 따로 프로젝트를 분리하는게 좋다.
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

    @Autowired
    AppProperties appProperties;

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
                .withClient(appProperties.getClientId())
                /**
                 * password scope을 지정한다.
                 * password scope은 username, password를 직접 입력하여 토큰을
                 * 발급 받기 때문에, 사용자 정보를 가지고 있는 구글, 애플, 페이스북 등
                 * 자체 인증 서비스를 제공하는 경우에만 사용해야 한다.
                 */
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read", "write")
                .secret(this.passwordEncoder.encode(appProperties.getClientSecret()))
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
