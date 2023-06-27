package com.kosa.gather_e.config.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtils jwtTokenUtils;
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = jwtTokenUtils.getTokenFromHeader(authorizationHeader);
            if (jwtTokenUtils.isValidToken(token)) {
                String userSeq = jwtTokenUtils.getUserSeqFromToken(token);
                System.out.println("토큰으로부터 가져온 유저데이터" + userSeq);
            }
        }
        chain.doFilter(request, response);
    }
}
