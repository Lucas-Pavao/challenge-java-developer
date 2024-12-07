package br.com.neurotech.challenge.controllers;

import br.com.neurotech.challenge.entity.VehicleModel;
import br.com.neurotech.challenge.service.CreditService;
import br.com.neurotech.challenge.exceptions.ClientNotEligibleForCreditException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/credit")
@Tag(name = "Credit", description = "Endpoints for checking credit eligibility for clients")
public class CreditController {

    private final CreditService creditService;

    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @GetMapping("/client/{clientId}/automotive")
    @Operation(summary = "Check automotive credit eligibility",
            description = "Verify if a client is eligible for automotive credit based on their profile and vehicle model",
            tags = {"Credit"},
            responses = {
                    @ApiResponse(description = "Eligible for credit", responseCode = "200", content = @Content(schema = @Schema(type = "string", example = "Apto para crédito automotivo."))),
                    @ApiResponse(description = "Client not eligible for credit", responseCode = "400", content = @Content(schema = @Schema(type = "string", example = "Cliente não elegível para crédito."))),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<String> checkCredit(@PathVariable Long clientId, @RequestParam VehicleModel model) {
        try {
            creditService.checkCredit(clientId, model);
            return ResponseEntity.ok("Apto para crédito automotivo.");
        } catch (ClientNotEligibleForCreditException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + ex.getMessage());
        }
    }
}