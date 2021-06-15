package be.craftworkz.axondemo.accounts.query.web.websockets;

import be.craftworkz.axondemo.accounts.query.domain.AccountDetailsEntity;
import be.craftworkz.axondemo.accounts.query.domain.AccountsOverviewEntity;
import be.craftworkz.axondemo.accounts.query.projection.FindAccountDetailsByIdQuery;
import be.craftworkz.axondemo.accounts.query.projection.FindAllAccountsQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
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
        log.info("Init account overview subscription");
        if (!accountsOverviewSubscription) {
            log.info("Not subscribed yet, subscribe");
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
        log.info("Init account details subscription");
        if (!accountDetailSubscriptions.contains(id)) {
            log.info("Not subscribed yet, subscribe");
            accountDetailSubscriptions.add(id);
            Consumer<AccountDetailsEntity> handleResult = details -> {
                log.debug("Send to /topic/accounts/" + id + "/details");
                simpMessagingTemplate.convertAndSend(
                        "/topic/accounts/" + id + "/details",
                        details.getData()
                );
            };
            queryGateway
                    .subscriptionQuery(new FindAccountDetailsByIdQuery(id), AccountDetailsEntity.class, AccountDetailsEntity.class)
                    .handle(handleResult, handleResult);
        }
    }
}
