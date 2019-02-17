package me.christ9979.demorestapi.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

/**
 * 토큰 기반으로 인증 정보가 있는지 없는지 확인하여 접근 제한을 한다.
 * 여기서는 Event Resource를 가지고 있는 서버와 같이 있는게 맞다.
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /**
     * 리소스에 대한 시큐리티 설정을 한다.
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("event");
    }

    /**
     * Http로 리소스에 접근할 때 설정 한다.
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.anonymous()
                .and()
            .authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/api/**")
                    /**
                     * anonymous()를 설정하면 익명사용자만
                     * 그 api를 사용하도록 허용한다.
                     * 인증한 사용자는 허용하지 않는다.
                     */
//                    .anonymous()
                    .permitAll()
                .anyRequest()
                    .authenticated()
                .and()
            .exceptionHandling()
                /**
                 * 리소스 접근에 실패했을때 403 응답을 내려준다.
                 */
                .accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }
}
