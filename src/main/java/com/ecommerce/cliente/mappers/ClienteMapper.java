package com.ecommerce.cliente.mappers;

import com.ecommerce.cliente.dtos.ClienteRecordDTO;
import com.ecommerce.cliente.dtos.EnderecoRecordDTO;
import com.ecommerce.cliente.embedded.Endereco;
import com.ecommerce.cliente.models.ClienteModel;
import org.mapstruct.Mapper;



@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteModel clienteDTOParaModel(ClienteRecordDTO clienteDTO);

    Endereco enderecoDTOParaEndereco(EnderecoRecordDTO enderecoDTO);


}
