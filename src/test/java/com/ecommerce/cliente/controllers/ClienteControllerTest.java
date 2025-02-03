package com.ecommerce.cliente.controllers;

import com.ecommerce.cliente.dtos.ClienteRecordDTO;
import com.ecommerce.cliente.dtos.ClienteStatusRecordDTO;
import com.ecommerce.cliente.exceptions.ResourceNotFoundException;
import com.ecommerce.cliente.models.ClienteModel;
import com.ecommerce.cliente.services.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.ecommerce.cliente.TesteDataFactory.*;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ClienteControllerTest {

    @Autowired
    private MockMvc mock;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private ClienteService clienteService;

    private List<ClienteModel> clientes;
    private List<ClienteModel> clientesInativos;
    private List<ClienteRecordDTO> clienteDTO;
    private ClienteStatusRecordDTO clienteStatusDTO;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        clientes = inciarClientesAtivos();
        clientesInativos = inciarClientesInativos();
        clienteDTO = iniciarClienteDTO();
        clienteStatusDTO = iniciarClienteStatusRecordDTO();
    }


    @DisplayName(" Quando registrar um cliente, " +
            "então retornar cliente registrado com http status 201")
    @Test
    void quandoRegistrarCliente_EntaoRetornarClienteRegistradoComHttpStatus201() throws Exception {

        when(clienteService.registrarCliente(any(ClienteRecordDTO.class))).thenReturn(clientes.get(0));

        ResultActions resultado = mock.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(clienteDTO.get(0))));

        resultado.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Rodrigo Alves"))
                .andExpect(jsonPath("$.cpf").value("745.303.692-50"))
                .andExpect(jsonPath("$.email").value("teste@gmail.com"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @DisplayName(" Quando buscar clientes ativos, " +
            "então retornar clientes com http status 200")
    @Test
    void quandoBuscarClientesAtivos_EntaoRetornarClientesComHttpStatus200() throws Exception {

        Pageable pageable = PageRequest.of(0, 10);
        Page<ClienteModel> paginaClientes = new PageImpl<>(List.of(clientes.get(0),
                clientes.get(1)), pageable, 2);

        when(clienteService.buscarClientesAtivos(any(Pageable.class))).thenReturn(paginaClientes);

        ResultActions resultado = mock.perform(get("/clientes")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON));

        resultado.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @DisplayName(" Quando buscar um cliente ativo por cpf, " +
            "então retornar cliente com http status 200")
    @Test
    void quandoBuscarClienteAtivoPorCpf_EntaoRetornarClienteComHttpStatus200() throws Exception {

        when(clienteService.buscarClienteAtivoPorCpf("745.303.692-50"))
                .thenReturn(clientes.get(0));

        ResultActions resultado = mock.perform(get("/clientes/745.303.692-50")
                .contentType(MediaType.APPLICATION_JSON));

        resultado.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Rodrigo Alves"))
                .andExpect(jsonPath("$.cpf").value("745.303.692-50"))
                .andExpect(jsonPath("$.email").value("teste@gmail.com"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @DisplayName(" Quando buscar clientes inativos, " +
            "então retornar clientes com http status 200")
    @Test
    void quandoBuscarClientesInativos_EntaoRetornarClientesComHttpStatus200() throws Exception {

        Pageable pageable = PageRequest.of(0, 10);
        Page<ClienteModel> paginaClientes = new PageImpl<>(List.of(clientesInativos.get(0),
                clientesInativos.get(1)), pageable, 2);

        when(clienteService.buscarClientesInativos(any(Pageable.class)))
                .thenReturn(paginaClientes);

        ResultActions resultado = mock.perform(get("/clientes/inativos")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON));

        resultado.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @DisplayName(" Quando buscar um cliente inativo por cpf, " +
            "então retornar cliente com http status 200")
    @Test
    void quandoBuscarClienteInativoPorCpf_EntaoRetornarClienteComHttpStatus200() throws Exception {

        when(clienteService.buscarClienteInativoPorCpf("123.456.789-01")).thenReturn(clientesInativos.get(0));

        ResultActions resultado = mock.perform(
                get("/clientes/inativo/123.456.789-01")
                .contentType(MediaType.APPLICATION_JSON));

        resultado.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Lucas Silva"))
                .andExpect(jsonPath("$.cpf").value("123.456.789-01"))
                .andExpect(jsonPath("$.email").value("teste1@gmail.com"))
                .andExpect(jsonPath("$.ativo").value(false));
    }

    @DisplayName(" Quando atualizar dados do cliente, " +
            "então retornar cliente atualizado com http status 200")
    @Test
    void quandoAtualizarDadosCliente_EntaoRetornarClienteAtualizadoComHttpStatus200() throws Exception {

        when(clienteService.atualizarDadosCliente(any(UUID.class),
                any(ClienteRecordDTO.class))).thenReturn(clientes.get(2));

        ResultActions resultado = mock.perform(
                put("/clientes/7ecc1e5b-846c-4e64-ac61-a54b2656e1b3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(clienteDTO.get(1))));

        resultado.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Rodrigo Alves"))
                .andExpect(jsonPath("$.cpf").value("745.303.692-50"))
                .andExpect(jsonPath("$.email").value("rodrigo@hotmail.com"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @DisplayName(" Quando atualizar status do cliente, " +
            "então retornar http status 204")
    @Test
    void quandoAtualizarStatusCliente_EntaoRetornarHttpStatus204() throws Exception {

        doNothing().when(clienteService).alternarStatusCliente(any(UUID.class),
                any(ClienteStatusRecordDTO.class));
        ResultActions resultado = mock.perform(
                patch("/clientes/7ecc1e5b-846c-4e64-ac61-a54b2656e1b3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(clienteStatusDTO)));

        resultado.andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName(" Quando deletar o cliente, " +
            "então retornar http status 204")
    @Test
    void quandoDeletarCliente_EntaoRetornarHttpStatus204() throws Exception {

        doNothing().when(clienteService).deletarCliente(any(UUID.class));

        ResultActions resultado = mock.perform(
                delete("/clientes/7ecc1e5b-846c-4e64-ac61-a54b2656e1b3"));

        resultado.andDo(print())
                .andExpect(status().isNoContent());
    }


}