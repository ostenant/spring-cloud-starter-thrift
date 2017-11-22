package com.icekredit.rpc.thrift.examples.http.enums;

public enum WithdrawStatus {
    FINISHED(1),
    PROCCEDING(2),
    FAILED(3);

    private final int value;

    private WithdrawStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static WithdrawStatus findByValue(int value) {
        switch (value) {
            case 1:
                return FINISHED;
            case 2:
                return PROCCEDING;
            case 3:
                return FAILED;
            default:
                return null;
        }
    }
}
