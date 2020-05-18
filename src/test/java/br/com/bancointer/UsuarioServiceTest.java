package br.com.bancointer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.bancointer.error.RSAException;
import br.com.bancointer.error.ResourceNotFoundException;
import br.com.bancointer.model.ChaveCliente;
import br.com.bancointer.model.ChaveServidor;
import br.com.bancointer.model.Usuario;
import br.com.bancointer.repository.ChaveClienteRepository;
import br.com.bancointer.repository.ChaveServidorRepository;
import br.com.bancointer.service.CriptografiaRSA;
import br.com.bancointer.service.UsuarioService;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestInstance(Lifecycle.PER_CLASS)
public class UsuarioServiceTest {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private ChaveServidorRepository chaveServidorRepository;

	@Autowired
	private ChaveClienteRepository chaveClienteRepository;

	@Autowired
	protected CriptografiaRSA criptografiaRSA;

	private ChaveServidor chaveServidor;

	@BeforeAll
	public void init() throws Exception {
		usuarioService.config();
		chaveServidor = chaveServidorRepository.findTop1ByOrderByIdDesc().get();

	}

	@Test
	public void buscaTodosUsuarios() throws Exception {
		Usuario user = criarUsuario();
		List<Usuario> listAll = this.usuarioService.listAll();
		assertEquals(1, listAll.stream().filter(u -> u.getId().equals(user.getId())).count());
	}

	@Test
	public void saveUsuarioTest() throws Exception {
		Usuario user = criarUsuario();
		assertThat(user.getId()).isNotNull();
		assertThat("Bruno").isEqualTo(user.getNome());
		assertThat("bruno.barbosa@bancointer.com.br").isEqualTo(user.getEmail());
	}

	@Test
	public void atualizaChavePublicaCliente() throws Exception {
		Usuario user = criarUsuario();
		PublicKey publica = criptografiaRSA.generateKeyPair().getPublic();
		String chave = criptografiaRSA.para(publica);
		usuarioService.atualizaChave(user, chave);
		Optional<ChaveCliente> ChaveSalva = chaveClienteRepository.findTop1ByUsuarioId(user.getId());
		if (ChaveSalva.isPresent()) {
			assertThat(chave).isEqualTo(ChaveSalva.get().getPublica());
		}

	}

	private Usuario criarUsuario() {
		Usuario user = criarUsuario("Bruno", "bruno.barbosa@bancointer.com.br");
		this.usuarioService.save(user);
		return user;
	}

	@Test
	public void removeUsuarioTest() {
		Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
			Usuario user = criarUsuario();
			usuarioService.delete(user.getId());
			usuarioService.find(user.getId());
		});

		assertTrue(exception.getMessage().contains("Usuário Não Encontrado com ID"));

	}

	@Test
	public void atualizaUsuarioTest() throws Exception {
		Usuario user = criarUsuario();

		KeyPair generateKeyPair = criptografiaRSA.generateKeyPair();
		PublicKey publica = generateKeyPair.getPublic();
		PrivateKey privada = generateKeyPair.getPrivate();
		String chave = criptografiaRSA.para(publica);
		usuarioService.atualizaChave(user, chave);
		user = usuarioService.find(user.getId());
		assertThat(user.getId()).isNotNull();
		assertThat("Bruno").isEqualTo(criptografiaRSA.decriptografa(user.getNome(), criptografiaRSA.para(privada)));

	}

	@Test
	public void salvarUsuarioSemNome() {
		Usuario user = criarUsuario(null, "bruno.barbosa@bancointer.com.br");
		assertThrows(RSAException.class, () -> usuarioService.save(user));

	}

	@Test
	public void salvarUsuarioSemEmail() {
		Usuario user = criarUsuario("Bruno", null);
		assertThrows(RSAException.class, () -> usuarioService.save(user));

	}

	private Usuario criarUsuario(String nome, String email) {
		Usuario user = new Usuario();
		try {
			user.setNome(criptografiaRSA.criptografa(nome, chaveServidor.getPublica()));
			user.setEmail(criptografiaRSA.criptografa(email, chaveServidor.getPublica()));
		} catch (Exception e) {

		}

		return user;
	}

	@Test
	public void buscasChaveDoUsuario() throws Exception {
		Usuario user = criarUsuario("Bruno","bruno.barbosa@bancointer.com.br");
		this.usuarioService.save(user);
		String chave = this.usuarioService.chave(user.getId()).getPublica();
		assertThat(user.getId()).isNotNull();

	}

}
