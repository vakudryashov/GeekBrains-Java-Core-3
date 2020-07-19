package Lesson_01;

public class Task_3 {
    public static void main(String[] args) throws Exception {

        System.out.println("Создадим три коробки.");
        Box box1 = new Box();
        Box box2 = new Box();
        Box box3 = new Box();

        System.out.println("Положим в первую 3 яблока ");
        for (int i = 0; i < 3; i++) {
            box1.put(new Apple());
        }

        System.out.println("Положим во вторую 3 апельсина ");
        for (int i = 0; i < 3; i++) {
            box2.put(new Orange());
        }

        System.out.println("Вес коробки 1: "+box1.getWeight());
        System.out.println("Вес коробки 2: "+box2.getWeight());
        System.out.println("Вес коробки 3: "+box3.getWeight());
        System.out.println("Коробки 1 и 2 весят одинаково: "+box1.compare(box2));

        System.out.println("\nПереложим всё из первой коробки в третью.");
        box1.shift(box3);
        System.out.println("Вес коробки 1: "+box1.getWeight());
        System.out.println("Вес коробки 2: "+box2.getWeight());
        System.out.println("Вес коробки 3: "+box3.getWeight());

        System.out.println("\nПереложим всё из второй коробки в первую.");
        box2.shift(box1);
        System.out.println("Вес коробки 1: "+box1.getWeight());
        System.out.println("Вес коробки 2: "+box2.getWeight());
        System.out.println("Вес коробки 3: "+box3.getWeight());

        System.out.println("\nПоложим во вторую коробку два апельсина");
        for (int i = 0; i < 2; i++) {
            box2.put(new Orange());
        }
        System.out.println("Вес коробки 1: "+box1.getWeight());
        System.out.println("Вес коробки 2: "+box2.getWeight());
        System.out.println("Вес коробки 3: "+box3.getWeight());
        System.out.println("Коробки 1 и 2 весят одинаково: "+box2.compare(box1));
        System.out.println("Коробки 2 и 3 весят одинаково: "+box2.compare(box3));
    }
}
