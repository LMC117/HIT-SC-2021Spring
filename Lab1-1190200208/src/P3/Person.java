package P3;

public class Person {
    private String name;
    private int index = -1;

    public Person(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
