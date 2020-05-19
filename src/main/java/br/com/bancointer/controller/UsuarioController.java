package br.com.bancointer.controller;

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
import org.springframework.web.bind.annotation.RestController;

import br.com.bancointer.model.ChaveCliente;
import br.com.bancointer.model.Usuario;
import br.com.bancointer.service.UsuarioService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("v1")
public class UsuarioController {

	@Autowired
	protected UsuarioService usuarioService;
	private Usuario cifra;

	@GetMapping(path = "usuarios")
	@ApiOperation(value = "Returna todos os usuários ", response = Usuario[].class)
	public ResponseEntity<?> findAll() {
		return new ResponseEntity<>(usuarioService.listAll(), HttpStatus.OK);
	}

	@GetMapping(path = "usuarios/{id}")
	@ApiOperation(value = "Localiza um usuário pelo ID", response = Usuario.class)
	public ResponseEntity<?> find(@PathVariable("id") Long id) {
		Usuario usuario = usuarioService.find(id);
		return new ResponseEntity<>(usuario, HttpStatus.OK);
	}

	@PostMapping(path = "usuarios")
	@ApiOperation(value = "Salva um usuário", response = Usuario.class)
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> save(@Valid @RequestBody Usuario usuario) {
		return new ResponseEntity<>(usuarioService.save(usuario), HttpStatus.CREATED);
	}

	@DeleteMapping(path = "usuarios/{id}")
	@ApiOperation(value = "Delete  um usuário")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		usuarioService.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping(path = "usuarios")
	@ApiOperation(value = "Atualiza um usuário")
	@Transactional
	public ResponseEntity<?> update(@RequestBody Usuario usuario) {
		usuarioService.existeUsuario(usuario.getId());
		usuarioService.save(usuario);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping(path = "usuarios/{id}/chave")
	@ApiOperation(value = "Busca a Chave Publica do Usuario", response = String.class)
	public ResponseEntity<?> getChave(@PathVariable("id") Long id) {
		ChaveCliente chave = usuarioService.chave(id);
		return new ResponseEntity<>(chave, HttpStatus.OK);
	}

	@PutMapping(path = "usuarios/{id}/chave")
	@ApiOperation(value = "Atualiza a Chave Publica do Usuario", response = String.class)
	public ResponseEntity<?> atualizaChave(@RequestBody(required = true) String chavePublica,
			@PathVariable("id") Long id) {
		Usuario user = usuarioService.find(id);
		usuarioService.atualizaChave(user, chavePublica);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	
	
	@RequestMapping(value = "/init", method = RequestMethod.OPTIONS)
	@ApiOperation(value = "EndPoint para configurar o par de chaves do Servidor, (Teste)", response = Usuario.class)
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> config() {
		usuarioService.config();
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/init/cifra", method = RequestMethod.OPTIONS)
	@ApiOperation(value = "Simula um client que cifra os dados para encaminhar para o Servidor (Teste)", response = Usuario.class)
	public ResponseEntity<?> cifra(@RequestBody Usuario usuario) {
		cifra = usuarioService.cifra(usuario);
		return new ResponseEntity<>(cifra, HttpStatus.OK);
	}

}
