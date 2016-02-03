package com.liga_basket_01.repository;

import com.liga_basket_01.domain.Jugadores;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Jugadores entity.
 */
public interface JugadoresRepository extends JpaRepository<Jugadores,Long> {

}
