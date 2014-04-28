package net.iryndin.cashrbcapi.dto;

/**
 * Currencies which are monitored. Currently only USD and EUR
 */
public enum CurrencyEnum {

    USD("USD", 3), EUR("EUR", 2);

    public final String ticker;
    public final int cashRbcId;

    CurrencyEnum(String ticker, int cashRbcId) {
        this.ticker = ticker;
        this.cashRbcId = cashRbcId;
    }

    public static CurrencyEnum fromString(String s) {
        for (CurrencyEnum e : CurrencyEnum.values()) {
            if (e.ticker.equalsIgnoreCase(s)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Wrong currency ticker: " + s);
    }
}
