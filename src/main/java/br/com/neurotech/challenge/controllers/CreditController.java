package br.com.neurotech.challenge.controllers;

import br.com.neurotech.challenge.dtos.CreditCheckResponseDto;
import br.com.neurotech.challenge.entity.VehicleModel;
import br.com.neurotech.challenge.exceptions.ClientNotEligibleForCreditException;
import br.com.neurotech.challenge.service.CreditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
                    @ApiResponse(description = "Eligible for credit", responseCode = "200", content = @Content(schema = @Schema(implementation = CreditCheckResponseDto.class))),
                    @ApiResponse(description = "Client not eligible for credit", responseCode = "400", content = @Content(schema = @Schema(type = "string", example = "Cliente não elegível para crédito."))),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<CreditCheckResponseDto> checkCredit(@PathVariable Long clientId, @RequestParam VehicleModel model) {
        try {
            CreditCheckResponseDto response = creditService.checkCredit(clientId, model);


            String link = linkTo(methodOn(CreditController.class)
                    .checkCredit(clientId, model))
                    .withSelfRel().toUri().toString();

            return ResponseEntity.ok()
                    .header("Location", link) // Adicionando o link no cabeçalho
                    .body(response);
        } catch (ClientNotEligibleForCreditException ex) {
            CreditCheckResponseDto response = new CreditCheckResponseDto(
                    clientId,
                    null,
                    model.name(),
                    false,
                    ex.getMessage()
            );


            String link = linkTo(methodOn(CreditController.class)
                    .checkCredit(clientId, model))
                    .withSelfRel().toUri().toString();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Location", link) // Adicionando o link no cabeçalho
                    .body(response);
        } catch (RuntimeException ex) {
            throw new RuntimeException("Erro inesperado ao verificar crédito: " + ex.getMessage(), ex);
        }
    }

    @GetMapping("/eligible-clients/hatch")
    @Operation(summary = "Find eligible clients for hatch",
            description = "Retrieve a list of clients eligible for Hatch vehicle credit with fixed interest rates.",
            tags = {"Credit"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Map.class)))),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = {@Content})
            })
    public ResponseEntity<List<Map<String, Object>>> getEligibleClientsForHatch() {
        List<Map<String, Object>> eligibleClients = creditService.findEligibleClientsForHatch();
        return ResponseEntity.ok()
                .header("Location", "/api/credit/eligible-clients/hatch")
                .body(eligibleClients);
    }
}
