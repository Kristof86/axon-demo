package be.sansoft.axondemo.accounts.view.projection.overview;

import be.sansoft.axondemo.accounts.view.projection.details.AccountDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author kristofennekens
 */

public interface AccountDetailsRepository extends JpaRepository<AccountDetailsEntity, String> {
}
