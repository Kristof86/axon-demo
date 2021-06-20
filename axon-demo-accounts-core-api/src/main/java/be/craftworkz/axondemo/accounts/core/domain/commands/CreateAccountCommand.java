package be.craftworkz.axondemo.accounts.core.domain.commands;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

/**
 * @author kristofennekens
 */
@Getter
public class CreateAccountCommand {

    private final String id;
    private String firstName, lastName, email, password;

    private CreateAccountCommand() {
        this.id = UUID.randomUUID().toString();
    }

    public static CreateAccountCommand of(
            String firstName,
            String lastName,
            String email,
            String password) {
        CreateAccountCommand cmd = new CreateAccountCommand();
        cmd.firstName = firstName;
        cmd.lastName = lastName;
        cmd.email = email;
        cmd.password = password;
        return cmd;
    }
}
