package com.bitcoin.dao;

import com.bitcoin.bean.WalletRestore;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class WalletDAOImpl implements WalletDAO {

    @Override
    public void insert(WalletRestore walletRestore) {
        String sql = "INSERT INTO WALLET_RESTORE " + "(ADDRESS, MNEMONIC, PASSWORD) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = null;
        Connection connection;
        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, walletRestore.getAddress());
            preparedStatement.setString(2, walletRestore.getMnemonicCode());
            preparedStatement.setString(3, walletRestore.getPassword());
            // execute insert SQL statement
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error while save WALLET_RESTORE : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public WalletRestore findById(int id) {
        String sql = "SELECT * FROM WALLET_RESTORE WHERE WALLET_RESTORE_ID = ?";
        PreparedStatement preparedStatement = null;
        Connection connection;
        WalletRestore walletRestore = null;
        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery(sql);
            while (rs.next()) {
                walletRestore = new WalletRestore();
                walletRestore.setAddress(rs.getString("ADDRESS"));
                walletRestore.setMnemonicCode(rs.getString("MNEMONIC"));
                walletRestore.setPassword(rs.getString("PASSWORD"));
                break;
            }
        } catch (SQLException e) {
            log.error("Error while save WALLET_RESTORE : " + e.getMessage());
            e.printStackTrace();
        }
        return walletRestore;
    }
}
