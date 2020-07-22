package Lesson_02.client;

import java.io.*;

public class LogService {
    private static final String filePath = "chatLog.txt";

    public static String[] getLines(int lineCount){
        String[] result = new String[Math.abs(lineCount)];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            Object[] arr = reader.lines().toArray();
            int count = Math.min(result.length, arr.length);
            if (lineCount>0){
                for (int i = 0; i < count; i++) {
                    result[i] = (String) arr[i];
                }
            }else{
                for (int i = 0; i < count; i++) {
                    result[count - 1 - i] = (String) arr[arr.length - 1 - i];
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void append(String line){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,true));
            writer.write(line+"\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
