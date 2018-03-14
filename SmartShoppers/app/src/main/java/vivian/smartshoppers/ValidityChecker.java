package vivian.smartshoppers;

/**
 * Created by John on 2/26/2016.
 */
public class ValidityChecker {
    public static boolean isEmailValid(String email){

        return email.matches(".*@.*\\..*");
    }
    public static boolean isPasswordValid(String password){

        if(password.length()<6) return false;
        if (!password.matches(".*[A-Z].*")) return false;
        if (!password.matches(".*[a-z].*")) return false;
        if (!password.matches(".*\\d.*")) return false;
        if (!password.matches(".*[~!.].*")) return false;

        return true;
    }
    public static boolean isCardNumValid(String num){
        if(num.matches(".*^\\d.*")) return false;
        return num.length()==16;
    }
    public static boolean isStringValid(String in){

        return !in.matches(".*:[^\\p{ASCII}].*");

    }

}
