package vivian.smartshoppers;
import junit.framework.*;
/**
 * Created by John on 2/26/2016.
 */
public class ValidityCheckerTest {
    String validEmail = "ab@a.com";
    String invalidEmail = "abcdefg";
    String validPassword = "Aa!1Bb@2";
    String[] invalidPasswords = {"Abc123", "ABC!123", "abc@123", "ABC!abc"};
    String validCardNum = "1234123412341234";
    String invalidCardNum = "1234";
    String[] invalidStrings = {"a", "Ëˆ"};
    public void isEmailValidTest(){
        Assert.assertTrue(ValidityChecker.isEmailValid(validEmail));
        Assert.assertFalse(ValidityChecker.isEmailValid(invalidEmail));
    }
    public void isPasswordValidTest(){
        Assert.assertTrue(ValidityChecker.isPasswordValid(validPassword));
        for(String in: invalidPasswords){
            Assert.assertFalse(ValidityChecker.isPasswordValid(in));
        }
    }
    public void isCardNumValidTest(){
        Assert.assertTrue(ValidityChecker.isCardNumValid(validCardNum));
        Assert.assertFalse(ValidityChecker.isCardNumValid(invalidCardNum));
    }
    public void isStringValidTest(){
        for(String in:invalidStrings) {
            Assert.assertFalse(ValidityChecker.isStringValid(in));
        }
    }
}
