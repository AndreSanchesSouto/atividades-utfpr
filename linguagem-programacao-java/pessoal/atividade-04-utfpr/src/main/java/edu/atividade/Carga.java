package edu.atividade;

public final class Carga extends Veiculo {
    private int cargaMax;
    private int tara;

    public Carga () {
        super();
        this.cargaMax = 0;
        this.tara = 0;
    }

    @Override
    public float calcVel(float velocMax) {
        return velocMax * 100000;
    }

    public int getCargaMax() {
        return cargaMax;
    }

    public void setCargaMax(int cargaMax) {
        this.cargaMax = cargaMax;
    }

    public int getTara() {
        return tara;
    }

    public void setTara(int tara) {
        this.tara = tara;
    }

    @Override
    public String toString() {
        return "Veiculo de carga {" +
                "\n  Marca: " + super.getMarca() +
                "\n  Modelo: " + super.getModelo() +
                "\n  Placa: " + super.getPlaca() +
                "\n  Velocidade Máx: " + super.getVelocMax() +
                "\n  Qtd Rodas: " + super.getQtdRodas() +
                "\n  Carga máxima: " + this.cargaMax +
                "\n  Tara: " + this.tara +
                "\n  Motor: {" +
                "\n    Potência: " + super.getMotor().getPotencia() +
                "\n    Qtd Pistões: " + super.getMotor().getQtdPist() +
                "\n  }" +
                "\n}";
    }
}
