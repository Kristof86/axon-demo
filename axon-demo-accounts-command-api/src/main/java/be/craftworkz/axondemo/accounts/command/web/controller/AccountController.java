package be.craftworkz.axondemo.accounts.command.web.controller;

import be.craftworkz.axondemo.accounts.command.web.request.ChangeNameRequest;
import be.craftworkz.axondemo.accounts.command.web.request.CreateAccountRequest;
import be.craftworkz.axondemo.accounts.core.domain.commands.DeleteAccountCommand;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Future;

/**
 * @author kristofennekens
 */
@Slf4j
@RequestMapping("/accounts")
@RestController
public class AccountController {
    private final CommandGateway commandGateway;

    public AccountController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping("/create")
    public ResponseEntity<Future<String>> createAccount(@RequestBody CreateAccountRequest request) {
        log.info("Create account {}", request);
        Future<String> future = commandGateway.send(request.toCommand());
        return ResponseEntity.ok(future);
    }

    @PostMapping("/{id}/changeName")
    public ResponseEntity<Future<String>> changeName(
            @RequestBody ChangeNameRequest request,
            @PathVariable("id") String id) {
        log.info("Create account {}", request);
        Future<String> future = commandGateway.send(request.toCommand(id));
        return ResponseEntity.ok(future);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Future<String>> deleteAccount(@PathVariable("id") String id) {
        log.info("Delete account {}", id);
        Future<String> future = commandGateway.send(DeleteAccountCommand.of(id));
        return ResponseEntity.ok(future);
    }
}
