package me.christ9979.demorestapi.configs;

import me.christ9979.demorestapi.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
/**
 * @EnableWebSecurity와 WebSecurityConfigurerAdapter를 상속받는 순간
 * SpringBoot Security의 자동 설정은 적용되지 않고, 개발자가 수동 설정해야 한다.
 * 그러나 Spring Security의 설정은 등록된다. (API 인증 정보 입력 등)
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    /**
     * AuthenticationManager를 Bean으로 등록해주어야 한다.
     * (UserDetails Service를 이용해 인증할수 있도록 해준다.)
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * AuthenticationManager를 만들기 위해 빌더로 세팅한다.
     * 아래의 세팅값으로 AuthenticationManager가 만들어지게 된다.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService)
                .passwordEncoder(passwordEncoder);

    }

    /**
     * WebSecurity는 전역 보안 (리소스 무시, 디버그 모드 설정, 사용자 지정 방화벽 정의 구현을 통한 요청 거부)
     * 에 영향을주는 구성 설정에 사용된다.
     * Spring Security Filter를 사용하지 않는다.
     * Ex) /resources/로 시작하는 모든 요청을 인증 하지 않는다.
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/docs/index.html");

        /**
         * 정적 리소스들에 대해 인증을 거치지 않도록 설정한다.
         */
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * HttpSecurity는 선택 일치에 따라 리소스 수준에서 웹 기반 보안을 구성할 수 있게 한다.
     * Spring Security Filter를 사용한다.
     * Ex) /admin/으로 시작하는 URL을
     * ADMIN 역할이있는 사용자로 제한하고 다른 URL을 성공적으로 인증해야한다고 선언한다.
     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.anonymous()
//                .and()
//            .formLogin()
//                .and()
//            .authorizeRequests()
//                .mvcMatchers(HttpMethod.GET, "/api/**").authenticated()
//                .anyRequest().authenticated();
//    }
}
