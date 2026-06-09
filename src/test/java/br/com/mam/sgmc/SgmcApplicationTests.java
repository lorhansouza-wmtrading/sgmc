package br.com.mam.sgmc;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestClient;

@SpringBootTest
class SgmcApplicationTests {

	@MockitoBean
	private RestClient keycloakRestClient;

	@Test
	void contextLoads() {
	}

}
