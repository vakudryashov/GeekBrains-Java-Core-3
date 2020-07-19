package Lesson_01;

import java.util.ArrayList;
import java.util.Arrays;

public class Task_1_2 {
    public static void main(String[] args) {

    }

    public static <T> void swap(T[] arr, int i, int j){
        T buf = arr[i];
        arr[i] = arr[j];
        arr[j] = buf;
    }

    public static <T> ArrayList<T> toArrayList(T[] arr){
        return new ArrayList<>(Arrays.asList(arr));
    }
}
