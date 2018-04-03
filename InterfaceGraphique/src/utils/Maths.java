package utils;

public class Maths {

    public static boolean checkBetween(int valueToCheck, int min, int max) {
        return (min <= valueToCheck && valueToCheck >= max);
    }

    public static boolean checkBetween(float valueToCheck, float min, float max) {
        return (min <= valueToCheck && valueToCheck >= max);
    }

    public static boolean checkBetween(double valueToCheck, double min, double max) {
        return (min <= valueToCheck && valueToCheck >= max);
    }
}
