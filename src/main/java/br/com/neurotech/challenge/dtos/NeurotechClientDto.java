package br.com.neurotech.challenge.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class NeurotechClientDto extends RepresentationModel<NeurotechClientDto> implements Serializable {


    private Long key;
    private String name;
    private Integer age;
    private Double income;

}