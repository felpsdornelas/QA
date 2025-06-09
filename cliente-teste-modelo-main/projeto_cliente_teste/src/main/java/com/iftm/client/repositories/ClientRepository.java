package com.iftm.client.repositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iftm.client.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

     // 1. Buscar cliente por nome exato (ignorando maiúsculas/minúsculas)     
     //@Query("SELECT c FROM Client c WHERE LOWER(c.name) = LOWER(:name)")
     //Client buscarPorNome(@Param("name") String nome);
     Client findByNameIgnoreCase(String name);

     // 2. Buscar todos os clientes cujo nome contenha a palavra (ignorando maiúsculas/minúsculas)
     //@Query("SELECT c FROM Client c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :palavra, '%'))")
    //List<Client> buscarPorNomePalavra(@Param("palavra") String nome);
     List<Client> findByNameContainingIgnoreCase(String palavra);

    // 3. Buscar clientes com salário superior a um valor
     //@Query("SELECT c FROM Client c WHERE c.income > :incomeFind")
     //List<Client> encontrarPorRendaMaior(@Param("incomeFind") Double income);
     Page<Client> findByIncomeGreaterThan(Double income, Pageable pageable);

     // 4. Buscar clientes com salário superior a um valor
     //@Query("SELECT c FROM Client c WHERE c.income < :incomeFind")
     //List<Client> encontrarPorRendaMenor(@Param("incomeFind") Double income);
     Page<Client> findByIncomeLessThan(Double income, Pageable pageable);

     // 5. Buscar clientes com salário entre dois valores
     // @Query ("SELECT c FROM Client c WHERE c.income BETWEEN :incomeMin AND :incomeMax")
     // List<Client> buscaRendaEntreValores(@Param("incomeMin") Double incomeMin,  
     //                                    @Param("incomeMax") Double incomeMax);
     Page<Client> findByIncomeBetween(Double incomeMin, Double incomeMax, Pageable pageable);

     // 6. Buscar clientes com data de nascimento entre duas datas
     // List<Client> findClientBybirthDateBetween(Instant dataInicio, Instant dataFinal);
     Page<Client> findByBirthDateBetween(Instant dataInicio, Instant dataFinal, Pageable pageable);

     

}
