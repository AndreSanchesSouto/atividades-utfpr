package edu.atividade.repositories;

import edu.atividade.entities.Carga;
import edu.atividade.entities.Passeio;

import java.util.ArrayList;
import java.util.List;

public class BDVeiculos {
    private List<Carga> veiculosCarga = new ArrayList<Carga>();
    private List<Passeio> veiculosPasseio = new ArrayList<Passeio>();

    public List<Carga> getVeiculosCarga() {
        return veiculosCarga;
    }

    public void setVeiculosCarga(List<Carga> veiculosCarga) {
        this.veiculosCarga = veiculosCarga;
    }

    public List<Passeio> getVeiculosPasseio() {
        return veiculosPasseio;
    }

    public void setVeiculosPasseio(List<Passeio> veiculosPasseio) {
        this.veiculosPasseio = veiculosPasseio;
    }

    public void adicionarVeiculoPasseio(Passeio v) {
        this.veiculosPasseio.add(v);
    }

    public void adicionarVeiculoCarga(Carga v) {
        this.veiculosCarga.add(v);
    }

    public void limparListPasseio() {
        this.veiculosPasseio.clear();
    }

    public void limparListCarga() {
        this.veiculosCarga.clear();
    }
}
