import java.util.*;
import java.io.*;

/* The class for the second project
 * Type "run Project2 inputFileName outputFileName" in the interaction pane to start   
 */
public class HuffmanCompressor {
  
  /* An array to store the nodes of the huffman tree.
   * Every method of the class would work on this array 
   */
  private static HuffmanNode[] nodeArr;
  
  /* A helper method to increase the length of an array*/
  private static HuffmanNode[] increaseArr(HuffmanNode[] arr) {
    int currentLength = arr.length;
    HuffmanNode[] newArr = new HuffmanNode[currentLength + 10];
    for (int i = 0; i < currentLength; i++) 
      newArr[i] = arr[i];
    return newArr;
  }
  
  /* The method that scans the input file and creates all the leaf nodes of the huffman tree
   * The leaf nodes are stored in nodeArr in increasing order (character's frequency)
   */
  private static void scan(String inputFileName) throws FileNotFoundException {
    Scanner scanner = new Scanner(new File(inputFileName));
    scanner.useDelimiter("");
    /* The counter to keep track of the number of elements added to the array below*/
    int arrLength = 0;
    /* A temporary array to store the nodes
     * Nodes will be ordered in decending order 
     * New node would be added to the end of the array 
     * Everytime a node's increaseFrequency() is called, it will be shifted leftward. This should not take a lot of steps because the array is already sorted and the amount of increment is only 1 
     */
    HuffmanNode[] arr = new HuffmanNode[70];
    while (scanner.hasNext()) {
      Character save = scanner.next().charAt(0);
      if (arrLength > 0) {
        for (int i = 0; i < arrLength; i++) { 
          if (arr[i].getCharacter() == save) {
            arr[i].increaseFrequency();
            int j = i;
            while (j > 0) {
              if (arr[j].compareTo(arr[j - 1]) > 0) {
                HuffmanNode temp = arr[j];
                arr[j] = arr[j - 1];
                arr[j - 1] = temp;
                j--;
              } else j = 0;
            }
            break;
          }
          if (i == arrLength - 1) {
            arr[arrLength] = new HuffmanNode(save, 0, null, null);
            arrLength++;
            if (arrLength == arr.length)
              arr = increaseArr(arr);
          }
        }
      } else {
        arr[arrLength] = new HuffmanNode(save, 1, null, null);
        arrLength++;
        if (arrLength == arr.length)
          arr = increaseArr(arr);
      }
    }
    /* Copy the temporary array to the class field array nodeArr
     * Nodes in nodeArr are ordered in ascending order
     */
    nodeArr = new HuffmanNode[arrLength];
    for (int i = arrLength - 1; i >= 0; i--) 
      nodeArr[arrLength - 1 - i] = arr[i];
  }
  
  /* The method that merges two huffman nodes and returns the parent node*/
  private static HuffmanNode merge(HuffmanNode leftNode, HuffmanNode rightNode) {
    return new HuffmanNode(null, leftNode.getFrequency() + rightNode.getFrequency(), leftNode, rightNode);
  }
  
  /* The method that construct the huffman tree from the given arrays of leaf nodes
   * Because the array nodeArr is sorted in increasing order, we will iterate through the array from i = 0 to end by two pivots leftPtr and rightPtr
   * nodeArr[rightPtr] = merge(nodeArr[leftPtr], nodeArr[rightPtr]), shift if the resulting node is larger than the nodes above it
   * The method is done when reaching the end of the array and returns the root of the Huffman tree
   */
  private static HuffmanNode huffman() {
    if (nodeArr.length == 1)
      return nodeArr[0];
    int leftPtr = 0;
    int rightPtr = 1;
    while (rightPtr < nodeArr.length - 1) {
      HuffmanNode temp = merge(nodeArr[leftPtr], nodeArr[rightPtr]);
      leftPtr++;
      rightPtr++;
      nodeArr[leftPtr] = temp;
      int tempPtr = leftPtr;
      while (nodeArr[tempPtr].compareTo(nodeArr[tempPtr + 1]) > 0) {
        HuffmanNode save = nodeArr[tempPtr];
        nodeArr[tempPtr] = nodeArr[tempPtr + 1];
        nodeArr[tempPtr + 1] = save;
        tempPtr++;
        if (tempPtr == nodeArr.length - 1)
          break;
      }
    }
    return merge(nodeArr[nodeArr.length - 2], nodeArr[nodeArr.length - 1]);
  }
  
  /*The method that return the encode of a character*/
  private static String code(Character character, String save, HuffmanNode root) {
    if (root.getCharacter() != null) {
      if (root.getCharacter().compareTo(character) == 0)
        return save; 
      else return null; 
    }
    String res; 
    res = code(character, save + "1", root.getRight());
    if (res != null)
      return res; 
    res = code(character, save + "0", root.getLeft());
    if (res != null)
      return res; 
    return null; 
  }
  
  /*the method that scans the input file again and creates the output file with all the text being encoded*/
  private static void output(String inputFileName, String outputFileName, HuffmanNode huffmanRoot) throws FileNotFoundException{
    Scanner scanner = new Scanner(new File(inputFileName));
    scanner.useDelimiter("");
    try {
      FileWriter fw = new FileWriter(outputFileName);
      while (scanner.hasNext()) 
          fw.write(code(scanner.next().charAt(0), "", huffmanRoot));
      fw.close();
      scanner.close();
    } catch (IOException e) {
      System.out.println("ERROR"); 
    }
  }
  
  /* The main method*/
  public static void main(String args[]) {
    try {
      scan(args[0]);
      output(args[0], args[1], huffman());
    } catch (FileNotFoundException e) {
      System.out.println("File is not found"); 
    }
  }
  
}