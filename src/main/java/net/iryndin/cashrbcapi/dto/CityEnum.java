package net.iryndin.cashrbcapi.dto;

/**
 * Cities which are monitored. Currently only Moscow and Saint-Petersburg
 */
public enum CityEnum {

    MOW("MOW", 1), SPB("SPB", 2);

    public final String ticker;
    public final int cashRbcId;

    CityEnum(String ticker, int cashRbcId) {
        this.ticker = ticker;
        this.cashRbcId = cashRbcId;
    }

    public static CityEnum fromString(String s) {
        for (CityEnum e : CityEnum.values()) {
            if (e.ticker.equalsIgnoreCase(s)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Wrong city label: " + s);
    }
}
