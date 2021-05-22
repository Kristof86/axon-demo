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
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author kristofennekens
 */
@Slf4j
@Controller
public class AccountsWebsocketController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public AccountsWebsocketController(SimpMessagingTemplate simpMessagingTemplate, CommandGateway commandGateway, QueryGateway queryGateway) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @MessageMapping("/accounts/details")
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

    @MessageMapping("/accounts/all")
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
}
