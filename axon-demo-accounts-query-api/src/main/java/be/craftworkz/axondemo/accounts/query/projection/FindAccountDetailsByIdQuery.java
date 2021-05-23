package be.craftworkz.axondemo.accounts.query.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author kristofennekens
 */
@EqualsAndHashCode
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAccountDetailsByIdQuery {

    private String id;
}
