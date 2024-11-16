package edu.grinnell.csc207.blockchains;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * Blocks to be stored in blockchains.
 *
 * @author David William Stroud
 * @author Sheilla Muligande
 * @author Samuel A. Rebelsky
 */
class Block {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The index of this block in the chain.
   */
  private int number;
  /**
   * The transaction that this block contains.
   */
  private Transaction tx;
  /**
   * The hash of the previous block in the chain.
   */
  private Hash prevHash;
  /**
   * The value required to make thisHash valid.
   */
  private long nonce;
  /**
   * The hash of this block.
   */
  private Hash thisHash;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a block with the given index, transaction,
   * and previous hash, computing the nonce needed to
   * make the block valid.
   * @param num The index of this block in the chain.
   * @param transaction The transaction this block contains.
   * @param prevHash The hash of the previous block.
   * @param validator
   *   The object used to determine if
   *   a hash is valid.
   */
  public Block(
    int num,
    Transaction transaction,
    Hash prevHash,
    HashValidator validator
  ) {
    Block candidate;
    long nonce = 0;
    do {
      candidate = new Block(
        num,
        transaction,
        prevHash,
        nonce
      );
      nonce++;
    } while (!validator.isValid(candidate.getHash()));
  } // Block(int, Transaction, Hash)

  /**
   * Create a block with the given index, transaction,
   * previous hash, and nonce.
   * @param num The index of this block in the chain.
   * @param transaction The transaction this block contains.
   * @param prevHash The hash of the previous block.
   * @param nonce The nonce to use in this block.
   */
  public Block(
    int num,
    Transaction transaction,
    Hash prevHash,
    long nonce
    ) {
      this.number = num;
      this.tx = transaction;
      this.prevHash = prevHash;
      this.nonce = nonce;

      this.thisHash = this.computeHash();
  } // Block(int, Transaction, Hash, long)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Compute the hash of the block given all the other info already
   * stored in the block.
   */
  Hash computeHash() {
    byte[] txBytes = this.getTransaction().getBytes();
    ByteBuffer buf = ByteBuffer.allocate(
      Integer.BYTES
      + txBytes.length
      + (this.getPrevHash() == null
          ? 0
          : this.getPrevHash().length())
      + Long.BYTES
    )
      .putInt(this.getNum())
      .put(txBytes);
    if (this.getPrevHash() != null) {
      buf.put(this.getPrevHash().getBytes());
    } // if
    buf.putLong(this.nonce);
    
    try {
      MessageDigest md = MessageDigest.getInstance("sha-256");
      md.update(buf);
      return new Hash(md.digest());
    } catch (Exception err) {
      // This should never happen.
      return new Hash(new byte[0]);
    } // try-catch
  } // computeHash()

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Get the number of the block.
   *
   * @return the number of the block.
   */
  int getNum() {
    return this.number;
  } // getNum()

  /**
   * Get the transaction stored in this block.
   *
   * @return the transaction.
   */
  Transaction getTransaction() {
    return this.tx;
  } // getTransaction()

  /**
   * Get the nonce of this block.
   *
   * @return the nonce.
   */
  long getNonce() {
    return this.nonce;
  } // getNonce()

  /**
   * Get the hash of the previous block.
   *
   * @return the hash of the previous block.
   */
  Hash getPrevHash() {
    return this.prevHash;
  } // getPrevHash

  /**
   * Get the hash of the current block.
   *
   * @return the hash of the current block.
   */
  Hash getHash() {
    return this.thisHash;
  } // getHash

  /**
   * Get a string representation of the block.
   *
   * @return a string representation of the block.
   */
  public String toString() {
    return String.format(
      "Block %d (Transaction: " +
      "[Source: %s, Target %s," +
      " Amount: %d], Nonce: %d, "
      + "prevHash: %s, hash: %s)",
      this.getNum(),
      this.getTransaction().getSource(),
      this.getTransaction().getTarget(),
      this.getTransaction().getAmount(),
      this.getNonce(),
      this.getPrevHash().toString(),
      this.getHash().toString()
    );
  } // toString()
} // class Block
