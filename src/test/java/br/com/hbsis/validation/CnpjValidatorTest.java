package br.com.hbsis.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CnpjValidatorTest {

	@Test
	public void cnpjIsValido() {
		CnpjValidator cnpjValidator = new CnpjValidator();

		final String cnpjHbsis = "81875973000176";

		boolean valid = cnpjValidator.isValid(cnpjHbsis, null);

		assertTrue(valid);
	}

	@Test
	public void cnpjComDigitoVerificadorErrado() {
		CnpjValidator cnpjValidator = new CnpjValidator();

		final String cnpjHbsis = "81875973000172";

		boolean valid = cnpjValidator.isValid(cnpjHbsis, null);

		assertFalse(valid);
	}

	@Test
	public void cnpjContainsLetras() {
		CnpjValidator cnpjValidator = new CnpjValidator();

		final String cnpjHbsis = "8187597300017A";

		boolean valid = cnpjValidator.isValid(cnpjHbsis, null);

		assertFalse(valid);
	}

	@Test
	public void cnpjContainsMuitosCaracteres() {
		CnpjValidator cnpjValidator = new CnpjValidator();

		final String cnpjHbsis = "818759730001762";

		boolean valid = cnpjValidator.isValid(cnpjHbsis, null);

		assertFalse(valid);
	}


}
