package dhbw.cloudia.phonenumber.boundary.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class ErrorTO {
    private String errorMessage;
}
