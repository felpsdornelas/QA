package com.iftm.client.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
// import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.iftm.client.dto.ClientDTO;
import com.iftm.client.repositories.ClientRepository;
import com.iftm.client.services.exceptions.ResourceNotFoundException;

// import ch.qos.logback.core.net.server.Client;
import com.iftm.client.entities.Client;

@ExtendWith(SpringExtension.class)
public class ClientServiceTest {
    
     // define qual classe receberá os Mocks criados
     @InjectMocks
     private ClientService service;

     // Vai injetar nessa
     @Mock
     private ClientRepository repository;

          private Long existingId;
          private Long nonExistingId;
          private Client client;
          private ClientDTO clientDTO;
          private PageImpl<Client> page;

     @BeforeEach
     void setUp() {
          existingId = 1L;
          nonExistingId = 100L;
          client = new Client();
               
          clientDTO = new ClientDTO(client);
          page = new PageImpl<>(List.of(client));

        // DELETE
        doNothing().when(repository).deleteById(existingId);
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);

        // FIND BY ID
        when(repository.findById(existingId)).thenReturn(Optional.of(client));
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        // FIND ALL PAGED
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        // FIND BY INCOME
        when(repository.findByIncomeGreaterThan(eq(5000.0), any(Pageable.class))).thenReturn(page);

        // UPDATE
        when(repository.findById(existingId)).thenReturn(Optional.of(client));
        doThrow(ResourceNotFoundException.class).when(repository).findById(nonExistingId);

        // INSERT
        when(repository.save(any())).thenReturn(client);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        assertDoesNotThrow(() -> service.delete(existingId));
        verify(repository, times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
      assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistingId));
      verify(repository, times(1)).deleteById(nonExistingId);
    }

    @Test
    public void findAllPagedShouldReturnPageOfClients() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ClientDTO> result = service.findAllPaged(pageRequest);
        assertNotNull(result);
        verify(repository, times(1)).findAll(pageRequest);
    }

    @Test
    public void findByIncomeShouldReturnPageOfClientsWithGivenIncome() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ClientDTO> result = service.findByIncome(5000.0, pageable);
        assertNotNull(result);
        verify(repository, times(1)).findByIncomeGreaterThan(5000.0, pageable);
}

    @Test
    public void findByIdShouldReturnClientDTOWhenIdExists() {
        ClientDTO result = service.findById(existingId);
        assertNotNull(result);
        assertEquals(ClientDTO.class, result.getClass());
        verify(repository, times(1)).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> service.findById(nonExistingId));
        verify(repository, times(1)).findById(nonExistingId);
    }

    @Test
    public void updateShouldReturnClientDTOWhenIdExists() {
        ClientDTO result = service.update(existingId, clientDTO);
        assertNotNull(result);
        verify(repository, times(1)).findById(existingId);
        verify(repository, times(1)).save(any());
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> service.update(nonExistingId, clientDTO));
        verify(repository, times(1)).findById(nonExistingId);
    }

    @Test
    public void insertShouldReturnClientDTO() {
        ClientDTO result = service.insert(clientDTO);
        assertNotNull(result);
        verify(repository, times(1)).save(any());
    }
}
