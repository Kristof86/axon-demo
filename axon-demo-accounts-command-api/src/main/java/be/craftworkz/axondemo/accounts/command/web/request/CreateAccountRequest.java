package be.craftworkz.axondemo.accounts.command.web.request;

import be.craftworkz.axondemo.accounts.core.domain.commands.CreateAccountCommand;
import lombok.Getter;
import lombok.ToString;

/**
 * @author kristofennekens
 */
@ToString
@Getter
public class CreateAccountRequest {

    private String firstName, lastName, email, password;

    public CreateAccountCommand toCommand() {
        return CreateAccountCommand.of(
                firstName,
                lastName,
                email,
                password
        );
    }

}
