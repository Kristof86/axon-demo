package be.sansoft.axondemo.accounts.web;

import be.sansoft.axondemo.accounts.domain.commands.DeleteAccountCommand;
import be.sansoft.axondemo.accounts.view.projection.details.AccountDetailsEntity;
import be.sansoft.axondemo.accounts.view.projection.overview.AccountsOverviewEntity;
import be.sansoft.axondemo.accounts.view.query.FindAccountDetailsByIdQuery;
import be.sansoft.axondemo.accounts.view.query.FindAllAccountsQuery;
import be.sansoft.axondemo.accounts.web.request.ChangeNameRequest;
import be.sansoft.axondemo.accounts.web.request.CreateAccountRequest;
import be.sansoft.axondemo.accounts.web.request.FindAccountDetailsRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
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

    @SendTo("/topic/account/detail")
    @MessageMapping("/account/detail")
    public void detail(FindAccountDetailsRequest request) {
        queryGateway
                .subscriptionQuery(new FindAccountDetailsByIdQuery(request.getId()), AccountDetailsEntity.class, AccountDetailsEntity.class)
                .handle(details -> {
                        simpMessagingTemplate.convertAndSend(
                                "/topic/accounts/" + request.getId() + "/details",
                                details.getData()
                        );
                },
                details -> {
                    simpMessagingTemplate.convertAndSend(
                            "/topic/accounts/" + request.getId() + "/details",
                            details.getData()
                    );
                });
    }

    public void all() {
        queryGateway.subscriptionQuery(new FindAllAccountsQuery(), AccountsOverviewEntity.class, AccountsOverviewEntity.class)
                .handle(overview -> {
                            simpMessagingTemplate.convertAndSend(
                                    "/topic/accounts/all",
                                    overview.getData().getRows()
                            );
                        },
                        overview -> {
                            simpMessagingTemplate.convertAndSend(
                                    "/topic/accounts/all",
                                    overview.getData().getRows()
                            );
                        });
    }

    /*
    @GetMapping("/findAllAccounts")
    public ResponseEntity<List<AccountsRow>> findAllAccounts() throws JsonProcessingException {
        AccountsEntity entity = queryGateway.query(new FindAllAccountsQuery(), AccountsEntity.class).join();
        List<AccountsRow> accounts = objectMapper.readValue(entity.getData(), List.class);
        return ResponseEntity.ok(accounts);
    }
     */


    @GetMapping("/findAccountDetails/{id}")
    public CompletableFuture<AccountDetailsEntity> findAccountDetails(@PathVariable("id") String id) throws JsonProcessingException {
        CompletableFuture<AccountDetailsEntity> query = queryGateway.query(new FindAccountDetailsByIdQuery(id), AccountDetailsEntity.class);
        return query;
    }
}
