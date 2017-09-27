package com.bitcoin.wallet;

import com.bitcoin.bean.WalletRestore;
import com.bitcoin.common.HDWallet;
import com.bitcoin.dao.WalletDAO;
import com.bitcoin.dao.WalletDAOImpl;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.tinhcao.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.KeyChainGroup;
import org.bitcoinj.wallet.Wallet;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class WalletImpl implements WalletInterface {

    @Override
    public Wallet createWallet(NetworkParameters params, String fileNamePrefix) {
        log.info("Building wallet from WalletAppKit");
        WalletAppKit kit = new WalletAppKit(params, new File("."), fileNamePrefix);
        kit.startAsync();
        kit.awaitRunning();
        // get created wallet
        Wallet wallet = kit.wallet();
        log.info("Finished building wallet from WalletAppKit");
        // shutting down
        kit.stopAsync();
        kit.awaitTerminated();
        return wallet;
    }

    @Override
    public Wallet.SendResult sendCoins(Wallet wallet, Address to, Coin value) {
        // Send Coin
        log.info("Start sending coin ...");
        WalletAppKit kit = new WalletAppKit(wallet.getNetworkParameters(), new File("."), "send-coin");
        try {
            kit.startAsync();
            kit.awaitRunning();
            Wallet.SendResult result = wallet.sendCoins(kit.peerGroup(), to, value);
            log.info("Coins sent with transaction hash: " + result.tx.getHashAsString());
            return result;
            // you can use a block explorer like https://www.biteasy.com/ to
            // inspect the transaction with the printed transaction hash.
        } catch (InsufficientMoneyException e) {
            log.error("Not enough coins in your wallet. Missing : " + e.missing.getValue());
            // execute BalanceFuture callback once your wallet has a certain
            // balance, wait until the we have enough balance and display a notice
            log.info("Resend money to: " + wallet.currentReceiveAddress().toString());
            ListenableFuture<Coin> balanceFuture = wallet.getBalanceFuture(value, Wallet.BalanceType.AVAILABLE);
            FutureCallback<Coin> callback = new FutureCallback<Coin>() {
                @Override
                public void onSuccess(Coin balance) {
                    log.info("Coins arrived and the wallet now has enough balance");
                }

                @Override
                public void onFailure(Throwable t) {
                    log.error("Error when send Coin : " + t.getMessage());
                }
            };
            Futures.addCallback(balanceFuture, callback);
        }
        // shutting down
        kit.stopAsync();
        kit.awaitTerminated();
        return null;
    }

    @Override
    public Coin getBalance(Wallet wallet, Wallet.BalanceType balanceType) {
        return wallet.getBalance(balanceType);
    }

    @Override
    public WalletRestore saveWallet(String address, String mnemonicCode, String password) {
        log.info("Save Wallet Information to database");
        WalletDAO walletDAO = new WalletDAOImpl();
        WalletRestore walletRestore = new WalletRestore();
        walletRestore.setAddress(address);
        walletRestore.setMnemonicCode(mnemonicCode);
        walletRestore.setPassword(password);
        walletDAO.insert(walletRestore);
        return walletRestore;
    }

    @Override
    public Wallet restoreWallet(NetworkParameters params, String mnemonicCode) {
        try {
            List<String> words = Arrays.asList(mnemonicCode.split("\\s+"));
            final Wallet wallet = new Wallet(params, new KeyChainGroup(params, new DeterministicSeed(words, null, "", Constants.EARLIEST_HD_SEED_CREATION_TIME)));

            if (!wallet.getParams().equals(params))
                throw new IOException("bad wallet backup network parameters: " + wallet.getParams().getId());
            if (!wallet.isConsistent()) throw new IOException("inconsistent wallet backup");
            return wallet;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<Wallet, Coin> checkBalance(List<Wallet> walletList, Wallet.BalanceType balanceType) {
        Map<Wallet, Coin> walletCoinMap = walletList.stream().collect(Collectors.toMap(wallet -> wallet, wallet -> wallet.getBalance(balanceType)));
        return walletCoinMap;
    }

    @Override
    public String getMnemonicCode(Wallet wallet) {
        HDWallet hdWallet = new HDWallet();
        hdWallet.fromWallet(wallet);
        return hdWallet.getCode();
    }
}
