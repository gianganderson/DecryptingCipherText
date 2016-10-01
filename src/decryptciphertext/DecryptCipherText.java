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
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
public class DecryptCipherText {

    /**
     * @param args the command line arguments
     */
    private static String cipherText = "DRPWPWXHDRDKDUBKIHQVQRIKPGWOVOESWPKPVOBBDVVVDXSURWRLUEBKOLVHIHBKHLHBLNDQRFLOQ";
    private static String shiftedText = "";
    private static HashMap<String, Integer> hm = new HashMap<String, Integer>();
    
    public static void main(String[] args) {
        // TODO code application logic here
        run();
        String msg = maxWordOccurence();
        System.out.println(msg);
        displayOutput(msg);
    }
    
    /**
     * shifts the encrypted message i times, then checks all the permutations.
     */
    
    public static void run() {
        String key = "";
        ArrayList<String> keyLength = new ArrayList<String>();
        keyLength = returnAllKeys(keyLength);
         
        for (int i = 1; i < 26; i++) {
            for (int j = 0; j < keyLength.size(); j++) {
                shiftedText = shiftCipher(cipherText, i);
                permutation(shiftedText, keyLength.get(j));
            }
        }
     
    }
    
    /**
     * 
     * @param keyLength
     * @return ArrayList of all keys
     * 1, 12, 123, 1234, 12345, 123456..
     */
    
    public static ArrayList<String> returnAllKeys(ArrayList<String> keyLength) {
        for (int i = 0; i < 5; i++) {
            if (i >= 1) {
                String someKey = keyLength.get(i - 1);
                someKey += String.valueOf(i + 1);
                keyLength.add(someKey);
            }else {
                keyLength.add(String.valueOf(i + 1)); 
            }                    
        }       
        return keyLength;
    }
    
    /**
     * 
     * @param str
     * @return number of words in the dictionary that are contained in the decrypted ciphertext
     */
    
    
    public static int readFile(String str) {
        int counter = 0;
        try {
           Scanner input = new Scanner(new File("dictionary.txt"));
           while (input.hasNext()) {
               String word = input.next().toUpperCase();
               if (str.contains(word)) {
                   counter++;
               }
           }

        }
        catch(FileNotFoundException e) {
            System.out.println("dictionary.txt not found");
            return -1;
        }
        return counter;
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
    
    /**
     * 
     * @param str
     * @param key 
     * Splits the string up by the number of rows. "24315"
     * Places the split up characters in an ArrayList of Strings. Adding the elements in the array list gives me the order.
     * and the index of the key, for example list.get(2) will fill up column 0, and list.get(4) will fill up column 1, and so on..
     * 
     * The most difficult part of this assignment was when you had to decrypt a ciphertext that required padding. 
     * I used this website to figure out the algorithm.
     * http://crypto.interactive-maths.com/columnar-transposition-cipher.html
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
        trimDownFile(arr, numOfRows, numOfCols, key);
    }
    
    /**
     * 
     * @param arr
     * @param rowLength
     * @param colLength
     * @param key 
     * Adds all of the decrypted ciphertext along with their "contained number of words" into a HashMap
     * The key is the decrypted cipher text and the value is the counter of words.
     */
    public static void trimDownFile(char[][] arr, int rowLength, int colLength, String key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rowLength; i++ ) {
            for (int k = 0; k < colLength; k++) {
                sb.append(arr[i][k]);
            }
        }
        int count = readFile(sb.toString());

        if (count > 25) {
            hm.put(sb.toString(), count);
        }
    }
    
    /**
     * 
     * @return the string with the maximum number of words contained in the decrypted ciphertext.
     */
    
    public static String maxWordOccurence() {
        int count = 0;
        int maxCount = 0;
        String decrypted = "";
        for (Map.Entry<String, Integer> entry : hm.entrySet()) {
            count = entry.getValue();
            if (count > maxCount) {
                maxCount = count;
                decrypted = entry.getKey();
            }
        }
        return decrypted;
    }
    /**
     * 
     * @param str 
     * prints the decrypted ciphertext to file.
     */
    public static void displayOutput(String str) {
        try {
            
            PrintWriter output = new PrintWriter(new FileWriter("permutations.txt"));
            output.print(str);
            output.close();
        } 
        
        catch (IOException ex) {
            System.out.println("permutations.txt not found");
            return;
        }
    }        
    
    /**
     * 
     * @param shiftedText
     * @param str 
     * Generates all permutations o the key and calls columnarTransposition.
     * http://stackoverflow.com/questions/4240080/generating-all-permutations-of-a-given-string
     */
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
