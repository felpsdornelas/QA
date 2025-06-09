package com.iftm.client.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.iftm.client.entities.Client;

@DataJpaTest
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository repositorio;

    @Test
    public void testarBuscarPorNomeExistenteDeNomeQueContemAPalavra() {
        String nomeEsperado = "Ado";
        List<Client> clientObtido = repositorio.buscarPorNomePalavra(nomeEsperado);
        assertFalse(clientObtido.isEmpty());
    }

    @Test
    public void testarBuscarPorNomeNaoExistenteDeNomeQueContemAPalavra() {
        String nomeEsperado = "lipe";
        List<Client> clientObtido = repositorio.buscarPorNomePalavra(nomeEsperado);
        assertTrue(clientObtido.isEmpty());
    }

    @Test
    public void testarClientesQueEstejamAcimaDoSalarioMaximo() {
        double tetoRenda = 5000.00;
        List<Client> clientObtido = repositorio.encontrarPorRendaMaior(tetoRenda);
        assertFalse(clientObtido.isEmpty());
    }

    @Test
    public void testarClientesQueEstejamAbaixoDoSalarioMinimo() {
        double tetoRenda = 2000.00;
        List<Client> clientObtido = repositorio.encontrarPorRendaMenor(tetoRenda);
        assertFalse(clientObtido.isEmpty());
    }
}