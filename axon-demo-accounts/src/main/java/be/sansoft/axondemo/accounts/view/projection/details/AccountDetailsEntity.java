package be.sansoft.axondemo.accounts.view.projection.details;

import be.sansoft.axondemo.accounts.view.projection.JpaAccountDetailsJsonConverter;
import be.sansoft.axondemo.accounts.view.projection.JpaAccountsOverviewJsonConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author kristofennekens
 */
@Entity
@Table(name = "account_details")
public class AccountDetailsEntity {

    @Id
    private String id;

    @Getter
    @Setter
    @Column(name = "data", columnDefinition = "text")
    @Convert(converter = JpaAccountDetailsJsonConverter.class)
    private AccountDetails data;

    public static AccountDetailsEntity of(String id, AccountDetails data) {
        AccountDetailsEntity entity = new AccountDetailsEntity();
        entity.id = id;
        entity.data = data;
        return entity;
    }

}
