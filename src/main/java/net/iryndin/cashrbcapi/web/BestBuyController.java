package net.iryndin.cashrbcapi.web;

import net.iryndin.cashrbcapi.dto.ApiResponseDTO;
import net.iryndin.cashrbcapi.dto.CityEnum;
import net.iryndin.cashrbcapi.dto.CurrencyEnum;
import net.iryndin.cashrbcapi.dto.ExchangeDataDTO;
import net.iryndin.cashrbcapi.service.DataHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Single controller which serves 2 urls (bestbuy and bestsell)
 */
@RestController
public class BestBuyController {

    @Autowired
    private DataHolderService dataHolderService;

    /**
     * Get lowest sell rates
     * @param cityStr
     * @param currencyStr
     * @param sum
     * @return
     */
    @RequestMapping("/bestbuy/{CITY}/{CURRENCY}/{SUM}")
    public ApiResponseDTO<List<ExchangeDataDTO>> bestBuy(
            @PathVariable("CITY") String cityStr,
            @PathVariable("CURRENCY") String currencyStr,
            @PathVariable("SUM") Integer sum) {

        try {
            CityEnum city = CityEnum.fromString(cityStr);
            CurrencyEnum currency = CurrencyEnum.fromString(currencyStr);
            checkSum(sum);
            return createSuccessResponse(dataHolderService.getBestBuy(city, currency, sum));
        } catch (Exception e) {
            return createFailedResponse(e.getMessage());
        }
    }

    /**
     * Get highest buy rates
     * @param cityStr
     * @param currencyStr
     * @param sum
     * @return
     */
    @RequestMapping("/bestsell/{CITY}/{CURRENCY}/{SUM}")
    public ApiResponseDTO<List<ExchangeDataDTO>> bestSell(
            @PathVariable("CITY") String cityStr,
            @PathVariable("CURRENCY") String currencyStr,
            @PathVariable("SUM") Integer sum) {

        try {
            CityEnum city = CityEnum.fromString(cityStr);
            CurrencyEnum currency = CurrencyEnum.fromString(currencyStr);
            checkSum(sum);
            return createSuccessResponse(dataHolderService.getBestSell(city, currency, sum));
        } catch (Exception e) {
            return createFailedResponse(e.getMessage());
        }
    }

    private void checkSum(Integer sum) {
        if (sum <= 0) {
            throw new IllegalArgumentException("Sum should be nonzero positive, sum = " + sum);
        }
    }

    static ApiResponseDTO<List<ExchangeDataDTO>> createFailedResponse(String msg) {
        return new ApiResponseDTO<>(false, msg, null);
    }

    static ApiResponseDTO<List<ExchangeDataDTO>> createSuccessResponse(List<ExchangeDataDTO> list) {
        return new ApiResponseDTO<>(true, "ok", list);
    }
}