package com.socialLogin.socialLogin.filter;

import com.socialLogin.socialLogin.entity.UserEntity;
import com.socialLogin.socialLogin.provider.JwtProvider;
import com.socialLogin.socialLogin.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String token = parseBearerToken(request);
            if(token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            String userId = jwtProvider.validate(token);
            if(userId == null) {
                filterChain.doFilter(request, response);
                return;
            }

            UserEntity userEntity = userRepository.findByUserId(userId);
            String role = userEntity.getRole(); //  role = ROLE_USER, ROLE_ADMIN

            List<GrantedAuthority> authorities = new ArrayList<>(); // GrantedAuthority -> 사용자가 가진 권한을 나타내는 인터페이스
            authorities.add(new SimpleGrantedAuthority(role));      // SimpleGrantedAuthority -> GrantedAuthority의 가장 간단한 구현체, 사용자의 권한을 문자열로 표현

            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            AbstractAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities);    //접근 주체에 대한 정보, 비밀번호 + 권한 등을 사용해 인증 객체 생성
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // 추가적인 인증 세부 정보 설정

            securityContext.setAuthentication(authenticationToken); // 비어있는 securityContext에 생성한 authenticationToken을 설
            SecurityContextHolder.setContext(securityContext);

        } catch(Exception exception) {
            exception.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {

        String authorization = request.getHeader("Authorization");

        // Authorization이 존재하는가?
        boolean hasAuthorization = StringUtils.hasText(authorization);
        if(!hasAuthorization) {
            return null;
        }

        // 인증 방식이 Bearer인가?
        boolean isBearer = authorization.startsWith("Bearer ");
        if(!isBearer) {
            return null;
        }

        String token = authorization.substring(7);
        return token;
    }

}
