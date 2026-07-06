package edu.atividade;

public final class Passeio extends Veiculo {
    private int qtdPassageiros;

    public Passeio() {
        super();
        this.qtdPassageiros = 0;
    }

    @Override
    public float calcVel(float velocMax) {
        return velocMax * 1000;
    }

    public int getQtdPassageiros() {
        return qtdPassageiros;
    }

    public void setQtdPassageiros(int qtdPassageiros) {
        this.qtdPassageiros = qtdPassageiros;
    }

    @Override
    public String toString() {
        return "Veiculo de passeio {" +
                "\n  Marca: " + super.getMarca() +
                "\n  Modelo: " + super.getModelo() +
                "\n  Placa: " + super.getPlaca() +
                "\n  Velocidade Máx: " + super.getVelocMax() +
                "\n  Qtd Rodas: " + super.getQtdRodas() +
                "\n  Qtd Passageiros: " + this.qtdPassageiros +
                "\n  Motor: {" +
                "\n    Potência: " + super.getMotor().getPotencia() +
                "\n    Qtd Pistões: " + super.getMotor().getQtdPist() +
                "\n  }" +
                "\n}";
    }
}
