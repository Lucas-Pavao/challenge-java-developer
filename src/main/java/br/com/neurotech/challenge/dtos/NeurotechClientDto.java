package br.com.neurotech.challenge.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class NeurotechClientDto {

    private String name;
    private Integer age;
    private Double income;

}