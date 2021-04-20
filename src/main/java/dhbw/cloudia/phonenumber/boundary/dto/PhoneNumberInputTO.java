package dhbw.cloudia.phonenumber.boundary.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
public class PhoneNumberInputTO {
    private String phoneNumberString;
    private String countryCode = "DE";
}
