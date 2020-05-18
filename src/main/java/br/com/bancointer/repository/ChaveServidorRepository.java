package br.com.bancointer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.com.bancointer.model.ChaveCliente;
import br.com.bancointer.model.ChaveServidor;

public interface ChaveServidorRepository extends PagingAndSortingRepository<ChaveServidor, Long> {

	Optional<ChaveServidor> findTop1ByOrderByIdDesc();


}
