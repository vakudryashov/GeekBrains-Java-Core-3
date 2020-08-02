import Lesson_06.Lesson_06;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class Task_3 {
    private Lesson_06 lesson06 = new Lesson_06();

    private static Stream<Arguments> arraysProvider() {
        return Stream.of(
                Arguments.of(new int[]{}, false),
                Arguments.of(new int[]{1,4,1,1,1,4,1}, true),
                Arguments.of(new int[]{1,1,1,1}, false),
                Arguments.of(new int[]{4,4}, false),
                Arguments.of(new int[]{1,4}, true),
                Arguments.of(new int[]{4,1,2,1,1,4}, false)
        );
    }

    @ParameterizedTest
    @MethodSource("arraysProvider")
    public void test3(int[] testArr, boolean expected){
        Assertions.assertEquals(expected, lesson06.task3(testArr));
    }
}
