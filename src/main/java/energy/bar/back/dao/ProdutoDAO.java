package energy.bar.back.dao;

import energy.bar.back.dto.ProdutoDTO;
import energy.bar.db.ConexaoBancoDeDados;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProdutoDAO {
    
    public boolean cadastrarProduto(ProdutoDTO produto, FuncionariosDAO funcionario) {
        String sql = "INSERT INTO tb_produtos (id_produto, nome, responsavel, estoque, validade, valor_custo, valor_venda, lote, data_cadastro, produto_unidade) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBancoDeDados.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, produto.getId());
            stmt.setString(2, produto.getNome());
            stmt.setString(3, produto.getResponsavel());
            stmt.setInt(4, produto.getEstoque());
            stmt.setString(5, produto.getValidade());
            stmt.setDouble(6, produto.getValorDeCusto());
            stmt.setDouble(7, produto.getValorDeVenda());
            stmt.setString(8, produto.getLote());
            stmt.setString(9, produto.getDataHoraCadastro());
            stmt.setInt(10, funcionario.getUnidadeDoProgama());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
