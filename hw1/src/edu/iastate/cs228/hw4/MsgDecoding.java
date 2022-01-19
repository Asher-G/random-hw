package edu.iastate.cs228.hw4;

/**
 * @author Asher Gust
 */
public class MsgDecoding {

    private static int staticCharIdx = 0;
    public static int charCount = 0;
    private static boolean done = false;

    public static void decode(MsgTree codes, String msg){
        while (staticCharIdx < msg.length()){
            decodeRecursive(codes,msg);
        }
    }

    private static void decodeRecursive(MsgTree codes, String msg){
        if (codes.payloadChar != 0 && !done) {
            System.out.print(codes.payloadChar);
            charCount++;
            done = true;
        } else {
            done = false;
            if (msg.charAt(staticCharIdx) == '0') {
                staticCharIdx++;
                decodeRecursive(codes.left, msg);
            } else {
                staticCharIdx++;
                decodeRecursive(codes.right, msg);
            }
        }
    }
}
