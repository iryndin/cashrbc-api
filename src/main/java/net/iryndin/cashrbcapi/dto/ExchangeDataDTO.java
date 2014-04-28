package net.iryndin.cashrbcapi.dto;

import java.math.BigDecimal;

/**
 * Main data holder
 * name - bank name
 * coord - location
 * buyRate - rate at which bank buys currency
 * sellRate - rate at which bank sell currency
 * sum - sum for exchange
 * info - any other info (usually address and phones)
 */
public class ExchangeDataDTO {
    private final String name;
    private final String coord;
    private final BigDecimal buyRate;
    private final BigDecimal sellRate;
    private final String commission;
    private final Integer sum;
    private final String info;

    public ExchangeDataDTO(String name, String coord, BigDecimal buyRate, BigDecimal sellRate, String commission, Integer sum, String info) {
        this.name = name;
        this.coord = coord;
        this.buyRate = buyRate;
        this.sellRate = sellRate;
        this.commission = commission;
        this.sum = sum;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public String getCoord() {
        return coord;
    }

    public BigDecimal getBuyRate() {
        return buyRate;
    }

    public BigDecimal getSellRate() {
        return sellRate;
    }

    public String getCommission() {
        return commission;
    }

    public Integer getSum() {
        return sum;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "ExchangeDataDTO{" +
                "name='" + name + '\'' +
                ", coord='" + coord + '\'' +
                ", buyRate=" + buyRate +
                ", sellRate=" + sellRate +
                ", commission='" + commission + '\'' +
                ", sum=" + sum +
                ", info='" + info + '\'' +
                '}';
    }
}
