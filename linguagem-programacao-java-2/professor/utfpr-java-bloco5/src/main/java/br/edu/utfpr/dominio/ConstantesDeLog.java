package br.edu.utfpr.dominio;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Constantes nomeadas e amostras de dados para os vídeos do Bloco 5.
 */
public final class ConstantesDeLog {

    private ConstantesDeLog() {
    }

    public static final int STATUS_MINIMO_DE_ERRO_SERVIDOR = 500;
    public static final long LIMITE_PADRAO_DE_LENTIDAO_MS = 1_000;
    public static final int QUANTIDADE_DE_CAMPOS_DA_LINHA = 7;

    public static List<RegistroAcesso> amostraDeRegistros() {
        return List.of(
                new RegistroAcesso("REQ-1", LocalDateTime.of(2026, 7, 20, 9, 15), "192.168.0.10", "GET", "/api/pedidos", 200, 120),
                new RegistroAcesso("REQ-2", LocalDateTime.of(2026, 7, 20, 9, 40), "192.168.0.11", "POST", "/api/pedidos", 500, 2_400),
                new RegistroAcesso("REQ-3", LocalDateTime.of(2026, 7, 20, 11, 5), "192.168.0.10", "GET", "/health", 200, 15),
                new RegistroAcesso("REQ-4", LocalDateTime.of(2026, 7, 20, 11, 9), "192.168.0.12", "GET", "/api/relatorios", 503, 80),
                new RegistroAcesso("REQ-5", LocalDateTime.of(2026, 7, 20, 14, 2), "192.168.0.10", "PUT", "/api/clientes", 200, 1_500));
    }

    public static List<String> amostraDeLinhasCruas() {
        return List.of(
                "REQ-1 2026-07-20T09:15:00 192.168.0.10 GET /api/pedidos 200 120",
                "REQ-2 2026-07-20T09:40:00 192.168.0.11 POST /api/pedidos 500 2400",
                "linha corrompida sem os campos esperados",
                "REQ-3 2026-07-20T11:05:00 192.168.0.10 GET /health 200 15",
                "REQ-4 2026-07-20T11:09:00 192.168.0.12 GET /api/relatorios 503 80",
                "REQ-5 2026-07-20T14:02:00 192.168.0.10 PUT /api/clientes 200 1500");
    }
}