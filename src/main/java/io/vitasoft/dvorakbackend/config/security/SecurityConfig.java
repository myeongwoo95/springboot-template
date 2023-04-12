package io.vitasoft.dvorakbackend.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() //rest api 이므로 basic auth 인증을 사용하지 않는다는 설정입니다.
                .csrf().disable()     // .csrf().disable() : rest api 이므로 csrf 보안을 사용하지 않습니다.
                .cors().and()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT를 사용하기 때문에 세션을 사용하지 않는다는 설정입니다.
                .and()
                .authorizeRequests()
                .antMatchers("/swagger-ui/**", "/api/auth/**", "/hello").permitAll()
                .antMatchers("/world").hasRole("USER")
                .anyRequest().hasRole("USER")
                .and()
                // Jwt 인증을 위하여 직접 구현한 JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 실행하겠다는 설정입니다.
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, objectMapper), UsernamePasswordAuthenticationFilter.class);

    }

    // 보안 검사를 무시해야 하는 요청을 설정하는 메서드
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/v3/api-docs", "/swagger-resources/**",
                "/swagger-ui/index.html", "/webjars/**", "/swagger/**"
        );
    }
}
