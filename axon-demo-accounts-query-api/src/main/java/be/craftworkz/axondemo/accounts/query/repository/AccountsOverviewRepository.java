package be.craftworkz.axondemo.accounts.query.repository;

import be.craftworkz.axondemo.accounts.query.domain.AccountsOverviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author kristofennekens
 */

public interface AccountsOverviewRepository extends JpaRepository<AccountsOverviewEntity, String> {

}
