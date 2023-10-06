import java.util.*;
import java.util.stream.Collectors;

public class MainStreams {
    public static void main(String[] args) {
        System.out.println(minValue(new int[]{1, 2, 3, 3, 2, 3}));
        System.out.println(minValue(new int[]{9, 8}));
        System.out.println(oddOrEven(Arrays.asList(1, 2, 3, 4, 5, 6)));
        System.out.println(oddOrEven(Arrays.asList(1, 2, 3, 4, 5, 6, 7)));
    }

    public static int minValue(int[] values) {
        OptionalInt result = Arrays.stream(values).distinct().sorted().reduce((x, y) -> x * 10 + y);
        return result.isPresent() ? result.getAsInt() : -1;
    }

    public static List<Integer> oddOrEven(List<Integer> integers) {
        int sum = integers.stream().mapToInt(Integer::intValue).sum();
        return integers.stream().filter(x -> x % 2 != sum % 2).collect(Collectors.toList());
    }
}
