package be.sansoft.axondemo.accounts.domain.events;

import be.sansoft.axondemo.accounts.domain.commands.ChangeNameCommand;
import be.sansoft.axondemo.accounts.domain.commands.DeleteAccountCommand;
import lombok.Getter;

/**
 * @author kristofennekens
 */
@Getter
public class NameChangedEvent {
    private String id, firstName, lastName;

    private NameChangedEvent() {}

    public static NameChangedEvent fromCommand(ChangeNameCommand cmd) {
        NameChangedEvent event = new NameChangedEvent();
        event.id = cmd.getId();
        event.firstName = cmd.getFirstName();
        event.lastName = cmd.getLastName();
        return event;
    }

}
