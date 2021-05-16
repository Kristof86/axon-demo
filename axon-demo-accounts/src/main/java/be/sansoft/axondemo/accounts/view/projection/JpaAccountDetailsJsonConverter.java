package be.sansoft.axondemo.accounts.view.projection;

import be.sansoft.axondemo.accounts.view.projection.details.AccountDetails;
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
public class JpaAccountDetailsJsonConverter implements AttributeConverter<AccountDetails, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(AccountDetails meta) {
        try {
            return objectMapper.writeValueAsString(meta);
        } catch (JsonProcessingException ex) {
            log.error("Could not convert to json");
            throw new RuntimeException(ex);
        }
    }

    @Override
    public AccountDetails convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, AccountDetails.class);
        } catch (IOException ex) {
            log.error("Could not deserialize json");
            throw new RuntimeException(ex);
        }
    }
}
