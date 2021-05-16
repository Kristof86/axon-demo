package be.sansoft.axondemo.accounts.domain.events;

import be.sansoft.axondemo.accounts.domain.commands.CreateAccountCommand;
import be.sansoft.axondemo.accounts.domain.commands.DeleteAccountCommand;
import lombok.Getter;

/**
 * @author kristofennekens
 */
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
