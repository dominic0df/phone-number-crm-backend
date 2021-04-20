package dhbw.cloudia.phonenumber.control;

import dhbw.cloudia.phonenumber.boundary.dto.PhoneNumberInputTO;
import dhbw.cloudia.phonenumber.boundary.dto.PhoneNumberOutputTO;
import dhbw.cloudia.phonenumber.control.exception.PhoneNumberParseException;
import dhbw.cloudia.phonenumber.control.helper.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PhoneNumberParsingService {

    private static final Pattern PREFIX_PATTERN = Pattern.compile("(('0'|'+')[0-9][1-9]{2} | [\\(]('0'|'+')[0-9][1-9]{2}[\\)] | [\\[]('0'|'+')[0-9][1-9]{2}[\\[])", Pattern.CASE_INSENSITIVE);
    private static final Pattern AREA_CODE_PATTERN = Pattern.compile("[1-9]{3,4}", Pattern.CASE_INSENSITIVE);
    private static final Pattern AREA_CODE_PATTERN_WITHOUT_PREFIX = Pattern.compile("[1-9]{3,5}", Pattern.CASE_INSENSITIVE);
    private static final Pattern NUMBER_PATTERN = Pattern.compile("('/'|'-'|' ')?[1-9]+", Pattern.CASE_INSENSITIVE);
    private static final Pattern EXTENSION_PATTERN = Pattern.compile("('/'|'-'|' ')?[1-9]{1,3}", Pattern.CASE_INSENSITIVE);
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(PREFIX_PATTERN.pattern() +
                    AREA_CODE_PATTERN.pattern() +
                    NUMBER_PATTERN.pattern() +
                    EXTENSION_PATTERN.pattern(),
            Pattern.CASE_INSENSITIVE);
    private static final Pattern PHONE_NUMBER_PATTERN_WITHOUT_PREFIX = Pattern.compile(
                    AREA_CODE_PATTERN_WITHOUT_PREFIX.pattern() +
                    NUMBER_PATTERN.pattern() +
                    EXTENSION_PATTERN.pattern(),
            Pattern.CASE_INSENSITIVE);
    public static final String COUNTRY_PREFIX_IDENTIFICATOR = "+";

    public PhoneNumberOutputTO parsePhoneNumber(PhoneNumberInputTO phoneNumberInputTO) {
        PhoneNumberOutputTO phoneNumberOutputTO = new PhoneNumberOutputTO();
        String phoneNumber = phoneNumberInputTO.getPhoneNumberString().trim();
        boolean hasCountryPrefix = phoneNumber.startsWith(COUNTRY_PREFIX_IDENTIFICATOR);
        if (hasCountryPrefix) {
            phoneNumberOutputTO.setPrefixNumber(phoneNumber.substring(0, 3));
            phoneNumber = phoneNumber.substring(4, phoneNumber.length() - 1);
        }
        return null;
    }
//        String phoneNumber = phoneNumberInputTO.getPhoneNumberString().trim();
//        isPhoneNumberValid(phoneNumber);
//        PhoneNumberOutputTO phoneNumberOutputTO = PhoneNumberOutputTO.builder().build();
//
//        Matcher matcher = PREFIX_PATTERN.matcher(phoneNumber);
//        boolean prefixFound = matcher.find();
//        if (prefixFound) {
//            String oldString = phoneNumber;
//            phoneNumber = phoneNumber.replaceFirst(PREFIX_PATTERN.pattern(), "");
//            phoneNumberOutputTO.setPrefixNumber(oldString.substring(0, oldString.length() - phoneNumber.length()));
//        } else {
//            throw new PhoneNumberParseException("prefix of phone number could not be parsed: " + phoneNumber);
//        }
//
//        matcher = AREA_CODE_PATTERN.matcher(phoneNumber);
//        boolean areaCodeFound = matcher.find();
//        if (areaCodeFound) {
//            phoneNumberOutputTO.setAreaCode(phoneNumber.split(AREA_CODE_PATTERN.pattern())[0]);
//            phoneNumber = phoneNumber.replaceFirst(AREA_CODE_PATTERN.pattern(), "");
//        } else {
//            throw new PhoneNumberParseException("area code of phone number could not be parsed: " + phoneNumber);
//        }
//
//        matcher = NUMBER_PATTERN.matcher(phoneNumber);
//        boolean numberPatternFound = matcher.find();
//        if (numberPatternFound) {
//            phoneNumberOutputTO.setPhoneNumber(phoneNumber.split(NUMBER_PATTERN.pattern())[0]);
//            phoneNumber = phoneNumber.replaceFirst(NUMBER_PATTERN.pattern(), "");
//        } else {
//            throw new PhoneNumberParseException("number pattern of phone number could not be parsed: " + phoneNumber);
//        }
//
//        matcher = EXTENSION_PATTERN.matcher(phoneNumber);
//        boolean extensionFound = matcher.find();
//        if (extensionFound) {
//            phoneNumberOutputTO.setPhoneExtension(phoneNumber.split(EXTENSION_PATTERN.pattern())[0]);
//        }
//
//        return phoneNumberOutputTO;
//    }
//
//    private Tuple<String, String> returnParsingSolution(Pattern pattern, String phoneNumber) {
//        Matcher matcher = pattern.matcher(phoneNumber);
//        boolean prefixFound = matcher.find();
//        if (prefixFound) {
//            String newPhoneElement = phoneNumber;
//            phoneNumber = phoneNumber.replaceFirst(PREFIX_PATTERN.pattern(), "");
//            newPhoneElement = newPhoneElement.substring(0, newPhoneElement.length() - phoneNumber.length());
//            return new Tuple<>(newPhoneElement, phoneNumber);
//        } else {
//            throw new PhoneNumberParseException("prefix of phone number could not be parsed: " + phoneNumber);
//        }
//    }
//
//    private boolean isPhoneNumberValid(String phoneNumber){
//        Matcher matcher = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
//        boolean phoneNumberMatch = matcher.find();
//        if(phoneNumberMatch) {
//            return true;
//        } else {
//            throw new PhoneNumberParseException("Phone number is not valid: " + phoneNumber);
//        }
//    }
}
