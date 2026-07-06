package edu.atividade.exceptions;

public class VeicExistException extends Exception {
    public VeicExistException() {
        super("Já existe um veículo com esta placa");
    }
}
