package be.craftworkz.axondemo.accounts.query.projection;

import be.craftworkz.axondemo.accounts.core.domain.events.AccountCreatedEvent;
import be.craftworkz.axondemo.accounts.core.domain.events.AccountDeletedEvent;
import be.craftworkz.axondemo.accounts.core.domain.events.NameChangedEvent;
import be.craftworkz.axondemo.accounts.query.domain.AccountDetails;
import be.craftworkz.axondemo.accounts.query.domain.AccountDetailsEntity;
import be.craftworkz.axondemo.accounts.query.projection.queries.FindAccountDetailsByIdQuery;
import be.craftworkz.axondemo.accounts.query.repository.AccountDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

/**
 * @author kristofennekens
 */
@Component
@Slf4j
public class AccountDetailsProjection {

    private final AccountDetailsRepository repository;
    private final QueryUpdateEmitter queryUpdateEmitter;

    public AccountDetailsProjection(AccountDetailsRepository repository, QueryUpdateEmitter queryUpdateEmitter) {
        this.repository = repository;
        this.queryUpdateEmitter = queryUpdateEmitter;
    }

    @EventHandler
    public void on(AccountCreatedEvent event) {
        log.debug("[EventHandler] Handle AccountCreatedEvent. Update Overview");

        repository.findById(event.getId()).ifPresentOrElse(
                entity -> {
                    entity.getData().setFirstName(event.getFirstName());
                    entity.getData().setLastName(event.getLastName());
                    entity.getData().setEmail(event.getEmail());
                    entity.getData().setPassword(event.getPassword());
                },
                () -> {
                    repository.save(
                            AccountDetailsEntity.of(
                                    event.getId(),
                                    AccountDetails.builder()
                                            .id(event.getId())
                                            .firstName(event.getFirstName())
                                            .lastName(event.getLastName())
                                            .email(event.getEmail())
                                            .password(event.getPassword())
                                            .build()
                            )
                    );
                }
        );

        // Let subscription queries know, the overview projection has been updated
        queryUpdateEmitter.emit(FindAccountDetailsByIdQuery.class,
                query -> query.getId().equals(event.getId()),
                findDetails(FindAccountDetailsByIdQuery.of(event.getId())));
    }

    @EventHandler
    public void on(NameChangedEvent event) {
        log.debug("[EventHandler] Handle NameChangedEvent. Update Overview");

        repository.findById(event.getId()).ifPresent(entity -> {
            entity.getData().setFirstName(event.getFirstName());
            entity.getData().setLastName(event.getLastName());

            // Let subscription queries know, the overview projection has been updated
            queryUpdateEmitter.emit(FindAccountDetailsByIdQuery.class,
                    query -> query.getId().equals(event.getId()),
                    findDetails(FindAccountDetailsByIdQuery.of(event.getId())));
            }
        );
    }

    @EventHandler
    public void on(AccountDeletedEvent event) {
        log.debug("[EventHandler] Handle AccountDeletedEvent. Update Overview");

        repository.findById(event.getId()).ifPresent(entity -> {
            repository.delete(entity);

            // Let subscription queries know, the overview projection has been updated
            queryUpdateEmitter.emit(FindAccountDetailsByIdQuery.class,
                    query -> query.getId().equals(event.getId()),
                    findDetails(FindAccountDetailsByIdQuery.of(event.getId())));
        });
    }

    @QueryHandler
    public AccountDetailsEntity findDetails(FindAccountDetailsByIdQuery query) {
        log.debug("[QueryHandler] Handle FindAccountDetailsByIdQuery");

        return repository.findById(query.getId()).orElse(null);
    }
}
