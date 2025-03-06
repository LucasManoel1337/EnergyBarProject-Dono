package energy.bar;

import energy.bar.back.dao.FuncionariosDAO;
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
import energy.bar.support.UnidadeService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

class TelaRelatorios extends JPanel {

    LabelEnergyBar labelEnergyBar = new LabelEnergyBar();
    String[] colunas = {"ID", "Produto", "Qnt", "Lucro Bruto"};
    DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0);
    public JTable tabelaProdutos = new JTable(modeloTabela);
    public JTextField campoNome = new JTextField();
    private JButton bAdicionar;
    private JLabel lfaltaDeDados = new JLabel("Todos os campos devem ser preenchidos!");
    private JLabel lCadastroFeito = new JLabel("Produto cadastrado com sucesso!");

    private ArrayList<String> unidadesIds = new ArrayList<>();
    private JTextField campoTotalProdutos = new JTextField();

    ConexaoBancoDeDados b = new ConexaoBancoDeDados();
    TimerAvisosLabels tir = new TimerAvisosLabels();

    private JTextField campoDataInicio = new JTextField();
    private JTextField campoDataFim = new JTextField();
    public JComboBox<String> campoUnidade = new JComboBox<>(UnidadeService.getUnidadesIds().toArray(new String[0]));

    public int unidadeDoProgama;

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
        scrollPane.setBounds(10, 110, 740, 440);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2));
        add(scrollPane);

        tabelaProdutos.getColumnModel().getColumn(0).setPreferredWidth(5);
        tabelaProdutos.getColumnModel().getColumn(1).setPreferredWidth(230);

        // Adiciona o renderer para a coluna de quantidade
        tabelaProdutos.getColumnModel().getColumn(2).setCellRenderer(new QuantidadeCellRenderer());

        tabelaProdutos.getColumnModel().getColumn(3).setPreferredWidth(70);

        carregarTodosProdutos();

        JLabel lDataInicio = new JLabel("Data inicio");
        lDataInicio.setFont(new Font("Arial", Font.BOLD, 16));
        lDataInicio.setBounds(10, 40, 120, 30);
        add(lDataInicio);
        campoDataInicio.setBounds(10, 65, 120, 30);
        estilizarCampo(campoDataInicio);
        campoDataInicio.setEditable(true);
        adicionarPlaceholder(campoDataInicio, "DD-MM-YYYY");
        add(campoDataInicio);

        JLabel lDataFim = new JLabel("Data fim");
        lDataFim.setFont(new Font("Arial", Font.BOLD, 16));
        lDataFim.setBounds(140, 40, 120, 30);
        add(lDataFim);
        campoDataFim.setBounds(140, 65, 120, 30);
        estilizarCampo(campoDataFim);
        campoDataFim.setEditable(true);
        adicionarPlaceholder(campoDataFim, "DD-MM-YYYY");
        add(campoDataFim);

        // Criando o JComboBox para as unidades
        carregarUnidades();

        JLabel lUnidade = new JLabel("Unidade");
        lUnidade.setFont(new Font("Arial", Font.BOLD, 16));
        lUnidade.setBounds(270, 40, 120, 30);
        add(lUnidade);
        campoUnidade.setBounds(270, 65, 50, 30);
        campoUnidade.setBackground(Color.LIGHT_GRAY);
        campoUnidade.setFont(new Font("Arial", Font.BOLD, 16));
        campoUnidade.setFocusable(false);
        add(campoUnidade);

        JButton btnAtualizar = new JButton("Filtrar");
        btnAtualizar.setBounds(330, 65, 100, 30);
        //btnAtualizar.addActionListener(e -> atualizarListaArquivos());
        estilizarBotao(btnAtualizar);
        add(btnAtualizar);

        JLabel lTotalDasCompras = new JLabel("Total Bruto vendido");
        lTotalDasCompras.setFont(new Font("Arial", Font.BOLD, 16));
        lTotalDasCompras.setBounds(600, 40, 170, 30);
        add(lTotalDasCompras);
        campoTotalProdutos.setBounds(600, 65, 150, 30);
        estilizarCampo(campoTotalProdutos);
        campoTotalProdutos.setEditable(false);
        add(campoTotalProdutos);
    }

    private void estilizarCampo(JTextField campo) {
        campo.setEditable(false);
        campo.setBackground(Color.LIGHT_GRAY);
        campo.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        campo.setFont(new Font("Arial", Font.BOLD, 16));
    }

    private void estilizarBotao(JButton botao) {
        botao.setFont(new Font("Arial", Font.BOLD, 14));
        botao.setBackground(new Color(32, 3, 3));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
    }

    public void carregarUnidades() {
        UnidadeService.atualizarUnidadesIds(); // Atualiza a lista antes de carregar

        DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>(
                UnidadeService.getUnidadesIds().toArray(new String[0])
        );

        campoUnidade.setModel(modelo); // Atualiza os itens do JComboBox
    }

    private void adicionarPlaceholder(JTextField campo, String textoPlaceholder) {
        campo.setText(textoPlaceholder);
        campo.setForeground(Color.GRAY);

        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (campo.getText().equals(textoPlaceholder)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (campo.getText().isEmpty()) {
                    campo.setText(textoPlaceholder);
                    campo.setForeground(Color.GRAY);
                }
            }
        });
    }

    public void carregarTodosProdutos() {
        String sqlCatalogo = "SELECT id AS produto_id, produto AS produto_nome FROM tb_catalogo";

        try (Connection conn = b.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlCatalogo); ResultSet rs = stmt.executeQuery()) {

            modeloTabela.setRowCount(0); // Limpa a tabela antes de inserir novos dados

            while (rs.next()) {
                int id = rs.getInt("produto_id");
                String produto = rs.getString("produto_nome");
                // Adiciona o produto com quantidade e valor inicializados em 0
                modeloTabela.addRow(new Object[]{id, produto, 0, 0.00});
            }

            //System.out.println("Produtos carregados do catálogo com sucesso!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar produtos do catálogo.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Chama o método para atualizar os dados com base nas compras
        atualizarDadosComBaseNasCompras();
    }

    private void atualizarDadosComBaseNasCompras() {
        String sqlCompra = "SELECT produto_id, quantidade, preco_unitario, desconto FROM tb_produtos_compras";

        // Configura os símbolos de formatação para usar ponto como separador decimal
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.00", symbols); // Formato para duas casas decimais

        try (Connection conn = b.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlCompra); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int produtoId = rs.getInt("produto_id");
                int quantidadeCompra = rs.getInt("quantidade");
                double desconto = rs.getDouble("desconto"); // Valor de desconto por unidade

                // Calcula o valor total da compra considerando o desconto
                double valorCompra = quantidadeCompra * desconto;

                // Percorre as linhas do modeloTabela para encontrar o produto com o mesmo ID
                for (int i = 0; i < modeloTabela.getRowCount(); i++) {
                    int id = Integer.parseInt(modeloTabela.getValueAt(i, 0).toString());
                    if (id == produtoId) {
                        // Atualiza a quantidade: soma a quantidade da compra à quantidade atual
                        int quantidadeAtual = Integer.parseInt(modeloTabela.getValueAt(i, 2).toString());
                        int novaQuantidade = quantidadeAtual + quantidadeCompra;
                        modeloTabela.setValueAt(novaQuantidade, i, 2);

                        // Atualiza o valor: soma o valor da compra ao valor atual
                        double valorAtual = Double.parseDouble(modeloTabela.getValueAt(i, 3).toString());
                        double novoValor = valorAtual + valorCompra;

                        // Define o novo valor formatado com duas casas decimais e ponto como separador decimal
                        String valorFormatado = df.format(novoValor);
                        modeloTabela.setValueAt(valorFormatado, i, 3);

                        break; // Sai do loop após encontrar e atualizar o produto
                    }
                }
            }

            // Atualiza o campo total de produtos com a soma formatada
            campoTotalProdutos.setText("R$ " + df.format(calcularSomaColuna3()));

            //System.out.println("Dados atualizados com base na tb_produtos_compras!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar dados dos produtos da compra.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public double calcularSomaColuna3() {
        BigDecimal soma = BigDecimal.ZERO;
        int totalLinhas = modeloTabela.getRowCount();

        for (int i = 0; i < totalLinhas; i++) {
            Object valorObjeto = modeloTabela.getValueAt(i, 3);
            if (valorObjeto != null) {
                try {
                    // Supondo que os valores na coluna sejam do tipo String e representem números
                    String valorStr = valorObjeto.toString().replace("R$", "").trim();
                    BigDecimal valor = new BigDecimal(valorStr);
                    soma = soma.add(valor);
                } catch (NumberFormatException e) {
                    // Trate a exceção conforme necessário, por exemplo, ignorando valores não numéricos
                    System.err.println("Valor inválido na linha " + i + ": " + valorObjeto);
                }
            }
        }

        // Arredonda a soma para duas casas decimais
        soma = soma.setScale(2, RoundingMode.HALF_UP);

        // Formata a soma para exibição
        DecimalFormat df = new DecimalFormat("#,##0.00");
        //System.out.println("Soma da coluna 3: R$ " + df.format(soma));

        return soma.doubleValue();
    }
}
