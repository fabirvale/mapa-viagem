package com.fabiana.mapa_viagem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fabiana.mapa_viagem.model.Hospital;


public interface HospitalRepository extends JpaRepository<Hospital, Long> {

}
