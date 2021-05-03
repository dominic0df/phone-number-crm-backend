package dhbw.cloudia.phonenumber.boundary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * phone number output TO holding phone number parts
 */
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
