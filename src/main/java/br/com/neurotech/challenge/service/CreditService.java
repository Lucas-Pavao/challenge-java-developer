package br.com.neurotech.challenge.service;

import br.com.neurotech.challenge.dtos.CreditCheckResponseDto;
import br.com.neurotech.challenge.entity.NeurotechClient;
import br.com.neurotech.challenge.entity.VehicleModel;
import br.com.neurotech.challenge.exceptions.ClientNotEligibleForCreditException;
import br.com.neurotech.challenge.repositories.ClientRepository;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CreditService {

	private final ClientRepository repository;

	public CreditService(ClientRepository repository) {
		this.repository = repository;
	}

	public CreditCheckResponseDto checkCredit(Long clientId, VehicleModel model) {
		try {
			NeurotechClient client = repository.findById(clientId)
					.orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

			boolean isEligible;
			String message = "Cliente elegível para crédito.";

			switch (model) {
				case HATCH:
					isEligible = isEligibleForHatch(client);
					if (!isEligible) {
						message = "Cliente não é elegível para crédito para veículo do tipo Hatch.";
					}
					break;

				case SUV:
					isEligible = isEligibleForSuv(client);
					if (!isEligible) {
						message = "Cliente não é elegível para crédito para veículo do tipo SUV.";
					}
					break;

				default:
					throw new IllegalArgumentException("Modelo de veículo desconhecido.");
			}

			CreditCheckResponseDto response = new CreditCheckResponseDto(
					client.getId(),
					client.getName(),
					model.name(),
					isEligible,
					message
			);

			return response;

		} catch (Exception ex) {
			throw new RuntimeException("Erro ao verificar crédito.", ex);
		}
	}

	private boolean isEligibleForHatch(NeurotechClient client) {
		return client.getIncome() >= 5000.00 && client.getIncome() <= 15000.00;
	}

	private boolean isEligibleForSuv(NeurotechClient client) {
		return client.getIncome() > 8000.00 && client.getAge() > 20;
	}
}
