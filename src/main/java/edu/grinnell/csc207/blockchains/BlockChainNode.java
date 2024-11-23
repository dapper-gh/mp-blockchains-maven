package edu.grinnell.csc207.blockchains;

/**
 * A single node in a linked list that represents a blockchain.
 * 
 * @author David William Stroud
 * @author Sheilla Muligande
 */
public class BlockChainNode {
  /**
   * The block that this node contains.
   */
  private Block value;
  /**
   * The next node.
   */
  private BlockChainNode next;

  /**
   * Create a new node with a pointer to the next node.
   * @param value1 The block that this node contains.
   * @param next1 The next node, or null if it is the first node.
   */
  public BlockChainNode(Block value1, BlockChainNode next1) {
    this.value = value1;
    this.next = next1;
  } // Node(T)

  /**
   * Get the block that this node contains.
   * 
   * @return The block that this node contains.
   */
  public Block getValue() {
    return this.value;
  } // getValue()

  /**
   * Get the next node, or null if no such node exists.
   * @return The next node.
   */
  public BlockChainNode getNext() {
    return this.next;
  } // getNext()

  /**
   * Set the next node.
   * 
   * @param next1 The next node which we want this node to store.
   */
  public void setNext(BlockChainNode next1) {
    this.next = next1;
  } // setNext(BlockChainNode)
} // class Node<T>