package com.iftm.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.iftm.client.entities.Client;
import com.iftm.client.repositories.ClientRepository;

@DataJpaTest
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    // Milena
    
    @Test
    @DisplayName("Testando método que retorna o cliente buscando por um nome existente")
    public void testarBuscaPorNomeExistente() {

        // Arrange
        String nomeExistente = "Clarice Lispector";
        String nomeEsperado = "Clarice Lispector";

        // Act
        Optional<Client> clientRetornado = clientRepository.findClientByName(nomeExistente);

        // Assert
        assertEquals(nomeEsperado, clientRetornado.orElseThrow().getName());
    }

    @Test
    @DisplayName("Testando método que retorna o cliente buscando por um nome Inexistente")
    public void testarBuscaPorNomeInexistente() {

        // Arrange
        String nomeInexistente = "João Siqueira";

        // Act
        Optional<Client> clientRetornado = clientRepository.findClientByName(nomeInexistente);

        // Assert
        assertTrue(clientRetornado.isEmpty());
       
    }

    @Test
    @DisplayName("Testando o método que retorna vários clientes com parte do nome similar ao texto - Texto existente")
    public void TestarPesquisaTextoExistente(){

        // Arrange
        String textoExistente = "ar";


        // Act
        List<Client> nomePorParametro = clientRepository.findClientByNameContains(textoExistente);

        // Assign
        for (Client cliente : nomePorParametro) {
            assertTrue(cliente.getName().contains(textoExistente));
        }
    }

    @Test
    @DisplayName("Testando o método que retorna vários clientes com parte do nome similar ao texto - Texto inexistente")
    public void TestarPesquisaTextoInexistente(){

        // Arrange
        String textoInexistente = "oz";


        // Act
        List<Client> nomePorParametro = clientRepository.findClientByNameContains(textoInexistente);

        // Assign
        assertTrue(nomePorParametro.isEmpty());
    }

    @Test
    @DisplayName("Testando o método que retorna vários clientes com parte do nome similar ao texto - Texto vazio")
    public void TestarPesquisaTextoVazio(){

        // Arrange
        String textoVazio = "";
        Long tamanhoBDEsperado = 12L;


        // Act
        List<Client> nomePorParametro = clientRepository.findClientByNameContains(textoVazio);

        // Assign
        assertEquals(tamanhoBDEsperado, nomePorParametro.size());
    }

    
}
