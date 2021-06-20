package be.craftworkz.axondemo.accounts.command.domain;

import be.craftworkz.axondemo.accounts.core.domain.commands.ChangeNameCommand;
import be.craftworkz.axondemo.accounts.core.domain.commands.CreateAccountCommand;
import be.craftworkz.axondemo.accounts.core.domain.commands.DeleteAccountCommand;
import be.craftworkz.axondemo.accounts.core.domain.events.AccountCreatedEvent;
import be.craftworkz.axondemo.accounts.core.domain.events.AccountDeletedEvent;
import be.craftworkz.axondemo.accounts.core.domain.events.NameChangedEvent;
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
        log.debug("[CommandHandler] Handle CreateAccountRequestCommand");
        AggregateLifecycle.apply(AccountCreatedEvent.fromCommand(cmd));
    }

    @CommandHandler
    public void handle(DeleteAccountCommand cmd) {
        log.debug("[CommandHandler] Handle DeleteAccountCommand");
        AggregateLifecycle.apply(AccountDeletedEvent.fromCommand(cmd));
        AggregateLifecycle.markDeleted();
    }

    @CommandHandler
    public void handle(ChangeNameCommand cmd) {
        log.debug("[CommandHandler] Handle ChangeNameCommand");
        AggregateLifecycle.apply(NameChangedEvent.fromCommand(cmd));
    }

    @EventSourcingHandler
    public void handle(AccountCreatedEvent event) {
        log.debug("[EventSourcingHandler] Handle AccountCreatedEvent");
        this.id = event.getId();
        this.firstName = event.getFirstName();
        this.lastName = event.getLastName();
        this.email = event.getEmail();
        this.password = event.getPassword();
    }

    @EventSourcingHandler
    public void handle(NameChangedEvent event) {
        log.debug("[EventSourcingHandler] Handle NameChangedEvent");
        this.firstName = event.getFirstName();
        this.lastName = event.getLastName();
    }

}
