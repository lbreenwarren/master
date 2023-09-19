public class Main {

    public static boolean sleepIn(boolean weekday, boolean vacation) {
		if (!weekday || vacation){
            return true;
        }
        else
            return false;
	}

    public static boolean nearHundred(int x) {
        if ((x >= 90 && x <= 110) || (x>=190 && x<=210)){
            return true;
        }
        else
            return false;
    }

    public static boolean sameFirstLast(int[] nums) {
        if (nums[0] == nums[nums.length - 1]) {
            return true;
        }
        return false;
    }

    public static boolean no23(int[] nums) {
        for (int i = 0; i < 2; ++i) {
            if (nums[i] == 2 || nums[i]==3) {
                return false;
            } 
        }
        return true;
    }

    public static String helloName(String name) {
        return "Hello " + name + "!";
    }
}