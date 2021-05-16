package be.sansoft.axondemo.accounts.view.projection.overview;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author kristofennekens
 */
@Data
@NoArgsConstructor
public class AccountsOverviewRowWrapper {

    private List<AccountsOverviewRow> rows;

    public static AccountsOverviewRowWrapper of(List<AccountsOverviewRow> rows) {
        AccountsOverviewRowWrapper wrapper = new AccountsOverviewRowWrapper();
        wrapper.rows = rows;
        return wrapper;
    }

}
