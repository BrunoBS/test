package br.com.bancointer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.bancointer.model.Usuario;
import br.com.bancointer.service.AppService;
import br.com.bancointer.service.DigitoUnicoService;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)

public class DigitoUnicoServiceTest {

	@Autowired
	private DigitoUnicoService digitoUnicoService;

	@Autowired
	private AppService appService;

	@Test
	public void digitoComApenasUmParametro() throws Exception {
		Assertions.assertEquals("1", this.digitoUnicoService.digitoUnico("1").getParametroUm());
		Assertions.assertEquals(1, this.digitoUnicoService.digitoUnico("1").getResultado());

		Assertions.assertEquals("10", this.digitoUnicoService.digitoUnico("10").getParametroUm());
		Assertions.assertEquals(1, this.digitoUnicoService.digitoUnico("10").getResultado(), 1);

		Assertions.assertEquals("11", this.digitoUnicoService.digitoUnico("11").getParametroUm());
		Assertions.assertEquals(2, this.digitoUnicoService.digitoUnico("11").getResultado(), 2);

		Assertions.assertEquals("99", this.digitoUnicoService.digitoUnico("99").getParametroUm());
		Assertions.assertEquals(9, this.digitoUnicoService.digitoUnico("99").getResultado());

		Assertions.assertEquals("9875", this.digitoUnicoService.digitoUnico("9875").getParametroUm());
		Assertions.assertEquals(2, this.digitoUnicoService.digitoUnico("9875").getResultado());

	}

	@Test
	public void digitoComApenasUmParametroInvalido() throws Exception {
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
				() -> digitoUnicoService.digitoUnico(""));
		Assertions.assertTrue(exception.getMessage().contains("O Número"));

		exception = Assertions.assertThrows(IllegalArgumentException.class,
				() -> digitoUnicoService.digitoUnico("Bruno"));
		Assertions.assertTrue(exception.getMessage().contains("O Número"));

	}

	@Test
	public void digitoComApenasUmParametroNegativo() throws Exception {
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
				() -> digitoUnicoService.digitoUnico("-1"));
		Assertions.assertTrue(exception.getMessage().contains("O Número"));

		exception = Assertions.assertThrows(IllegalArgumentException.class,
				() -> digitoUnicoService.digitoUnico("-10"));
		Assertions.assertTrue(exception.getMessage().contains("O Número"));

		exception = Assertions.assertThrows(IllegalArgumentException.class,
				() -> digitoUnicoService.digitoUnico("123123123123123123123123123123123123123123123123123123123"));
		Assertions.assertTrue(exception.getMessage().contains("O Número"));

	}

	@Test
	public void digitoComApenasDoisParametro() throws Exception {
		Assertions.assertEquals("11", this.digitoUnicoService.digitoUnico("11", "2").getParametroUm());
		Assertions.assertEquals(4, this.digitoUnicoService.digitoUnico("11", "2").getResultado());

		Assertions.assertEquals("120", this.digitoUnicoService.digitoUnico("120", "5").getParametroUm());
		Assertions.assertEquals(6, this.digitoUnicoService.digitoUnico("120", "5").getResultado());

		Assertions.assertEquals("9875", this.digitoUnicoService.digitoUnico("9875", "4").getParametroUm());
		Assertions.assertEquals(8, this.digitoUnicoService.digitoUnico("9875", "4").getResultado());
	}

	@Test
	public void digitoComApenasDoisParametroSegundoInvalido() throws Exception {
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
				() -> digitoUnicoService.digitoUnico("89", "0"));
		Assertions.assertTrue(exception.getMessage().contains("O Número"));

		exception = Assertions.assertThrows(IllegalArgumentException.class,
				() -> digitoUnicoService.digitoUnico("10", "Bruno"));
		Assertions.assertTrue(exception.getMessage().contains("O Número"));

		exception = Assertions.assertThrows(IllegalArgumentException.class,
				() -> digitoUnicoService.digitoUnico("10", Double.POSITIVE_INFINITY + ""));
		Assertions.assertTrue(exception.getMessage().contains("O Número"));

	}

	@Test
	public void digitoComApenasParametroNulo() throws Exception {
		Assertions.assertEquals("120", this.digitoUnicoService.digitoUnico("120", null).getParametroUm());
		Assertions.assertEquals(3, this.digitoUnicoService.digitoUnico("120", null).getResultado());

		Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
				() -> digitoUnicoService.digitoUnico(null, "-100"));
		Assertions.assertTrue(exception.getMessage().contains("O Número"));
	}

	@Test
	public void testCancheMemoria() throws Exception {

		Assertions.assertFalse(this.digitoUnicoService.digitoUnico("1000", null).isCache());
		Assertions.assertFalse(this.digitoUnicoService.digitoUnico("1001", null).isCache());
		Assertions.assertFalse(this.digitoUnicoService.digitoUnico("1002", null).isCache());
		Assertions.assertFalse(this.digitoUnicoService.digitoUnico("1003", null).isCache());
		Assertions.assertFalse(this.digitoUnicoService.digitoUnico("1004", null).isCache());
		Assertions.assertFalse(this.digitoUnicoService.digitoUnico("1005", null).isCache());
		Assertions.assertFalse(this.digitoUnicoService.digitoUnico("1006", null).isCache());
		Assertions.assertFalse(this.digitoUnicoService.digitoUnico("1007", null).isCache());
		Assertions.assertFalse(this.digitoUnicoService.digitoUnico("1008", null).isCache());
		Assertions.assertFalse(this.digitoUnicoService.digitoUnico("1009", null).isCache());

		Assertions.assertTrue(this.digitoUnicoService.digitoUnico("1000", null).isCache());
		Assertions.assertTrue(this.digitoUnicoService.digitoUnico("1001", null).isCache());
		Assertions.assertTrue(this.digitoUnicoService.digitoUnico("1002", null).isCache());
		Assertions.assertTrue(this.digitoUnicoService.digitoUnico("1003", null).isCache());
		Assertions.assertTrue(this.digitoUnicoService.digitoUnico("1004", null).isCache());
		Assertions.assertTrue(this.digitoUnicoService.digitoUnico("1005", null).isCache());
		Assertions.assertTrue(this.digitoUnicoService.digitoUnico("1006", null).isCache());
		Assertions.assertTrue(this.digitoUnicoService.digitoUnico("1007", null).isCache());
		Assertions.assertTrue(this.digitoUnicoService.digitoUnico("1008", null).isCache());
		Assertions.assertTrue(this.digitoUnicoService.digitoUnico("1009", null).isCache());

	}

}
