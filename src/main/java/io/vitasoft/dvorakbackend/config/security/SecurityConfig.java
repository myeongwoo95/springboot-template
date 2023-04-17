package io.vitasoft.dvorakbackend.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vitasoft.dvorakbackend.config.oauth.CustomDefaultOAuth2UserService;
import io.vitasoft.dvorakbackend.config.oauth.handler.OAuth2LoginFailureHandler;
import io.vitasoft.dvorakbackend.config.oauth.handler.OAuth2LoginSuccessHandler;
import io.vitasoft.dvorakbackend.config.security.handler.LoginFailureHandler;
import io.vitasoft.dvorakbackend.config.security.handler.LoginSuccessHandler;
import io.vitasoft.dvorakbackend.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomDefaultOAuth2UserService customDefaultOAuth2UserService;
    private final MemberRepository memberRepository;

    @Bean public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean public LoginSuccessHandler loginSuccessHandler() { return new LoginSuccessHandler(jwtTokenProvider, memberRepository); }
    @Bean public LoginFailureHandler loginFailureHandler() { return new LoginFailureHandler(); }

    /**
     * CustomJsonUsernamePasswordAuthenticationFilter 빈 등록
     * 커스텀 필터를 사용하기 위해 만든 커스텀 필터를 Bean으로 등록
     * setAuthenticationManager(authenticationManager())로 위에서 등록한 AuthenticationManager(ProviderManager) 설정
     * 로그인 성공 시 호출할 handler, 실패 시 호출할 handler로 위에서 등록한 handler 설정
     */
    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
                = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customJsonUsernamePasswordLoginFilter;
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService, userRepository);
        return jwtAuthenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable() // rest api 이므로 basic auth 인증을 사용하지 않는다는 설정입니다.
            .csrf().disable()     // .csrf().disable() : rest api 이므로 csrf 보안을 사용하지 않습니다.
            .cors()
        .and()
            .formLogin().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT를 사용하기 때문에 세션을 사용하지 않는다는 설정입니다.

        .and()
            .authorizeRequests()
            .antMatchers("/swagger-ui/**", "/api/auth/**", "/hello").permitAll()
            .anyRequest().hasRole("USER")
        .and()
            .oauth2Login()
            .successHandler(oAuth2LoginSuccessHandler) // 동의하고 계속하기를 눌렀을 때 Handler 설정
            .failureHandler(oAuth2LoginFailureHandler) // 소셜 로그인 실패 시 핸들러 설정
            .userInfoEndpoint().userService(customDefaultOAuth2UserService) // customUserService 설정
        .and()
           // Jwt 인증을 위하여 직접 구현한 JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 실행하겠다는 설정입니다.
          .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, objectMapper), UsernamePasswordAuthenticationFilter.class);

    }
}
