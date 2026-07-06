package edu.atividade;

public class Motor {
    private Integer qtdPist;
    private Integer potencia;

    public Motor() {
        this.qtdPist = 0;
        this.potencia = 0;
    }

    public Integer getQtdPist() {
        return qtdPist;
    }

    public void setQtdPist(Integer qtdPist) {
        this.qtdPist = qtdPist;
    }

    public Integer getPotencia() {
        return potencia;
    }

    public void setPotencia(Integer potencia) {
        this.potencia = potencia;
    }
}
