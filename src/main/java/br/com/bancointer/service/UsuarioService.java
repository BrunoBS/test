package br.com.bancointer.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.bancointer.error.RSAException;
import br.com.bancointer.error.ResourceNotFoundException;
import br.com.bancointer.model.ChaveCliente;
import br.com.bancointer.model.ChaveServidor;
import br.com.bancointer.model.Usuario;
import br.com.bancointer.repository.ChaveClienteRepository;
import br.com.bancointer.repository.ChaveServidorRepository;
import br.com.bancointer.repository.DigitoUnicoRepository;
import br.com.bancointer.repository.UsuarioRepository;

@Component
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private ChaveServidorRepository chaveServidorRepository;

	@Autowired
	private ChaveClienteRepository chaveClienteRepository;
	@Autowired
	private DigitoUnicoRepository digitoUnicoRepository;

	@Autowired
	protected CriptografiaRSA criptografiaRSA;

	public void config() {
		try {
			Optional<ChaveServidor> chaveServidor = chaveServidorRepository.findTop1ByOrderByIdDesc();
			ChaveServidor keyServidor;
			if (!chaveServidor.isPresent()) {
				KeyPair generateKeyPair = criptografiaRSA.generateKeyPair();
				keyServidor = new ChaveServidor();
				keyServidor.setPrivada(criptografiaRSA.para(generateKeyPair.getPrivate()));
				keyServidor.setPublica(criptografiaRSA.para(generateKeyPair.getPublic()));
				chaveServidorRepository.save(keyServidor);
			}
		} catch (Exception e) {
			throw new RSAException("Erro ao configurar a Chave do Servidor!");
		}

	}

	public Usuario cifra(Usuario user) {
		try {
			Optional<ChaveServidor> chaveServidor = chaveServidorRepository.findTop1ByOrderByIdDesc();
			criptografar(user, chaveServidor.get().getPublica());
			return user;
		} catch (Exception e) {
			throw new RSAException("Erro ao cifrar o objeto!");
		}

	}

	public List<Usuario> listAll() {

		List<Usuario> lista = usuarioRepository.findAll();
		lista.forEach(u -> {
			Optional<ChaveCliente> c = chaveClienteRepository.findTop1ByUsuarioId(u.getId());
			if (c.isPresent()) {
				try {
					ChaveCliente chave = chave(u.getId());
					if (!chave.getPublica().isEmpty()) {
						criptografar(u, chave.getPublica());
					}
				} catch (Exception e) {
					throw new RSAException("Chave inválida!");
				}
			}
		});
		return lista;
	}

	@Transactional
	public Usuario save(Usuario usuario) {
		try {
			Optional<ChaveServidor> chaveServidor = chaveServidorRepository.findTop1ByOrderByIdDesc();
			decriptografa(usuario, chaveServidor.get().getPrivada());
			usuarioRepository.save(usuario);
			ChaveCliente chave = salvaChaveRSA(usuario);
			if (!chave.getPublica().isEmpty()) {
				criptografar(usuario, chave.getPublica());
			}
		} catch (Exception e) {
			throw new RSAException("Chave inválida!");
		}
		return usuario;
	}

	public Usuario find(Long id) throws RSAException {

		existeUsuario(id);
		Usuario usuario = usuarioRepository.findById(id).get();

		try {
			ChaveCliente chaveCliente = chave(id);
			criptografar(usuario, chaveCliente.getPublica());
		} catch (Exception e) {
			throw new RSAException("Chave inválida!");
		}

		return usuario;
	}

	public ChaveCliente chave(Long id) {
		existeUsuario(id);
		Optional<ChaveCliente> c = chaveClienteRepository.findTop1ByUsuarioId(id);
		if (!c.isPresent()) {
			throw new RSAException("Não foi localizada nenhum chave configurada para o cliente " + id);
		}
		return c.get();
	}

	@Transactional
	public void delete(Long id) {
		existeUsuario(id);
		chaveClienteRepository.deleteChaveUsuario(id);
		digitoUnicoRepository.deleteDigitoUnicoUsuario(id);
		usuarioRepository.deleteById(id);
	}

	public void existeUsuario(Long id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		if (!usuario.isPresent())
			throw new ResourceNotFoundException("Usuário Não Encontrado com ID " + id);
	}

	public void atualizaChave(Usuario usuario, String chavePublicaCliente) {
		try {
			validaChave(chavePublicaCliente);
			ChaveCliente chave = salvaChaveRSA(usuario);
			chave.setPublica(chavePublicaCliente);
			chaveClienteRepository.save(chave);
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
			throw new RSAException("Chave inválida:" + e.getMessage());
		} catch (Exception e) {
			throw new RSAException("Erro ao salvar a chave publica:" + e.getMessage());
		}

	}

	private void validaChave(String chave)
			throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		criptografiaRSA.generatePublic(chave);
	}

	private Usuario criptografar(Usuario usuario, String chave) throws Exception {
		usuario.setEmail(criptografiaRSA.criptografa(usuario.getEmail(), chave));
		usuario.setNome(criptografiaRSA.criptografa(usuario.getNome(), chave));
		return usuario;
	}

	private Usuario decriptografa(Usuario usuario, String chave) throws Exception {
		usuario.setEmail(criptografiaRSA.decriptografa(usuario.getEmail(), chave));
		usuario.setNome(criptografiaRSA.decriptografa(usuario.getNome(), chave));
		return usuario;
	}

	private ChaveCliente salvaChaveRSA(Usuario usuario) throws Exception {
		Optional<ChaveServidor> chaveServidor = chaveServidorRepository.findTop1ByOrderByIdDesc();
		ChaveServidor keyServidor;
		if (!chaveServidor.isPresent()) {
			KeyPair generateKeyPair = criptografiaRSA.generateKeyPair();
			keyServidor = new ChaveServidor();
			keyServidor.setPrivada(criptografiaRSA.para(generateKeyPair.getPrivate()));
			keyServidor.setPublica(criptografiaRSA.para(generateKeyPair.getPublic()));
			chaveServidorRepository.save(keyServidor);
		} else {
			keyServidor = chaveServidor.get();
		}
		Optional<ChaveCliente> c = chaveClienteRepository.findTop1ByUsuarioId(usuario.getId());
		ChaveCliente chave;
		if (!c.isPresent()) {
			chave = new ChaveCliente();
			chave.setChaveServidor(keyServidor);
			chave.setPublica("");
			chave.setUsuario(usuario);
			chaveClienteRepository.save(chave);
			return chave;
		}

		return c.get();

	}

}
