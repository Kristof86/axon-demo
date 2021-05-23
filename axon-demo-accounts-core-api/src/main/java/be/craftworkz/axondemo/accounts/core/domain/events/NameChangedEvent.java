package be.craftworkz.axondemo.accounts.core.domain.events;

import be.craftworkz.axondemo.accounts.core.domain.commands.ChangeNameCommand;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author kristofennekens
 */
@EqualsAndHashCode
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
