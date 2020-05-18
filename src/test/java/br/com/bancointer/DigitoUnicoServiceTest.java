package br.com.bancointer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.bancointer.service.DigitoUnicoService;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestInstance(Lifecycle.PER_CLASS)

public class DigitoUnicoServiceTest {

	@Autowired
	private DigitoUnicoService digitoUnicoService;

	@Test
	public void digitoComApenasUmParametro() throws Exception {
		assertThat(this.digitoUnicoService.digitoUnico("1").getParametroUm()).isEqualTo("1");
		assertThat(this.digitoUnicoService.digitoUnico("1").getResultado()).isEqualTo(1);

		assertThat(this.digitoUnicoService.digitoUnico("10").getParametroUm()).isEqualTo("10");
		assertThat(this.digitoUnicoService.digitoUnico("10").getResultado()).isEqualTo(1);

		assertThat(this.digitoUnicoService.digitoUnico("11").getParametroUm()).isEqualTo("11");
		assertThat(this.digitoUnicoService.digitoUnico("11").getResultado()).isEqualTo(2);

		assertThat(this.digitoUnicoService.digitoUnico("99").getParametroUm()).isEqualTo("99");
		assertThat(this.digitoUnicoService.digitoUnico("99").getResultado()).isEqualTo(9);

		assertThat(this.digitoUnicoService.digitoUnico("9875").getParametroUm()).isEqualTo("9875");
		assertThat(this.digitoUnicoService.digitoUnico("9875").getResultado()).isEqualTo(2);

	}

	@Test
	public void digitoComApenasUmParametroInvalido() throws Exception {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> digitoUnicoService.digitoUnico(""));
		assertTrue(exception.getMessage().contains("O Número"));

		exception = assertThrows(IllegalArgumentException.class, () -> digitoUnicoService.digitoUnico("Bruno"));
		assertTrue(exception.getMessage().contains("O Número"));

	}

	@Test
	public void digitoComApenasUmParametroNegativo() throws Exception {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> digitoUnicoService.digitoUnico("-1"));
		assertTrue(exception.getMessage().contains("O Número"));

		exception = assertThrows(IllegalArgumentException.class, () -> digitoUnicoService.digitoUnico("-10"));
		assertTrue(exception.getMessage().contains("O Número"));

		exception = assertThrows(IllegalArgumentException.class,
				() -> digitoUnicoService.digitoUnico("123123123123123123123123123123123123123123123123123123123"));
		assertTrue(exception.getMessage().contains("O Número"));

	}

	@Test
	public void digitoComApenasDoisParametro() throws Exception {
		assertThat(this.digitoUnicoService.digitoUnico("11", "2").getParametroUm()).isEqualTo("11");
		assertThat(this.digitoUnicoService.digitoUnico("11", "2").getResultado()).isEqualTo(4);

		assertThat(this.digitoUnicoService.digitoUnico("120", "5").getParametroUm()).isEqualTo("120");
		assertThat(this.digitoUnicoService.digitoUnico("120", "5").getResultado()).isEqualTo(6);

		assertThat(this.digitoUnicoService.digitoUnico("9875", "4").getParametroUm()).isEqualTo("9875");
		assertThat(this.digitoUnicoService.digitoUnico("9875", "4").getResultado()).isEqualTo(8);
	}
 
	@Test
	public void digitoComApenasDoisParametroSegundoInvalido() throws Exception {
		Exception exception = assertThrows(IllegalArgumentException.class,
				() -> digitoUnicoService.digitoUnico("89", "0"));
		assertTrue(exception.getMessage().contains("O Número"));

		exception = assertThrows(IllegalArgumentException.class, () -> digitoUnicoService.digitoUnico("10", "Bruno"));
		assertTrue(exception.getMessage().contains("O Número"));
 
		exception = assertThrows(IllegalArgumentException.class,
				() -> digitoUnicoService.digitoUnico("10", Double.POSITIVE_INFINITY + ""));
		assertTrue(exception.getMessage().contains("O Número"));

	}

	@Test
	public void digitoComApenasParametroNulo() throws Exception {
		assertThat(this.digitoUnicoService.digitoUnico("120", null).getParametroUm()).isEqualTo("120");
		assertThat(this.digitoUnicoService.digitoUnico("120", null).getResultado()).isEqualTo(3);

		Exception exception = assertThrows(IllegalArgumentException.class,
				() -> digitoUnicoService.digitoUnico(null, "-100"));
		assertTrue(exception.getMessage().contains("O Número"));
	}

	@Test
	public void testCancheMemoria() throws Exception {

		assertFalse(this.digitoUnicoService.digitoUnico("1000", null).isCache());
		assertFalse(this.digitoUnicoService.digitoUnico("1001", null).isCache());
		assertFalse(this.digitoUnicoService.digitoUnico("1002", null).isCache());
		assertFalse(this.digitoUnicoService.digitoUnico("1003", null).isCache());
		assertFalse(this.digitoUnicoService.digitoUnico("1004", null).isCache());
		assertFalse(this.digitoUnicoService.digitoUnico("1005", null).isCache());
		assertFalse(this.digitoUnicoService.digitoUnico("1006", null).isCache());
		assertFalse(this.digitoUnicoService.digitoUnico("1007", null).isCache());
		assertFalse(this.digitoUnicoService.digitoUnico("1008", null).isCache());
		assertFalse(this.digitoUnicoService.digitoUnico("1009", null).isCache());

		assertTrue(this.digitoUnicoService.digitoUnico("1000", null).isCache());
		assertTrue(this.digitoUnicoService.digitoUnico("1001", null).isCache());
		assertTrue(this.digitoUnicoService.digitoUnico("1002", null).isCache());
		assertTrue(this.digitoUnicoService.digitoUnico("1003", null).isCache());
		assertTrue(this.digitoUnicoService.digitoUnico("1004", null).isCache());
		assertTrue(this.digitoUnicoService.digitoUnico("1005", null).isCache());
		assertTrue(this.digitoUnicoService.digitoUnico("1006", null).isCache());
		assertTrue(this.digitoUnicoService.digitoUnico("1007", null).isCache());
		assertTrue(this.digitoUnicoService.digitoUnico("1008", null).isCache());
		assertTrue(this.digitoUnicoService.digitoUnico("1009", null).isCache());


	}

}
