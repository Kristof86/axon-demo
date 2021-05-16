package be.sansoft.axondemo.accounts.web.request;

import be.sansoft.axondemo.accounts.domain.commands.ChangeNameCommand;
import be.sansoft.axondemo.accounts.domain.commands.CreateAccountCommand;
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
