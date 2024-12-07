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
import java.util.UUID;

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
        return ResponseEntity.ok(service.findAll());
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
        return ResponseEntity.ok(service.findById(id));
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
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(client));
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

        return ResponseEntity.ok(service.update(id, client));
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
        return ResponseEntity.noContent().build();
    }
}
