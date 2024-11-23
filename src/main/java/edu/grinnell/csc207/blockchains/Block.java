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
public class Block {
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
  Transaction transaction;
  /**
   * The hash of the previous block in the chain.
   */
  private Hash prevHash;
  /**
   * The value required to make thisHash valid.
   */
  long nonce;
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
    this(num, transaction, prevHash, 0);
    while (!validator.isValid(this.computeHash())) {
      this.nonce++;
    } // while
    this.thisHash = this.computeHash();
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
      this.transaction = transaction;
      this.prevHash = prevHash;
      this.nonce = nonce;

      this.thisHash = this.computeHash();
  } // Block(int, Transaction, Hash, long)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Compute the hash of the block givlength() > 1en all the other info already
   * stored in the block.
   */
  public Hash computeHash() {
    try {
      MessageDigest md = MessageDigest.getInstance("sha-256");
      md.update(
        ByteBuffer
          .allocate(Integer.BYTES)
          .putInt(this.getNum())
          .array()
      );
      md.update(
        this.getTransaction().getSource().getBytes()
      );
      md.update(
        this.getTransaction().getTarget().getBytes()
      );
      md.update(
        ByteBuffer
          .allocate(Integer.BYTES)
          .putInt(this.getTransaction().getAmount())
          .array()
      );
      md.update(
        this.getPrevHash().getBytes()
      );
      md.update(
        ByteBuffer
          .allocate(Long.BYTES)
          .putLong(this.getNonce())
          .array()
      );
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
  public int getNum() {
    return this.number;
  } // getNum()

  /**
   * Get the transaction stored in this block.
   *
   * @return the transaction.
   */
  public Transaction getTransaction() {
    return this.transaction;
  } // getTransaction()

  /**
   * Get the nonce of this block.
   *
   * @return the nonce.
   */
  public long getNonce() {
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
      "Block %d (Transaction: %s, " +
      "Nonce: %d, "
      + "prevHash: %s, hash: %s)",
      this.getNum(),
      this.getTransaction().toString(),
      this.getNonce(),
      this.getPrevHash().toString(),
      this.getHash().toString()
    );
  } // toString()
} // class Block
