package br.com.neurotech.challenge.controllers;

import br.com.neurotech.challenge.dtos.NeurotechClientDto;
import br.com.neurotech.challenge.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NeurotechClientControllerTest {

    @InjectMocks
    private NeurotechClientController controller;

    @Mock
    private ClientService service;

    private NeurotechClientDto clientDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientDto = new NeurotechClientDto(1L, "John Doe", 30, 5000.0);
    }

    @Test
    void getAllClients_ShouldReturnClientList() {
        when(service.findAll()).thenReturn(List.of(clientDto));

        ResponseEntity<List<NeurotechClientDto>> response = controller.getAllClients();

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        verify(service, times(1)).findAll();
    }

    @Test
    void getClientById_ShouldReturnClient() {
        when(service.findById(1L)).thenReturn(clientDto);
        when(service.getHateoasLinks(1L)).thenReturn(Map.of("self", "/api/neurotech-clients/1"));

        ResponseEntity<NeurotechClientDto> response = controller.getClientById(1L);

        assertNotNull(response);
        assertEquals("John Doe", response.getBody().getName());
        verify(service, times(1)).findById(1L);
    }

    @Test
    void createClient_ShouldReturnCreatedClient() {
        when(service.create(clientDto)).thenReturn(clientDto);
        when(service.getHateoasLinks(1L)).thenReturn(Map.of("self", "/api/neurotech-clients/1"));

        ResponseEntity<NeurotechClientDto> response = controller.createClient(clientDto);

        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals("John Doe", response.getBody().getName());
        verify(service, times(1)).create(clientDto);
    }

    @Test
    void updateClient_ShouldReturnUpdatedClient() {
        when(service.update(1L, clientDto)).thenReturn(clientDto);
        when(service.getHateoasLinks(1L)).thenReturn(Map.of("self", "/api/neurotech-clients/1"));

        ResponseEntity<NeurotechClientDto> response = controller.updateClient(1L, clientDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("John Doe", response.getBody().getName());
        verify(service, times(1)).update(1L, clientDto);
    }

    @Test
    void deleteClient_ShouldCallServiceDelete() {
        controller.deleteClient(1L);
        verify(service, times(1)).delete(1L);
    }
}
