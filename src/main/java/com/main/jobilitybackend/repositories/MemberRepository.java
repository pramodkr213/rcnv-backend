package com.main.jobilitybackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.jobilitybackend.entities.Clubmembers;

public interface MemberRepository extends JpaRepository<Clubmembers, Long> {

}
