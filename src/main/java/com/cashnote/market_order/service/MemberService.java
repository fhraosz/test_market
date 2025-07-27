package com.cashnote.market_order.service;

import com.cashnote.market_order.domain.entity.MemberEntity;
import com.cashnote.market_order.domain.repository.MemberRepository;
import com.cashnote.market_order.exception.OrderException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
	private final MemberRepository memberRepository;

	@Transactional
	public MemberEntity findMemberEntityById(Long memberId) {
		return memberRepository.findById(memberId)
				.orElseThrow(() -> {
					log.error("존재하지 않는 회원: id={}", memberId);
					return new OrderException("회원 정보를 찾을 수 없습니다.");
				});
	}
}
