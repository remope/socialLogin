package com.socialLogin.socialLogin.dto;

import com.socialLogin.socialLogin.filter.JwtAuthenticationFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;

@Configurable   // 어노테이션 Bean을 사용할거다
@Configuration  // WebSecurityConfig가 Bean method를 가지고 있다
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.
                cors(cors -> cors
                        .configurationSource(corsConfigurationSource())

                )
                .csrf(CsrfConfigurer::disable)  //JWT와 같은 토큰 기반 인증 시스템에서는 CSRF보호가 필요하지 않다고 함
                .httpBasic(HttpBasicConfigurer::disable)    // JWT 기반 인증을 사용할 경우 일반적으로 비활성화
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )   // 세션 상태를 저장하지 않도록 함 -> JWT 기반 인증은 상태를 서버에 저장하지 않기 때문
                .authorizeHttpRequests(request -> request   // Http 요청에 대한 권한을 설정
                        .requestMatchers("/", "/v1/auth/**")   //어떤 패턴에 대해 작업을 할거냐?  .requestMatchers("/", "/api/v1/auth/**")
                        .permitAll()        // .requestMatchers("/api/v1/user/**").hasRole("USER")  -> 해당 url은 USER 권한을 가진 사람만
                                            // .requestMatchers("/qpi/v1/admin/**").hasRole("ADMIN") -> 해당 url은 admin 권한을 가진 사람만
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new FailedAuthenticationEntryPoint()))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOrigin("*"); //모든 출처에 대해 허용
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

}

class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        // {"code": "NP", "message": "No Permission"}
        response.getWriter().write("{\"code\": \"NP\", \"message\": \"No Permission\"}");
    }
}