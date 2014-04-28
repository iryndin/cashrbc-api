package net.iryndin.cashrbcapi.service;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import net.iryndin.cashrbcapi.dto.CityEnum;
import net.iryndin.cashrbcapi.dto.CurrencyEnum;
import net.iryndin.cashrbcapi.dto.ExchangeDataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service which holds data cache
 */
@Service
public class DataHolderService {

    final Logger log = LoggerFactory.getLogger(getClass());

    static class CityCurrencyKey {
        private final CityEnum city;
        private final CurrencyEnum currency;

        public CityCurrencyKey(CityEnum city, CurrencyEnum currency) {
            this.city = city;
            this.currency = currency;
        }

        public CityEnum getCity() {
            return city;
        }

        public CurrencyEnum getCurrency() {
            return currency;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CityCurrencyKey that = (CityCurrencyKey) o;

            if (city != that.city) return false;
            if (currency != that.currency) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = city.hashCode();
            result = 31 * result + currency.hashCode();
            return result;
        }
    };

    private final Map<CityCurrencyKey, List<ExchangeDataDTO>> cache = new ConcurrentHashMap<>();

    public void update(CityEnum city, CurrencyEnum currency, List<ExchangeDataDTO> list) {
        CityCurrencyKey key = new CityCurrencyKey(city, currency);
        cache.put(key, list);
    }

    /**
     * Filter predicate
     */
    static class GreaterOrEqualSumPredicate implements Predicate<ExchangeDataDTO> {

        private final int n;

        GreaterOrEqualSumPredicate(int n) {
            this.n = n;
        }

        @Override
        public boolean apply(ExchangeDataDTO input) {
            return n >= input.getSum();
        }
    };

    final static Predicate<ExchangeDataDTO> throwAwayNullSellRatesPredicate  = new Predicate<ExchangeDataDTO>() {

        @Override
        public boolean apply(ExchangeDataDTO input) {
            return input.getSellRate() != null;
        }
    };

    final static Predicate<ExchangeDataDTO> throwAwayNullBuyRatesPredicate  = new Predicate<ExchangeDataDTO>() {

        @Override
        public boolean apply(ExchangeDataDTO input) {
            return input.getBuyRate() != null;
        }
    };

    final static Comparator<ExchangeDataDTO> sellRatesAscComparator  = new Comparator<ExchangeDataDTO>() {

        @Override
        public int compare(ExchangeDataDTO o1, ExchangeDataDTO o2) {
            return o1.getSellRate().compareTo(o2.getSellRate());
        }
    };

    final static Comparator<ExchangeDataDTO> buyRatesDescComparator  = new Comparator<ExchangeDataDTO>() {

        @Override
        public int compare(ExchangeDataDTO o1, ExchangeDataDTO o2) {
            return o2.getBuyRate().compareTo(o1.getBuyRate());
        }
    };

    /**
     * Client want to buy currency.
     * She needs to get lowest sell rates
     * @param city
     * @param currency
     * @param sum
     * @return
     */
    public List<ExchangeDataDTO> getBestBuy(CityEnum city, CurrencyEnum currency, int sum) {
        List<ExchangeDataDTO> list = new ArrayList<>(Collections2.filter(getFilteredData(city, currency, sum), throwAwayNullSellRatesPredicate));
        Collections.sort(list, sellRatesAscComparator);
        return list;
    }

    /**
     * Client want to sell currency.
     * She needs to get highest buy rates
     * @param city
     * @param currency
     * @param sum
     * @return
     */
    public List<ExchangeDataDTO> getBestSell(CityEnum city, CurrencyEnum currency, int sum) {
        List<ExchangeDataDTO> list = new ArrayList<>(Collections2.filter(getFilteredData(city, currency, sum), throwAwayNullBuyRatesPredicate));
        Collections.sort(list, buyRatesDescComparator);
        return list;
    }

    private Collection<ExchangeDataDTO> getFilteredData(CityEnum city, CurrencyEnum currency, int sum) {
        CityCurrencyKey key = new CityCurrencyKey(city, currency);
        List<ExchangeDataDTO> list = cache.get(key);
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        Collection<ExchangeDataDTO> filtered = Collections2.filter(list, new GreaterOrEqualSumPredicate(sum));
        return filtered;
    }
}
