package be.sansoft.axondemo.accounts.view.projection.overview;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author kristofennekens
 */

public interface AccountsOverviewRepository extends JpaRepository<AccountsOverviewEntity, String> {
}
