package edu.grinnell.csc207.blockchains;

import java.util.Arrays;
import java.util.HexFormat;

/**
 * Encapsulated hashes.
 *
 * @author David William Stroud
 * @author Sheilla Muligande
 */
public class Hash {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The digest this object represents, stored as an array
   * of bytes with no other references to it.
   */
  private byte[] inner;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new encapsulated hash.
   *
   * @param data
   *   The data to copy into the hash.
   */
  public Hash(byte[] data) {
    this.inner = Arrays.copyOf(data, data.length);
  } // Hash(byte[])

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Determine how many bytes are in the hash.
   *
   * @return the number of bytes in the hash.
   */
  public int length() {
    return this.inner.length;
  } // length()

  /**
   * Get the ith byte.
   *
   * @param i
   *   The index of the byte to get, between 0 (inclusive) and
   *   length() (exclusive).
   *
   * @return the ith byte
   */
  public byte get(int i) {
    return this.inner[i];
  } // get()

  /**
   * Convert to a hex string.
   *
   * @return the hash as a hex string.
   */
  public String toString() {
    return HexFormat.of().withUpperCase().formatHex(this.inner);
  } // toString()

  /**
   * Determine if this is equal to another object.
   *
   * @param other
   *   The object to compare to.
   *
   * @return true if the two objects are conceptually equal and false
   *   otherwise.
   */
  public boolean equals(Object other) {
    return (other instanceof Hash)
    && Arrays.equals(((Hash) other).inner, this.inner);
  } // equals(Object)

  /**
   * Returns a copy of this hash's bytes.
   * @return A copy of this hash's bytes.
   */
  public byte[] getBytes() {
    return Arrays.copyOf(this.inner, this.length());
  } // getBytes()

  /*
   * Get the hash code of this object.
   *
   * @return the hash code.
   */
  public int hashCode() {
    return this.toString().hashCode();
  } // hashCode()
} // class Hash
