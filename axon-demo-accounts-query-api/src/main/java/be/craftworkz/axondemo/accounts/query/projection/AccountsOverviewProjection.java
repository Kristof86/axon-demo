package be.craftworkz.axondemo.accounts.query.projection;

import be.craftworkz.axondemo.accounts.core.domain.events.AccountCreatedEvent;
import be.craftworkz.axondemo.accounts.core.domain.events.AccountDeletedEvent;
import be.craftworkz.axondemo.accounts.core.domain.events.NameChangedEvent;
import be.craftworkz.axondemo.accounts.query.domain.AccountsOverviewEntity;
import be.craftworkz.axondemo.accounts.query.projection.queries.FindAllAccountsQuery;
import be.craftworkz.axondemo.accounts.query.repository.AccountsOverviewRepository;
import be.craftworkz.axondemo.accounts.query.domain.AccountsOverviewRow;
import be.craftworkz.axondemo.accounts.query.domain.AccountsOverviewRowWrapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author kristofennekens
 */
@Component
@Slf4j
public class AccountsOverviewProjection {

    private final AccountsOverviewRepository repository;
    private final QueryUpdateEmitter queryUpdateEmitter;

    public AccountsOverviewProjection(AccountsOverviewRepository repository, QueryUpdateEmitter queryUpdateEmitter) {
        this.repository = repository;
        this.queryUpdateEmitter = queryUpdateEmitter;
    }

    @EventHandler
    public void on(AccountCreatedEvent event) {
        log.debug("[EventHandler] Handle AccountDeletedEvent. Update Details");

        List<AccountsOverviewEntity> accounts = repository.findAll();
        if (CollectionUtils.isEmpty(accounts)) {
            repository.save(AccountsOverviewEntity.of(
                    AccountsOverviewRowWrapper.of(List.of(buildAccountsRow(event)))
            ));
        } else {
            List<AccountsOverviewRow> overviewRows =  accounts.get(0).getData().getRows();
            overviewRows.add(buildAccountsRow(event));
        }

        // Let subscription queries know, the details projection has been updated
        queryUpdateEmitter.emit(FindAllAccountsQuery.class,
                query -> true,
                findAll(new FindAllAccountsQuery()));
    }

    @EventHandler
    public void on(NameChangedEvent event) {
        log.debug("[EventHandler] Handle NameChangedEvent. Update Details");

        AccountsOverviewEntity entity = repository.findAll().get(0);
        entity.getData().getRows().stream().filter(row -> row.getId().equals(event.getId())).findFirst().ifPresent( row -> {
            row.setFirstName(event.getFirstName());
            row.setLastName(event.getLastName());

            // Let subscription queries know, the details projection has been updated
            queryUpdateEmitter.emit(FindAllAccountsQuery.class,
                    query -> true,
                    findAll(new FindAllAccountsQuery()));
        });
    }

    @EventHandler
    public void on(AccountDeletedEvent event) {
        log.debug("[EventHandler] Handle AccountDeletedEvent. Update Details");

        List<AccountsOverviewEntity> accounts = repository.findAll();
        if (CollectionUtils.isEmpty(accounts)) {
            return;
        }

        List<AccountsOverviewRow> overviewRows =  accounts.get(0).getData().getRows();
        overviewRows.remove(overviewRows.stream().filter(row -> row.getId().equals(event.getId())).findFirst().get());

        // Let subscription queries know, the details projection has been updated
        queryUpdateEmitter.emit(FindAllAccountsQuery.class,
                query -> true,
                findAll(new FindAllAccountsQuery()));
    }

    private AccountsOverviewRow buildAccountsRow(AccountCreatedEvent event) {
        return AccountsOverviewRow.builder()
                .id(event.getId())
                .firstName(event.getFirstName())
                .lastName(event.getLastName())
                .email(event.getEmail())
                .build();
    }

    @QueryHandler
    public AccountsOverviewEntity findAll(FindAllAccountsQuery query) {
        log.debug("[QueryHandler] Handle FindAllAccountsQuery");

        List<AccountsOverviewEntity> all = repository.findAll();
        return CollectionUtils.isEmpty(all) ? null : all.get(0);
    }
}
