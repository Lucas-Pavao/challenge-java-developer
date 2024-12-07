package br.com.neurotech.challenge.converters;

import org.springframework.stereotype.Component;

@Component
public interface GenericConverter<D, M> {
    D convertToDto(M model);

    M convertToModel(D dto);
}