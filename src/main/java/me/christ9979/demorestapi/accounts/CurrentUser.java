package me.christ9979.demorestapi.accounts;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
/**
 * 원래는 AccountAdapter가 Autowired가 되는데,
 * Spring Expression Language로 AccountAdapter의 Account 필드를 꺼내서
 * 바로 사용할 수 있도록 한다.
 *
 * 하지만 익명 유저가 api를 호출한다면, Spring Security의 현재 유저의 Authentication의 principal은
 * 'anonymousUser'라는 문자열이다.
 * 익명이 아니면 principal은 객체이므로 account를 추출한다.
 */
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")
public @interface CurrentUser {
}
