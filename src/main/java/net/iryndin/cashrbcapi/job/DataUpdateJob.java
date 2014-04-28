package net.iryndin.cashrbcapi.job;

import net.iryndin.cashrbcapi.dto.CityEnum;
import net.iryndin.cashrbcapi.dto.CurrencyEnum;
import net.iryndin.cashrbcapi.dto.ExchangeDataDTO;
import net.iryndin.cashrbcapi.service.DataHolderService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Main data update job.
 * Data is parsed from http://quote.rbc.ru/cash/
 * JSoup is used for HTML parsing
 */
@Component
public class DataUpdateJob {

    final Logger log = LoggerFactory.getLogger(getClass());

    final static int[] sumValues = {1, 2, 3};

    @Autowired
    private DataHolderService dataHolderService;

    /**
     * Run update every 2 minutes
     */
    @Scheduled(initialDelay = 5*1000, fixedRate = 2 * 60 * 1000)
    public void updateData() {
        log.info("Update data");
        for (CityEnum city : CityEnum.values()) {
            for (CurrencyEnum currency : CurrencyEnum.values()) {
                List<ExchangeDataDTO> list = new ArrayList<>(256*8);
                for (int sum : sumValues) {
                    try {
                        updateData(city, currency, sum, list);
                    } catch (IOException e) {
                        log.error("Error",e);
                    }
                }
                dataHolderService.update(city, currency, list);
            }
        }
    }

    /**
     * Request url: http://quote.rbc.ru/cash/?city=2&currency=3&summa=1&pagerLimiter=200&pageNumber=1
     *
     * @param city
     * @param currency
     * @param sum
     */
    private void updateData(CityEnum city, CurrencyEnum currency, int sum, List<ExchangeDataDTO> list) throws IOException {
        String url = createUrl(city, currency, sum);
        log.debug("Request url: {}", url);
        Document doc = Jsoup.parse(new URL(url), 50000);
        Elements elems = doc.select("#tableBody tr");
        for (Element tr : elems) {
            try {
                ExchangeDataDTO d = extractRowData(tr);
                list.add(d);
            } catch (Exception e) {
                log.error("Error when request url: " + url,e);
            }
        }
    }

    private ExchangeDataDTO extractRowData(Element tr) {
        String name = extractTdText(tr, "td.name");
        String coord = extractTdText(tr, "td.currencies");
        BigDecimal buyRate = extractBigDecimal(tr, "td.pok");
        BigDecimal sellRate = extractBigDecimal(tr, "td.prod");
        String commission = extractTdText(tr, "td.kom");
        Integer sum = extractInteger(tr, "td.sum");
        String info = extractTdText(tr, "td.info");

        return new ExchangeDataDTO(name, coord, buyRate, sellRate, commission, sum, info);
    }

    private String extractTdText(Element tr, String sel) {
        Element el = tr.select(sel).first();
        if (el != null) {
            return el.text().trim();
        } else {
            return null;
        }
    }

    private BigDecimal extractBigDecimal(Element tr, String sel) {
        String text = extractTdText(tr, sel);
        if (text != null && !text.isEmpty()) {
            return new BigDecimal(text);
        } else {
            return null;
        }
    }

    private Integer extractInteger(Element tr, String sel) {
        BigDecimal n = extractBigDecimal(tr, sel);
        if (n != null) {
            return n.intValue();
        } else {
            return null;
        }
    }

    private String createUrl(CityEnum city, CurrencyEnum currency, int sum) {
        StringBuilder sb = new StringBuilder();

        sb.append("http://quote.rbc.ru/cash/");
        sb.append("?");
        sb.append("city").append("=").append(city.cashRbcId);
        sb.append("&");
        sb.append("currency").append("=").append(currency.cashRbcId);
        sb.append("&");
        sb.append("summa").append("=").append(sum);
        sb.append("&");
        sb.append("pagerLimiter").append("=").append(200);
        sb.append("&");
        sb.append("pageNumber").append("=").append(1);

        return sb.toString();
    }
}
