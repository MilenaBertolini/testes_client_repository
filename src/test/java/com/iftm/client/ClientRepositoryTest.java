package com.iftm.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

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

    // Diego

    @Test
    @DisplayName("Testando os métodos que retornam vários clientes baseado no salário - Salário entre um intervalo")
    public void TesteBuscaSalarioNoIntervaloDosValorePassados(){

        // Arrange
        Double valorMenor = 2400.00;
        Double valorMaior = 8000.00;

        // Act
        List<Client> salarioEntreValores = clientRepository.findClientBySalaryBetween(valorMenor, valorMaior);

        // Assign
        for (Client cliente : salarioEntreValores) {
            assertTrue(
                (cliente.getIncome() > valorMenor) && (cliente.getIncome() < valorMaior)
            );
        }
        
    }


    @Test
    @DisplayName("Testando o método que retorna vários clientes a data de aniversário entre 2 datas informadas")
    public void TesteClienteComDataDeAniversarioEntreDuasDatas(){

        // Arrange
        Instant dataMenor = Instant.parse("1990-06-20T10:25:43Z");
        Instant dataMaior = Instant.now();

        // Act
        List<Client> datasNascimento = clientRepository.findClientByBirthDateBetween(dataMenor, dataMaior);

        // Assign
        for (Client cliente : datasNascimento) {
            assertTrue(
                ((cliente.getBirthDate().isAfter(dataMenor) && cliente.getBirthDate().isBefore(dataMaior)) )
            );
        }
    }

    @Test
    @DisplayName("Testando atualização de um cliente")
    public void testarAtualizacaoCliente() {
        
        // Arrange
        Client cliente = new Client(13L, "Carlos Silva", "12345678910", 4500.00, Instant.now(), 1);
        cliente = clientRepository.save(cliente);

        // Act
        cliente.setName("Carlos Eduardo Silva");
        Client clienteAtualizado = clientRepository.save(cliente);

        // Assert
        Optional<Client> clienteRetornado = clientRepository.findById(cliente.getId());

        assertTrue(clienteRetornado.isPresent());
        assertEquals("Carlos Eduardo Silva", clienteAtualizado.getName());
    }   

    @Test
    @DisplayName("Verificar se o delete realmente apaga o registro de um cliente existente")
    public void testarDeletePorIdApagaClienteExistente(){

        // Arrange
        Long idExistente = 12L;
        Long tamanhoBdEsperado = 11L;

        // Act
        clientRepository.deleteById(idExistente);
        Optional<Client> resultadoDelete = clientRepository.findById(idExistente);

        // Assign
        assertEquals(tamanhoBdEsperado, clientRepository.count());
        assertTrue(resultadoDelete.isEmpty());

    }

    @Test
    @DisplayName("Verificar se ao deletar um id não existente é retornado um erro")
    public void testarExcluirPorIdNaoExistenteGeraErro(){

        // Arrange
        Long idNaoExistente = 1000L;
        Long tamanhoBdEsperado = 12L;

        // Act and Assign
       assertThrows(EmptyResultDataAccessException.class,
                () -> {
                    clientRepository.deleteById(idNaoExistente);
                }
        );
        assertEquals(tamanhoBdEsperado, clientRepository.count());

    }
}
