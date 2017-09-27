package com.bitcoin.dao;

import com.bitcoin.bean.WalletRestore;

public interface WalletDAO {
    /**
     * Insert wallet information to DB
     *
     * @param walletRestore
     */
    void insert(WalletRestore walletRestore);

    /**
     * Get Wallet restore information by given id
     *
     * @param id
     * @return WalletRestore
     */
    WalletRestore findById(int id);
}
