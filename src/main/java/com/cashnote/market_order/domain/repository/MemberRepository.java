package com.cashnote.market_order.domain.repository;

import com.cashnote.market_order.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
}