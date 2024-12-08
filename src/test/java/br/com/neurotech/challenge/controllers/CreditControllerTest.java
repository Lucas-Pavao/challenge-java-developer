package br.com.neurotech.challenge.controllers;

import br.com.neurotech.challenge.dtos.CreditCheckResponseDto;
import br.com.neurotech.challenge.entity.VehicleModel;
import br.com.neurotech.challenge.exceptions.ClientNotEligibleForCreditException;
import br.com.neurotech.challenge.service.CreditService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CreditControllerTest {

    private final CreditService creditService = mock(CreditService.class);
    private final CreditController creditController = new CreditController(creditService);

    @Test
    void testCheckCredit_Success() {
        Long clientId = 1L;
        VehicleModel model = VehicleModel.HATCH;
        CreditCheckResponseDto expectedResponse = new CreditCheckResponseDto(
                clientId, "John Doe", model.name(), true, "Apto para crédito automotivo na modalidade: Juros Fixos"
        );

        when(creditService.checkCredit(clientId, model)).thenReturn(expectedResponse);

        ResponseEntity<CreditCheckResponseDto> response = creditController.checkCredit(clientId, model);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(creditService, times(1)).checkCredit(clientId, model);
    }

    @Test
    void testCheckCredit_ClientNotEligible() {
        Long clientId = 2L;
        VehicleModel model = VehicleModel.SUV;
        String errorMessage = "Cliente não é elegível para crédito para veículo do tipo SUV.";

        when(creditService.checkCredit(clientId, model))
                .thenThrow(new ClientNotEligibleForCreditException(errorMessage));

        ResponseEntity<CreditCheckResponseDto> response = creditController.checkCredit(clientId, model);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(clientId, response.getBody().getClientId());
        assertEquals(errorMessage, response.getBody().getMessage());
        verify(creditService, times(1)).checkCredit(clientId, model);
    }

    @Test
    void testCheckCredit_RuntimeException() {
        Long clientId = 3L;
        VehicleModel model = VehicleModel.HATCH;

        when(creditService.checkCredit(clientId, model)).thenThrow(new RuntimeException("Erro inesperado"));

        try {
            creditController.checkCredit(clientId, model);
        } catch (RuntimeException ex) {
            assertEquals("Erro inesperado ao verificar crédito: Erro inesperado", ex.getMessage());
        }

        verify(creditService, times(1)).checkCredit(clientId, model);
    }

    @Test
    void testGetEligibleClientsForHatch_Success() {
        List<Map<String, Object>> eligibleClients = List.of(
                Map.of("name", "John Doe", "income", 10000.00),
                Map.of("name", "Jane Smith", "income", 12000.00)
        );

        when(creditService.findEligibleClientsForHatch()).thenReturn(eligibleClients);

        ResponseEntity<List<Map<String, Object>>> response = creditController.getEligibleClientsForHatch();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(eligibleClients, response.getBody());
        verify(creditService, times(1)).findEligibleClientsForHatch();
    }
}
