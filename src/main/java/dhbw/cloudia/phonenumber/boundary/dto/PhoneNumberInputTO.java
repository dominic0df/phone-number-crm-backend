package dhbw.cloudia.phonenumber.boundary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumberInputTO {
    private String phoneNumberString;
    private String countryCode = "DE";
}
