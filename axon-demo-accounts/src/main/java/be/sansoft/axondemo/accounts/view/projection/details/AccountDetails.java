package be.sansoft.axondemo.accounts.view.projection.details;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kristofennekens
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDetails {

    private String id, firstName, lastName, email, password;

}
