package ibf2022.ssf.assessment.services;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ibf2022.ssf.assessment.models.Quotation;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class QuotationService {

    public Quotation getQuotations(List<String> items) throws Exception {
        JsonArrayBuilder jsonArrBuilder = Json.createArrayBuilder();
        for (String item : items) {
            jsonArrBuilder.add(item);
        }

        JsonArray jsonArr = jsonArrBuilder.build();
        String restEndpoint = "https://quotation.chuklee.com/quotation";
        String url = UriComponentsBuilder
                .fromUriString(restEndpoint)
                .toUriString();

        RequestEntity<String> req = RequestEntity.post(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonArr.toString());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = null;

        String payload = "";
        int statusCode = 0;
        try {
            resp = restTemplate.exchange(req, String.class);
            payload = resp.getBody();
            statusCode = resp.getStatusCode().value();
        } catch (HttpClientErrorException ex) {
            payload = ex.getResponseBodyAsString();
            statusCode = ex.getStatusCode().value();
            return null;
        } finally {
            System.out.printf("URL: %s\n", url);
            System.out.printf("Payload: %s\n", payload);
            System.out.printf("Status Code: %s\n", statusCode);
        }

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject json = reader.readObject();
        String quoteId = json.getString("quoteId");

        Map<String, Float> quotationsMap = new HashMap<>();
        JsonArray quotationsArr = json.getJsonArray("quotations");
        for (int i = 0; i < quotationsArr.size(); i++) {
            JsonObject quotationsObj = quotationsArr.getJsonObject(i);
            String item = quotationsObj.getString("item");
            Float unitPrice = quotationsObj.getJsonNumber("unitPrice").bigDecimalValue().floatValue();
            quotationsMap.put(item, unitPrice);
        }

        Quotation quotation = new Quotation();
        quotation.setQuoteId(quoteId);
        quotation.setQuotations(quotationsMap);

        return quotation;
    }

}
