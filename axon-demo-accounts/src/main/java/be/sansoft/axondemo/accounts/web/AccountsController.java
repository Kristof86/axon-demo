package be.sansoft.axondemo.accounts.web;

import be.sansoft.axondemo.accounts.domain.commands.DeleteAccountCommand;
import be.sansoft.axondemo.accounts.web.request.ChangeNameRequest;
import be.sansoft.axondemo.accounts.web.request.CreateAccountRequest;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Future;

/**
 * @author kristofennekens
 */
@Slf4j
@RestController
public class AccountsController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public AccountsController(SimpMessagingTemplate simpMessagingTemplate, CommandGateway commandGateway, QueryGateway queryGateway) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping("/createAccount")
    public ResponseEntity<Future<String>> createAccount(@RequestBody CreateAccountRequest request) {
        log.info("Create account {}", request);
        Future<String> future = commandGateway.send(request.toCommand());
        return ResponseEntity.ok(future);
    }

    @PostMapping("/changeName/{id}")
    public ResponseEntity<Future<String>> changeName(
            @RequestBody ChangeNameRequest request,
            @PathVariable("id") String id) {
        log.info("Create account {}", request);
        Future<String> future = commandGateway.send(request.toCommand(id));
        return ResponseEntity.ok(future);
    }

    @DeleteMapping("/deleteAccount/{id}")
    public ResponseEntity<Future<String>> deleteAccount(@PathVariable("id") String id) {
        log.info("Delete account {}", id);
        Future<String> future = commandGateway.send(DeleteAccountCommand.of(id));
        return ResponseEntity.ok(future);
    }
}
