package be.sansoft.axondemo.accounts.domain.events;

import be.sansoft.axondemo.accounts.domain.commands.CreateAccountCommand;
import lombok.Getter;

/**
 * @author kristofennekens
 */
@Getter
public class AccountCreatedEvent {
    private String id, firstName, lastName, email, password;

    private AccountCreatedEvent() {}

    public static AccountCreatedEvent fromCommand(CreateAccountCommand cmd) {
        AccountCreatedEvent event = new AccountCreatedEvent();
        event.id = cmd.getId();
        event.firstName = cmd.getFirstName();
        event.lastName = cmd.getLastName();
        event.email = cmd.getEmail();
        event.password = cmd.getPassword();
        return event;
    }

}
