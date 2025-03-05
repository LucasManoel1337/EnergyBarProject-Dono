package energy.bar.back.dto;

public class ProdutoDTO {

    private int id;
    private String nome;
    private String responsavel;
    private int estoque;
    private String validade;
    private double valorDeCusto;
    private double valorDeVenda;
    private String lote;
    private String dataHoraCadastro;
    private int produtoUnidade;

    public ProdutoDTO(int id, String nome, String responsavel, int estoque, String validade, double valorDeCusto, double valorDeVenda, String lote, String dataHoraCadastro, int produtoUnidade) {
        this.id = id;
        this.nome = nome;
        this.responsavel = responsavel;
        this.estoque = estoque;
        this.validade = validade;
        this.valorDeCusto = valorDeCusto;
        this.valorDeVenda = valorDeVenda;
        this.lote = lote;
        this.dataHoraCadastro = dataHoraCadastro;
        this.produtoUnidade = produtoUnidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public double getValorDeCusto() {
        return valorDeCusto;
    }

    public void setValorDeCusto(double valorDeCusto) {
        this.valorDeCusto = valorDeCusto;
    }

    public double getValorDeVenda() {
        return valorDeVenda;
    }

    public void setValorDeVenda(double valorDeVenda) {
        this.valorDeVenda = valorDeVenda;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getDataHoraCadastro() {
        return dataHoraCadastro;
    }

    public void setDataHoraCadastro(String dataHoraCadastro) {
        this.dataHoraCadastro = dataHoraCadastro;
    }

    public int getProdutoUnidade() {
        return produtoUnidade;
    }

    public void setProdutoUnidade(int produtoUnidade) {
        this.produtoUnidade = produtoUnidade;
    }
}
