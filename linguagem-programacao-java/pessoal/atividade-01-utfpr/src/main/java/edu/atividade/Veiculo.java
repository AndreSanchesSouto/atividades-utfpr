package edu.atividade;

public class Veiculo {
    private String marca;
    private String modelo;
    private Float velocMax;
    private Integer qtdRodas;
    private Motor motor;
    private String placa;

    public Veiculo() {
        this.marca = " ";
        this.modelo = " ";
        this.velocMax = 0f;
        this.qtdRodas = 0;
        this.placa = " ";
        this.motor = new Motor();
    }

    public Motor getMotor() {
        return motor;
    }

    public void setMotor(Motor motor) {
        this.motor = motor;
    }

    public Integer getQtdRodas() {
        return qtdRodas;
    }

    public void setQtdRodas(Integer qtdRodas) {
        this.qtdRodas = qtdRodas;
    }

    public Float getVelocMax() {
        return velocMax;
    }

    public void setVelocMax(Float velocMax) {
        this.velocMax = velocMax;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    @Override
    public String toString() {
        return "Veiculo {" +
                "\n  Marca: " + marca +
                "\n  Modelo: " + modelo +
                "\n  Placa: " + placa +
                "\n  Velocidade Máx: " + velocMax +
                "\n  Qtd Rodas: " + qtdRodas +
                "\n  Motor: {" +
                "\n    Potência: " + motor.getPotencia() +
                "\n    Qtd Pistões: " + motor.getQtdPist() +
                "\n  }" +
                "\n}";
    }
}
