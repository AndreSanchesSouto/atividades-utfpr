package br.org.utfpr.todo;

public record Todo(
        String id,
        String titulo,
        String descricao,
        boolean feito
) {
    public Todo {
        if (titulo == null || titulo.length() < 3) {
            throw new IllegalArgumentException("Um titulo deve ter no mínimo 3 caracteres");
        }
    }
}
