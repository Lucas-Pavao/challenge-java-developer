package br.com.neurotech.challenge.service;

import br.com.neurotech.challenge.entity.NeurotechClient;
import br.com.neurotech.challenge.entity.VehicleModel;
import br.com.neurotech.challenge.exceptions.ClientNotEligibleForCreditException;
import br.com.neurotech.challenge.repositories.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class CreditService {

	private final ClientRepository repository;

	public CreditService(ClientRepository repository) {
		this.repository = repository;
	}

	public boolean checkCredit(Long clientId, VehicleModel model) {
		try {
			NeurotechClient client = repository.findById(clientId)
					.orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

			switch (model) {
				case HATCH:
					if (!isEligibleForHatch(client)) {
						// Lança exceção personalizada com mensagem
						throw new ClientNotEligibleForCreditException("Cliente não é elegível para crédito para veículo do tipo Hatch.");
					}
					return true;

				case SUV:
					if (!isEligibleForSuv(client)) {
						// Lança exceção personalizada com mensagem
						throw new ClientNotEligibleForCreditException("Cliente não é elegível para crédito para veículo do tipo SUV.");
					}
					return true;

				default:
					throw new IllegalArgumentException("Modelo de veículo desconhecido.");
			}
		} catch (ClientNotEligibleForCreditException ex) {
			// Aqui tratamos a exceção personalizada, evitando erro 500
			throw ex;  // Se necessário, podemos manipular e devolver uma resposta com status 400
		} catch (RuntimeException ex) {
			// Captura e relança exceções com mensagens mais significativas
			throw new RuntimeException("Erro ao verificar elegibilidade para crédito", ex);
		}
	}

	private boolean isEligibleForHatch(NeurotechClient client) {
		return client.getIncome() >= 5000.00 && client.getIncome() <= 15000.00;
	}

	private boolean isEligibleForSuv(NeurotechClient client) {
		return client.getIncome() > 8000.00 && client.getAge() > 20;
	}
}
