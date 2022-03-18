package org.pmv.springboot.bank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.hamcrest.Matchers.*;

/**
 * Test de integración de servicios Rest
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class AccountControllerWebClientTest {

    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient client;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void transfer_test() throws JsonProcessingException {
        // GIVEN
        TransferDto dto = new TransferDto();
        dto.setSourceAccount(1L);
        dto.setDestinationAccount(2L);
        dto.setBankId(1L);
        dto.setAmount(new BigDecimal("100"));

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("message", "Transferencia realizada con éxito!");
        response.put("transfer", dto);

        // WHEN
        client.post().uri("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                // THEN
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(respuesta -> {
                    try {
                        JsonNode json = objectMapper.readTree(respuesta.getResponseBody());
                        assertEquals("Transferencia realizada con éxito!", json.path("message").asText());
                        assertEquals(1L, json.path("transfer").path("sourceAccount").asLong());
                        assertEquals(LocalDate.now().toString(), json.path("date").asText());
                        assertEquals("100", json.path("transfer").path("amount").asText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .jsonPath("$.message").isNotEmpty()
                .jsonPath("$.message").value(is("Transferencia realizada con éxito!"))
                .jsonPath("$.message").value(valor -> assertEquals("Transferencia realizada con éxito!", valor))
                .jsonPath("$.message").isEqualTo("Transferencia realizada con éxito!")
                .jsonPath("$.transfer.sourceAccount").isEqualTo(dto.getSourceAccount())
                .jsonPath("$.date").isEqualTo(LocalDate.now().toString())
                .json(objectMapper.writeValueAsString(response));

    }

    @Test
    @Order(2)
    void account_details_test() throws JsonProcessingException {
        Account cuenta = new Account(1L, "Juan", new BigDecimal("900"));
        client.get().uri("/api/accounts/1").exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.holder").isEqualTo("Juan")
            .jsonPath("$.balance").isEqualTo(900)
            .json(objectMapper.writeValueAsString(cuenta));
    }

    @Test
    @Order(3)
    void account_details_test_2() {
        client.get().uri("/api/accounts/2").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Account.class)
                .consumeWith(response -> {
                    Account account = response.getResponseBody();
                    assertNotNull(account);
                    assertEquals("Jose", account.getHolder());
                    assertEquals("2100.00", account.getBalance().toPlainString());
                });
    }

    @Test
    @Order(4)
    void find_all_test() {
        client.get().uri("/api/accounts").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].holder").isEqualTo("Juan")
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].balance").isEqualTo(900)
                .jsonPath("$[1].holder").isEqualTo("Jose")
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].balance").isEqualTo(2100)
                .jsonPath("$").isArray()
                .jsonPath("$").value(hasSize(2));
    }

    @Test
    @Order(5)
    void find_all_test_2() {
        client.get().uri("/api/accounts").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Account.class)
                .consumeWith(response -> {
                    List<Account> accounts = response.getResponseBody();
                    assertNotNull(accounts);
                    assertEquals(2, accounts.size());
                    assertEquals(1L, accounts.get(0).getId());
                    assertEquals("Juan", accounts.get(0).getHolder());
                    assertEquals(900, accounts.get(0).getBalance().intValue());
                    assertEquals(2L, accounts.get(1).getId());
                    assertEquals("Jose", accounts.get(1).getHolder());
                    assertEquals("2100.0", accounts.get(1).getBalance().toPlainString());
                })
                .hasSize(2)
                .value(hasSize(2));
    }

    @Test
    @Order(6)
    void save_test() {
        // given
        Account account = new Account(null, "Pepe", new BigDecimal("3000"));

        // when
        client.post().uri("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(account)
                .exchange()
                // then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(3)
                .jsonPath("$.holder").isEqualTo("Pepe")
                .jsonPath("$.holder").value(is("Pepe"))
                .jsonPath("$.balance").isEqualTo(3000);
    }

    @Test
    @Order(7)
    void save_test_2() {
        // given
        Account account = new Account(null, "Antonio", new BigDecimal("3500"));

        // when
        client.post().uri("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(account)
                .exchange()
                // then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Account.class)
                .consumeWith(response -> {
                    Account c = response.getResponseBody();
                    assertNotNull(c);
                    assertEquals(4L, c.getId());
                    assertEquals("Antonio", c.getHolder());
                    assertEquals("3500", c.getBalance().toPlainString());
                });
    }

    @Test
    @Order(8)
    void testEliminar() {
        client.get().uri("/api/accounts").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Account.class)
                .hasSize(4);

        client.delete().uri("/api/accounts/3")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        client.get().uri("/api/accounts").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Account.class)
                .hasSize(3);

        client.get().uri("/api/accounts/3").exchange()
                .expectStatus().is5xxServerError();
//                .expectStatus().isNotFound()
//                .expectBody().isEmpty();
    }
}