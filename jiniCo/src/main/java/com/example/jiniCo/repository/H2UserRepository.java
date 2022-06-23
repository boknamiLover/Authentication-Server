package com.example.jiniCo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.jiniCo.entity.H2User;

public interface H2UserRepository extends JpaRepository<H2User, String> {

	// 이름과 주민번호로 검색
	Optional<H2User> findByNameAndRegNo(String name, String regNo);

}
