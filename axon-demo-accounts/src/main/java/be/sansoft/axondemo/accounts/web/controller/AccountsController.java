package be.sansoft.axondemo.accounts.web.controller;

import be.sansoft.axondemo.accounts.domain.commands.DeleteAccountCommand;
import be.sansoft.axondemo.accounts.view.projection.details.AccountDetails;
import be.sansoft.axondemo.accounts.view.projection.details.AccountDetailsEntity;
import be.sansoft.axondemo.accounts.view.projection.overview.AccountsOverviewEntity;
import be.sansoft.axondemo.accounts.view.projection.overview.AccountsOverviewRow;
import be.sansoft.axondemo.accounts.view.projection.overview.AccountsOverviewRowWrapper;
import be.sansoft.axondemo.accounts.view.query.FindAccountDetailsByIdQuery;
import be.sansoft.axondemo.accounts.view.query.FindAllAccountsQuery;
import be.sansoft.axondemo.accounts.web.websockets.WebsocketDataProvider;
import be.sansoft.axondemo.accounts.web.request.ChangeNameRequest;
import be.sansoft.axondemo.accounts.web.request.CreateAccountRequest;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author kristofennekens
 */
@Slf4j
@RestController
public class AccountsController {
    private final WebsocketDataProvider websocketDataProvider;
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public AccountsController(WebsocketDataProvider websocketDataProvider, CommandGateway commandGateway, QueryGateway queryGateway) {
        this.websocketDataProvider = websocketDataProvider;
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

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountsOverviewRow>> findAll() {
        AccountsOverviewEntity overview = queryGateway
                .query(new FindAllAccountsQuery(), ResponseTypes.instanceOf(AccountsOverviewEntity.class))
                .join();

        return ResponseEntity.ok(
                overview != null ? overview.getData().getRows() : List.of()
        );
    }

    @GetMapping("/accounts/{id}/details")
    public ResponseEntity<AccountDetails> findDetails(
            @PathVariable("id") String id) {
        websocketDataProvider.addAccountDetailsSubscription(id);
        return ResponseEntity.ok(
                queryGateway
                        .query(new FindAccountDetailsByIdQuery(id), ResponseTypes.instanceOf(AccountDetailsEntity.class))
                        .join().getData()
        );
    }
}
