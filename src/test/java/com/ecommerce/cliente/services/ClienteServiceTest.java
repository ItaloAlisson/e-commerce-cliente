package com.ecommerce.cliente.services;

import com.ecommerce.cliente.dtos.ClienteRecordDTO;
import com.ecommerce.cliente.dtos.ClienteStatusRecordDTO;
import com.ecommerce.cliente.exceptions.ConflictException;
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


}