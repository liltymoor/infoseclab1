package lilty.infra.infoseclab1.io.dto.auth;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class LogPassDto {
    String login;
    String pass;
}
