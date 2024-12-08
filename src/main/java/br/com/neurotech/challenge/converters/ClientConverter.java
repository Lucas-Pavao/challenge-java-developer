package br.com.neurotech.challenge.converters;

import br.com.neurotech.challenge.dtos.NeurotechClientDto;
import br.com.neurotech.challenge.entity.NeurotechClient;
import org.springframework.stereotype.Component;

@Component
public class ClientConverter implements GenericConverter<NeurotechClientDto, NeurotechClient>  {
    @Override
    public  NeurotechClientDto convertToDto(NeurotechClient clientModel) {
        if (clientModel == null) {
            return null;
        }
        return new NeurotechClientDto(
                clientModel.getId(),
                clientModel.getName(),
                clientModel.getAge(),
                clientModel.getIncome()
        );
    }

    @Override
    public  NeurotechClient convertToModel(NeurotechClientDto clientDto) {
        if (clientDto == null) {
            return null;
        }
        return NeurotechClient.builder()
                .name(clientDto.getName())
                .age(clientDto.getAge())
                .income(clientDto.getIncome())
                .build();
    }
}
