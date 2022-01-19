package edu.iastate.cs228.hw3;

import java.io.*;

/**
 *
 * @author Asher Gust
 *
 */
public class Infix2Postfix {
    public static void main(String[] args) {
        {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.txt")));
                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    writer.write(InfixExpression.postfix(line));
                    writer.newLine();
                } writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
