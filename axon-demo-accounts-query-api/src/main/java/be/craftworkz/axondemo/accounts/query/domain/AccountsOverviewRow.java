package be.craftworkz.axondemo.accounts.query.domain;

import lombok.*;

/**
 * @author kristofennekens
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountsOverviewRow {

    private String id, firstName, lastName, email;
}
