package br.com.bancointer.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.bancointer.model.DigitoUnico;
import br.com.bancointer.model.Usuario;
import br.com.bancointer.repository.DigitoUnicoRepository;
import br.com.bancointer.service.DigitoUnicoService;
import br.com.bancointer.service.UsuarioService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("v1")
public class DigitoUnicoController {

	@Autowired
	protected DigitoUnicoService digitoUnicoService;

	@Autowired
	protected UsuarioService usuarioService;

	@Autowired
	protected DigitoUnicoRepository digitoUnicoRepository;

	@PostMapping(path = { "/calculoDigito" })
	@ApiOperation(value = "Calcula Digito Unico", response = Usuario[].class)
	public DigitoUnico calcula(@RequestParam(name = "idUser", required = false) Long idUser,
			@RequestParam(name = "parametroUm") String parametroUm,
			@RequestParam(name = "parametroDois", required = false) String parametroDois) {

		DigitoUnico digitoUnico = digitoUnicoService.digitoUnico(parametroUm, parametroDois);
		if (idUser != null) {
			Usuario usuario = usuarioService.find(idUser);
			digitoUnico.setUsuario(usuario);
			digitoUnicoRepository.save(digitoUnico);

		}

		return digitoUnico;

	}

	@GetMapping(path = "digitos/usuario/{id}")
	public ResponseEntity<?> find(@PathVariable("id") Long id) {
		List<DigitoUnico> digitos = digitoUnicoRepository.findByUsuarioId(id);
		return new ResponseEntity<>(digitos, HttpStatus.OK);
	}
}