package be.craftworkz.axondemo.accounts.core.domain.commands;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * @author kristofennekens
 */
@Slf4j
@Getter
public class DeleteAccountCommand {

    @TargetAggregateIdentifier
    private String id;

    private DeleteAccountCommand() {
        // needed for serialization
    }

    private DeleteAccountCommand(String id) {
        log.debug("New DeleteAccountCommand with id {}", id);
        this.id = id;
    }
    
    public static DeleteAccountCommand of(String id) {
        return new DeleteAccountCommand(id);
    }

}
