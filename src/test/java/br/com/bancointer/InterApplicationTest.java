package br.com.bancointer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InterApplicationTest {

	@Test
	public void mainTest() {
		InterApplication.main(new String[] {});
	}

}
