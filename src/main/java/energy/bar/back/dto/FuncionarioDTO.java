package energy.bar.back.dto;

public class FuncionarioDTO {
    private int id;
    private String nome;

    // Construtor
    public FuncionarioDTO (int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return id + " - " + nome;  // Para exibir no ComboBox
    }
}
