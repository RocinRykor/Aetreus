package studio.rrprojects.aetreus.utils;

public class RandomUtils {
    /**
     * @param min floor of random range
     * @param max cieling of max range
     * @return int
     */
    public static int getRandomRange(int min, int max) {
        return (int) ((Math.random() * (1 + max-min)) + min);
    }
}
