package com.bitcoin.wallet;

import com.bitcoin.bean.WalletRestore;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.wallet.Wallet;

import java.util.List;
import java.util.Map;

public interface WalletInterface {
    /**
     * Create wallet and save to file
     *
     * @param params         {@link NetworkParameters}
     * @param fileNamePrefix name of wallet file
     * @return instance of {@link Wallet} was created
     */
    Wallet createWallet(NetworkParameters params, String fileNamePrefix);

    /**
     * Send {@link Coin} to an address
     *
     * @param wallet Wallet to send coin
     * @param to     {@link Address} to receive coin
     * @param value  Number of {@link Coin}
     * @return {@link org.bitcoinj.wallet.Wallet.SendResult}
     */
    Wallet.SendResult sendCoins(Wallet wallet, Address to, Coin value);

    /**
     * Save wallet information to database
     *
     * @param address      address
     * @param mnemonicCode mnemonic of wallet
     * @param password     encrypt password of wallet
     * @return {@link WalletRestore}
     */
    WalletRestore saveWallet(String address, String mnemonicCode, String password);

    /**
     * Restore wallet by mnemonic Code
     *
     * @param mnemonicCode mnemonic Code
     * @param params       {@link NetworkParameters}
     * @return Restored Wallet
     */
    Wallet restoreWallet(NetworkParameters params, String mnemonicCode);

    /**
     * Get balance of Wallet
     *
     * @param wallet      Wallet to check
     * @param balanceType {@link Wallet.BalanceType}
     * @return Number of {@link Coin} in Wallet
     */
    Coin getBalance(Wallet wallet, Wallet.BalanceType balanceType);

    /**
     * Get balance from List of Wallet base on {@link Wallet.BalanceType}
     *
     * @param wallet      List Wallet to check
     * @param balanceType {@link Wallet.BalanceType}
     * @return Number of {@link Coin} in Wallet
     */
    Map<Wallet, Coin> checkBalance(List<Wallet> wallet, Wallet.BalanceType balanceType);

    /**
     * Get MnemonicCode for given wallet
     *
     * @param wallet
     * @return MnemonicCode of given wallet
     */
    String getMnemonicCode(Wallet wallet);
}
