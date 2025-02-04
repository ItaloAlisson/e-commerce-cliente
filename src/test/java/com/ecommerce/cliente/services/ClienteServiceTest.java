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

import static com.ecommerce.cliente.TesteDataFactory.*;
import static com.ecommerce.cliente.TesteDataFactory.iniciarClienteStatusRecordDTO;
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

    private List<ClienteModel> clientes;
    private List<ClienteModel> clientesInativos;
    private List<ClienteRecordDTO> clienteDTO;
    private ClienteStatusRecordDTO clienteStatusDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientes = inciarClientesAtivos();
        clientesInativos = inciarClientesInativos();
        clienteDTO = iniciarClienteDTO();
        clienteStatusDTO = iniciarClienteStatusRecordDTO();
    }

    @DisplayName(" Quando registrar o cliente" +
            "então retornar o cliente registrado")
    @Test
    void quandoRegistrarCliente_EntaoRetornarClienteRegistrado() {

        doNothing().when(validator).existePorCpf(clienteDTO.get(0).cpf());
        doNothing().when(validator).existePorEmail(clienteDTO.get(0).email());
        when(mapper.clienteDTOParaModel(clienteDTO.get(0))).thenReturn(clientes.get(0));
        when(mapper.enderecoDTOParaEndereco(clienteDTO.get(0).endereco()))
                .thenReturn(clientes.get(0).getEndereco());
        when(clienteRepository.save(any(ClienteModel.class))).thenReturn(clientes.get(0));
        var resultado = clienteService.registrarCliente(clienteDTO.get(0));

        assertNotNull(resultado);
        verify(validator).existePorCpf(clienteDTO.get(0).cpf());
        verify(validator).existePorEmail(clienteDTO.get(0).email());
        verify(clienteRepository).save(clientes.get(0));
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
            "            então retornar clientes")
    @Test
    void quandoBuscarClientesAtivos_EntaoRetornarClientes() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<ClienteModel> paginaClientes = new PageImpl<>(List.of(clientes.get(0),
                clientes.get(1)), pageable, 2);

        when(clienteRepository.findByAtivoTrue(any(Pageable.class))).thenReturn(paginaClientes);

        var resultado = clienteService.buscarClientesAtivos(pageable);

        assertNotNull(resultado);
        verify(clienteRepository).findByAtivoTrue(pageable);
    }

    @DisplayName("Quando buscar o cliente ativo por cpf" +
            "            então retornar cliente")
    @Test
    void quandoBuscarClienteAtivoPorCpf_EntaoRetornarCliente() {

        when(clienteRepository.findByCpfAndAtivoTrue("745.303.692-50")).thenReturn(Optional.ofNullable(clientes.get(0)));

        var resultado = clienteService.buscarClienteAtivoPorCpf("745.303.692-50");

        assertNotNull(resultado);
        assertEquals("745.303.692-50", resultado.getCpf());
        verify(clienteRepository).findByCpfAndAtivoTrue("745.303.692-50");
    }

    @DisplayName("Quando buscar o cliente ativo por cpf inexistente" +
            "            então lançar ResourceNotFoundException")
    @Test
    void quandoBuscarClienteAtivoPorCpfInexistente_EntaoLancarResourceNotFoundException() {

        when(clienteRepository.findByCpfAndAtivoTrue("745.303.692-50")).thenReturn(Optional.empty());

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> clienteService.buscarClienteAtivoPorCpf("745.303.692-50"));

        assertEquals("Cliente com o CPF " +
                "745.303.692-50" +
                " não foi encontrado.", exception.getMessage());
        verify(clienteRepository).findByCpfAndAtivoTrue("745.303.692-50");
    }

    @DisplayName("Quando buscar clientes inativos" +
            "            então retornar clientes")
    @Test
    void quandoBuscarClientesInativos_EntaoRetornarClientes() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<ClienteModel> paginaClientes = new PageImpl<>(List.of(clientesInativos.get(0),
                clientesInativos.get(1)), pageable, 2);

        when(clienteRepository.findByAtivoFalse(any(Pageable.class))).thenReturn(paginaClientes);

        var resultado = clienteService.buscarClientesInativos(pageable);

        assertNotNull(resultado);
        verify(clienteRepository).findByAtivoFalse(pageable);
    }

    @DisplayName("Quando buscar o cliente inativo por cpf" +
            "então retornar cliente")
    @Test
    void quandoBuscarClienteInativoPorCpf_EntaoRetornarCliente() {

        when(clienteRepository.findByCpfAndAtivoFalse("123.456.789-01")).thenReturn(Optional.ofNullable(clientesInativos.get(0)));

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




}