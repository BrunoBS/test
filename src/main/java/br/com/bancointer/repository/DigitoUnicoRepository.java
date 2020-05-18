package br.com.bancointer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.com.bancointer.model.DigitoUnico;

public interface DigitoUnicoRepository extends PagingAndSortingRepository<DigitoUnico, Long> {

	List<DigitoUnico> findByUsuarioId(Long id);

	@Modifying
	@Query("DELETE from DigitoUnico WHERE usuario.id = :usuario")
	public void deleteDigitoUnicoUsuario(@Param("usuario") Long id);
}
