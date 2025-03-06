package energy.bar.support;

import energy.bar.db.ConexaoBancoDeDados;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UnidadeService {
    private static List<String> unidadesIds = new ArrayList<>();

    // Método para carregar as unidades do banco
    public static List<String> getUnidadesIds() {
        if (unidadesIds.isEmpty()) {
            atualizarUnidadesIds();
        }
        return new ArrayList<>(unidadesIds);
    }

    // Método para atualizar as unidades (chamar após cadastrar uma nova unidade)
    public static void atualizarUnidadesIds() {
        unidadesIds.clear(); // Limpa a lista antes de recarregar

        try (Connection conn = ConexaoBancoDeDados.getConnection()) {
            String query = "SELECT id FROM tb_unidades";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    unidadesIds.add(String.valueOf(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}