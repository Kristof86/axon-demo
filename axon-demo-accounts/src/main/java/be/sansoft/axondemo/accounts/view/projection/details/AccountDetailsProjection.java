package be.sansoft.axondemo.accounts.view.projection.details;

import be.sansoft.axondemo.accounts.domain.events.AccountCreatedEvent;
import be.sansoft.axondemo.accounts.domain.events.ChangeNameEvent;
import be.sansoft.axondemo.accounts.view.projection.overview.AccountDetailsRepository;
import be.sansoft.axondemo.accounts.view.query.FindAccountDetailsByIdQuery;
import be.sansoft.axondemo.accounts.view.query.FindAllAccountsQuery;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

/**
 * @author kristofennekens
 */
@Component
public class AccountDetailsProjection {

    private final AccountDetailsRepository repository;
    private final QueryUpdateEmitter queryUpdateEmitter;

    public AccountDetailsProjection(AccountDetailsRepository repository, QueryUpdateEmitter queryUpdateEmitter) {
        this.repository = repository;
        this.queryUpdateEmitter = queryUpdateEmitter;
    }

    @EventHandler
    public void handle(AccountCreatedEvent event) {
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

        queryUpdateEmitter.emit(FindAccountDetailsByIdQuery.class,
                query -> query.getId().equals(event.getId()),
                findDetails(new FindAccountDetailsByIdQuery(event.getId())));
    }

    @EventHandler
    public void handle(ChangeNameEvent event) {
        AccountDetailsEntity entity = repository.findById(event.getId()).get();
        entity.getData().setFirstName(event.getFirstName());
        entity.getData().setLastName(event.getLastName());

        queryUpdateEmitter.emit(FindAccountDetailsByIdQuery.class,
                query -> query.getId().equals(event.getId()),
                findDetails(new FindAccountDetailsByIdQuery(event.getId())));
    }

    @QueryHandler
    public AccountDetailsEntity findDetails(FindAccountDetailsByIdQuery query) {
        return repository.findById(query.getId()).orElse(null);
    }

}
