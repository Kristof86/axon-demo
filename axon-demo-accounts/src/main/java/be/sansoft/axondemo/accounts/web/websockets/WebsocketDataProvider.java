package be.sansoft.axondemo.accounts.web.websockets;

import be.sansoft.axondemo.accounts.view.projection.details.AccountDetailsEntity;
import be.sansoft.axondemo.accounts.view.projection.overview.AccountsOverviewEntity;
import be.sansoft.axondemo.accounts.view.query.FindAccountDetailsByIdQuery;
import be.sansoft.axondemo.accounts.view.query.FindAllAccountsQuery;
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
    private final QueryGateway queryGateway;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public WebsocketDataProvider(QueryGateway queryGateway, SimpMessagingTemplate simpMessagingTemplate) {
        this.queryGateway = queryGateway;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void sendUpdatesToSubscribers() {
        updateAccountsOverview();
    }

    private void updateAccountsOverview() {
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

    public void addAccountDetailsSubscription(String id) {
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
                    .subscriptionQuery(new FindAccountDetailsByIdQuery(id), AccountDetailsEntity.class, AccountDetailsEntity.class)
                    .handle(handleResult, handleResult);
        }
    }
}
