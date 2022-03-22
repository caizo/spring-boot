package org.pmv.springboot.account;

import org.pmv.springboot.bank.TransferDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping(value = "/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;



    @GetMapping("/{accountId}")
    //@ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Account> findAccountById(@PathVariable(value = "accountId") Long accountId){
        Account account = null;
        try {
            account = accountService.findById(accountId);
        } catch (NoSuchElementException e) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(account);
    }


    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferDto dto) {
        accountService.transfer(dto.getBankId(),dto.getSourceAccount(),
                dto.getDestinationAccount(),
                dto.getAmount());

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("message", "Transferencia realizada con éxito!");
        response.put("transfer", dto);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Account> findAll(){
        return accountService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account save(@RequestBody Account account){
        return accountService.save(account);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        accountService.deleteById(id);
    }
}
