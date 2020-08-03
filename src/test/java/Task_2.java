import Lesson_06.Lesson_06;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class Task_2 {
    private Lesson_06 lesson06 = new Lesson_06();

    private static Stream<Arguments> arraysProvider() {
        return Stream.of(
                Arguments.of(new int[]{1,2,3,5,6,7}, RuntimeException.class),
                Arguments.of(new int[]{1,2,3,4,5,6,7}, new int[]{5,6,7}),
                Arguments.of(new int[]{4,2,3,5,6,4,7}, new int[]{7}),
                Arguments.of(new int[]{4,2,3,4,5,6,4,7,4}, new int[]{})
        );
    }

    @ParameterizedTest
    @MethodSource("arraysProvider")
    public void test2(int[] testArr, Object expected){
        if (expected instanceof int[]) Assertions.assertArrayEquals((int[]) expected, lesson06.task2(testArr));
        else Assertions.assertThrows(RuntimeException.class,()->{
            lesson06.task2(testArr);
        });
    }
}
