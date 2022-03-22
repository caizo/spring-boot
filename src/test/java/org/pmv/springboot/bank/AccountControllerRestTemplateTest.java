package org.pmv.springboot.bank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.pmv.springboot.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.MethodOrderer.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
class AccountControllerRestTemplateTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private ObjectMapper objectMapper;

    @LocalServerPort
    private int puerto;

    @BeforeEach
    void setUp(){
        objectMapper = new ObjectMapper();
    }





    @Order(1)
    @Test
    void transfer_test() throws JsonProcessingException {
        TransferDto dto = new TransferDto();
        dto.setAmount(new BigDecimal("100"));
        dto.setDestinationAccount(2L);
        dto.setSourceAccount(1L);
        dto.setBankId(1L);

        ResponseEntity<String> response = restTemplate.
                postForEntity(crearUri("/api/accounts/transfer"), dto, String.class);
        System.out.println("PUERTOOOOO: " + puerto);
        String json = response.getBody();
        System.out.println(json);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(json);
        assertTrue(json.contains("Transferencia realizada con éxito!"));
        assertTrue(json.contains("{\"sourceAccount\":1,\"destinationAccount\":2,\"amount\":100,\"bankId\":1}"));

        JsonNode jsonNode = objectMapper.readTree(json);
        assertEquals("Transferencia realizada con éxito!", jsonNode.path("message").asText());
        assertEquals(LocalDate.now().toString(), jsonNode.path("date").asText());
        assertEquals("100", jsonNode.path("transfer").path("amount").asText());
        assertEquals(1L, jsonNode.path("transfer").path("sourceAccount").asLong());

        Map<String, Object> response2 = new HashMap<>();
        response2.put("date", LocalDate.now().toString());
        response2.put("status", "OK");
        response2.put("message", "Transferencia realizada con éxito!");
        response2.put("transfer", dto);

        assertEquals(objectMapper.writeValueAsString(response2), json);
    }


    @Test
    @Order(2)
    void detail_account_test() {
        ResponseEntity<Account> response = restTemplate.getForEntity(crearUri("/api/accounts/1"), Account.class);
        Account account = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        assertNotNull(account);
        assertEquals(1L, account.getId());
        assertEquals("Juan", account.getHolder());
        assertEquals("900.00", account.getBalance().toPlainString());
        assertEquals(new Account(1L, "Juan", new BigDecimal("900.00")), account);
    }

    @Test
    @Order(3)
    void find_all_test() throws JsonProcessingException {
        ResponseEntity<Account[]> respuesta = restTemplate.getForEntity(crearUri("/api/accounts"), Account[].class);
        List<Account> accounts = Arrays.asList(respuesta.getBody());

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, respuesta.getHeaders().getContentType());

        assertEquals(2, accounts.size());
        assertEquals(1L, accounts.get(0).getId());
        assertEquals("Juan", accounts.get(0).getHolder());
        assertEquals("900.00", accounts.get(0).getBalance().toPlainString());
        assertEquals(2L, accounts.get(1).getId());
        assertEquals("Jose", accounts.get(1).getHolder());
        assertEquals("2100.00", accounts.get(1).getBalance().toPlainString());

        JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(accounts));
        assertEquals(1L, json.get(0).path("id").asLong());
        assertEquals("Juan", json.get(0).path("holder").asText());
        assertEquals("900.0", json.get(0).path("balance").asText());
        assertEquals(2L, json.get(1).path("id").asLong());
        assertEquals("Jose", json.get(1).path("holder").asText());
        assertEquals("2100.0", json.get(1).path("balance").asText());
    }

    @Test
    @Order(4)
    void save_Test() {
        Account account = new Account(null, "Antonio", new BigDecimal("3800"));

        ResponseEntity<Account> response = restTemplate.postForEntity(crearUri("/api/accounts"), account, Account.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        Account cuentaCreada = response.getBody();
        assertNotNull(cuentaCreada);
        assertEquals(3L, cuentaCreada.getId());
        assertEquals("Antonio", cuentaCreada.getHolder());
        assertEquals("3800", cuentaCreada.getBalance().toPlainString());
    }


    @Test
    @Order(5)
    void delete_test() {

        ResponseEntity<Account[]> response = restTemplate.getForEntity(crearUri("/api/accounts"), Account[].class);
        List<Account> accounts = Arrays.asList(response.getBody());
        assertEquals(3, accounts.size());

        //client.delete(crearUri("/api/cuentas/3"));
        Map<String, Long> pathVariables = new HashMap<>();
        pathVariables.put("id", 3L);
        ResponseEntity<Void> exchange = restTemplate.exchange(crearUri("/api/accounts/{id}"),
                HttpMethod.DELETE, null, Void.class,
                pathVariables);

        assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode());
        assertFalse(exchange.hasBody());

        response = restTemplate.getForEntity(crearUri("/api/accounts"), Account[].class);
        accounts = Arrays.asList(response.getBody());
        assertEquals(2, accounts.size());

        ResponseEntity<Account> responseDetail = restTemplate.getForEntity(crearUri("/api/accounts/3"), Account.class);
        assertEquals(HttpStatus.NO_CONTENT, responseDetail.getStatusCode());
        //assertEquals(500, responseDetail.getStatusCodeValue());
        assertFalse(responseDetail.hasBody());
    }
    
    
    
    private String crearUri(String uri) {
        return "http://localhost:" + puerto + uri;
    }
}