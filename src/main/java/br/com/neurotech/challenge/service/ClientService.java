package br.com.neurotech.challenge.service;

import br.com.neurotech.challenge.controllers.NeurotechClientController;
import br.com.neurotech.challenge.converters.GenericConverter;
import br.com.neurotech.challenge.dtos.NeurotechClientDto;
import br.com.neurotech.challenge.exceptions.ClientNotFoundException;
import br.com.neurotech.challenge.repositories.ClientRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.neurotech.challenge.entity.NeurotechClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ClientService {

	private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

	private final ClientRepository repository;
	private final GenericConverter<NeurotechClientDto, NeurotechClient> converter;

	public ClientService(ClientRepository repository, GenericConverter<NeurotechClientDto, NeurotechClient> converter) {
		this.repository = repository;
		this.converter = converter;
	}

	public List<NeurotechClientDto> findAll() {
		try {
			logger.info("Iniciando a busca de todos os clientes.");
			List<NeurotechClient> clients = repository.findAll();
			logger.info("Clientes encontrados: {}", clients.size());
			return clients.stream()
					.map(converter::convertToDto)
					.collect(Collectors.toList());
		} catch (Exception ex) {
			logger.error("Erro ao buscar clientes: {}", ex.getMessage(), ex);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar clientes no banco de dados", ex);
		}
	}

	public NeurotechClientDto findById(Long id) {
		try {
			logger.info("Buscando cliente com ID: {}", id);
			NeurotechClient client = repository.findById(id)
					.orElseThrow(() -> new ClientNotFoundException("Cliente com ID " + id + " não encontrado."));
			NeurotechClientDto dto = converter.convertToDto(client);
			logger.info("Cliente com ID {} encontrado.", id);
			return dto;
		} catch (Exception ex) {
			logger.error("Erro ao buscar cliente por ID {}: {}", id, ex.getMessage(), ex);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar cliente por ID", ex);
		}
	}

	@Transactional
	public NeurotechClientDto create(@Valid NeurotechClientDto clientDto) {
		try {
			logger.info("Criando um novo cliente.");
			NeurotechClient client = converter.convertToModel(clientDto);
			NeurotechClient savedClient = repository.save(client);
			NeurotechClientDto dto = converter.convertToDto(savedClient);
			logger.info("Cliente criado com sucesso: {}", savedClient.getId());
			return dto;
		} catch (Exception ex) {
			logger.error("Erro ao salvar cliente: {}", ex.getMessage(), ex);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar cliente no banco de dados", ex);
		}
	}

	@Transactional
	public NeurotechClientDto update(Long id, @Valid NeurotechClientDto clientDto) {
		try {
			logger.info("Atualizando cliente com ID: {}", id);
			NeurotechClient client = converter.convertToModel(clientDto);

			NeurotechClient existingClient = repository.findById(id)
					.orElseThrow(() -> new ClientNotFoundException("Cliente com ID " + id + " não encontrado."));

			existingClient.setName(client.getName());
			existingClient.setAge(client.getAge());
			existingClient.setIncome(client.getIncome());

			NeurotechClient updatedClient = repository.save(existingClient);
			NeurotechClientDto dto = converter.convertToDto(updatedClient);
			logger.info("Cliente com ID {} atualizado com sucesso.", id);
			return dto;
		} catch (Exception ex) {
			logger.error("Erro ao atualizar cliente com ID {}: {}", id, ex.getMessage(), ex);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar cliente no banco de dados", ex);
		}
	}

	@Transactional
	public void delete(Long id) {
		try {
			logger.info("Deletando cliente com ID: {}", id);
			if (!repository.existsById(id)) {
				throw new ClientNotFoundException("Cliente com ID " + id + " não encontrado.");
			}
			repository.deleteById(id);
			logger.info("Cliente com ID {} deletado com sucesso.", id);
		} catch (Exception ex) {
			logger.error("Erro ao excluir cliente com ID {}: {}", id, ex.getMessage(), ex);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao excluir cliente no banco de dados", ex);
		}
	}

	public Map<String, String> getHateoasLinks(Long id) {
		String selfLink = linkTo(methodOn(NeurotechClientController.class).getClientById(id)).withSelfRel().getHref();
		return Map.of("self", selfLink);
	}
}
