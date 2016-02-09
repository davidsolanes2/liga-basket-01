package com.liga_basket_01.web.rest;

import com.liga_basket_01.Application;
import com.liga_basket_01.domain.Jugadores;
import com.liga_basket_01.repository.JugadoresRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the JugadoresResource REST controller.
 *
 * @see JugadoresResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class JugadoresResourceIntTest {

    private static final String DEFAULT_NOMBRE_JUGADOR = "AAAAA";
    private static final String UPDATED_NOMBRE_JUGADOR = "BBBBB";

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_NUM_CANASTAS = 1;
    private static final Integer UPDATED_NUM_CANASTAS = 2;

    private static final Integer DEFAULT_NUM_ASISTENCIAS = 1;
    private static final Integer UPDATED_NUM_ASISTENCIAS = 2;

    private static final Integer DEFAULT_NUM_REBOTES = 1;
    private static final Integer UPDATED_NUM_REBOTES = 2;
    private static final String DEFAULT_POSICION = "AAAAA";
    private static final String UPDATED_POSICION = "BBBBB";

    @Inject
    private JugadoresRepository jugadoresRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restJugadoresMockMvc;

    private Jugadores jugadores;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JugadoresResource jugadoresResource = new JugadoresResource();
        ReflectionTestUtils.setField(jugadoresResource, "jugadoresRepository", jugadoresRepository);
        this.restJugadoresMockMvc = MockMvcBuilders.standaloneSetup(jugadoresResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        jugadores = new Jugadores();
        jugadores.setNombreJugador(DEFAULT_NOMBRE_JUGADOR);
        jugadores.setFechaNacimiento(DEFAULT_FECHA_NACIMIENTO);
        jugadores.setNumCanastas(DEFAULT_NUM_CANASTAS);
        jugadores.setNumAsistencias(DEFAULT_NUM_ASISTENCIAS);
        jugadores.setNumRebotes(DEFAULT_NUM_REBOTES);
        jugadores.setPosicion(DEFAULT_POSICION);
    }

    @Test
    @Transactional
    public void createJugadores() throws Exception {
        int databaseSizeBeforeCreate = jugadoresRepository.findAll().size();

        // Create the Jugadores

        restJugadoresMockMvc.perform(post("/api/jugadoress")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jugadores)))
                .andExpect(status().isCreated());

        // Validate the Jugadores in the database
        List<Jugadores> jugadoress = jugadoresRepository.findAll();
        assertThat(jugadoress).hasSize(databaseSizeBeforeCreate + 1);
        Jugadores testJugadores = jugadoress.get(jugadoress.size() - 1);
        assertThat(testJugadores.getNombreJugador()).isEqualTo(DEFAULT_NOMBRE_JUGADOR);
        assertThat(testJugadores.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testJugadores.getNumCanastas()).isEqualTo(DEFAULT_NUM_CANASTAS);
        assertThat(testJugadores.getNumAsistencias()).isEqualTo(DEFAULT_NUM_ASISTENCIAS);
        assertThat(testJugadores.getNumRebotes()).isEqualTo(DEFAULT_NUM_REBOTES);
        assertThat(testJugadores.getPosicion()).isEqualTo(DEFAULT_POSICION);
    }

    @Test
    @Transactional
    public void checkNombreJugadorIsRequired() throws Exception {
        int databaseSizeBeforeTest = jugadoresRepository.findAll().size();
        // set the field null
        jugadores.setNombreJugador(null);

        // Create the Jugadores, which fails.

        restJugadoresMockMvc.perform(post("/api/jugadoress")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jugadores)))
                .andExpect(status().isBadRequest());

        List<Jugadores> jugadoress = jugadoresRepository.findAll();
        assertThat(jugadoress).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJugadoress() throws Exception {
        // Initialize the database
        jugadoresRepository.saveAndFlush(jugadores);

        // Get all the jugadoress
        restJugadoresMockMvc.perform(get("/api/jugadoress?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(jugadores.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombreJugador").value(hasItem(DEFAULT_NOMBRE_JUGADOR.toString())))
                .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
                .andExpect(jsonPath("$.[*].numCanastas").value(hasItem(DEFAULT_NUM_CANASTAS)))
                .andExpect(jsonPath("$.[*].numAsistencias").value(hasItem(DEFAULT_NUM_ASISTENCIAS)))
                .andExpect(jsonPath("$.[*].numRebotes").value(hasItem(DEFAULT_NUM_REBOTES)))
                .andExpect(jsonPath("$.[*].posicion").value(hasItem(DEFAULT_POSICION.toString())));
    }

    @Test
    @Transactional
    public void getJugadores() throws Exception {
        // Initialize the database
        jugadoresRepository.saveAndFlush(jugadores);

        // Get the jugadores
        restJugadoresMockMvc.perform(get("/api/jugadoress/{id}", jugadores.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(jugadores.getId().intValue()))
            .andExpect(jsonPath("$.nombreJugador").value(DEFAULT_NOMBRE_JUGADOR.toString()))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()))
            .andExpect(jsonPath("$.numCanastas").value(DEFAULT_NUM_CANASTAS))
            .andExpect(jsonPath("$.numAsistencias").value(DEFAULT_NUM_ASISTENCIAS))
            .andExpect(jsonPath("$.numRebotes").value(DEFAULT_NUM_REBOTES))
            .andExpect(jsonPath("$.posicion").value(DEFAULT_POSICION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJugadores() throws Exception {
        // Get the jugadores
        restJugadoresMockMvc.perform(get("/api/jugadoress/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJugadores() throws Exception {
        // Initialize the database
        jugadoresRepository.saveAndFlush(jugadores);

		int databaseSizeBeforeUpdate = jugadoresRepository.findAll().size();

        // Update the jugadores
        jugadores.setNombreJugador(UPDATED_NOMBRE_JUGADOR);
        jugadores.setFechaNacimiento(UPDATED_FECHA_NACIMIENTO);
        jugadores.setNumCanastas(UPDATED_NUM_CANASTAS);
        jugadores.setNumAsistencias(UPDATED_NUM_ASISTENCIAS);
        jugadores.setNumRebotes(UPDATED_NUM_REBOTES);
        jugadores.setPosicion(UPDATED_POSICION);

        restJugadoresMockMvc.perform(put("/api/jugadoress")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jugadores)))
                .andExpect(status().isOk());

        // Validate the Jugadores in the database
        List<Jugadores> jugadoress = jugadoresRepository.findAll();
        assertThat(jugadoress).hasSize(databaseSizeBeforeUpdate);
        Jugadores testJugadores = jugadoress.get(jugadoress.size() - 1);
        assertThat(testJugadores.getNombreJugador()).isEqualTo(UPDATED_NOMBRE_JUGADOR);
        assertThat(testJugadores.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testJugadores.getNumCanastas()).isEqualTo(UPDATED_NUM_CANASTAS);
        assertThat(testJugadores.getNumAsistencias()).isEqualTo(UPDATED_NUM_ASISTENCIAS);
        assertThat(testJugadores.getNumRebotes()).isEqualTo(UPDATED_NUM_REBOTES);
        assertThat(testJugadores.getPosicion()).isEqualTo(UPDATED_POSICION);
    }

    @Test
    @Transactional
    public void deleteJugadores() throws Exception {
        // Initialize the database
        jugadoresRepository.saveAndFlush(jugadores);

		int databaseSizeBeforeDelete = jugadoresRepository.findAll().size();

        // Get the jugadores
        restJugadoresMockMvc.perform(delete("/api/jugadoress/{id}", jugadores.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Jugadores> jugadoress = jugadoresRepository.findAll();
        assertThat(jugadoress).hasSize(databaseSizeBeforeDelete - 1);
    }
}
