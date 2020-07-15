package Lesson_01;

import java.util.ArrayList;

public class Box<T extends Fruit> {
    private ArrayList<T> box;

    public Box() {
        this.box = new ArrayList<>();
    }

    public void put(T fruit) throws Exception{
        if (box.size() == 0 || box.get(0).getClass() == fruit.getClass()) {
            box.add(fruit);
        }else{
            throw new Exception(String.format("Нельзя положить %s в коробку c %s!%n",
                    fruit.getClass().getSimpleName(), box.get(0).getClass().getSimpleName()));
        }
    }
    public float getWeight(){
        float oneFruitWeight = 0;
        if (box.size() > 0) {
            if (box.get(0) instanceof Apple) oneFruitWeight = Apple.weight;
            else if (box.get(0) instanceof Orange) oneFruitWeight = Orange.weight;
        }
        return box.size() * oneFruitWeight;
    }

    public boolean compare(Box<?> anotherBox){
        return getWeight() == anotherBox.getWeight();
    }

    public void shift(Box<T> anotherBox) throws Exception{
        if (box.size() == 0) return;
        for (T fruit :box) {
            anotherBox.put(fruit);
        }
        box.clear();
    }
}
