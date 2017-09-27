package com.bitcoin.common;

import com.google.common.base.Joiner;
import lombok.*;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.Wallet;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HDWallet {
    private String code;
    private Long time;

    public HDWallet fromWallet(Wallet wallet) {
        DeterministicSeed keyChainSeed = wallet.getKeyChainSeed();
        setCode(Joiner.on(" ").join(keyChainSeed.getMnemonicCode()));
        setTime(keyChainSeed.getCreationTimeSeconds());
        return this;
    }
}
