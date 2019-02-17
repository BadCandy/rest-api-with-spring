package me.christ9979.demorestapi.configs;

import me.christ9979.demorestapi.accounts.Account;
import me.christ9979.demorestapi.accounts.AccountRole;
import me.christ9979.demorestapi.accounts.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /**
     * 어떤 방식으로 패스워드를 인코딩 할지 명시되어 있는 패스워드 인코더를 등록한다.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    @Bean
//    public ApplicationRunner applicationRunner() {
//        return new ApplicationRunner() {
//
//            @Autowired
//            AccountService accountService;
//
//            @Override
//            public void run(ApplicationArguments args) throws Exception {
//                Account account = Account.builder()
//                        .email("christ9979@email.com")
//                        .password("christ9979")
//                        .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
//                        .build();
//                accountService.saveAccount(account);
//            }
//        };
//    }
}
