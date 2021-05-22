package be.sansoft.axondemo.accounts.web;

import be.sansoft.axondemo.accounts.domain.commands.ChangeNameCommand;
import be.sansoft.axondemo.accounts.domain.commands.DeleteAccountCommand;
import be.sansoft.axondemo.accounts.view.projection.details.AccountDetails;
import be.sansoft.axondemo.accounts.view.projection.details.AccountDetailsEntity;
import be.sansoft.axondemo.accounts.view.projection.overview.AccountsOverviewEntity;
import be.sansoft.axondemo.accounts.view.projection.overview.AccountsOverviewRow;
import be.sansoft.axondemo.accounts.view.query.FindAccountDetailsByIdQuery;
import be.sansoft.axondemo.accounts.view.query.FindAllAccountsQuery;
import be.sansoft.axondemo.accounts.web.request.ChangeNameRequest;
import be.sansoft.axondemo.accounts.web.request.CreateAccountRequest;
import be.sansoft.axondemo.accounts.web.request.FindAccountDetailsRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author kristofennekens
 */
@Slf4j
@RestController
public class AccountsController {

    private final ReactorQueryGateway reactorQueryGateway;
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final ObjectMapper objectMapper;

    public AccountsController(ReactorQueryGateway reactorQueryGateway, CommandGateway commandGateway, QueryGateway queryGateway, ObjectMapper objectMapper) {
        this.reactorQueryGateway = reactorQueryGateway;
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
        this.objectMapper = objectMapper;
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

    @MessageMapping("accounts.detail")
    public Flux<AccountDetails> all(FindAccountDetailsRequest request) {
        return reactorQueryGateway.subscriptionQuery(new FindAccountDetailsByIdQuery(request.getId()), AccountDetailsEntity.class)
                .map(AccountDetailsEntity::getData);
    }

    @MessageMapping("accounts.all")
    public Flux<List<AccountsOverviewRow>> all() {
        QueryGateway queryGateway = null;
        SubscriptionQueryResult<AccountsOverviewEntity, AccountsOverviewEntity> accountsOverviewEntityAccountsOverviewEntitySubscriptionQueryResult = queryGateway.subscriptionQuery(new FindAllAccountsQuery(), AccountsOverviewEntity.class, AccountsOverviewEntity.class);
        return reactorQueryGateway.subscriptionQuery(new FindAllAccountsQuery(), AccountsOverviewEntity.class).map(entity -> entity.getData().getRows());

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
