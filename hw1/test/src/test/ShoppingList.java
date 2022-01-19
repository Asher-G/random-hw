package test;

import java.util.Scanner;
import java.util.LinkedList;

public class ShoppingList {
    public static void main (String[] args) {
        Scanner scnr = new Scanner(System.in);

        // TODO: Declare a LinkedList called shoppingList of type ListItem
        LinkedList<ListItem> list = new LinkedList<ListItem>();
        String item;

        // TODO: Scan inputs (items) and add them to the shoppingList LinkedList
        //       Read inputs until a -1 is input
        Scanner in = new Scanner(System.in);
        while(true){
            item = in.nextLine();
            if (item.equalsIgnoreCase("-1")) break;
            list.add(new ListItem(item));
        }


        // TODO: Print the shoppingList LinkedList using the printNodeData() method
        for (int i = 0; i < list.size(); i++){
            list.get(i).printNodeData();
        }

    }
}