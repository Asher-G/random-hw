package edu.iastate.cs228.hw4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 * @author Asher Gust
 */
public class ArchivedMessageReconstruction {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(System.in);
        System.out.print("Please enter filename to decode: ");
        Scanner scnr = new Scanner(new File(in.next()));

        String treeCode = scnr.useDelimiter("\\A").next();
        treeCode = treeCode.substring(0, treeCode.lastIndexOf("\n"));

        String message = treeCode.substring(treeCode.lastIndexOf("\n") + 1);
        treeCode = treeCode.substring(0, treeCode.lastIndexOf("\n"));
        MsgTree treeRoot = new MsgTree(treeCode);

        System.out.println("\n\n character    code");
        System.out.println("--------------------");
        MsgTree.printCodes(treeRoot, "");
        System.out.println("\nMESSAGE:");
        MsgDecoding.decode(treeRoot, message);

        System.out.println("\n\nSTATISTICS: ");
        double bitsPerChar = message.length() / (double) MsgDecoding.charCount;
        System.out.printf("Avg bits/char: %.01f \n", bitsPerChar);
        System.out.println("Total Characters: " + MsgDecoding.charCount);
        double spaceSavings = (1 - bitsPerChar/16.0) * 100;
        System.out.printf("Space savings: %.01f \n", spaceSavings);
        System.out.println("%");

        in.close();
        scnr.close();
    }
}
