import java.util.regex.Pattern;

public class Matchers {

    protected static boolean matcherNumber(String string){
        Pattern number = Pattern.compile("[0-9]");
        return number.matcher(string).matches();
    }

    protected static boolean matcherCapital(String string){
        Pattern capital = Pattern.compile("[A-Z]");
        return capital.matcher(string).matches();
    }

    protected static boolean matcherLower(String string){
        Pattern lower = Pattern.compile("[a-z]");
        return lower.matcher(string).matches();
    }

    protected static boolean matcherLowerBrackets(String string){
        Pattern lower_brackets = Pattern.compile("[()]");
        return lower_brackets.matcher(string).matches();
    }

    protected static boolean matcherMiddleBrackets(String string){
        Pattern middle_brackets = Pattern.compile("[\\[\\]]");
        return middle_brackets.matcher(string).matches();
    }

    protected static boolean matcherBrackets(String string){
        Pattern brackets = Pattern.compile("[\\[\\]()]");
        return brackets.matcher(string).matches();
    }

    protected static boolean matcherDot(String string){
        Pattern pattern = Pattern.compile("[·]");
        return pattern.matcher(string).matches();
    }

}
