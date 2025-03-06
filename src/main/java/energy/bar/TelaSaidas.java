package energy.bar;

import energy.bar.db.ConexaoBancoDeDados;
import energy.bar.support.LabelEnergyBar;
import energy.bar.support.UnidadeService;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

class TelaSaidas extends JPanel {

    private EnergyBarProject mainApp;
    private JTextField campoDataInicio = new JTextField();
    private JTextField campoDataFim = new JTextField();
    private JTextField campoTotalDasCompras = new JTextField();
    private LabelEnergyBar labelEnergyBar = new LabelEnergyBar();
    private JPanel painelArquivos = new JPanel();

    private ArrayList<String> unidadesIds = new ArrayList<>();
    public JComboBox<String> campoUnidade = new JComboBox<>(UnidadeService.getUnidadesIds().toArray(new String[0]));

    public int unidadeDoPrograma;

    public TelaSaidas(EnergyBarProject mainApp) {
        setLayout(null);
        this.mainApp = mainApp;

        // Criando e adicionando a label EnergyBar
        JLabel energyBarLabel = labelEnergyBar.criarLabelEnergyBar();
        add(energyBarLabel, BorderLayout.NORTH);

        JLabel label = new JLabel("Tela Saídas", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        add(label, BorderLayout.CENTER);

        painelArquivos.setLayout(null);
        JScrollPane scrollPane = new JScrollPane(painelArquivos);
        scrollPane.setPreferredSize(new Dimension(720, 400));
        scrollPane.setBounds(0, 120, 765, 440);
        add(scrollPane, BorderLayout.SOUTH);

        JButton btnAtualizar = new JButton("Filtrar");
        btnAtualizar.setBounds(330, 65, 100, 30);
        btnAtualizar.addActionListener(e -> atualizarListaArquivos());
        estilizarBotao(btnAtualizar);
        add(btnAtualizar);

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

        JLabel lTotalDasCompras = new JLabel("Total das compras");
        lTotalDasCompras.setFont(new Font("Arial", Font.BOLD, 16));
        lTotalDasCompras.setBounds(600, 40, 150, 30);
        add(lTotalDasCompras);
        campoTotalDasCompras.setBounds(600, 65, 150, 30);
        estilizarCampo(campoTotalDasCompras);
        campoTotalDasCompras.setEditable(false);
        add(campoTotalDasCompras);

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

        // Inicializa a listagem a partir do banco de dados
        atualizarListaArquivos();

        JLabel lDataEHora = new JLabel("Data e Hora");
        lDataEHora.setFont(new Font("Arial", Font.BOLD, 16));
        lDataEHora.setBounds(10, 94, 120, 30);
        add(lDataEHora);

        JLabel lFuncionario = new JLabel("Funcionario");
        lFuncionario.setFont(new Font("Arial", Font.BOLD, 16));
        lFuncionario.setBounds(180, 94, 120, 30);
        add(lFuncionario);

        JLabel lTipoDeCliente = new JLabel("Cliente");
        lTipoDeCliente.setFont(new Font("Arial", Font.BOLD, 16));
        lTipoDeCliente.setBounds(310, 94, 120, 30);
        add(lTipoDeCliente);

        JLabel lPagamento = new JLabel("Pagamento");
        lPagamento.setFont(new Font("Arial", Font.BOLD, 16));
        lPagamento.setBounds(420, 94, 120, 30);
        add(lPagamento);

        JLabel lDesconto = new JLabel("Des.");
        lDesconto.setFont(new Font("Arial", Font.BOLD, 16));
        lDesconto.setBounds(510, 94, 120, 30);
        add(lDesconto);

        JLabel lTotal = new JLabel("Total");
        lTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lTotal.setBounds(570, 94, 120, 30);
        add(lTotal);
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

    public void atualizarListaArquivos() {
        painelArquivos.removeAll();
        painelArquivos.revalidate();
        painelArquivos.repaint();
        listarHistoricoDeCompras(painelArquivos);
    }

    private void listarHistoricoDeCompras(JPanel painel) {
        painel.removeAll(); // Limpa o painel antes de adicionar novos elementos
        try (Connection conn = ConexaoBancoDeDados.getConnection()) {
            String sql;
            PreparedStatement stmt;

            LocalDateTime dataInicio = obterDataDoCampo(campoDataInicio);
            LocalDateTime dataFim = obterDataDoCampo(campoDataFim);

            if (!unidadesIds.isEmpty()) {
                unidadeDoPrograma = Integer.parseInt(campoUnidade.getSelectedItem().toString());
            }

            // Verifica se as datas foram fornecidas e aplica o filtro
            if (dataInicio != null && dataFim != null) {
                // Se houver data, o SQL deve filtrar pela data e pela unidade
                sql = "SELECT c.id AS compra_id, c.data_compra, c.funcionario, c.tipo_cliente, c.forma_pagamento, c.desconto, c.valor_total "
                        + "FROM tb_compras c "
                        + "WHERE c.data_compra BETWEEN ? AND ? "
                        + "AND c.compra_unidade = ?"; // SQL com filtro por unidade
                stmt = conn.prepareStatement(sql);
                stmt.setTimestamp(1, Timestamp.valueOf(dataInicio));
                stmt.setTimestamp(2, Timestamp.valueOf(dataFim));
                stmt.setInt(3, unidadeDoPrograma);
            } else {
                // Se não houver datas, filtra apenas pela unidade
                sql = "SELECT c.id AS compra_id, c.data_compra, c.funcionario, c.tipo_cliente, c.forma_pagamento, c.desconto, c.valor_total "
                        + "FROM tb_compras c "
                        + "WHERE c.compra_unidade = ?"; // SQL com filtro por unidade
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, unidadeDoPrograma);
            }

            ResultSet rs = stmt.executeQuery();
            int y = 10;
            double total = 0;

            while (rs.next()) {
                int compraId = rs.getInt("compra_id");
                String dataCompra = rs.getString("data_compra");
                String funcionarioNome = rs.getString("funcionario");
                String tipoCliente = rs.getString("tipo_cliente");
                String formaPagamento = rs.getString("forma_pagamento");
                String desconto = rs.getString("desconto");
                String valorTotal = rs.getString("valor_total");
                total += Double.parseDouble(valorTotal);

                JTextField txtDataCompra = new JTextField(dataCompra);
                txtDataCompra.setBounds(10, y, 160, 30);
                estilizarCampo(txtDataCompra);

                JTextField txtFuncionario = new JTextField(funcionarioNome);
                txtFuncionario.setBounds(180, y, 120, 30);
                estilizarCampo(txtFuncionario);

                JTextField txtTipoCliente = new JTextField(tipoCliente);
                txtTipoCliente.setBounds(310, y, 100, 30);
                estilizarCampo(txtTipoCliente);

                JTextField txtFormaPagamento = new JTextField(formaPagamento);
                txtFormaPagamento.setBounds(420, y, 80, 30);
                estilizarCampo(txtFormaPagamento);

                JTextField txtDesconto = new JTextField(desconto);
                txtDesconto.setBounds(510, y, 50, 30);
                estilizarCampo(txtDesconto);

                JTextField txtValorTotal = new JTextField("R$ " + valorTotal);
                txtValorTotal.setBounds(570, y, 80, 30);
                estilizarCampo(txtValorTotal);

                JButton btnVisualizar = new JButton("Ver");
                btnVisualizar.setBounds(660, y, 80, 30);
                estilizarBotao(btnVisualizar);
                btnVisualizar.addActionListener(e -> {
                    TelaHistoricoCompra telaHistoricoCompra = mainApp.getTelaHistoricoCompra();
                    telaHistoricoCompra.campoFuncionario.setText(funcionarioNome);
                    telaHistoricoCompra.campoTipocliente.setText(tipoCliente);
                    telaHistoricoCompra.campoFormaDePagamento.setText(formaPagamento);
                    telaHistoricoCompra.campoDesconto.setText(desconto);
                    telaHistoricoCompra.campoTotalDaCompra.setText("R$ " + valorTotal);
                    telaHistoricoCompra.carregarProdutosPorCompra(compraId);
                    mainApp.exibirTela(telaHistoricoCompra);
                });

                painel.add(txtDataCompra);
                painel.add(txtFuncionario);
                painel.add(txtTipoCliente);
                painel.add(txtFormaPagamento);
                painel.add(txtDesconto);
                painel.add(txtValorTotal);
                painel.add(btnVisualizar);

                y += 40;
            }
            campoTotalDasCompras.setText(String.format("R$ " + "%.2f", total));
            painel.revalidate();
            painel.repaint();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private LocalDateTime obterDataDoCampo(JTextField campo) {
        try {
            String texto = campo.getText().trim();
            if (texto.isEmpty() || texto.equals("DD-MM-YYYY")) {
                return null;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return LocalDate.parse(texto, formatter).atStartOfDay();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Data inválida! Formato esperado: DD-MM-YYYY", "Erro de Data", JOptionPane.ERROR_MESSAGE);
            return null;
        }
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
}
