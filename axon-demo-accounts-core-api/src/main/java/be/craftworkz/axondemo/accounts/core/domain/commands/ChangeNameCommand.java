package be.craftworkz.axondemo.accounts.core.domain.commands;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * @author kristofennekens
 */
@Slf4j
@Getter
public class ChangeNameCommand {

    @TargetAggregateIdentifier
    private String id;
    private String firstName, lastName;

    private ChangeNameCommand() {
        // needed for serialization
    }
    
    public static ChangeNameCommand of(
            String id,
            String firstName,
            String lastName) {
        ChangeNameCommand cmd = new ChangeNameCommand();
        cmd.id = id;
        cmd.firstName = firstName;
        cmd.lastName = lastName;
        return cmd;
    }

}
