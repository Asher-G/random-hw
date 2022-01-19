package test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class Test {

    private static void quickSortRec(int[] numbers, int first, int last)
    {
        if (first >= last) return;

        int firstEnd = partition(numbers, first, last);

        quickSortRec(numbers, first, firstEnd);
        quickSortRec(numbers,firstEnd +1, last);
    }


    private static int partition(int[] numbers, int first, int last)
    {
        int pivot = numbers[last];
        while (true){
            while (numbers[first] < pivot){
                first++;
            }
            while (pivot < numbers[last]){
                last--;
            }
            if (first >= last) {
                return last;
            } else {
                int temp = numbers[first];
                numbers[first] = numbers[last];
                numbers[last] = temp;
                first++;
                last--;
            }

        }
    }

    public static void main(String args[]){

//        String st = "   1 + -2 * 3";
//        Scanner scnr = new Scanner(st);
//
//        while (scnr.hasNext()){
//            System.out.println(scnr.next());
//        }

        Deque<Integer> stack = new ArrayDeque<Integer>();
        stack.addFirst(1);
        stack.addFirst(2);
        stack.addFirst(3);
        System.out.println(stack.peek());
        System.out.println(stack.pop());
        System.out.println(stack.peek());

    }
}
