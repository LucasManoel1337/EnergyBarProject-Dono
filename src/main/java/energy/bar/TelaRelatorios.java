package energy.bar;

import energy.bar.support.LabelEnergyBar;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import energy.bar.db.ConexaoBancoDeDados;
import energy.bar.support.TimerAvisosLabels;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import energy.bar.support.QuantidadeCellRenderer;

class TelaRelatorios extends JPanel {

    LabelEnergyBar labelEnergyBar = new LabelEnergyBar();
    String[] colunas = {"ID", "Produto", "Qnt"};
    DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0);
    public JTable tabelaProdutos = new JTable(modeloTabela);
    public JTextField campoNome = new JTextField();
    private JButton bAdicionar;
    private JLabel lfaltaDeDados = new JLabel("Todos os campos devem ser preenchidos!");
    private JLabel lCadastroFeito = new JLabel("Produto cadastrado com sucesso!");

    ConexaoBancoDeDados b = new ConexaoBancoDeDados();
    TimerAvisosLabels tir = new TimerAvisosLabels();

    public TelaRelatorios() {
        setLayout(null);

        // Label falta de dados
        lfaltaDeDados.setFont(new Font("Arial", Font.BOLD, 16));
        lfaltaDeDados.setBounds(220, 510, 350, 40); // Define posição e tamanho
        lfaltaDeDados.setForeground(Color.RED);
        lfaltaDeDados.setVisible(false);
        add(lfaltaDeDados);

        // Label falta de dados
        lCadastroFeito.setFont(new Font("Arial", Font.BOLD, 16));
        lCadastroFeito.setBounds(250, 510, 350, 40); // Define posição e tamanho
        lCadastroFeito.setForeground(Color.GREEN);
        lCadastroFeito.setVisible(false);
        add(lCadastroFeito);

        // Criando e adicionando a label EnergyBar
        JLabel energyBarLabel = labelEnergyBar.criarLabelEnergyBar();
        add(energyBarLabel);

        tabelaProdutos.setRowHeight(30);
        tabelaProdutos.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaProdutos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        tabelaProdutos.getTableHeader().setBackground(new Color(32, 3, 3));
        tabelaProdutos.getTableHeader().setForeground(Color.WHITE);
        tabelaProdutos.setBackground(new Color(245, 245, 245));
        tabelaProdutos.setForeground(new Color(0, 0, 0));
        tabelaProdutos.setGridColor(new Color(200, 200, 200));
        tabelaProdutos.setSelectionBackground(new Color(52, 152, 219));
        tabelaProdutos.setSelectionForeground(Color.WHITE);
        tabelaProdutos.setRowHeight(30);
        tabelaProdutos.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(modeloTabela);
        tabelaProdutos.setRowSorter(sorter);

        sorter.setComparator(0, (o1, o2) -> {
            try {
                int id1 = Integer.parseInt(o1.toString());
                int id2 = Integer.parseInt(o2.toString());
                return Integer.compare(id1, id2);
            } catch (NumberFormatException e) {
                return 0;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
        scrollPane.setBounds(10, 70, 400, 480);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2));
        add(scrollPane);

        tabelaProdutos.getColumnModel().getColumn(0).setPreferredWidth(5);
        tabelaProdutos.getColumnModel().getColumn(1).setPreferredWidth(230);

        // Adiciona o renderer para a coluna de quantidade
        tabelaProdutos.getColumnModel().getColumn(2).setCellRenderer(new QuantidadeCellRenderer());

        carregarTodosProdutos();
    }

    public void carregarTodosProdutos() {
        String sql = "SELECT c.id AS produto_id, c.produto AS produto_nome, "
                + "COALESCE(SUM(pc.quantidade), 0) AS total_qnt "
                + "FROM tb_catalogo c "
                + "LEFT JOIN tb_produtos_compras pc ON c.id = pc.produto_id "
                + "GROUP BY c.id, c.produto";

        try (Connection conn = b.getConnection(); // Usa sua conexão existente
                 PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            modeloTabela.setRowCount(0); // Limpa a tabela antes de inserir novos dados

            while (rs.next()) {
                int id = rs.getInt("produto_id");
                String produto = rs.getString("produto_nome");
                int quantidade = rs.getInt("total_qnt"); // Se não existir na tb_produtos_compras, será 0

                modeloTabela.addRow(new Object[]{id, produto, quantidade});
            }

            System.out.println("Produtos do catálogo carregados com sucesso!");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar produtos do catálogo.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

}
