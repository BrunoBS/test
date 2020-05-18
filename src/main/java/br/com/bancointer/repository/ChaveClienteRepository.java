package br.com.bancointer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.com.bancointer.model.ChaveCliente;

public interface ChaveClienteRepository extends PagingAndSortingRepository<ChaveCliente, Long> {

	Optional<ChaveCliente> findTop1ByUsuarioId(Long usuarioID);

	@Modifying
	@Query("DELETE from ChaveCliente WHERE usuario.id = :usuario")
	public void deleteChaveUsuario(@Param("usuario") Long id);

}
