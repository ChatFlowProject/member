package chatflow.memberservice.security;

import chatflow.memberservice.dto.MemberDto;
import chatflow.memberservice.service.MemberService;
import chatflow.memberservice.vo.RequestSignIn;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private MemberService memberService;
    private Environment environment;
    // WebSecurity 클래스에서 생성한 AuthenticationManager를 생성자 주입
//    private AuthenticationManager manager;

    public AuthenticationFilter(AuthenticationManager authenticationManager, MemberService memberService, Environment environment) {
        super(authenticationManager);
        this.memberService = memberService;
        this.environment = environment;
    }

    @Override // Note: 로그인시 인증 시도
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            RequestSignIn credential = new ObjectMapper().readValue(req.getInputStream(), RequestSignIn.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken( // 아래의 내용으로 인증 토큰 생성
                            credential.getEmail(), // member의 name(email)
                            credential.getPassword(),  // member의 password
                            new ArrayList<>() // 토큰에 들어갈 권한 목록
                    )
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // Note: 인증 성공후 로직 처리 ex) 임의의 토큰 생성
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String memberName = ((User) auth.getPrincipal()).getUsername();
        log.debug(memberName + " is successfully authenticated.");

        MemberDto memberDetails = memberService.getMemberDetailsByEmail(memberName);

        // JWT JWA Specification(RFC 7518, 섹션 3.2)에 따르면 HMAC-SHA 알고리즘에 사용되는 key의 크기는 256비트 이상이어야 함
        byte[] secretKeyBytes = Base64.getEncoder().encode(Objects.requireNonNull(environment.getProperty("token.secret")).getBytes()); // key생성에 사용되는 변수 지정(환경 변수 정보 받아서)
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
        Instant now = Instant.now();
        String token = Jwts.builder()
                .subject(memberDetails.getMemberId()) // 어떤 내용으로 Jwt토큰 생성할 것인지
                .expiration(Date.from(now.plusMillis(Long.parseLong(Objects.requireNonNull(environment.getProperty("token.expiration_time")))))) // 토큰 유효 기간
                .issuedAt(Date.from(now)) // 토큰 발행 날짜
                .signWith(secretKey) // 서명 - 암호화된 key를 사용함
                .compact();

        res.addHeader("token", token); // 토큰 반환
        res.addHeader("memberId", memberDetails.getMemberId()); // uuid 반환
    }
}