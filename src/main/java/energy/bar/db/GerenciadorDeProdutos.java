package energy.bar.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class GerenciadorDeProdutos {

    // Método para verificar estoque zero e remover lote
    public static void removerLotesComEstoqueZero() {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        try {
            // Conectar ao banco de dados
            connection = ConexaoBancoDeDados.getConnection();
            
            // Consultar produtos com estoque igual a zero
            String sqlConsulta = "SELECT id, lote FROM tb_produtos WHERE estoque = 0";
            statement = connection.createStatement();
            rs = statement.executeQuery(sqlConsulta);

            // Verificar se há produtos com estoque zero
            boolean encontrouProdutoComEstoqueZero = false;
            while (rs.next()) {
                int idProduto = rs.getInt("id");
                String loteProduto = rs.getString("lote");
                
                // Remover o lote do banco de dados
                removerProdutoPorLote(connection, idProduto, loteProduto);
                System.out.println("Produto removido: ID = " + idProduto + ", Lote = " + loteProduto);
                encontrouProdutoComEstoqueZero = true;
            }

            if (!encontrouProdutoComEstoqueZero) {
                System.out.println("Não há produtos com estoque 0.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Fechar os recursos
            try {
                if (rs != null) rs.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Método para remover produto pelo ID e lote
    private static void removerProdutoPorLote(Connection connection, int idProduto, String loteProduto) {
        PreparedStatement stmt = null;

        try {
            // Preparar SQL para remover o produto do banco de dados
            String sqlRemover = "DELETE FROM tb_produtos WHERE id = ? AND lote = ?";
            stmt = connection.prepareStatement(sqlRemover);
            stmt.setInt(1, idProduto);
            stmt.setString(2, loteProduto);

            // Executar a remoção
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Produto com ID = " + idProduto + " e Lote = " + loteProduto + " removido com sucesso.");
            } else {
                System.out.println("Nenhum produto encontrado para remover com ID = " + idProduto + " e Lote = " + loteProduto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
