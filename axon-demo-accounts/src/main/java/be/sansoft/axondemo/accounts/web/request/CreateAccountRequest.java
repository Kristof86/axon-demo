package be.sansoft.axondemo.accounts.web.request;

import be.sansoft.axondemo.accounts.domain.commands.CreateAccountCommand;
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
