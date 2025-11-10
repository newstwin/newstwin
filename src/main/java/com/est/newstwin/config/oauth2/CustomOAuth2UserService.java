package com.est.newstwin.config.oauth2;

import com.est.newstwin.domain.Member;
import com.est.newstwin.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * 구글 로그인 성공 후 사용자 정보를 가져와
 * DB에 Member 저장 or 조회
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 구글 프로필 정보
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // DB에 회원 없으면 새로 생성
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(
                        Member.builder()
                                .email(email)
                                .memberName(name)
                                .password("")               // 소셜 로그인은 비밀번호 없음
                                .role(Member.Role.ROLE_USER)
                                .isActive(true)
                                .build()
                ));

        return new CustomOAuth2User(member, oAuth2User.getAttributes());
    }
}
