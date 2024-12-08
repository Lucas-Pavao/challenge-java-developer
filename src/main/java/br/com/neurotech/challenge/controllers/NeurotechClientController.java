package br.com.neurotech.challenge.controllers;

import br.com.neurotech.challenge.dtos.NeurotechClientDto;
import br.com.neurotech.challenge.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/neurotech-clients")
@Tag(name = "Neurotech Clients", description = "Endpoints for managing Neurotech clients")
public class NeurotechClientController {

    private final ClientService service;

    public NeurotechClientController(ClientService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Find all clients", description = "Retrieve a list of all Neurotech clients",
            tags = {"Neurotech Clients"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = NeurotechClientDto.class)))) ,
            @ApiResponse(description = "Internal Error", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<List<NeurotechClientDto>> getAllClients() {
        List<NeurotechClientDto> clients = service.findAll();
        return ResponseEntity.ok()
                .header("Link", linkToSelfList()) // Link para a lista de clientes
                .body(clients);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a client by ID", description = "Retrieve client details by their ID",
            tags = {"Neurotech Clients"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = NeurotechClientDto.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<NeurotechClientDto> getClientById(@PathVariable Long id) {
        NeurotechClientDto clientDto = service.findById(id);
        Map<String, String> hateoasLinks = service.getHateoasLinks(id);

        return ResponseEntity.ok()
                .header("Link", hateoasLinks.get("self"))
                .body(clientDto);
    }

    @PostMapping
    @Operation(summary = "Create a client", description = "Add a new Neurotech client to the system",
            tags = {"Neurotech Clients"}, responses = {
            @ApiResponse(description = "Created", responseCode = "201",
                    content = @Content(schema = @Schema(implementation = NeurotechClientDto.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<NeurotechClientDto> createClient(@Valid @RequestBody NeurotechClientDto client) {
        NeurotechClientDto createdClient = service.create(client);
        Map<String, String> hateoasLinks = service.getHateoasLinks(createdClient.getKey());

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Link", hateoasLinks.get("self"))
                .body(createdClient);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a client", description = "Update details of an existing Neurotech client",
            tags = {"Neurotech Clients"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = NeurotechClientDto.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
            @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<NeurotechClientDto> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody NeurotechClientDto client) {

        NeurotechClientDto updatedClient = service.update(id, client);
        Map<String, String> hateoasLinks = service.getHateoasLinks(id);

        return ResponseEntity.ok()
                .header("Link", hateoasLinks.get("self"))
                .body(updatedClient);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a client by ID", description = "Remove a Neurotech client from the system by their ID",
            tags = {"Neurotech Clients"}, responses = {
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        service.delete(id);
        Map<String, String> hateoasLinks = service.getHateoasLinks(id);

        return ResponseEntity.noContent()
                .header("Link", hateoasLinks.get("self"))
                .build();
    }
    
    private String linkToSelfList() {
        return "/api/neurotech-clients";
    }
}
