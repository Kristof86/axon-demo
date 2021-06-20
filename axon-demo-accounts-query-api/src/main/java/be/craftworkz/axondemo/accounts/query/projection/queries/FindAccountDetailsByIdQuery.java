package be.craftworkz.axondemo.accounts.query.projection.queries;

import lombok.*;

/**
 * @author kristofennekens
 */
@Getter
public class FindAccountDetailsByIdQuery {

    private String id;

    private FindAccountDetailsByIdQuery() {}

    public static FindAccountDetailsByIdQuery of(
String id) {
        FindAccountDetailsByIdQuery query =new FindAccountDetailsByIdQuery();
        query.id = id;
        return query;
    }
}
