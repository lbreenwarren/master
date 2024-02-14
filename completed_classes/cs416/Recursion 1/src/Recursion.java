public class Recursion {
    public static boolean isPalindrome(String input) {
        if(input.length() <= 1) {
            return true;
        }
        else if(input.charAt(0) != input.charAt(input.length() - 1)) {
            return false;
        }
        else {
            String nextCall = input.substring(1,input.length() - 1);
            return isPalindrome(nextCall);
        }
    }

    public static int countLetter( String input, char letter ) {

        if (input.length() == 0) {
            return 0;
        }
        else if (input.charAt(0) == letter) {
            return 1 + countLetter(input.substring(1), letter);
        }
        else {
            return countLetter(input.substring(1), letter);
        }
    }

    public static int maxValue( int[] list, int n ) {
        if (n == 1) {
            return list[0];
        }
        else {
            return (Math.max(list[n-1], maxValue(list, n-1)));
        }
    }

}
