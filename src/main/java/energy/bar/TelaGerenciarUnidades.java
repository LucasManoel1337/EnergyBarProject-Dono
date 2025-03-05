package energy.bar;

import energy.bar.db.ConexaoBancoDeDados;
import energy.bar.support.LabelEnergyBar;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class TelaGerenciarUnidades extends JPanel {

    LabelEnergyBar labelEnergyBar = new LabelEnergyBar();
    
    String[] colunas = {"ID", "Nome"};
    DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0);
    public JTable tabelaUnidades = new JTable(modeloTabela);

    public TelaGerenciarUnidades() {
        setLayout(null);

        // Criando e adicionando a label EnergyBar
        JLabel energyBarLabel = labelEnergyBar.criarLabelEnergyBar();
        add(energyBarLabel);

        tabelaUnidades.setFont(new Font("Arial", Font.PLAIN, 14)); // Fonte da tabela
        tabelaUnidades.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16)); // Fonte do cabeçalho
        tabelaUnidades.getTableHeader().setBackground(new Color(32, 3, 3)); // Cor de fundo do cabeçalho
        tabelaUnidades.getTableHeader().setForeground(Color.WHITE); // Cor do texto do cabeçalho
        tabelaUnidades.setBackground(new Color(245, 245, 245)); // Cor de fundo da tabela
        tabelaUnidades.setForeground(new Color(0, 0, 0)); // Cor do texto
        tabelaUnidades.setGridColor(new Color(200, 200, 200)); // Cor das linhas de grade
        tabelaUnidades.setSelectionBackground(new Color(52, 152, 219)); // Cor de fundo ao selecionar
        tabelaUnidades.setSelectionForeground(Color.WHITE); // Cor do texto ao selecionar
        tabelaUnidades.setRowHeight(30); // Ajustando a altura das linhas
        tabelaUnidades.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1)); // Bordas da tabela

        // Usando TableRowSorter para ordenar numericamente a coluna ID
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(modeloTabela);
        tabelaUnidades.setRowSorter(sorter);

        // Definir o tipo de comparação para a coluna ID (coluna 0)
        sorter.setComparator(0, (o1, o2) -> {
            try {
                int id1 = Integer.parseInt(o1.toString());
                int id2 = Integer.parseInt(o2.toString());
                return Integer.compare(id1, id2); // Comparação numérica
            } catch (NumberFormatException e) {
                return 0; // Em caso de erro de conversão
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabelaUnidades);
        scrollPane.setVisible(true);
        scrollPane.setBounds(10, 70, 400, 480); // Definindo o tamanho e posição
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2)); // Bordas do JScrollPane
        add(scrollPane);
        atualizarTabelaUnidades();
        
        JLabel lId = new JLabel("Nome da nova unidade");
        lId.setFont(new Font("Arial", Font.BOLD, 16));
        lId.setBounds(420, 60, 300, 40); // Define posição e tamanho
        lId.setVisible(true);
        add(lId);
        JTextField campoNovaUnidade = new JTextField();
        campoNovaUnidade.setBounds(420, 90, 220, 30);
        campoNovaUnidade.setBackground(Color.LIGHT_GRAY);
        campoNovaUnidade.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        campoNovaUnidade.setFont(new Font("Arial", Font.BOLD, 16));
        campoNovaUnidade.setVisible(true);
        add(campoNovaUnidade);
        
        JButton bCadastrar = new JButton();
        bCadastrar.setText("Cadastrar");
        bCadastrar.setFont(new Font("Arial", Font.BOLD, 14));
        bCadastrar.setBounds(650, 90, 100, 30);
        bCadastrar.setBackground(new Color(32, 3, 3));
        bCadastrar.setForeground(Color.WHITE);
        bCadastrar.setFocusPainted(false);
        bCadastrar.setBorderPainted(false);
        bCadastrar.setVisible(true);
        add(bCadastrar);
        
        bCadastrar.addActionListener(e -> {
            String nomeUnidade = campoNovaUnidade.getText().trim();
            if (nomeUnidade.isEmpty()) {
                System.out.println("O nome da unidade não pode ser vazio.");
                return;
            }
            
            // Verifica se já existe a unidade
            if (existeUnidade(nomeUnidade)) {
                System.out.println("Unidade com o nome '" + nomeUnidade + "' já existe.");
            } else {
                // Se não existir, cria a unidade
                criarUnidade(nomeUnidade);
                atualizarTabelaUnidades();
            }
        });
    }

    private boolean existeUnidade(String nomeUnidade) {
        boolean existe = false;
        try (Connection conn = ConexaoBancoDeDados.getConnection()) {
            String query = "SELECT COUNT(*) FROM tb_unidades WHERE nome = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nomeUnidade);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    existe = true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return existe;
    }

    private void criarUnidade(String nomeUnidade) {
        try (Connection conn = ConexaoBancoDeDados.getConnection()) {
            String query = "INSERT INTO tb_unidades (nome) VALUES (?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nomeUnidade);
                stmt.executeUpdate();
                System.out.println("Unidade '" + nomeUnidade + "' cadastrada com sucesso.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void atualizarTabelaUnidades() {
    // Limpa a tabela antes de adicionar novos dados
    modeloTabela.setRowCount(0);
    try (Connection conn = ConexaoBancoDeDados.getConnection()) {
        // Consulta SQL para buscar todas as unidades
        String query = "SELECT id, nome FROM tb_unidades";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            
            // Adiciona as unidades à tabela
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                modeloTabela.addRow(new Object[] { id, nome });
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}
}
