package io.ostenant.rpc.thrift.examples.http.enums;

public enum Region {
    NORTH(1),
    CENTRAL(2),
    SOUTH(3),
    EAST(4),
    SOUTHWEST(5),
    NORTHWEST(6),
    NORTHEAST(7);

    private final int value;

    private Region(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Region findByValue(int value) {
        switch (value) {
            case 1:
                return NORTH;
            case 2:
                return CENTRAL;
            case 3:
                return SOUTH;
            case 4:
                return EAST;
            case 5:
                return SOUTHWEST;
            case 6:
                return NORTHWEST;
            case 7:
                return NORTHEAST;
            default:
                return null;
        }
    }
}
