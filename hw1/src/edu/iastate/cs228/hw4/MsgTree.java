package edu.iastate.cs228.hw4;

/**
 * @author Asher Gust
 */
public class MsgTree {

    public char payloadChar;
    public MsgTree left;
    public MsgTree right;
    private static int staticCharIdx = 0;

    //Constructor building the tree from a string
    public MsgTree (String encodingString){
        if (encodingString.charAt(staticCharIdx) != '^'){
            payloadChar = encodingString.charAt(staticCharIdx);
        } else {
            payloadChar = 0;
            staticCharIdx++;
            left = new MsgTree(encodingString);
            staticCharIdx++;
            right = new MsgTree(encodingString);
        }

    }

    //Constructor for a single node with null children
    public MsgTree(char payloadChar){
        this.payloadChar = payloadChar;
    }

    public static void printCodes(MsgTree root, String code){
        if (root.payloadChar != 0){
            System.out.println(root.payloadChar + "             " + code);
        } else {
            //left branch
            printCodes(root.left, code + "0");
            //right branch
            printCodes(root.right, code + "1");
        }
    }

}
