package be.craftworkz.axondemo.accounts.core.domain.events;

import be.craftworkz.axondemo.accounts.core.domain.commands.DeleteAccountCommand;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author kristofennekens
 */
@EqualsAndHashCode
@Getter
public class AccountDeletedEvent {
    private String id;

    private AccountDeletedEvent() {}

    public static AccountDeletedEvent fromCommand(DeleteAccountCommand cmd) {
        AccountDeletedEvent event = new AccountDeletedEvent();
        event.id = cmd.getId();
        return event;
    }

}
