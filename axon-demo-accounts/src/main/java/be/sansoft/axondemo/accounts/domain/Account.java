package be.sansoft.axondemo.accounts.domain;

import be.sansoft.axondemo.accounts.domain.commands.ChangeNameCommand;
import be.sansoft.axondemo.accounts.domain.commands.CreateAccountCommand;
import be.sansoft.axondemo.accounts.domain.commands.DeleteAccountCommand;
import be.sansoft.axondemo.accounts.domain.events.AccountCreatedEvent;
import be.sansoft.axondemo.accounts.domain.events.AccountDeletedEvent;
import be.sansoft.axondemo.accounts.domain.events.NameChangedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

/**
 * @author kristofennekens
 */
@Slf4j
@Aggregate
public class Account {

    @AggregateIdentifier
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public Account() {}

    @CommandHandler
    public Account(CreateAccountCommand cmd) {
        log.debug("Handle CreateAccountRequestCommand");
        AggregateLifecycle.apply(AccountCreatedEvent.fromCommand(cmd));
    }

    @CommandHandler
    public void handle(DeleteAccountCommand cmd) {
        AggregateLifecycle.apply(AccountDeletedEvent.fromCommand(cmd));
        AggregateLifecycle.markDeleted();
    }

    @CommandHandler
    public void handle(ChangeNameCommand cmd) {
        AggregateLifecycle.apply(NameChangedEvent.fromCommand(cmd));
    }

    @EventSourcingHandler
    public void handle(AccountCreatedEvent event) {
        log.debug("Handle AccountCreatedEvent");
        this.id = event.getId();
        this.firstName = event.getFirstName();
        this.lastName = event.getLastName();
        this.email = event.getEmail();
        this.password = event.getPassword();
    }

    @EventSourcingHandler
    public void handle(NameChangedEvent event) {
        this.firstName = event.getFirstName();
        this.lastName = event.getLastName();
    }

}
