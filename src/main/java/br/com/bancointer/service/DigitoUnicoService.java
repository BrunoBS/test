package br.com.bancointer.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.bancointer.model.DigitoUnico;

@Component
public class DigitoUnicoService {

	private double MAX_N = Math.pow(10, 1000000);
	private double MAX_K = Math.pow(10, 5);
	private Integer digito = 0;

	private static final Map<Integer, DigitoUnico> DIGITOS = new HashMap<>();
	private static int index = 0;

	private static Optional<DigitoUnico> getDigito(String parametroUm, String parametroDois) {
		return DIGITOS.entrySet().stream().filter(e -> (e.getValue().getParametroUm().equals(parametroUm)
				&& e.getValue().getParametroDois().equals(parametroDois))).map(Map.Entry::getValue).findFirst();

	}

	private static void add(DigitoUnico digito) {

		if (index > 9) {
			index = 0;
		}

		DIGITOS.put(index, digito);
		index++;

	}

	public DigitoUnico digitoUnico(String parametroUm) throws IllegalArgumentException {
		return digitoUnico(parametroUm, "1");
	}

	public DigitoUnico digitoUnico(String parametroUm, String parametroDois) throws IllegalArgumentException {
		parametroUm = (parametroUm == null ? "0" : parametroUm);
		parametroDois = (parametroDois == null ? "1" : parametroDois);

		Optional<DigitoUnico> digitoEncontrado = DigitoUnicoService.getDigito(parametroUm, parametroDois);
		if (digitoEncontrado.isPresent()) {
			digitoEncontrado.get().setCache(Boolean.TRUE);
			return digitoEncontrado.get();
		}

		StringBuilder parametroConcatenado = new StringBuilder();
		this.digito = 0;

		valida(parametroUm, MAX_N);
		valida(parametroDois, MAX_K);

		int intK = Integer.parseInt(parametroDois) - 1;
		parametroConcatenado.append(parametroUm);

		while (intK > 0) {
			parametroConcatenado.append(parametroUm);
			intK--;
		}

		calcular(parametroConcatenado.toString());
		DigitoUnico digitoUnico = new DigitoUnico(parametroUm, parametroDois, digito);

		DigitoUnicoService.add(digitoUnico);
		return digitoUnico;
	}

	private void valida(String parametro, double maximo) {
		Integer p = 0;
		try {
			p = (Integer.parseInt(parametro));
		} catch (Exception e) {
			throw new IllegalArgumentException("O Número " + parametro + " é Inválido!");
		}

		if (p < 1 || p >= maximo) {
			throw new IllegalArgumentException(
					"O Número " + p + " é Inválido, deverá ser um número maior que 0 e menor que " + maximo + ".");
		}

	}

	private void calcular(String valor) {
		char[] result = valor.toCharArray();
		for (char c : result) {
			this.digito += Character.getNumericValue(c);
		}

		if (this.digito > 9) {
			String r = this.digito.toString();
			this.digito = 0;
			this.calcular(r);
		}
	}

}
