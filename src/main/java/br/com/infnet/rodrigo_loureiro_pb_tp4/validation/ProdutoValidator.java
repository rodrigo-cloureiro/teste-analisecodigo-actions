package br.com.infnet.rodrigo_loureiro_pb_tp4.validation;

import br.com.infnet.rodrigo_loureiro_pb_tp4.exception.EntradaInvalidaException;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.regex.Pattern;

public final class ProdutoValidator {
    private static final Pattern ALPHA_NUM_ASCII = Pattern.compile("^[\\p{L}0-9\\s,\\-().!]+$");
    private static final Pattern SQL_LIKE = Pattern.compile(
            "(?i)\\b(select|insert|update|delete|drop|union|alter|create|truncate|exec|declare|grant|revoke)\\b"
                    + "|(--|#|/\\*|\\*/|;|\\bOR\\b\\s+\\d+\\s*=\\s*\\d+|\\bAND\\b\\s+\\d+\\s*=\\s*\\d+|\\b1=1\\b|\\bOR\\b\\s+'1'='1')"
    );

    public static void validarId(UUID uuid, String campo) {
        if (uuid == null || uuid.toString().isBlank()) {
            throw new EntradaInvalidaException("O campo " + campo + " não pode ser vazio ou nulo.");
        }
    }

    public static void validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new EntradaInvalidaException("O campo " + campo + " não pode ser vazio ou nulo.");
        }

        if (valor.length() > 255) {
            throw new EntradaInvalidaException("O campo " + campo + " é muito longo");
        }

        if (!ALPHA_NUM_ASCII.matcher(valor).matches() || SQL_LIKE.matcher(valor.toLowerCase()).find()) {
            throw new EntradaInvalidaException("O conteúdo não é permitido.");
        }
    }

    public static void validarPreco(BigDecimal preco) {
        if (preco == null || preco.compareTo(BigDecimal.ZERO) < 0) {
            throw new EntradaInvalidaException("O preço não pode ser negativo ou nulo.");
        }
    }

    public static void validarQuantidade(int quantidade) {
        if (quantidade < 0) {
            throw new EntradaInvalidaException("A quantidade não pode ser negativa.");
        }
    }
}
