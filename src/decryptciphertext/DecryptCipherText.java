/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decryptciphertext;

/**
 *
 * @author gianganderson
 */
import java.io.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
public class DecryptCipherText {

    /**
     * @param args the command line arguments
     */
    private static String cipherText = "DRPWPWXHDRDKDUBKIHQVQRIKPGWOVOESWPKPVOBBDVVVDXSURWRLUEBKOLVHIHBKHLHBLNDQRFLOQ";
//    DRPWPWXHDRDKDUBKIHQVQRIKPGWOVOESWPKPVOBBDVVVDXSURWRLUEBKOLVHIHBKHLHBLNDQRFLOQ  
//    FUBHAEPPFOYRTEMHOMNTETHSMIOMNTEISOUYRLFEIBYHAKYYMOAHADANLSTOISLACSSRIEALY
    
    public static void main(String[] args) {
        // TODO code application logic here
        run();
    }
    
    public static void run() {
        String shiftedText = "";
//        for (int i = 1; i < 26; i++) {
//            shiftedText = shiftCipher(cipherText, i);
//            permutation(shiftedText, "12345");
//        }
        shiftedText = shiftCipher(cipherText, 23);
        permutation(shiftedText, "12345");
    }
    
    /**
     * 
     * @param str
     * @param shiftParameter 
     * Shifts each character in the string shiftParameter amount of times.
     * @return 
     */

    public static String shiftCipher(String str, int shiftParameter) {
        //Cannot shift more than 26 characters
        if (shiftParameter >= 26) {
            return "Not valid.";
        }
        str = str.toUpperCase();
        //
        StringBuilder sb = new StringBuilder();
    	for (int i = 0; i < str.length(); i++) {
                int v = ((int) str.charAt(i)) + shiftParameter;
                //if the value is above 90 you want to reset back to the decimal value of 65, 65 = A.....
                if (v > 90) {
                    //find how many shifts more than 90, since 90 = Z
                    int diff = v - 90;
                    //go back to start + the difference, if difference was 1, then 64 + 1 = 65 = A
                    v = 64 + diff;
                }
                //append to stringbuilder
                sb.append((char) v );
    	}
        return sb.toString();
    }
    
    public static String shiftLeftCipher(String str, int shiftParameter) {
        if (shiftParameter >= 26) {
            return "Not valid.";
        }
        str = str.toUpperCase();
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            int v = ((int) str.charAt(i)) - shiftParameter;
            if (v < 65) {
                int diff = 65 - v;
                v = 91 - diff;
            }
            sb.append((char) v);
        }
        
        return sb.toString();
    }
    
    /**
     * 
     * @param str
     * @param key 
     * Splits the string up by the number of rows. "24315"
     * Places the split up characters in an ArrayList of Strings. Adding the elements in the array list gives me the order.
     * and the index of the key, for example list.get(2) will fill up column 0, and list.get(4) will fill up column 1, and so on..
     */
    public static void columnarTransposition(String str, String key) {
        int numOfCols = key.length();
        int strLength = str.length();
        int numOfRows = 0;
        int charsPad = 0;
        //hold the first two keys
        ArrayList<String> splittedStrings = new ArrayList<String>();
        ArrayList<Integer> keys = new ArrayList<Integer>();
        if (str.length() % key.length() != 0) {
             numOfRows = str.length() / key.length() + 1;
             int mod = str.length() % key.length();
             charsPad = key.length() - mod;
        } else {
            numOfRows = str.length() / key.length();
        }
        char[][] arr = new char[numOfRows][numOfCols];
        //if the the key length is 6 and the string needs to be added by 4 letters. keySplit = 2.
        //this means take the first two integers in the key.
        int keySplit = key.length() - charsPad;
        //add them to an arraylist to check if you want to split by numOfRows or numOfRows -1
        for (int i = 0; i < keySplit; i++) {
            keys.add(Character.getNumericValue(key.charAt(i)));
        }
        //to split the string
        int currentPos = 0;
        for (int i = 1; i < key.length() + 1; i++) {
            if (!keys.contains(i)) {
                //split by numOfRows -1
                splittedStrings.add(str.substring(currentPos, currentPos + numOfRows - 1));
                currentPos += numOfRows -1;
            }
            else {
                //split by numOfRows 
                splittedStrings.add(str.substring(currentPos, currentPos + numOfRows));
                currentPos += numOfRows;
            }
        }
        
        for (int i = 0; i < key.length(); i++) {
            int pos = Character.getNumericValue(key.charAt(i));
            //retrieve its element in the array list, 453621 would be list.get(3) and I know that '4' is in the 0th index.
            //so it should be placed into the 0th column, 5 should be placed in the 1st column, etc...
            String split = splittedStrings.get(pos - 1);
            //place it horizontally into the 2D array.
            for (int k = 0; k < split.length(); k++) {
                arr[k][i] = split.charAt(k);
            }
        }
        printArray(arr, numOfRows, numOfCols);
        displayOutput(arr, numOfRows, numOfCols);
    }
//    public static void columnarTransposition(String str, String key) {
//        int numOfRows = str.length() / key.length();
//        int numOfCols = key.length();
//        int strLength = str.length();
//        char[][] arr = new char[numOfRows][numOfCols];
//        ArrayList<String> list = new ArrayList<String>();
//        list.add(str.substring(0, numOfRows));
//        strLength -= numOfRows;
//        //split by the number of rows
//        for (int i = numOfRows; i < str.length(); i+=numOfRows) {
//            //when the string length is not divisible by the key length, must also pad the array at the end?
//            if (strLength < numOfRows) {
//                list.add(str.substring(i, i + strLength));
//            }
//            else{
//                list.add(str.substring(i, i + numOfRows));
//            }
//            strLength -= numOfRows;
//        }
//        //look at each character in the key
//        for (int i = 0; i < key.length(); i++) {
//            int pos = Character.getNumericValue(key.charAt(i));
//            //retrieve its element in the array list, 453621 would be list.get(3) and I know that '4' is in the 0th index.
//            //so it should be placed into the 0th column, 5 should be placed in the 1st column, etc...
//            String split = list.get(pos - 1);
//            //place it horizontally into the 2D array.
//            for (int k = 0; k < split.length(); k++) {
//                arr[k][i] = split.charAt(k);
//            }
//        }
//        printArray(arr, numOfRows, numOfCols);
//        displayOutput(arr, numOfRows, numOfCols);
//    }
    
    public static void printArray(char[][] arr, int rowLength, int colLength) {
        for (int i = 0; i < rowLength; i++ ) {
            for (int k = 0; k < colLength; k++) {
                System.out.print(arr[i][k] +  " ");
            }
        }
        System.out.println();
    }
    
    public static void displayOutput(char[][] arr, int rowLength, int colLength) {
        try {
            PrintWriter output = new PrintWriter(new FileWriter("decrypted.txt", true));
            for (int i = 0; i < rowLength; i++ ) {
                for (int k = 0; k < colLength; k++) {
                    output.print(arr[i][k] + " ");
                }
            }    
            output.println();
            output.close();
 
        } 
        
        catch (IOException ex) {
            System.out.println("decrpyted.txt not found");
            return;
        }
    }
    
    public static void permutation(String shiftedText, String str) { 
        permutation(shiftedText, "", str); 
    }

    private static void permutation(String shiftedText,String prefix, String str) {
        int n = str.length();
        if (n == 0) {
            columnarTransposition(shiftedText, prefix);
        }
        else {
            for (int i = 0; i < n; i++)
                permutation(shiftedText,prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n));
        }
    }
    
}
