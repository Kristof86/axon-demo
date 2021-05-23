package be.craftworkz.axondemo.accounts.query.web.controller;

import be.craftworkz.axondemo.accounts.query.domain.AccountDetails;
import be.craftworkz.axondemo.accounts.query.domain.AccountDetailsEntity;
import be.craftworkz.axondemo.accounts.query.domain.AccountsOverviewEntity;
import be.craftworkz.axondemo.accounts.query.domain.AccountsOverviewRow;
import be.craftworkz.axondemo.accounts.query.projection.FindAccountDetailsByIdQuery;
import be.craftworkz.axondemo.accounts.query.projection.FindAllAccountsQuery;
import be.craftworkz.axondemo.accounts.query.web.websockets.WebsocketDataProvider;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author kristofennekens
 */
@Slf4j
@RequestMapping("/accounts")
@RestController
public class AccountController {
    private final WebsocketDataProvider websocketDataProvider;
    private final QueryGateway queryGateway;

    public AccountController(WebsocketDataProvider websocketDataProvider, QueryGateway queryGateway) {
        this.websocketDataProvider = websocketDataProvider;
        this.queryGateway = queryGateway;
    }

    @GetMapping("")
    public ResponseEntity<List<AccountsOverviewRow>> findAll() {
        websocketDataProvider.addAccountsOverviewSubscription();
        AccountsOverviewEntity overview = queryGateway
                .query(new FindAllAccountsQuery(), ResponseTypes.instanceOf(AccountsOverviewEntity.class))
                .join();

        return ResponseEntity.ok(
                overview != null ? overview.getData().getRows() : List.of()
        );
    }

    @GetMapping("/{id}/details")
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