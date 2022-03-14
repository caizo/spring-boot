package org.pmv.springboot.bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;



    @GetMapping("/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public Account findAccountById(@PathVariable(value = "accountId") Long accountId){
        return accountService.findById(accountId);
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
}
