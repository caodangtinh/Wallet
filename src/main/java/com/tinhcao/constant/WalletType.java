package com.tinhcao.constant;

public enum WalletType {
    LITE_COIN(1), DASH(2), RIPPLE(3), MONERO(4), NEM(5);

    private final int typeCode;

    WalletType(int typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeCode() {
        return this.typeCode;
    }
}
