package br.com.infnet.rodrigo_loureiro_pb_tp4.model.cotacao;

import br.com.infnet.rodrigo_loureiro_pb_tp4.exception.ConversaoMoedaException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@ToString
@Getter
@Setter
public class Cotacao {
    private String code;
    private String codein;
    private String name;
    private double high;
    private double low;
    private double varBid;
    private double pctChange;
    private double bid;
    private double ask;
    private long timestamp;
    @JsonProperty("create_date")
    private String createDate;

    private final HttpClient httpClient;
    private final String BASE_URL = "https://economia.awesomeapi.com.br/last/BRL-";

    public Cotacao() {
        this.httpClient = HttpClient.newBuilder().build();
    }

    public Cotacao(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public BigDecimal currencyConversion(String moeda) {
        try {
            String url = BASE_URL + moeda.toUpperCase();
            HttpResponse<String> response = response(url);
            ObjectMapper objectMapper = JsonMapper.builder().build();

            if (response.statusCode() >= 400 && response.statusCode() < 500) {
                String errorMessage = objectMapper.readTree(response.body())
                        .get("message")
                        .asText();
                throw new ConversaoMoedaException("Requisição inválida: " + errorMessage);
            }
            if (response.statusCode() >= 500)
                throw new ConversaoMoedaException("Erro no servidor da API.");

            CotacaoPayload cotacaoPayload = objectMapper.readValue(response.body(), CotacaoPayload.class);
            Cotacao cotacao = cotacaoPayload.getCotacao();

            return BigDecimal.valueOf(cotacao.getBid());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new ConversaoMoedaException("Erro ao obter cotação da moeda: " + e.getMessage());
        }
    }

    private HttpResponse<String> response(String url) throws URISyntaxException, InterruptedException, IOException {
        return this.httpClient.send(request(url), HttpResponse.BodyHandlers.ofString());
    }

    private HttpRequest request(String url) throws URISyntaxException {
        return HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(new URI(url))
                .build();
    }
}
