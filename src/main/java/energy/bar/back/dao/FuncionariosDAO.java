package energy.bar.back.dao;

import energy.bar.db.ConexaoBancoDeDados;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JComboBox;

public class FuncionariosDAO {
    
    private static int unidadeDoProgama = 1;

    public static void buscarFuncionariosPorUnidade(JComboBox<String> campoResponsavel) {
        // Consultar os funcionários pela unidade e situação
        String query = "SELECT id, nome, situacao, unidade FROM tb_funcionarios WHERE unidade = ? AND situacao = true";
        
        // Lista para armazenar os nomes dos funcionários encontrados
        ArrayList<String> funcionariosAtivos = new ArrayList<>();
        
        // Conexão e PreparedStatement definidos fora do try-with-resources
        try (Connection connection = ConexaoBancoDeDados.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
             
            // Definir o parâmetro da consulta
            statement.setInt(1, unidadeDoProgama);
            
            // Executar a consulta
            try (ResultSet resultSet = statement.executeQuery()) {
                // Processar os resultados
                while (resultSet.next()) {
                    String nome = resultSet.getString("nome");
                    // Adicionar o nome do funcionário no array
                    funcionariosAtivos.add(nome);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Verificar se encontrou funcionários e adicionar ao JComboBox
        if (funcionariosAtivos.isEmpty()) {
            System.out.println("Nenhum funcionário encontrado para a unidade " + unidadeDoProgama + " com a situação ativa.");
        } else {
            // Adicionar os nomes encontrados no JComboBox
            // Inicializar o array com o primeiro valor sendo um espaço em branco (caso você queira um item vazio como opção)
            String[] responsaveis = new String[funcionariosAtivos.size() + 1];  // +1 para o item vazio
            responsaveis[0] = "";  // O primeiro valor é um espaço em branco
            
            // Preencher o array com os nomes dos funcionários
            for (int i = 0; i < funcionariosAtivos.size(); i++) {
                responsaveis[i + 1] = funcionariosAtivos.get(i);
            }
            
            // Atualizar o JComboBox com os nomes dos responsáveis
            campoResponsavel.setModel(new javax.swing.DefaultComboBoxModel<>(responsaveis));
        }
    }

    public static int getUnidadeDoProgama() {
        return unidadeDoProgama;
    }
}