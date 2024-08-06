package com.iftm.client.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iftm.client.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Milena
    
    @Query("SELECT c FROM Client c WHERE LOWER(c.name) = LOWER(:name)")
    Optional<Client> findClientByName(@Param("name") String name);

    @Query("SELECT c FROM Client c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :param, '%'))")
    public List<Client> findClientByNameContains(@Param("param") String param);

}