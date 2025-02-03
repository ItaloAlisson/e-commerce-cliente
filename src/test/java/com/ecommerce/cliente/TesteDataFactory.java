package com.ecommerce.cliente;

import com.ecommerce.cliente.dtos.ClienteRecordDTO;
import com.ecommerce.cliente.dtos.ClienteStatusRecordDTO;
import com.ecommerce.cliente.dtos.EnderecoRecordDTO;
import com.ecommerce.cliente.embedded.Endereco;
import com.ecommerce.cliente.models.ClienteModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TesteDataFactory {

    public static List<ClienteRecordDTO> iniciarClienteDTO() {

        List<ClienteRecordDTO> clientesRecordDTO = new ArrayList<>();

        var enderecoDTO = new EnderecoRecordDTO(
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "São Paulo",
                "SP",
                "01310-100"
        );

       var clienteDTO = new ClienteRecordDTO(
               "Rodrigo Alves",
                LocalDate.of(2002, 02, 07),
                "teste@gmail.com",
                "745.303.692-50",
                enderecoDTO);

        var enderecoDTOAtualizado = new EnderecoRecordDTO(
                "Rua dos Três Poderes",
                "2045",
                "Vila Mariana",
                "Rio de Janeiro",
                "RJ",
                "22040-200"
        );

        var clienteDTOAtualizado = new ClienteRecordDTO(
                "Rodrigo Alves",
                LocalDate.of(2002, 02, 07),
                "rodrigo@hotmail.com",
                "745.303.692-50",
                enderecoDTOAtualizado);

        clientesRecordDTO.add(clienteDTO);
        clientesRecordDTO.add(clienteDTOAtualizado);
        return clientesRecordDTO;
    }

    public static ClienteStatusRecordDTO iniciarClienteStatusRecordDTO(){
       return new ClienteStatusRecordDTO(false);

    }

    public static List<ClienteModel> inciarClientesAtivos() {

        List<ClienteModel> clientes = new ArrayList<>();

        var endereco01 = new Endereco(
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "São Paulo",
                "SP",
                "01310-100"
        );

        var cliente01 = new ClienteModel(UUID.fromString("7ecc1e5b-846c-4e64-ac61-a54b2656e1b3"),
                "Rodrigo Alves",
                LocalDate.of(2002, 02, 07),
                "teste@gmail.com",
                "745.303.692-50",
                endereco01,
                true);

        var endereco02 = new Endereco(
                "Rua das Flores",
                "250",
                "Jardim Primavera",
                "Curitiba",
                "PR",
                "80530-200"
        );

        var cliente02 = new ClienteModel(UUID.fromString("970c6f82-f78a-4a2a-bd6f-37f2a5b243a7"),
                "José Carlos",
                LocalDate.of(1970, 10, 14),
                "teste2@hotmail.com",
                "594.642.567-61",
                endereco02,
                true);

        var enderecoAtualizado = new Endereco(
                "Rua dos Três Poderes",
                "2045",
                "Vila Mariana",
                "Rio de Janeiro",
                "RJ",
                "22040-200"
        );

        var clienteAtualizado = new ClienteModel(UUID.fromString("7ecc1e5b-846c-4e64-ac61-a54b2656e1b3"),
                "Rodrigo Alves",
                LocalDate.of(2002, 02, 07),
                "rodrigo@hotmail.com",
                "745.303.692-50",
                enderecoAtualizado,
                true);

        clientes.add(cliente01);
        clientes.add(cliente02);
        clientes.add(clienteAtualizado);
        return clientes;

    }
    public static List<ClienteModel> inciarClientesInativos() {

        List<ClienteModel> clientes = new ArrayList<>();

        var endereco01 = new Endereco(
                "Rua dos Três Irmãos",
                "472",
                "Centro",
                "Belo Horizonte",
                "MG",
                "30130-150"
        );

        var cliente01 = new ClienteModel(UUID.fromString("1e478fc6-51c4-4e76-b127-b47bd1a33791"),
                "Lucas Silva",
                LocalDate.of(1995, 06, 22),
                "teste1@gmail.com",
                "123.456.789-01",
                endereco01, false);

        var endereco02 = new Endereco(
                "Avenida Rio Branco",
                "890",
                "Centro",
                "Rio de Janeiro",
                "RJ",
                "20040-003"
        );

        var cliente02 = new ClienteModel(UUID.fromString("037f9b53-de34-4fa6-a21c-275d9f03cdd9"),
                "Maria Fernanda",
                LocalDate.of(1985, 12, 03),
                "test2@gmail.com",
                "987.654.321-00",
                endereco02,
                false);

        clientes.add(cliente01);
        clientes.add(cliente02);
        return clientes;

    }
}
