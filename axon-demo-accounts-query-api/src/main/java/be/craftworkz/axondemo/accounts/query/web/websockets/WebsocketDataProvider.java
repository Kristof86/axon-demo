package be.craftworkz.axondemo.accounts.query.web.websockets;

import be.craftworkz.axondemo.accounts.query.domain.AccountDetailsEntity;
import be.craftworkz.axondemo.accounts.query.domain.AccountsOverviewEntity;
import be.craftworkz.axondemo.accounts.query.projection.queries.FindAccountDetailsByIdQuery;
import be.craftworkz.axondemo.accounts.query.projection.queries.FindAllAccountsQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author kristofennekens
 */
@Slf4j
@Component
public class WebsocketDataProvider {

    private final List<String> accountDetailSubscriptions = new ArrayList<>();
    private boolean accountsOverviewSubscription;
    private final QueryGateway queryGateway;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public WebsocketDataProvider(QueryGateway queryGateway, SimpMessagingTemplate simpMessagingTemplate) {
        this.queryGateway = queryGateway;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void addAccountsOverviewSubscription() {
        // Only subscribe once
        if (!accountsOverviewSubscription) {
            accountsOverviewSubscription = true;

            Consumer<AccountsOverviewEntity> handleResult = overview -> {
                log.debug("Send to /topic/accounts/all");
                simpMessagingTemplate.convertAndSend(
                        "/topic/accounts/all",
                        overview.getData().getRows()
                );
            };

            queryGateway
                    .subscriptionQuery(new FindAllAccountsQuery(), AccountsOverviewEntity.class, AccountsOverviewEntity.class)
                    .handle(handleResult, handleResult);
        }
    }

    public void addAccountDetailsSubscription(String id) {
        // Only subscribe once for every id
        if (!accountDetailSubscriptions.contains(id)) {
            accountDetailSubscriptions.add(id);

            Consumer<AccountDetailsEntity> handleResult = details -> {
                log.debug("Send to /topic/accounts/" + id + "/details");
                simpMessagingTemplate.convertAndSend(
                        "/topic/accounts/" + id + "/details",
                        details.getData()
                );
            };

            queryGateway
                    .subscriptionQuery(FindAccountDetailsByIdQuery.of(id), AccountDetailsEntity.class, AccountDetailsEntity.class)
                    .handle(handleResult, handleResult);
        }
    }
}
