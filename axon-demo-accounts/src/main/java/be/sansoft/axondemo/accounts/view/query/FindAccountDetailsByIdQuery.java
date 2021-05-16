package be.sansoft.axondemo.accounts.view.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author kristofennekens
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAccountDetailsByIdQuery {

    private String id;
}
