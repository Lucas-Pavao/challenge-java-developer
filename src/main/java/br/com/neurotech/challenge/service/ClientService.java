package br.com.neurotech.challenge.service;

import br.com.neurotech.challenge.converters.GenericConverter;
import br.com.neurotech.challenge.dtos.NeurotechClientDto;
import br.com.neurotech.challenge.exceptions.ClientNotFoundException;
import br.com.neurotech.challenge.repositories.ClientRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.neurotech.challenge.entity.NeurotechClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

	private final ClientRepository repository;
	private final GenericConverter<NeurotechClientDto, NeurotechClient> converter;

	public ClientService(ClientRepository repository, GenericConverter<NeurotechClientDto, NeurotechClient> converter) {
		this.repository = repository;
        this.converter = converter;
    }

	public List<NeurotechClientDto> findAll() {
		try {
			List<NeurotechClient> clients = repository.findAll();
			return clients.stream()
					.map(client -> converter.convertToDto(client))
					.collect(Collectors.toList());
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar clientes no banco de dados", ex);
		}
	}

	public NeurotechClientDto findById(Long id) {
		try {
			NeurotechClient client = repository.findById(id)
					.orElseThrow(() -> new ClientNotFoundException("Cliente com ID " + id + " não encontrado."));
			return converter.convertToDto(client);
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar cliente por ID", ex);
		}
	}

	@Transactional
	public NeurotechClientDto create(@Valid NeurotechClientDto clientDto) {
		try {
			NeurotechClient client = converter.convertToModel(clientDto);
			NeurotechClient savedClient = repository.save(client);
			return converter.convertToDto(savedClient);
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar cliente no banco de dados", ex);
		}
	}

	@Transactional
	public NeurotechClientDto update(Long id, @Valid NeurotechClientDto clientDto) {
		try {
			NeurotechClient client = converter.convertToModel(clientDto);

			NeurotechClient existingClient = repository.findById(id)
					.orElseThrow(() -> new ClientNotFoundException("Cliente com ID " + id + " não encontrado."));

			existingClient.setName(client.getName());
			existingClient.setAge(client.getAge());
			existingClient.setIncome(client.getIncome());

			NeurotechClient updatedClient = repository.save(existingClient);
			return converter.convertToDto(updatedClient);
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar cliente no banco de dados", ex);
		}
	}

	@Transactional
	public void delete(Long id) {
		try {
			if (!repository.existsById(id)) {
				throw new ClientNotFoundException("Cliente com ID " + id + " não encontrado.");
			}
			repository.deleteById(id);
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao excluir cliente no banco de dados", ex);
		}
	}
}
