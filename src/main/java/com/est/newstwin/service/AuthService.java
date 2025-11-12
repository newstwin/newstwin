package com.est.newstwin.service;

import com.est.newstwin.domain.Member;
import com.est.newstwin.dto.auth.LoginRequestDto;
import com.est.newstwin.dto.auth.LoginResponseDto;
import com.est.newstwin.exception.CustomException;
import com.est.newstwin.exception.ErrorCode;
import com.est.newstwin.repository.MemberRepository;
import com.est.newstwin.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 서비스
 * - 로그인 시 이메일 인증 여부(isVerified)와 관리자 비활성화(isActive) 모두 검증
 * - 모든 조건 충족 시 JWT 발급
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 로그인 후 JWT 토큰 발급
     */
    public LoginResponseDto login(LoginRequestDto requestDto) {

        // 이메일로 사용자 조회
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 관리자에 의해 비활성화된 계정인지 확인
        if (Boolean.FALSE.equals(member.getIsActive())) {
            throw new CustomException(ErrorCode.ACCOUNT_DEACTIVATED);
        }

        // 이메일 인증이 완료되지 않은 계정인지 확인
        if (Boolean.FALSE.equals(member.getIsVerified())) {
            throw new CustomException(ErrorCode.EMAIL_NOT_VERIFIED);
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        // JWT 토큰 발급
        String accessToken = jwtTokenProvider.generateToken(member);

        // 로그인 응답 반환
        return LoginResponseDto.of(accessToken, null, member.getMemberName());
    }
}
