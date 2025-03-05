package energy.bar.support;

public class Produto {
    private String codigo;
    private String nome;
    private int quantidade;
    private double preco;
    private String lote;

    public Produto(String codigo, String nome, int quantidade, double preco, String lote) {
        this.codigo = codigo;
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
        this.lote = lote;
    }

    // Getters e Setters
    public String getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public int getQuantidade() { return quantidade; }
    public double getPreco() { return preco; }
    public String getLote() { return lote; }

    @Override
    public String toString() {
        return codigo + " | " + nome + " | " + quantidade + " | " + preco + " | " + lote;
    }

    public Object getId() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Object getValorVenda() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
