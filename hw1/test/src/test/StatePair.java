package test;

public class StatePair <Type1 extends Comparable<Type1>, Type2 extends Comparable<Type2>> {
    private Type1 value1;
    private Type2 value2;

    // TODO: Define a constructor, mutators, and accessors
    //       for StatePair

    public StatePair(Type1 v1, Type2 v2) {
        value1 = v1;
        value2 = v2;
    }
    public void setValue1(Type1 v1){
        value1 = v1;
    }
    public void setValue2(Type2 v2){
        value2 = v2;
    }

    public Type1 getValue1(){
        return value1;
    }
    public Type2 getValue2(){
        return value2;
    }

    public void printInfo() {
        System.out.println(value1 + ": " + value2);
    }
}