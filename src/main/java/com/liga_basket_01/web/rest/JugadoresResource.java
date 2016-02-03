package com.liga_basket_01.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.liga_basket_01.domain.Jugadores;
import com.liga_basket_01.repository.JugadoresRepository;
import com.liga_basket_01.web.rest.util.HeaderUtil;
import com.liga_basket_01.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Jugadores.
 */
@RestController
@RequestMapping("/api")
public class JugadoresResource {

    private final Logger log = LoggerFactory.getLogger(JugadoresResource.class);
        
    @Inject
    private JugadoresRepository jugadoresRepository;
    
    /**
     * POST  /jugadoress -> Create a new jugadores.
     */
    @RequestMapping(value = "/jugadoress",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Jugadores> createJugadores(@Valid @RequestBody Jugadores jugadores) throws URISyntaxException {
        log.debug("REST request to save Jugadores : {}", jugadores);
        if (jugadores.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("jugadores", "idexists", "A new jugadores cannot already have an ID")).body(null);
        }
        Jugadores result = jugadoresRepository.save(jugadores);
        return ResponseEntity.created(new URI("/api/jugadoress/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("jugadores", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /jugadoress -> Updates an existing jugadores.
     */
    @RequestMapping(value = "/jugadoress",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Jugadores> updateJugadores(@Valid @RequestBody Jugadores jugadores) throws URISyntaxException {
        log.debug("REST request to update Jugadores : {}", jugadores);
        if (jugadores.getId() == null) {
            return createJugadores(jugadores);
        }
        Jugadores result = jugadoresRepository.save(jugadores);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("jugadores", jugadores.getId().toString()))
            .body(result);
    }

    /**
     * GET  /jugadoress -> get all the jugadoress.
     */
    @RequestMapping(value = "/jugadoress",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Jugadores>> getAllJugadoress(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Jugadoress");
        Page<Jugadores> page = jugadoresRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/jugadoress");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /jugadoress/:id -> get the "id" jugadores.
     */
    @RequestMapping(value = "/jugadoress/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Jugadores> getJugadores(@PathVariable Long id) {
        log.debug("REST request to get Jugadores : {}", id);
        Jugadores jugadores = jugadoresRepository.findOne(id);
        return Optional.ofNullable(jugadores)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /jugadoress/:id -> delete the "id" jugadores.
     */
    @RequestMapping(value = "/jugadoress/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteJugadores(@PathVariable Long id) {
        log.debug("REST request to delete Jugadores : {}", id);
        jugadoresRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("jugadores", id.toString())).build();
    }
}
