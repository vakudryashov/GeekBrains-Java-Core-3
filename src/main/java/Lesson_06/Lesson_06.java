package Lesson_06;

public class Lesson_06 {
    public int[] task2(int[] arr){
        int count = 0;
        int i;
        for (i = arr.length-1; i >= 0; i--) {
            if (arr[i] == 4) break;
            count++;
        }
        if (i < 0) throw new RuntimeException("Массив не содержит числа 4");
        int[] result = new int[count];
        for (int j = 0; j<count; j++) {
            result[j] = arr[++i];
        }
        return result;
    }

    public boolean task3(int[] arr){
        boolean findOne = false;
        boolean findFour = false;
        boolean findOther = false;
        for (int i :arr) {
            if (i == 1) findOne = true;
            else if (i == 4) findFour = true;
            else findOther = true;
            if (findOne && findFour && findOther) break;
        }
        return findOne && findFour && !findOther;
    }
}
