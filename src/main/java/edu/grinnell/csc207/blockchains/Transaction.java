package edu.grinnell.csc207.blockchains;

import java.nio.ByteBuffer;

/**
 * A simple transaction.
 *
 * @author
 *   Samuel A. Rebelsky
 */
public class Transaction {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The source of a transfer.
   */
  private String source;

  /**
   * The target of a transfer.
   */
  private String target;

  /**
   * The amount transferred.
   */
  private int amount;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new Transaction. For a deposit, rather than a transfer,
   * use the empty string for the source.
   *
   * @param src
   *   The source of the transaction (or empty for a deposit).
   * @param tgt
   *   The person receiving the transaction.
   * @int amt
   *   The funds transfered.
   */
  public Transaction(String src, String tgt, int amt) {
    this.source = src;
    this.target = tgt;
    this.amount = amt;
  } // Transaction(src)

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Get the source of the transaction.
   *
   * @return The source (or the empty string, if it'sa deposit).
   */
  public String getSource() {
    return this.source;
  } // getSource()

  /**
   * Get the target of the transaction.
   *
   * @return The target.
   */
  public String getTarget() {
    return this.target;
  } // getTarget()

  /**
   * Get the amount of the transaction.
   *
   * @return the amount.
   */
  public int getAmount() {
    return this.amount;
  } // getAmount()

  /**
   * Convert to string form.
   *
   * @return a string of the form [Source: source, Target: target,
   *    Amount: amounts] if the source is nonempty. Otherwise, returns
   *    a string in the form [Deposit, Target: target, Amount: amount].
   */
  public String toString() {
    return String.format("[%s, Target: %s, Amount: %s]",
        ("".equals(this.source)) ? "Deposit" : "Source: " + this.source,
        this.target,
        this.amount);
  } // toString()

  /**
   * Returns this transaction as an array of bytes.
   * @return Bytes that represent this transaction.
   */
  public byte[] getBytes() {
    return ByteBuffer.allocate(
      this.getSource().getBytes().length
      + this.getTarget().getBytes().length
      + Integer.BYTES
    )
      .put(this.getSource().getBytes())
      .put(this.getTarget().getBytes())
      .putInt(this.getAmount())
      .array();
  } // getBytes()
} // class Transaction
