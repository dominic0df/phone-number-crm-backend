package dhbw.cloudia.phonenumber.boundary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Error TO holding error message for end user
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorTO {
    private String errorMessage;
}
