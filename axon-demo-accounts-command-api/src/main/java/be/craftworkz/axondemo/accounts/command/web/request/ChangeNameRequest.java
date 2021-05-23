package be.craftworkz.axondemo.accounts.command.web.request;

import be.craftworkz.axondemo.accounts.core.domain.commands.ChangeNameCommand;
import lombok.Getter;
import lombok.ToString;

/**
 * @author kristofennekens
 */
@ToString
@Getter
public class ChangeNameRequest {

    private String firstName, lastName;

    public ChangeNameCommand toCommand(String id) {
        return ChangeNameCommand.of(
                id,
                firstName,
                lastName
        );
    }

}
