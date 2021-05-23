package be.craftworkz.axondemo.accounts.query.repository;

import be.craftworkz.axondemo.accounts.query.domain.AccountDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author kristofennekens
 */

public interface AccountDetailsRepository extends JpaRepository<AccountDetailsEntity, String> {
}
