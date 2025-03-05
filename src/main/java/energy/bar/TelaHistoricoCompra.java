package energy.bar;

import energy.bar.support.LabelEnergyBar;
import energy.bar.support.TimerAvisosLabels;
import java.awt.BorderLayout;
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

import energy.bar.EnergyBarProject;
import energy.bar.TelaSaidas;
import energy.bar.db.ConexaoBancoDeDados;
import energy.bar.support.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

class TelaHistoricoCompra extends JPanel {

    private EnergyBarProject mainApp;
    TelaSaidas telaSaidas = new TelaSaidas(mainApp); // Passar a instância correta

    TimerAvisosLabels tir = new TimerAvisosLabels();
    LabelEnergyBar labelEnergyBar = new LabelEnergyBar();

    String[] colunas = {"ID", "Produto", "Qtn", "Valor", "Lote"};
    DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0);
    public JTable tabelaProdutos = new JTable(modeloTabela);

    public JTextField campoQtn = new JTextField("000");
    public JTextField campoId = new JTextField("000");
    public JTextField campoTipocliente = new JTextField();
    public JTextField campoTotalDaCompra = new JTextField();
    public JTextField campoDesconto = new JTextField();
    public JTextField campoFormaDePagamento = new JTextField();
    public JTextField campoFuncionario = new JTextField();

    private JButton botaoCarregar;

    public TelaHistoricoCompra(EnergyBarProject mainApp) {
        setLayout(new BorderLayout());
        this.mainApp = mainApp;
        inicializarComponentes();

        // Criando e adicionando a label EnergyBar
        JLabel energyBarLabel = labelEnergyBar.criarLabelEnergyBar();
        add(energyBarLabel);

        setLayout(null);

        // Criando e adicionando a label EnergyBar
        //JLabel energyBarLabel = labelEnergyBar.criarLabelEnergyBar(dir);
        add(energyBarLabel);

        tabelaProdutos.setRowHeight(30);
        tabelaProdutos.setFont(new Font("Arial", Font.PLAIN, 14)); // Fonte da tabela
        tabelaProdutos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16)); // Fonte do cabeçalho
        tabelaProdutos.getTableHeader().setBackground(new Color(32, 3, 3)); // Cor de fundo do cabeçalho
        tabelaProdutos.getTableHeader().setForeground(Color.WHITE); // Cor do texto do cabeçalho
        tabelaProdutos.setBackground(new Color(245, 245, 245)); // Cor de fundo da tabela
        tabelaProdutos.setForeground(new Color(0, 0, 0)); // Cor do texto
        tabelaProdutos.setGridColor(new Color(200, 200, 200)); // Cor das linhas de grade
        tabelaProdutos.setSelectionBackground(new Color(52, 152, 219)); // Cor de fundo ao selecionar
        tabelaProdutos.setSelectionForeground(Color.WHITE); // Cor do texto ao selecionar
        tabelaProdutos.setRowHeight(30); // Ajustando a altura das linhas
        tabelaProdutos.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1)); // Bordas da tabela

        // Usando TableRowSorter para ordenar numericamente a coluna ID
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(modeloTabela);
        tabelaProdutos.setRowSorter(sorter);

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

        tabelaProdutos.getColumnModel().getColumn(0).setPreferredWidth(5);
        tabelaProdutos.getColumnModel().getColumn(1).setPreferredWidth(80);
        tabelaProdutos.getColumnModel().getColumn(2).setPreferredWidth(10);

        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
        scrollPane.setVisible(true);
        scrollPane.setBounds(10, 70, 400, 400); // Definindo o tamanho e posição
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2)); // Bordas do JScrollPane
        add(scrollPane);

        JLabel lTipoCliente = new JLabel("Tipo de cliente");
        lTipoCliente.setFont(new Font("Arial", Font.BOLD, 16));
        lTipoCliente.setBounds(420, 70, 330, 30);
        lTipoCliente.setVisible(true);
        add(lTipoCliente);
        campoTipocliente.setBounds(420, 100, 330, 30);
        campoTipocliente.setBackground(Color.LIGHT_GRAY);
        campoTipocliente.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        campoTipocliente.setFocusable(false);
        campoTipocliente.setFont(new Font("Arial", Font.BOLD, 16));
        campoTipocliente.setVisible(true);
        campoTipocliente.setEditable(false);
        add(campoTipocliente);

        JLabel lFormaDePagamento = new JLabel("Forma de pagamento");
        lFormaDePagamento.setFont(new Font("Arial", Font.BOLD, 16));
        lFormaDePagamento.setBounds(420, 130, 330, 30);
        lFormaDePagamento.setVisible(true);
        add(lFormaDePagamento);
        campoFormaDePagamento.setBounds(420, 160, 330, 30);
        campoFormaDePagamento.setBackground(Color.LIGHT_GRAY);
        campoFormaDePagamento.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        campoFormaDePagamento.setFocusable(false);
        campoFormaDePagamento.setFont(new Font("Arial", Font.BOLD, 16));
        campoFormaDePagamento.setVisible(true);
        campoFormaDePagamento.setEditable(false);
        add(campoFormaDePagamento);

        // Label e TextField ID
        JLabel lDesconto = new JLabel("Desconto");
        lDesconto.setFont(new Font("Arial", Font.BOLD, 16));
        lDesconto.setBounds(420, 190, 330, 30);
        lDesconto.setVisible(true);
        add(lDesconto);
        campoDesconto.setBounds(420, 220, 330, 30);
        campoDesconto.setBackground(Color.LIGHT_GRAY);
        campoDesconto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        campoDesconto.setFont(new Font("Arial", Font.BOLD, 16));
        campoDesconto.setVisible(true);
        campoDesconto.setEditable(false);
        add(campoDesconto);

        JLabel lFuncionario = new JLabel("Funcionario");
        lFuncionario.setFont(new Font("Arial", Font.BOLD, 16));
        lFuncionario.setBounds(420, 250, 330, 30);
        lFuncionario.setVisible(true);
        add(lFuncionario);
        campoFuncionario.setBounds(420, 280, 330, 30);
        campoFuncionario.setBackground(Color.LIGHT_GRAY);
        campoFuncionario.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        campoFuncionario.setFocusable(false);
        campoFuncionario.setFont(new Font("Arial", Font.BOLD, 16));
        campoFuncionario.setVisible(true);
        campoFuncionario.setEditable(false);
        add(campoFuncionario);

        JLabel lTotalDaCompra = new JLabel("Total da compra");
        lTotalDaCompra.setFont(new Font("Arial", Font.BOLD, 16));
        lTotalDaCompra.setBounds(420, 310, 300, 40); // Define posição e tamanho
        lTotalDaCompra.setVisible(true);
        add(lTotalDaCompra);
        campoTotalDaCompra.setBounds(420, 340, 330, 30);
        campoTotalDaCompra.setBackground(Color.LIGHT_GRAY);
        campoTotalDaCompra.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        campoTotalDaCompra.setFont(new Font("Arial", Font.BOLD, 16));
        campoTotalDaCompra.setVisible(true);
        campoTotalDaCompra.setEditable(false);
        add(campoTotalDaCompra);
    }

    private void inicializarComponentes() {
        JButton btnVoltar = new JButton("VOLTAR");
        btnVoltar.setBounds(10, 500, 190, 30);
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 14));
        btnVoltar.setBackground(new Color(32, 3, 3));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setBorderPainted(false);
        add(btnVoltar);
        btnVoltar.addActionListener(e -> {
            mainApp.exibirTela(mainApp.getTelaSaidas()); // Voltar para a tela de produtos
        });
    }

    public void setCampoTipocliente(JTextField campoTipocliente) {
        this.campoTipocliente = campoTipocliente;
    }

    public void setCampoTotalDaCompra(JTextField campoTotalDaCompra) {
        this.campoTotalDaCompra = campoTotalDaCompra;
    }

    public void setCampoDesconto(JTextField campoDesconto) {
        this.campoDesconto = campoDesconto;
    }

    public void setCampoFuncionario(JTextField campoFuncionario) {
        this.campoFuncionario = campoFuncionario;
    }

    public void carregarProdutosPorCompra(int idCompra) {
        // Limpar a tabela antes de adicionar novos produtos
        modeloTabela.setRowCount(0);

        try (Connection conn = ConexaoBancoDeDados.getConnection()) {
            String sql = "SELECT * FROM tb_produtos_compras WHERE compra_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCompra);

            ResultSet rs = stmt.executeQuery();

            // Adiciona os produtos na tabela
            while (rs.next()) {
                int idProduto = rs.getInt("produto_id");
                String nomeProduto = rs.getString("produto_nome");
                int quantidade = rs.getInt("quantidade");
                double preco = rs.getDouble("preco_unitario");
                String lote = rs.getString("lote");

                // Adicionar os dados à tabela de produtos
                modeloTabela.addRow(new Object[]{idProduto, nomeProduto, quantidade, preco, lote});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar os produtos da compra!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
