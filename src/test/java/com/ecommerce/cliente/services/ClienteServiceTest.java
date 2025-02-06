package com.ecommerce.cliente.services;

import com.ecommerce.cliente.dtos.ClienteRecordDTO;
import com.ecommerce.cliente.dtos.ClienteStatusRecordDTO;
import com.ecommerce.cliente.exceptions.ConflictException;
import com.ecommerce.cliente.exceptions.ResourceNotFoundException;
import com.ecommerce.cliente.mappers.ClienteMapper;
import com.ecommerce.cliente.models.ClienteModel;
import com.ecommerce.cliente.repositories.ClienteRepository;
import com.ecommerce.cliente.validation.ClienteValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ecommerce.cliente.TesteDataFactory.*;
import static com.ecommerce.cliente.TesteDataFactory.clienteStatusRecordDTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @InjectMocks
    ClienteService clienteService;
    @Mock
    ClienteRepository clienteRepository;
    @Mock
    private ClienteValidator validator;
    @Mock
    private ClienteMapper mapper;

    private List<ClienteModel> clientesDB;
    private List<ClienteModel> clientesParaPersistencia;
    private List<ClienteModel> clientesInativosDB;
    private List<ClienteRecordDTO> clienteDTO;
    private ClienteStatusRecordDTO clienteStatusDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientesDB = clientesAtivosDB();
        clientesParaPersistencia = clientesParaPersistencia();
        clientesInativosDB = clientesInativosDB();
        clienteDTO = clienteDTO();
        clienteStatusDTO = clienteStatusRecordDTO();
    }

    @DisplayName(" Quando registrar o cliente" +
            "então retornar o cliente registrado")
    @Test
    void quandoRegistrarCliente_EntaoRetornarClienteRegistrado() {

        doNothing().when(validator).existePorCpf(clienteDTO.get(0).cpf());
        doNothing().when(validator).existePorEmail(clienteDTO.get(0).email());
        when(mapper.clienteDTOParaModel(clienteDTO.get(0))).thenReturn(clientesParaPersistencia.get(0));
        when(mapper.enderecoDTOParaEndereco(clienteDTO.get(0).endereco()))
                .thenReturn(clientesParaPersistencia.get(0).getEndereco());
        when(clienteRepository.save(clientesParaPersistencia.get(0))).thenReturn(clientesDB.get(0));
        var resultado = clienteService.registrarCliente(clienteDTO.get(0));

        assertNotNull(resultado);
        verify(validator).existePorCpf(clienteDTO.get(0).cpf());
        verify(validator).existePorEmail(clienteDTO.get(0).email());
        verify(clienteRepository).save(clientesParaPersistencia.get(0));
    }

    @DisplayName(" Quando registrar o cliente com cpf existente" +
            "então lançar ConflictException")
    @Test
    void quandoRegistrarClienteCpfExistente_EntaoLancarConflictException() {

        doThrow(new ConflictException("CPF " +
                clienteDTO.get(0).cpf() + " já cadastrado!"))
                .when(validator).existePorCpf(clienteDTO.get(0).cpf());

        var exception = assertThrows(ConflictException.class,
                () -> clienteService.registrarCliente(clienteDTO.get(0)));

        assertEquals("CPF " +
                clienteDTO.get(0).cpf()
                + " já cadastrado!", exception.getMessage());
        verify(validator).existePorCpf(clienteDTO.get(0).cpf());
    }

    @DisplayName(" Quando registrar o cliente com e-mail existente" +
            "então lançar ConflictException")
    @Test
    void quandoRegistrarClienteEmailExistente_EntaoLancarConflictException() {

        doThrow(new ConflictException("E-mail " +
                clienteDTO.get(0).email() + " já cadastrado!"))
                .when(validator).existePorEmail(clienteDTO.get(0).email());

        var exception = assertThrows(ConflictException.class,
                () -> clienteService.registrarCliente(clienteDTO.get(0)));

        assertEquals("E-mail " +
                clienteDTO.get(0).email()
                + " já cadastrado!", exception.getMessage());
        verify(validator).existePorEmail(clienteDTO.get(0).email());
    }

    @DisplayName("Quando buscar  clientes ativos" +
            "            então retornar clientesDB")
    @Test
    void quandoBuscarClientesAtivos_EntaoRetornarClientes() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<ClienteModel> paginaClientes = new PageImpl<>(List.of(clientesDB.get(0),
                clientesDB.get(1)), pageable, 2);

        when(clienteRepository.findByAtivoTrue(any(Pageable.class))).thenReturn(paginaClientes);

        var resultado = clienteService.buscarClientesAtivos(pageable);

        assertNotNull(resultado);
        verify(clienteRepository).findByAtivoTrue(pageable);
    }

    @DisplayName("Quando buscar o cliente ativo por cpf" +
            "            então retornar cliente")
    @Test
    void quandoBuscarClienteAtivoPorCpf_EntaoRetornarCliente() {

        when(clienteRepository.findByCpfAndAtivoTrue("745.303.692-50")).thenReturn(Optional.ofNullable(clientesDB.get(0)));

        var resultado = clienteService.buscarClienteAtivoPorCpf("745.303.692-50");

        assertNotNull(resultado);
        assertEquals("745.303.692-50", resultado.getCpf());
        verify(clienteRepository).findByCpfAndAtivoTrue("745.303.692-50");
    }

    @DisplayName("Quando buscar o cliente ativo por cpf inexistente" +
            "            então lançar ResourceNotFoundException")
    @Test
    void quandoBuscarClienteAtivoPorCpfInexistente_EntaoLancarResourceNotFoundException() {

        when(clienteRepository.findByCpfAndAtivoTrue("462.789.844-40")).thenReturn(Optional.empty());

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> clienteService.buscarClienteAtivoPorCpf("462.789.844-40"));

        assertEquals("Cliente com o CPF " +
                "462.789.844-40" +
                " não foi encontrado.", exception.getMessage());
        verify(clienteRepository).findByCpfAndAtivoTrue("462.789.844-40");
    }

    @DisplayName("Quando buscar clientes inativos" +
            "            então retornar clientesDB")
    @Test
    void quandoBuscarClientesInativos_EntaoRetornarClientes() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<ClienteModel> paginaClientes = new PageImpl<>(List.of(clientesInativosDB.get(0),
                clientesInativosDB.get(1)), pageable, 2);

        when(clienteRepository.findByAtivoFalse(any(Pageable.class))).thenReturn(paginaClientes);

        var resultado = clienteService.buscarClientesInativos(pageable);

        assertNotNull(resultado);
        verify(clienteRepository).findByAtivoFalse(pageable);
    }

    @DisplayName("Quando buscar o cliente inativo por cpf" +
            "então retornar cliente")
    @Test
    void quandoBuscarClienteInativoPorCpf_EntaoRetornarCliente() {

        when(clienteRepository.findByCpfAndAtivoFalse("123.456.789-01")).thenReturn(Optional.ofNullable(clientesInativosDB.get(0)));

        var resultado = clienteService.buscarClienteInativoPorCpf("123.456.789-01");

        assertNotNull(resultado);
        assertEquals("123.456.789-01", resultado.getCpf());
        verify(clienteRepository).findByCpfAndAtivoFalse("123.456.789-01");
    }

    @DisplayName("Quando buscar o cliente inativo por cpf inexistente" +
            "            então lançar ResourceNotFoundException")
    @Test
    void quandoBuscarClienteInativoPorCpfInexistente_EntaoLancarResourceNotFoundException() {

        when(clienteRepository.findByCpfAndAtivoFalse("123.456.789-01")).thenReturn(Optional.empty());

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> clienteService.buscarClienteInativoPorCpf("123.456.789-01"));

        assertEquals("Cliente com o CPF " +
                "123.456.789-01" +
                " não foi encontrado.", exception.getMessage());
        verify(clienteRepository).findByCpfAndAtivoFalse("123.456.789-01");
    }

    @DisplayName(" Quando atualizar dados do cliente" +
            "então retornar o cliente atualizado")
    @Test
    void quandoAtualizarDadosCliente_EntaoRetornarClienteAtualizado() {

        when(clienteRepository.findById(UUID.fromString("7ecc1e5b-846c-4e64-ac61-a54b2656e1b3")))
                .thenReturn(Optional.ofNullable(clientesDB.get(0)));
        when(clienteRepository.save(any(ClienteModel.class))).thenReturn(clientesDB.get(2));

        var resultado = clienteService.atualizarDadosCliente(UUID.fromString(
                "7ecc1e5b-846c-4e64-ac61-a54b2656e1b3"),clienteDTO.get(1));

        assertNotNull(resultado);
        verify(clienteRepository).findById(UUID.fromString("7ecc1e5b-846c-4e64-ac61-a54b2656e1b3"));
        ArgumentCaptor<ClienteModel> captor = ArgumentCaptor.forClass(ClienteModel.class);
        verify(clienteRepository).save(captor.capture());
    }

    @DisplayName("Quando atualizar dados do cliente inexistente" +
            "            então lançar ResourceNotFoundException")
    @Test
    void quandoAtualizarDadosClienteInexistente_EntaoLancarResourceNotFoundException() {

        when(clienteRepository.findById(UUID.fromString("822fdfb3-02a7-4f57-b3e9-3925a3ab7865")))
                .thenReturn(Optional.empty());

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> clienteService.atualizarDadosCliente(UUID.fromString(
                        "822fdfb3-02a7-4f57-b3e9-3925a3ab7865"),clienteDTO.get(0)));

        assertEquals("Cliente com o ID " +
                "822fdfb3-02a7-4f57-b3e9-3925a3ab7865" +
                " não foi encontrado.", exception.getMessage());
        verify(clienteRepository).findById(UUID.fromString("822fdfb3-02a7-4f57-b3e9-3925a3ab7865"));
    }

    @DisplayName(" Deve alternar o status do cliente")
    @Test
    void deveAlternarStatusCliente() {

        when(clienteRepository.findById(UUID.fromString("7ecc1e5b-846c-4e64-ac61-a54b2656e1b3")))
                .thenReturn(Optional.ofNullable(clientesDB.get(0)));

        clienteService.alternarStatusCliente(UUID.fromString(
                "7ecc1e5b-846c-4e64-ac61-a54b2656e1b3"),clienteStatusDTO);


        verify(clienteRepository).findById(UUID.fromString("7ecc1e5b-846c-4e64-ac61-a54b2656e1b3"));
       assertFalse(clientesDB.get(0).isAtivo());
    }

    @DisplayName("Quando alternar status do cliente inexistente" +
            "            então lançar ResourceNotFoundException")
    @Test
    void quandoAlternarStatusClienteInexistente_EntaoLancarResourceNotFoundException() {

        when(clienteRepository.findById(UUID.fromString("822fdfb3-02a7-4f57-b3e9-3925a3ab7865")))
                .thenReturn(Optional.empty());

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> clienteService.alternarStatusCliente(UUID.fromString(
                        "822fdfb3-02a7-4f57-b3e9-3925a3ab7865"),clienteStatusDTO));

        assertEquals("Cliente com o ID " +
                "822fdfb3-02a7-4f57-b3e9-3925a3ab7865" +
                " não foi encontrado.", exception.getMessage());
        verify(clienteRepository).findById(UUID.fromString("822fdfb3-02a7-4f57-b3e9-3925a3ab7865"));
    }

    @DisplayName(" Deve deletar o cliente")
    @Test
    void deveDeletarCliente() {

        when(clienteRepository.findById(UUID.fromString("7ecc1e5b-846c-4e64-ac61-a54b2656e1b3")))
                .thenReturn(Optional.ofNullable(clientesDB.get(0)));

        clienteService.deletarCliente(UUID.fromString(
                "7ecc1e5b-846c-4e64-ac61-a54b2656e1b3"));


        verify(clienteRepository).findById(UUID.fromString("7ecc1e5b-846c-4e64-ac61-a54b2656e1b3"));
        verify(clienteRepository).delete(clientesDB.get(0));
    }

    @DisplayName("Quando deletar o cliente inexistente" +
            "            então lançar ResourceNotFoundException")
    @Test
    void quandoDeletarClienteInexistente_EntaoLancarResourceNotFoundException() {

        when(clienteRepository.findById(UUID.fromString("822fdfb3-02a7-4f57-b3e9-3925a3ab7865")))
                .thenReturn(Optional.empty());

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> clienteService.deletarCliente(UUID.fromString(
                        "822fdfb3-02a7-4f57-b3e9-3925a3ab7865")));

        assertEquals("Cliente com o ID " +
                "822fdfb3-02a7-4f57-b3e9-3925a3ab7865" +
                " não foi encontrado.", exception.getMessage());
        verify(clienteRepository).findById(UUID.fromString("822fdfb3-02a7-4f57-b3e9-3925a3ab7865"));
    }




}