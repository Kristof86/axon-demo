package be.craftworkz.axondemo.accounts.query.domain;

import be.craftworkz.axondemo.accounts.query.domain.json.JpaAccountsOverviewJsonConverter;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author kristofennekens
 */
@Entity
@Table(name = "accounts_overview")
public class AccountsOverviewEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    @Getter
    @Setter
    @Column(name = "data", columnDefinition = "text")
    @Convert(converter = JpaAccountsOverviewJsonConverter.class)
    private AccountsOverviewRowWrapper data;

    public static AccountsOverviewEntity of(AccountsOverviewRowWrapper data) {
        AccountsOverviewEntity entity = new AccountsOverviewEntity();
        entity.data = data;
        return entity;
    }
}
