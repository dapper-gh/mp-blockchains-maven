package edu.grinnell.csc207.blockchains;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * A full blockchain.
 *
 * @author David William Stroud
 * @author Sheilla Muligande
 */
public class BlockChain implements Iterable<Transaction> {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The object used to determine if a hash is valid.
   */
  private HashValidator validator;
  /**
   * The most recent block in the chain.
   */
  private BlockChainNode lastBlock;
  /**
   * The least recent block in the chain.
   */
  private BlockChainNode firstBlock;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new blockchain using a validator to check elements.
   *
   * @param check
   *   The validator used to check elements.
   */
  public BlockChain(HashValidator check) {
    this.validator = check;
    this.firstBlock = new BlockChainNode(
      new Block(0, new Transaction("", "", 0), new Hash(new byte[] {}), validator),
      null
    );
    this.lastBlock = this.firstBlock;
  } // BlockChain(HashValidator)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Mine for a new valid block for the end of the chain, returning that
   * block.
   *
   * @param t
   *   The transaction that goes in the block.
   *
   * @return a new block with correct number, hashes, and such.
   */
  public Block mine(Transaction t) {
    return new Block(
      this.lastBlock.getValue().getNum() + 1,
      t,
      this.lastBlock.getValue().getHash(),
      this.validator
    );
  } // mine(Transaction)

  /**
   * Get the number of blocks curently in the chain.
   *
   * @return the number of blocks in the chain, including the initial block.
   */
  public int getSize() {
    return this.lastBlock.getValue().getNum() + 1;
  } // getSize()

  /**
   * Add a block to the end of the chain.
   *
   * @param blk
   *   The block to add to the end of the chain.
   *
   * @throws IllegalArgumentException if (a) the hash is not valid, (b)
   *   the hash is not appropriate for the contents, or (c) the previous
   *   hash is incorrect.
   */
  public void append(Block blk) {
    if (!blk.getPrevHash().equals(this.lastBlock.getValue().getHash())) {
      throw new IllegalArgumentException(
        "Previous hash of new block is not valid"
      );
    } // if

    if (!blk.getHash().equals(blk.computeHash())) {
      throw new IllegalArgumentException(
        "Current hash of new block is not consistent"
      );
    } // if

    if (!this.validator.isValid(blk.getHash())) {
      throw new IllegalArgumentException(
        "Current hash of new block is not valid"
      );
    } // if

    if (this.firstBlock == null) {
      this.firstBlock = new BlockChainNode(blk, null);
    } // if

    BlockChainNode newNode = new BlockChainNode(blk, null);
    this.lastBlock.setNext(newNode);
    this.lastBlock = newNode;
  } // append()

  /**
   * Attempt to remove the last block from the chain.
   *
   * @return false if the chain has only one block (in which case it's
   *   not removed) or true otherwise (in which case the last block
   *   is removed).
   */
  public boolean removeLast() {
    // This method is awful, but the assignment said to use
    // singly linked lists.
    if (this.getSize() < 2) {
      return false;
    } // if
    BlockChainNode curr = this.firstBlock;
    while (curr.getValue().getNum() != this.getSize() - 2) {
      curr = curr.getNext();
    } // while
    this.lastBlock = curr;
    curr.setNext(null);
    return true;
  } // removeLast()

  /**
   * Get the hash of the last block in the chain.
   *
   * @return the hash of the last sblock in the chain.
   */
  public Hash getHash() {
    return this.lastBlock.getValue().getHash();
  } // getHash()BlockChainNode prev

  /**
   * Determine if the blockchain is correct in that (a) the balances are
   * legal/correct at every step, (b) that every block has a correct
   * previous hash field, (c) that every block has a hash that is correct
   * for its contents, and (d) that every block has a valid hash.
   *
   * @return true if the blockchain is correct and false otherwise.
   */
  public boolean isCorrect() {
    try {
      this.check();
      return true;
    } catch (Exception ex) {
      return false;
    } // try-catch
  } // isCorrect()

  /**
   * Determine if the blockchain is correct in that (a) the balances are
   * legal/correct at every step, (b) that every block has a correct
   * previous hash field, (c) that every block has a hash that is correct
   * for its contents, and (d) that every block has a valid hash.
   *
   * @throws Exception
   *   If things are wrong at any block.
   */
  public void check() throws Exception {
    HashMap<String, Integer> balances = new HashMap<>();
    
    Iterator<Block> iter = this.blocks();
    Block prev = null;
    while (iter.hasNext()) {
      Block blk = iter.next();
      Transaction tx = blk.getTransaction();
      if (tx.getAmount() < 0) {
        throw new Exception(
          "Block "
          + blk.getNum()
          + ": tx amount below 0"
        );
      } // if
      if (!tx.getSource().isEmpty()) {
        int newAmount = balances.merge(tx.getSource(), -tx.getAmount(), (a, b) -> a + b);
        if (newAmount < 0) {
          throw new Exception(
            "Block "
            + blk.getNum()
            + ": tx causes user '"
            + tx.getSource()
            + "' to drop below 0 balance"
          );
        } // if
      } // if
      balances.merge(tx.getTarget(), tx.getAmount(), (a, b) -> a + b);

      if (prev != null && !blk.getPrevHash().equals(prev.getHash())) {
        throw new Exception(
          "Block "
          + blk.getNum()
          + ": previous hash is inconsistent"
        );
      } // if

      if (!blk.getHash().equals(blk.computeHash())) {
        throw new Exception(
          "Block "
          + blk.getNum()
          + ": hash is inconsistent"
        );
      } // if

      if (!this.validator.isValid(blk.getHash())) {
        throw new Exception(
          "Block "
          + blk.getNum()
          + ": hash is invalid"
        );
      } // if

      prev = blk;
    } // while
  } // check()

  /**
   * Return an iterator of all the people who participated in the
   * system.
   *
   * @return an iterator of all the people in the system.
   */
  public Iterator<String> users() {
    HashSet<String> users = new HashSet<>();
    for (Transaction tx : this) {
      if (!tx.getTarget().isEmpty()) {
        users.add(tx.getTarget());
      } // if
    } // for

    return users.iterator();
  } // users()

  /**
   * Find one user's balance.
   *
   * @param user
   *   The user whose balance we want to find.
   *
   * @return that user's balance (or 0, if the user is not in the system).
   */
  public int balance(String user) {
    int current = 0;

    for (Transaction tx : this) {
      if (tx.getTarget().equals(user)) {
        current += tx.getAmount();
      } // if
      if (tx.getSource().equals(user)) {
        current -= tx.getAmount();
      } // if
    } // for

    return current;
  } // balance()

  /**
   * Get an interator for all the blocks in the chain.
   *
   * @return an iterator for all the blocks in the chain.
   */
  public Iterator<Block> blocks() {
    return new Iterator<Block>() {
      private BlockChainNode curr = BlockChain.this.firstBlock;

      public boolean hasNext() {
        return this.curr != null;
      } // hasNext()

      public Block next() {
        Block thisBlock = this.curr.getValue();
        this.curr = this.curr.getNext();
        return thisBlock;
      } // next()
    };
  } // blocks()

  /**
   * Get an interator for all the transactions in the chain.
   *
   * @return an iterator for all the blocks in the chain.
   */
  public Iterator<Transaction> iterator() {
    Iterator<Block> inner = BlockChain.this.blocks();
    inner.next();
    return new Iterator<Transaction>() {
      public boolean hasNext() {
        return inner.hasNext();
      } // hasNext()

      public Transaction next() {
        return inner.next().getTransaction();
      } // next()
    };
  } // iterator()

} // class BlockChain
