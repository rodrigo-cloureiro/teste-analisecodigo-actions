package br.com.infnet.rodrigo_loureiro_pb_tp4.cotacao;

import br.com.infnet.rodrigo_loureiro_pb_tp4.exception.ConversaoMoedaException;
import br.com.infnet.rodrigo_loureiro_pb_tp4.model.cotacao.Cotacao;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CotacaoTest {
    private Cotacao cotacao;
    private Cotacao cotacaoWithMock;
    private HttpClient httpClientMock;

    @BeforeEach
    public void setUp() {
        this.cotacao = new Cotacao();
        this.httpClientMock = mock(HttpClient.class);
        this.cotacaoWithMock = new Cotacao(httpClientMock);
    }

    @AfterEach
    public void tearDown() {
        if (this.cotacao != null) {
            this.cotacao = null;
        }
        if (this.httpClientMock != null) {
            reset(httpClientMock);
        }
        if (this.cotacaoWithMock != null) {
            this.cotacaoWithMock = null;
        }
    }

    @Test
    public void deveRealizarACotacaoComSucesso() {
        assertDoesNotThrow(() -> cotacao.currencyConversion("USD"));
        assertDoesNotThrow(() -> cotacao.currencyConversion("EUR"));
    }

    @Test
    public void deveLancarExcecaoAoRealizarRequisicaoInvalida() {
        String moeda = "ROD";
        ConversaoMoedaException exception = assertThrows(
                ConversaoMoedaException.class, () -> cotacao.currencyConversion(moeda)
        );

        assertTrue(exception.getMessage().contains("Requisição inválida"));
        assertTrue(exception.getMessage().contains("moeda nao encontrada"));
        assertTrue(exception.getMessage().contains(moeda));
    }

    @Test
    public void deveLancarExcecaoQuandoOcorrerErroInternoDaApi() throws IOException, InterruptedException {
        HttpResponse<String> httpResponseMock = mock(HttpResponse.class);
        when(httpResponseMock.statusCode()).thenReturn(500);

        when(httpClientMock.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(httpResponseMock);

        ConversaoMoedaException exception = getException("USD");

        assertEquals("Erro no servidor da API.", exception.getMessage());
    }

    @Test
    public void deveTratarErroDeRede() throws IOException, InterruptedException {
        when(httpClientMock.send(
                any(HttpRequest.class), any(HttpResponse.BodyHandler.class)
        )).thenThrow(new IOException("Falha de rede!"));

        ConversaoMoedaException exception = getException("USD");

        assertEquals("Erro ao obter cotação da moeda: Falha de rede!", exception.getMessage());
    }

    @Test
    public void deveTratarTimeout() throws IOException, InterruptedException {
        when(httpClientMock.send(
                any(HttpRequest.class), any(HttpResponse.BodyHandler.class)
        )).thenThrow(new InterruptedException("Timeout!"));

        ConversaoMoedaException exception = getException("USD");

        assertEquals("Erro ao obter cotação da moeda: Timeout!", exception.getMessage());
    }

    private ConversaoMoedaException getException(String moeda) {
        return assertThrows(ConversaoMoedaException.class, () -> this.cotacaoWithMock.currencyConversion(moeda));
    }
}
