package be.sansoft.axondemo.accounts.domain.events;

import be.sansoft.axondemo.accounts.domain.commands.ChangeNameCommand;
import be.sansoft.axondemo.accounts.domain.commands.DeleteAccountCommand;
import lombok.Getter;

/**
 * @author kristofennekens
 */
@Getter
public class ChangeNameEvent {
    private String id, firstName, lastName;

    private ChangeNameEvent() {}

    public static ChangeNameEvent fromCommand(ChangeNameCommand cmd) {
        ChangeNameEvent event = new ChangeNameEvent();
        event.id = cmd.getId();
        event.firstName = cmd.getFirstName();
        event.lastName = cmd.getLastName();
        return event;
    }

}
