package dhbw.cloudia.phonenumber.boundary;

import dhbw.cloudia.phonenumber.boundary.dto.PhoneNumberInputTO;
import dhbw.cloudia.phonenumber.boundary.dto.PhoneNumberOutputTO;
import dhbw.cloudia.phonenumber.control.PhoneNumberParsingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-API class containing the endpoints for the service concerning parsing phone number strings.
 */
@RestController
@RequestMapping(value = "/phone-number")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PhoneNumberApi {

    private final PhoneNumberParsingService phoneNumberParsingService;

    /**
     * REST-API method taking a phone number string and returning the parsed ISO 5008 phone number
     * @param phoneNumberInputTO Phone number object
     * @return parsed phone number in special object (ISO 5008)
     */
    @PostMapping
    public ResponseEntity<PhoneNumberOutputTO> transformPhoneNumber(@RequestBody PhoneNumberInputTO phoneNumberInputTO) {
        return ResponseEntity.ok(this.phoneNumberParsingService.parsePhoneNumber(phoneNumberInputTO));
    }
}
