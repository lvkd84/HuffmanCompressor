/* The class that represents the node of the huffman tree*/
public class HuffmanNode implements Comparable<HuffmanNode> {
  
  private Character inChar; 
  
  private int frequency;
  
  private HuffmanNode left; 
  
  private HuffmanNode right;
  
  public HuffmanNode(Character inChar, int freq, HuffmanNode left, HuffmanNode right) {
    this.inChar = inChar; 
    frequency = freq; 
    this.left = left; 
    this.right = right; 
  }
  
  public void increaseFrequency() {
    frequency++; 
  }
  
  public int getFrequency() {
    return frequency;
  }
  
  public Character getCharacter() {
    return inChar;
  }
  
  public HuffmanNode getLeft() {
    return left; 
  }
  
  public HuffmanNode getRight() {
    return right;
  }
  
  public int compareTo(HuffmanNode node) {
    if (node == null)
      return -1;
    return frequency - node.getFrequency(); 
  }
}