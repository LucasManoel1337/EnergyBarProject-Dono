package energy.bar;

import energy.bar.db.ConexaoBancoDeDados;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class EnergyBarProject {
    
    private String versaoPrograma = "0.9.0";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss - dd-MM-yyyy");
    String dataHoraAtual = LocalDateTime.now().format(formatter);

    private TelaHistoricoCompra telaHistoricoCompra;
    private TelaSaidas telaSaidas;


    public TelaHistoricoCompra getTelaHistoricoCompra() {
        return telaHistoricoCompra;
    }


    public TelaSaidas getTelaSaidas() {
        return telaSaidas;
    }

    private JLabel labelVersao;

    private JFrame janela;
    private JPanel painelPrincipal;
    private JPanel painelConteudo;
    private PainelFaixa painelFaixa;

    private JLabel labelDataHora;

    private BotaoPersonalizado bInicio, bSaidas, bRelatorios, bGerenciarFuncionarios, bGerenciarUnidades; // Adicionado botão de cadastro
    private Font fontePadrao = new Font("Arial", Font.BOLD, 20);
    private Color corPadrao = Color.WHITE;
    private Color corSelecionada = new Color(180, 155, 183);

    private TelaInicio telaInicio;
    private TelaRelatorios telaRelatorios;
    private TelaGerenciarFuncionarios telaGerenciarFuncionarios;
    private TelaGerenciarUnidades telaGerenciarUnidades;

    public EnergyBarProject() throws ParseException, IOException {

        telaSaidas = new TelaSaidas(this); // Passe "this" para TelaCadastrarProduto
        telaHistoricoCompra = new TelaHistoricoCompra(this);

        configurarJanela();
        configurarPainelPrincipal();
        configurarPainelFaixa();
        configurarTelas(); // Inicializa as telas
        configurarBotoes();
        configurarPainelConteudo();
        adicionarComponentes();
    }

    public void exibirTela(JPanel tela) {
        painelConteudo.removeAll();
        painelConteudo.add(tela, BorderLayout.CENTER);
        painelConteudo.revalidate();
        painelConteudo.repaint();
    }

    private void configurarJanela() {
        janela = new JFrame("Energy Bar - Dono");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setBounds(0, 0, 1000, 600);
        janela.setLocationRelativeTo(null);
        janela.setResizable(false);

        try {
            ImageIcon icon = new ImageIcon("Arquivos de suporte/imagens/logo.png");
            janela.setIconImage(icon.getImage());
        } catch (Exception e) {

        }
    }

    private void configurarPainelPrincipal() {
        painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
    }

    private void configurarPainelFaixa() {
        painelFaixa = new PainelFaixa();
        painelFaixa.setLayout(new GridLayout(7, 1, 5, 10));

        javax.swing.Timer timer = new javax.swing.Timer(1000, e -> atualizarDataHora());
        timer.start();
    }

    private String obterDataHoraAtual() {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return agora.format(formatter);
    }

    private void atualizarDataHora() {
        labelDataHora.setText(obterDataHoraAtual());
    }

    private void configurarTelas() throws ParseException, IOException {
        telaInicio = new TelaInicio();
        telaSaidas = new TelaSaidas(this);
        telaHistoricoCompra = new TelaHistoricoCompra(this);
        try {
            telaRelatorios = new TelaRelatorios();
        } catch (Exception e) {
            System.err.println("Erro ao criar a tela inicial: " + e.getMessage());
        }
        telaGerenciarFuncionarios = new TelaGerenciarFuncionarios();
        telaGerenciarUnidades = new TelaGerenciarUnidades();
    }

    private void configurarBotoes() throws ParseException {
        // Inicializando o labelDataHora antes de usar
        labelDataHora = new JLabel(obterDataHoraAtual(), SwingConstants.CENTER);
        labelDataHora.setFont(new Font("Arial", Font.PLAIN, 14));
        labelDataHora.setForeground(Color.WHITE);

        // Inicializando os botões
        bInicio = criarBotao("INICIO", telaInicio);
        bInicio.setBackground(corSelecionada);
        bSaidas = criarBotao("VENDAS", telaSaidas);
        bRelatorios = criarBotao("RELATORIOS", telaRelatorios);
        bGerenciarFuncionarios = criarBotao("FUNCIONARIOS", telaGerenciarFuncionarios);
        bGerenciarUnidades = criarBotao("UNIDADES", telaGerenciarUnidades);

        // Adicionando os botões e o labelDataHora no painelFaixa
        painelFaixa.add(bInicio);
        painelFaixa.add(bSaidas);
        painelFaixa.add(bRelatorios);
        painelFaixa.add(bGerenciarFuncionarios);
        painelFaixa.add(bGerenciarUnidades);
        painelFaixa.add(labelDataHora); // Agora o labelDataHora é adicionado corretamente

        labelVersao = new JLabel("Versão: " + versaoPrograma, SwingConstants.CENTER);
        labelVersao.setFont(new Font("Arial", Font.PLAIN, 14));
        labelVersao.setForeground(Color.WHITE);
        painelFaixa.add(labelVersao);

        // Inicializando o timer para atualizar a data e hora
        javax.swing.Timer timer = new javax.swing.Timer(1000, e -> atualizarDataHora());
        timer.start();
    }

    private BotaoPersonalizado criarBotao(String texto, JPanel tela) {
        BotaoPersonalizado botao = new BotaoPersonalizado(texto, corPadrao, corSelecionada, fontePadrao);
        botao.addActionListener(e -> {
            resetarBotoes();
            botao.selecionar();
            atualizarTela(tela);
        });
        return botao;
    }

    private void resetarBotoes() {
        bInicio.desmarcar();
        bSaidas.desmarcar();
        bRelatorios.desmarcar();
        bGerenciarFuncionarios.desmarcar();
        bGerenciarUnidades.desmarcar();
    }

    private void atualizarTela(JPanel novaTela) {
        painelConteudo.removeAll();
        painelConteudo.add(novaTela, BorderLayout.CENTER);
        painelConteudo.revalidate();
        painelConteudo.repaint();
        
        if (novaTela instanceof TelaSaidas) {
            telaSaidas.atualizarListaArquivos();
            telaSaidas.carregarUnidades();
        } if (novaTela instanceof TelaGerenciarFuncionarios) {
            telaGerenciarFuncionarios.atualizarTabelaFuncionarios();
            telaGerenciarFuncionarios.carregarUnidades();
        } if (novaTela instanceof TelaGerenciarUnidades) {
            telaGerenciarUnidades.atualizarTabelaUnidades();
        } if (novaTela instanceof TelaRelatorios) {
            telaRelatorios.carregarTodosProdutos();
            telaRelatorios.carregarUnidades();
        }
    }

    private void configurarPainelConteudo() {
        painelConteudo = new JPanel();
        painelConteudo.setLayout(new BorderLayout());
        atualizarTela(telaInicio); // Tela inicial padrão
    }

    private void adicionarComponentes() {
        painelPrincipal.add(painelFaixa, BorderLayout.WEST);
        painelPrincipal.add(painelConteudo, BorderLayout.CENTER);
        janela.add(painelPrincipal);
        janela.setVisible(true);
    }

    public static void main(String[] args) throws ParseException, IOException, SQLException {
        Connection conexao = null;

        // Formatar data e hora atual
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss - dd-MM-yyyy");
        String dataHoraAtual = LocalDateTime.now().format(formatter);

        try {
            conexao = ConexaoBancoDeDados.conectar();
            System.out.println("[" + dataHoraAtual + "] - [EnergyBarApp.java] - Conexao estabelecida com sucesso!");
            System.out.println("[" + dataHoraAtual + "] - [EnergyBarApp.java] - Inicializando sistema.");


            new EnergyBarProject();

        } catch (SQLException e) {
            System.err.println("Erro ao conectar ou executar operações no banco de dados: " + e.getMessage());

            // Exibir janela de aviso caso não consiga conectar
            JOptionPane.showMessageDialog(null,
                    "Ocorreu um problema em relação ao banco de dados ao tentar abrir o programa, verifique se está conectado com a internet. Caso o erro persista, contate um administrador do programa!",
                    "Energy Bar",
                    JOptionPane.ERROR_MESSAGE);

            System.exit(1);
        }
    }
}
