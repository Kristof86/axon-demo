package be.sansoft.axondemo.accounts.view.projection;

import be.sansoft.axondemo.accounts.view.projection.overview.AccountsOverviewRowWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

/**
 * @author kristofennekens
 */
@Slf4j
@Converter
public class JpaAccountsOverviewJsonConverter implements AttributeConverter<AccountsOverviewRowWrapper, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(AccountsOverviewRowWrapper meta) {
        try {
            return objectMapper.writeValueAsString(meta);
        } catch (JsonProcessingException ex) {
            log.error("Could not convert to json");
            throw new RuntimeException(ex);
        }
    }

    @Override
    public AccountsOverviewRowWrapper convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, AccountsOverviewRowWrapper.class);
        } catch (IOException ex) {
            log.error("Could not deserialize json");
            throw new RuntimeException(ex);
        }
    }
}
