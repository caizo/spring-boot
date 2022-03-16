package org.pmv.springboot.bank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.pmv.springboot.bank.DataTest.ACCOUNT_1;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;


@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean AccountService accountService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void find_account_by_id_test() throws Exception {
        // GIVEN THIS CONTEXT
        when(accountService.findById(1L)).thenReturn(ACCOUNT_1.orElseThrow());

        // WHEN THIS ACTION IS PERFORMED
        mvc.perform(get("/api/accounts/1").contentType(APPLICATION_JSON))
        // THEN VERIFY RESULTS
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.holder").value("Juan"))
                .andExpect(jsonPath("$.balance").value("1000"));

        verify(accountService).findById(1L);

    }

    @Test
    void transfer_test() throws Exception, JsonProcessingException {

        // GIVEN
        TransferDto dto = new TransferDto();
        dto.setSourceAccount(1L);
        dto.setDestinationAccount(2L);
        dto.setAmount(new BigDecimal("100"));
        dto.setBankId(1L);

        System.out.println(objectMapper.writeValueAsString(dto));

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("message", "Transferencia realizada con éxito!");
        response.put("transfer", dto);

        System.out.println(objectMapper.writeValueAsString(response));

        // WHEN
        mvc.perform(post("/api/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))

        // THEN
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.message").value("Transferencia realizada con éxito!"))
                .andExpect(jsonPath("$.transfer.sourceAccount").value(dto.getSourceAccount()))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

    }

    @Test
    void find_all_test() throws Exception {
        // Given
        List<Account> accounts = Arrays.asList(ACCOUNT_1.orElseThrow(),
                DataTest.ACCOUNT_2.orElseThrow()
        );
        when(accountService.findAll()).thenReturn(accounts);

        // When
        mvc.perform(get("/api/accounts").contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].holder").value("Juan"))
                .andExpect(jsonPath("$[1].holder").value("Jose"))
                .andExpect(jsonPath("$[0].balance").value("1000"))
                .andExpect(jsonPath("$[1].balance").value("2000"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(objectMapper.writeValueAsString(accounts)));

        verify(accountService).findAll();
    }

    @Test
    void save_test() throws Exception {
        // Given
        Account account = new Account(null, "Pepe", new BigDecimal("3000"));
        when(accountService.save(any())).then(invocation ->{
            Account c = invocation.getArgument(0);
            c.setId(3L);
            return c;
        });

        // when
        mvc.perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                // Then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.holder", is("Pepe")))
                .andExpect(jsonPath("$.balance", is(3000)));
        verify(accountService).save(any());

    }
}