package com.bitcoin.bean;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WalletRestore implements Serializable {

    private static final long serialVersionUID = 5168671510353997641L;
    private int id;
    private String address;
    private String mnemonicCode;
    private String password;
}
