package dhbw.cloudia.phonenumber.control;

import dhbw.cloudia.phonenumber.boundary.dto.PhoneNumberInputTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneNumberParsingServiceTest {

    private PhoneNumberParsingService phoneNumberParsingService;

    @BeforeEach
    void init(){
        this.phoneNumberParsingService = new PhoneNumberParsingService();
    }

    @Test
    void getOutputTokens() {
    }

    @Test
    void getPrefixWithoutBracketsCorrectlyPlus(){
        PhoneNumberInputTO phoneNumberInputTO = PhoneNumberInputTO.builder().phoneNumberString("+49 0201 123456").build();
        String expectedPrefix = "49";

        String actualPrefix = this.phoneNumberParsingService.parsePhoneNumber(phoneNumberInputTO).getPrefixNumber();
        assertEquals(expectedPrefix, actualPrefix);
    }

    @Test
    void getPrefixWithoutBracketsCorrectly(){
        PhoneNumberInputTO phoneNumberInputTO = PhoneNumberInputTO.builder().phoneNumberString("+49 0201 123456").build();
        String expectedPrefix = "49";

        String actualPrefix = this.phoneNumberParsingService.parsePhoneNumber(phoneNumberInputTO).getPrefixNumber();
        assertEquals(expectedPrefix, actualPrefix);
    }

    @Test
    void getPrefixWithoutBracketsCorrectlyPlusAndBrackets(){
        PhoneNumberInputTO phoneNumberInputTO = PhoneNumberInputTO.builder().phoneNumberString("(+49) 0201 123456").build();
        String expectedPrefix = "49";

        String actualPrefix = this.phoneNumberParsingService.parsePhoneNumber(phoneNumberInputTO).getPrefixNumber();
        assertEquals(expectedPrefix, actualPrefix);
    }

    @Test
    void getPrefixWithoutBracketsCorrectlyBrackets(){
        PhoneNumberInputTO phoneNumberInputTO = PhoneNumberInputTO.builder().phoneNumberString("(+49) 0201 123456").build();
        String expectedPrefix = "49";

        String actualPrefix = this.phoneNumberParsingService.parsePhoneNumber(phoneNumberInputTO).getPrefixNumber();
        assertEquals(expectedPrefix, actualPrefix);
    }

    @Test
    void getAreaCodeThreeDigitsNoZero(){
        PhoneNumberInputTO phoneNumberInputTO = PhoneNumberInputTO.builder().phoneNumberString("+49201 123456").build();
        String expectedPrefix = "201";

        String actualPrefix = this.phoneNumberParsingService.parsePhoneNumber(phoneNumberInputTO).getAreaCode();
        assertEquals(expectedPrefix, actualPrefix);
    }

    @Test
    void getAreaCodeFourDigitsNoZero(){
        PhoneNumberInputTO phoneNumberInputTO = PhoneNumberInputTO.builder().phoneNumberString("+492013123456").build();
        String expectedPrefix = "2013";

        String actualPrefix = this.phoneNumberParsingService.parsePhoneNumber(phoneNumberInputTO).getAreaCode();
        assertEquals(expectedPrefix, actualPrefix);
    }

    @Test
    void getAreaCodeFiveDigitsZero(){
        PhoneNumberInputTO phoneNumberInputTO = PhoneNumberInputTO.builder().phoneNumberString("+49 02013123456").build();
        String expectedPrefix = "0201";

        String actualPrefix = this.phoneNumberParsingService.parsePhoneNumber(phoneNumberInputTO).getAreaCode();
        assertEquals(expectedPrefix, actualPrefix);
    }

    @Test
    void getAreaCodeFourDigitsZero(){
        PhoneNumberInputTO phoneNumberInputTO = PhoneNumberInputTO.builder().phoneNumberString("+490203 123456").build();
        String expectedPrefix = "0203";

        String actualPrefix = this.phoneNumberParsingService.parsePhoneNumber(phoneNumberInputTO).getAreaCode();
        assertEquals(expectedPrefix, actualPrefix);
    }

    @Test
    void getAreaCodeThreeDigitsZero(){
        PhoneNumberInputTO phoneNumberInputTO = PhoneNumberInputTO.builder().phoneNumberString("+49 023 123456").build();
        String expectedPrefix = "023";

        String actualPrefix = this.phoneNumberParsingService.parsePhoneNumber(phoneNumberInputTO).getAreaCode();
        assertEquals(expectedPrefix, actualPrefix);
    }

    @Test
    void getNumber(){
        PhoneNumberInputTO phoneNumberInputTO = PhoneNumberInputTO.builder().phoneNumberString("+49 023 123456").build();
        String expectedPrefix = "123456";

        String actualPrefix = this.phoneNumberParsingService.parsePhoneNumber(phoneNumberInputTO).getAreaCode();
        assertEquals(expectedPrefix, actualPrefix);
    }

    @Test
    void getExtensionThreeDigits(){
        PhoneNumberInputTO phoneNumberInputTO = PhoneNumberInputTO.builder().phoneNumberString("+49 023 1234-456").build();
        String expectedPrefix = "456";

        String actualPrefix = this.phoneNumberParsingService.parsePhoneNumber(phoneNumberInputTO).getPhoneExtension();
        assertEquals(expectedPrefix, actualPrefix);
    }

    @Test
    void getExtensionTwoDigits(){
        PhoneNumberInputTO phoneNumberInputTO = PhoneNumberInputTO.builder().phoneNumberString("+49 023 1234-45").build();
        String expectedPrefix = "45";

        String actualPrefix = this.phoneNumberParsingService.parsePhoneNumber(phoneNumberInputTO).getPhoneExtension();
        assertEquals(expectedPrefix, actualPrefix);
    }

    @Test
    void getExtensionOneDigits(){
        PhoneNumberInputTO phoneNumberInputTO = PhoneNumberInputTO.builder().phoneNumberString("+49 023 1234-4").build();
        String expectedPrefix = "4";

        String actualPrefix = this.phoneNumberParsingService.parsePhoneNumber(phoneNumberInputTO).getPhoneExtension();
        assertEquals(expectedPrefix, actualPrefix);
    }

    @Test
    void getExtensionOneDigitsSlash(){
        PhoneNumberInputTO phoneNumberInputTO = PhoneNumberInputTO.builder().phoneNumberString("+49 023 1234/4").build();
        String expectedPrefix = "4";

        String actualPrefix = this.phoneNumberParsingService.parsePhoneNumber(phoneNumberInputTO).getPhoneExtension();
        assertEquals(expectedPrefix, actualPrefix);
    }

    @Test
    void getExtensionOneDigitsSpace(){
        PhoneNumberInputTO phoneNumberInputTO = PhoneNumberInputTO.builder().phoneNumberString("+49 023 1234 4").build();
        String expectedPrefix = "4";

        String actualPrefix = this.phoneNumberParsingService.parsePhoneNumber(phoneNumberInputTO).getPhoneExtension();
        assertEquals(expectedPrefix, actualPrefix);
    }
}