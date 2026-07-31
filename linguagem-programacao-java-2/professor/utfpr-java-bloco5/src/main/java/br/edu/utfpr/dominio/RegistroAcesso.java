package br.edu.utfpr.dominio;

import java.time.LocalDateTime;

public record RegistroAcesso(
        String identificador,
        LocalDateTime instante,
        String enderecoIp,
        String metodo,
        String rota,
        int status,
        long tempoRespostaMs) {

    public boolean ehErroDeServidor() {
        return status >= ConstantesDeLog.STATUS_MINIMO_DE_ERRO_SERVIDOR;
    }

    public boolean ehLenta(long limiteEmMilissegundos) {
        return tempoRespostaMs > limiteEmMilissegundos;
    }
}