package be.sansoft.axondemo.accounts.view.projection.overview;

import lombok.*;

/**
 * @author kristofennekens
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountsOverviewRow {

    private String id, firstName, lastName, email, password;
}
