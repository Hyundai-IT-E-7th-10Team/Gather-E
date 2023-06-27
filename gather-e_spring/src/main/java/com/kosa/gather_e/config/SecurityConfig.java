package com.kosa.gather_e.config;

import com.kosa.gather_e.config.util.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 환경 설정을 구성하기 위한 클래스
 * 웹 서비스가 로드 될떼 Spring Container에 의해 관리
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    //정적 자원에 대한 security 설정
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        //정적 자원에 대한 Security 적용 안함
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    // 비밀번호 암호화에 사용하기 위한 인코더 설정
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic().disable()
                .csrf().disable()//서버에 인증정보를 저장하지 않기에 csrf disable
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//세션기반의 인증을 사용안함
                .and()
                .formLogin().disable()//커스텀한 필터를 사용해 로그인 수행
                .authorizeRequests()
                .antMatchers("api/auth/**").permitAll()
                .anyRequest().authenticated()//auth 요청은 모두에게 허용
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

}
