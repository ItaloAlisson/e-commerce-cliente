package com.ecommerce.cliente.validation;

import com.ecommerce.cliente.dtos.ClienteRecordDTO;
import com.ecommerce.cliente.exceptions.ConflictException;
import com.ecommerce.cliente.repositories.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.ecommerce.cliente.TesteDataFactory.clienteDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteValidatorTest {

    @InjectMocks
    ClienteValidator clienteValidator;
    @Mock
    ClienteRepository clienteRepository;

    private List<ClienteRecordDTO> clienteDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clienteDTO = clienteDTO();
    }

    @DisplayName(" Deve validar o cliente com cpf inexistente")
    @Test
    void deveValidarClienteCpfInexistente() {

        when(clienteRepository.existsByCpf("641.496.185-00")).thenReturn(false);

        clienteValidator.existePorCpf("641.496.185-00");

        verify(clienteRepository).existsByCpf("641.496.185-00");
    }

    @DisplayName(" Quando validar o cliente com cpf existente" +
            "então lançar ConflictException")
    @Test
    void quandoValidarClienteCpfExistente_EntaoLancarConflictException() {

        when(clienteRepository.existsByCpf(clienteDTO.get(0).cpf())).thenReturn(true);

        var exception = assertThrows(ConflictException.class,
                () -> clienteValidator.existePorCpf(clienteDTO.get(0).cpf()));

        assertEquals("CPF " +
                clienteDTO.get(0).cpf()
                + " já cadastrado!", exception.getMessage());
        verify(clienteRepository).existsByCpf(clienteDTO.get(0).cpf());
    }

    @DisplayName(" Deve validar o cliente com e-mail inexistente")
    @Test
    void DeveValidarClienteEmailInexistente() {


        when(clienteRepository.existsByEmail("robertoalmeida@teste.com")).thenReturn(false);

        clienteValidator.existePorEmail("robertoalmeida@teste.com");

        verify(clienteRepository).existsByEmail("robertoalmeida@teste.com");
    }

    @DisplayName(" Quando validar o cliente com e-mail existente" +
            "então lançar ConflictException")
    @Test
    void quandoValidarClienteEmailExistente_EntaoLancarConflictException() {


        when(clienteRepository.existsByEmail(clienteDTO.get(0).email())).thenReturn(true);

        var exception = assertThrows(ConflictException.class,
                () -> clienteValidator.existePorEmail(clienteDTO.get(0).email()));

        assertEquals("E-mail " +
                clienteDTO.get(0).email()
                + " já cadastrado!", exception.getMessage());
        verify(clienteRepository).existsByEmail(clienteDTO.get(0).email());
    }
}