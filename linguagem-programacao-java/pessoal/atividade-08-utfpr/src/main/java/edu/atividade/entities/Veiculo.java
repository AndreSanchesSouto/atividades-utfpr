package edu.atividade.entities;

public abstract class Veiculo {
    private String placa;
    private String marca;
    private String modelo;
    private String cor;
    private float velocMax;
    private int qtdRodas;
    private Motor motor;

    public Veiculo() {
        this.marca = " ";
        this.modelo = " ";
        this.velocMax = 0f;
        this.qtdRodas = 0;
        this.placa = " ";
        this.cor = " ";
        this.motor = new Motor();
    }

    public abstract float calcVel(float velocMax);

    public Motor getMotor() {
        return motor;
    }

    public final void setMotor(Motor motor) {
        this.motor = motor;
    }

    public int getQtdRodas() {
        return qtdRodas;
    }

    public final void setQtdRodas(Integer qtdRodas) {
        this.qtdRodas = qtdRodas;
    }

    public float getVelocMax() {
        return velocMax;
    }

    public final void setVelocMax(float velocMax) {
        this.velocMax = velocMax;
    }

    public String getModelo() {
        return modelo;
    }

    public final void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public final void setMarca(String marca) {
        this.marca = marca;
    }

    public String getPlaca() {
        return placa;
    }

    public final void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getCor() {
        return cor;
    }

    public final void setCor(String cor) {
        this.cor = cor;
    }
}
