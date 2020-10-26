package cscie97.smartcity.ledger;

import cscie97.smartcity.controller.LoggerUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

/**
 * Author: Stephen Sheldon
 **/
public class Transaction {

    /**
     * Unique identifier for the transaction assigned to the
     * transaction by the Ledger System.
     */
    private String transactionId;

    /**
     * The transaction amount to be deducted from the payer account
     * and added to the receiver's account. Must be greater or equal
     * to 0 and less than or equal to Integer.MAX_VALUE.
     */
    private Integer amount;

    /**
     * The fee is taken from the payer account and added to the master
     * account. The fee must be greater than or equal to 10.
     */
    private Integer fee;

    /**
     * An arbitrary string that may be up to 1024 characters in length.
     */
    private String note;

    /**
     * The account receiving the transaction amount.
     */
    private Account receiver;

    /**
     * The account issueing the transaction. The amount of the transaction
     * and the transaction fee will be deducted from the payer's account balance.
     */
    private Account payer;

    /**
     * The hash of the transaction. The hash is generated by concatenating
     * the string values of all properties within the transaction and then
     * hashing it using SHA-256. This transaction hash is used to maintain
     * the integrity of the transaction. Once a block reaches 10 transactions
     * the hash of each transaction is generated and used in generating
     * the hash values of the merkle tree for the block.
     */
    private String transactionHash;

    /**
     * Transaction constructor.
     * @param transactionId The ID for the transaction.
     * @param amount        The amount to be transferred in this transaction.
     * @param fee           The fee associated with this transaction.
     * @param note          The arbitrary note associated with this transaction.
     * @param receiver      The receiving account of this transaction.
     * @param payer         The paying account of this transaction.
     */
    public Transaction(String transactionId, Integer amount, Integer fee, String note, Account receiver, Account payer) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.fee = fee;
        this.note = note;
        this.receiver = receiver;
        this.payer = payer;

        // Compute the hash for this transaction below.

        // First concatenate the string value properties of our transaction to generate our hash string.
        String hashString = transactionId + amount.toString() + fee.toString() + note + receiver.toString() + payer.toString();

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            LoggerUtil.log(Level.SEVERE, "SHA-256 algorithm not found.", false);
        }
        byte[] hash = digest.digest(hashString.getBytes(StandardCharsets.UTF_8));

        // Convert our byte array to a string
        transactionHash = new String(hash, StandardCharsets.UTF_8);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Integer getAmount() {
        return amount;
    }

    public Integer getFee() {
        return fee;
    }

    public Account getReceiver() {
        return receiver;
    }

    public Account getPayer() {
        return payer;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return  " transactionId=" + transactionId +
                ", amount=" + amount +
                ", fee=" + fee +
                ", note=" + note +
                ", receiver=" + receiver.getAddress() +
                ", payer=" + payer.getAddress() +
                ", transactionHash=" + transactionHash + "\n";
    }
}
