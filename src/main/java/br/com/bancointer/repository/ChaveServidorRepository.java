package br.com.bancointer.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.bancointer.model.ChaveServidor;

public interface ChaveServidorRepository extends PagingAndSortingRepository<ChaveServidor, Long> {

	Optional<ChaveServidor> findTop1ByOrderByIdDesc();


}
		