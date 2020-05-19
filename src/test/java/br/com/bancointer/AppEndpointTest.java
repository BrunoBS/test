package br.com.bancointer;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import br.com.bancointer.model.ChaveCliente;
import br.com.bancointer.model.Usuario;
import br.com.bancointer.service.CriptografiaRSA;
import br.com.bancointer.service.AppService;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class AppEndpointTest {
	@Autowired
	private TestRestTemplate restTemplate;
	@LocalServerPort
	private int port;

	@MockBean
	private AppService usuarioService;

	@Autowired
	protected CriptografiaRSA criptografiaRSA;

	private KeyPair generateKeyPair;

	@TestConfiguration
	static class Config {
		@Bean
		public RestTemplateBuilder restTemplateBuilder() {
			return new RestTemplateBuilder();
		}
	}

	@BeforeAll
	public void setup() throws Exception {

		this.generateKeyPair = criptografiaRSA.generateKeyPair();
		Usuario user = new Usuario(1L, "Bruno", "bruno.barbosa@bancointer.com.br");
		this.usuarioService.save(user);

		BDDMockito.when(usuarioService.find(user.getId())).thenReturn(user);
	}

	@Test
	public void listaTodosOsUsuarios() {

		List<Usuario> students = Arrays.asList(new Usuario(2L, "Bruno", "bruno.barbosa@bancointer.com.br"),
				new Usuario(3L, "Bruno", "bruno.barbosa@bancointer.com.br"));
		BDDMockito.when(usuarioService.listAll()).thenReturn(students);

		ResponseEntity<String> response = restTemplate.getForEntity("/v1/usuarios", String.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);

	}

	@Test
	public void buscaUsuarioPorID() {
		ResponseEntity<Usuario> response = restTemplate.getForEntity("/v1/usuarios/{id}", Usuario.class, 1L);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);

	}

	@Test
	public void apagarUsuarioTest() {
		BDDMockito.doNothing().when(usuarioService).delete(1L);
		ResponseEntity<Usuario> response = restTemplate.exchange("/v1/usuarios/{id}", HttpMethod.DELETE, null,
				Usuario.class, 1L);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);

	}

	@Test
	public void salvaUmUsuario() {
		String chave = criptografiaRSA.para(generateKeyPair.getPublic());
		Usuario usuario = new Usuario(1L, "Bruno", "bruno.barbosa@bancointer.com.br");
		BDDMockito.when(usuarioService.save(usuario)).thenReturn(usuario);

		HttpHeaders headers = new HttpHeaders();
		String encoding3 = Base64.getEncoder().encodeToString(chave.getBytes(StandardCharsets.UTF_8));

		headers.set("keyPublica", encoding3);
		HttpEntity<Usuario> entity = new HttpEntity<Usuario>(usuario, headers);
		ResponseEntity<Usuario> response = restTemplate.postForEntity("/v1/usuarios/", entity, Usuario.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(201);

	} 

	@Test
	public void atualizaUmUsuario() {
		Usuario usuario = new Usuario(1L, "Bruno", "bruno.barbosa@bancointer.com.br");
		BDDMockito.when(usuarioService.save(usuario)).thenReturn(usuario);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Usuario> entity = new HttpEntity<Usuario>(usuario, headers);
		ResponseEntity<Usuario> response = restTemplate.exchange("/v1/usuarios/", HttpMethod.PUT, entity, Usuario.class,
				usuario);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);

	}

	@Test
	public void buscaChave() {

		ChaveCliente chave = new ChaveCliente();
		chave.setPublica(criptografiaRSA.para(generateKeyPair.getPublic()));

		BDDMockito.when(usuarioService.chave(1L)).thenReturn(chave);
		ResponseEntity<ChaveCliente> response = restTemplate.getForEntity("/v1/usuarios/{id}/chave/",
				ChaveCliente.class, 1L);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(response.getBody().getPublica()).isEqualTo(chave.getPublica());

	}

}