package br.com.bancointer.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.bancointer.model.ChaveCliente;
import br.com.bancointer.model.DigitoUnico;
import br.com.bancointer.model.Usuario;
import br.com.bancointer.repository.DigitoUnicoRepository;
import br.com.bancointer.service.DigitoUnicoService;
import br.com.bancointer.service.AppService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("v1")
public class AppController {

	@Autowired
	protected AppService usuarioService;
	@Autowired
	protected DigitoUnicoService digitoUnicoService;

	@Autowired
	protected DigitoUnicoRepository digitoUnicoRepository;

	@GetMapping(path = "usuarios")
	@ApiOperation(value = "Retornar uma lista de todos os clientes", response = Usuario[].class)
	public ResponseEntity<?> findAll() {
		return new ResponseEntity<>(usuarioService.listAll(), HttpStatus.OK);
	}

	@GetMapping(path = "usuarios/{id}")
	@ApiOperation(value = "Localizar um cliente pelo seu ID", response = Usuario.class)
	public ResponseEntity<?> find(@PathVariable("id") Long id) {
		Usuario usuario = usuarioService.find(id);
		return new ResponseEntity<>(usuario, HttpStatus.OK);
	}

	@PostMapping(path = "usuarios")
	@ApiOperation(value = "Salvar um cliente", response = Usuario.class)
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> save(@Valid @RequestBody Usuario usuario) {
		return new ResponseEntity<>(usuarioService.save(usuario), HttpStatus.CREATED);
	}

	@DeleteMapping(path = "usuarios/{id}")
	@ApiOperation(value = "Apagar um cliente")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		usuarioService.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping(path = "usuarios")
	@ApiOperation(value = "Atualizar um cliente")
	@Transactional
	public ResponseEntity<?> update(@RequestBody Usuario usuario) {
		usuarioService.existeUsuario(usuario.getId());
		usuarioService.save(usuario);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping(path = "usuarios/{id}/chave")
	@ApiOperation(value = "Busca a chave pública do cliente", response = String.class)
	public ResponseEntity<?> getChave(@PathVariable("id") Long id) {
		ChaveCliente chave = usuarioService.chave(id);
		return new ResponseEntity<>(chave, HttpStatus.OK);
	}

	@PutMapping(path = "usuarios/{id}/chave")
	@ApiOperation(value = "Atualizar a chave pública do cliente", response = String.class)
	public ResponseEntity<?> atualizaChave(@RequestBody(required = true) String chavePublica,
			@PathVariable("id") Long id) {
		usuarioService.atualizaChave(id, chavePublica);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/init", method = RequestMethod.OPTIONS)
	@ApiOperation(value = "Configurar o par de chaves do servidor (Teste)", response = Usuario.class)
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> config() {
		usuarioService.config();
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/init/cifra", method = RequestMethod.OPTIONS)
	@ApiOperation(value = "Cifrar  dados do cliente para enviar para o servidor (Teste)", response = Usuario.class)
	public ResponseEntity<?> cifra(@RequestBody Usuario usuario) {
		Usuario userCifrado = usuarioService.cifra(usuario);
		return new ResponseEntity<>(userCifrado, HttpStatus.OK);
	}

	@PostMapping(path = { "/calculoDigito" })
	@ApiOperation(value = "Calcuar o Dígito único.", response = Usuario[].class)
	public DigitoUnico calcula(@RequestParam(name = "idUser", required = false) Long idUser,
			@RequestParam(name = "parametroUm") String parametroUm,
			@RequestParam(name = "parametroDois", required = false) String parametroDois) {

		DigitoUnico digitoUnico = digitoUnicoService.digitoUnico(parametroUm, parametroDois);
		if (idUser != null) {
			usuarioService.salvaDigitoUnico(digitoUnico, idUser);
		}

		return digitoUnico;

	}

	@GetMapping(path = "digitos/usuario/{id}")
	@ApiOperation(value = "Lista de todos Digitos calculado pelo usuário.", response = Usuario[].class)
	public ResponseEntity<?> findCauculos(@PathVariable("id") Long id) {
		usuarioService.existeUsuario(id);
		List<DigitoUnico> digitos = digitoUnicoRepository.findByUsuarioId(id);
		return new ResponseEntity<>(digitos, HttpStatus.OK);
	}

}
