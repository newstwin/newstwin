package com.est.newstwin.service;

import com.est.newstwin.domain.*;
import com.est.newstwin.dto.member.MemberRequestDto;
import com.est.newstwin.dto.member.MemberResponseDto;
import com.est.newstwin.exception.CustomException;
import com.est.newstwin.exception.ErrorCode;
import com.est.newstwin.repository.*;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationTokenRepository tokenRepository;
    private final EmailService emailService;

    public MemberResponseDto signup(MemberRequestDto requestDto) {
        if (memberRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        Member member = new Member(
                requestDto.getMemberName(),
                encodedPassword,
                requestDto.getEmail(),
                Member.Role.ROLE_USER,
                true // isActive 기본 true (관리자 차단 아님)
        );
        member.setIsVerified(false); // 이메일 인증 전 false

        Member savedMember = memberRepository.save(member);

        EmailVerificationToken token = EmailVerificationToken.create(savedMember);
        tokenRepository.save(token);
        emailService.sendVerificationEmail(savedMember.getEmail(), token.getToken());

        return MemberResponseDto.fromEntity(savedMember);
    }

    public String verifyEmail(String tokenValue) {
        EmailVerificationToken token = tokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));

        if (token.isExpired()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        Member member = token.getMember();
        member.verifyEmail();
        memberRepository.save(member);

        tokenRepository.delete(token);
        return "이메일 인증이 완료되었습니다. 로그인해주세요.";
    }

    public void resendVerificationEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getIsVerified()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE); // 이미 인증됨
        }

        tokenRepository.findAll().stream()
                .filter(t -> t.getMember().getId().equals(member.getId()))
                .forEach(tokenRepository::delete);

        EmailVerificationToken newToken = EmailVerificationToken.create(member);
        tokenRepository.save(newToken);
        emailService.sendVerificationEmail(member.getEmail(), newToken.getToken());
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDto> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(member -> {
                    List<UserSubscription> subs = userSubscriptionRepository.findAllByMember(member);
                    List<String> categoryNames = subs.stream().map(s -> s.getCategory().getCategoryName()).toList();
                    boolean hasActive = subs.stream().anyMatch(UserSubscription::getIsActive);

                    return MemberResponseDto.builder()
                            .id(member.getId())
                            .memberName(member.getMemberName())
                            .email(member.getEmail())
                            .role(member.getRole().name())
                            .isActive(member.getIsActive())
                            .isVerified(member.getIsVerified())
                            .receiveEmail(member.getReceiveEmail() != null ? member.getReceiveEmail() : false)
                            .categories(categoryNames)
                            .categoryIds(subs.stream().map(s -> s.getCategory().getId()).toList())
                            .subscriptionStatus(hasActive ? "구독중" : "구독 없음")
                            .build();
                })
                .toList();
    }

    public MemberResponseDto toggleMemberStatus(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원 없음"));

        member.setIsActive(!member.getIsActive());
        memberRepository.save(member);

        List<Category> activeCategories = userSubscriptionRepository.findAllByMember(member).stream()
                .filter(UserSubscription::getIsActive)
                .map(UserSubscription::getCategory)
                .toList();

        return MemberResponseDto.fromEntityWithCategories(member, activeCategories);
    }

    public MemberResponseDto toggleSubscriptionStatus(Long memberId, List<Long> categoryIds) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원 없음"));

        List<Category> categories = categoryRepository.findAllById(categoryIds);

        for (Category category : categories) {
            UserSubscription sub = userSubscriptionRepository.findByMemberAndCategory(member, category)
                    .orElseGet(() -> userSubscriptionRepository.save(UserSubscription.create(member, category, false)));
            sub.setIsActive(!sub.getIsActive());
            userSubscriptionRepository.save(sub);
        }

        List<Category> activeCategories = userSubscriptionRepository.findAllByMember(member).stream()
                .filter(UserSubscription::getIsActive)
                .map(UserSubscription::getCategory)
                .toList();

        return MemberResponseDto.fromEntityWithCategories(member, activeCategories);
    }

    public MemberResponseDto toggleReceiveEmail(Long memberId) {
      Member member = memberRepository.findById(memberId)
          .orElseThrow(() -> new RuntimeException("회원 없음"));

      member.setReceiveEmail(!member.getReceiveEmail());
      memberRepository.save(member);

      List<Category> activeCategories = userSubscriptionRepository.findAllByMember(member).stream()
          .filter(UserSubscription::getIsActive)
          .map(UserSubscription::getCategory)
          .toList();

      return MemberResponseDto.fromEntityWithCategories(member, activeCategories);
    }
}
