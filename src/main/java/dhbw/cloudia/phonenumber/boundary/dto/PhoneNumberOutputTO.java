package dhbw.cloudia.phonenumber.boundary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PhoneNumberOutputTO {
    private String prefixNumber;
    private String areaCode;
    private String phoneNumber;
    private String phoneExtension;
}
