package dhbw.cloudia.phonenumber.boundary.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class PhoneNumberOutputTO {
    private String prefixNumber;
    private String areaCode;
    private String phoneNumber;
    private String phoneExtension;
}
