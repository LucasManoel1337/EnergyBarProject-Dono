package energy.bar.db;

import energy.bar.back.dao.FuncionariosDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class GeradorDeProdutos {
    
    public GeradorDeProdutos(){
    }

    private static Random random = new Random();

    // Método para gerar produtos para testes com valores aleatórios
    public static void gerarProdutosDeTeste() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");

        List<String[]> produtos = Arrays.asList(
                new String[]{"001", "Agua S/gás", gerarResponsavel(), String.valueOf(gerarEstoque()), gerarDataVencimento(), gerarValorCusto() + "", gerarValorVenda(gerarValorCusto()) + "", gerarLote(), LocalDateTime.now().format(formatter)},
                new String[]{"002", "Agua C/gás", gerarResponsavel(), String.valueOf(gerarEstoque()), gerarDataVencimento(), gerarValorCusto() + "", gerarValorVenda(gerarValorCusto()) + "", gerarLote(), LocalDateTime.now().format(formatter)},
                new String[]{"003", "Getorate Morango", gerarResponsavel(), String.valueOf(gerarEstoque()), gerarDataVencimento(), gerarValorCusto() + "", gerarValorVenda(gerarValorCusto()) + "", gerarLote(), LocalDateTime.now().format(formatter)},
                new String[]{"004", "Getorate Limão", gerarResponsavel(), String.valueOf(gerarEstoque()), gerarDataVencimento(), gerarValorCusto() + "", gerarValorVenda(gerarValorCusto()) + "", gerarLote(), LocalDateTime.now().format(formatter)},
                new String[]{"005", "Getorate Maracuja", gerarResponsavel(), String.valueOf(gerarEstoque()), gerarDataVencimento(), gerarValorCusto() + "", gerarValorVenda(gerarValorCusto()) + "", gerarLote(), LocalDateTime.now().format(formatter)},
                new String[]{"006", "Água de Coco", gerarResponsavel(), String.valueOf(gerarEstoque()), gerarDataVencimento(), gerarValorCusto() + "", gerarValorVenda(gerarValorCusto()) + "", gerarLote(), LocalDateTime.now().format(formatter)},
                new String[]{"007", "Suco Natural Laranja", gerarResponsavel(), String.valueOf(gerarEstoque()), gerarDataVencimento(), gerarValorCusto() + "", gerarValorVenda(gerarValorCusto()) + "", gerarLote(), LocalDateTime.now().format(formatter)},
                new String[]{"008", "Suco Natural Uva", gerarResponsavel(), String.valueOf(gerarEstoque()), gerarDataVencimento(), gerarValorCusto() + "", gerarValorVenda(gerarValorCusto()) + "", gerarLote(), LocalDateTime.now().format(formatter)},
                new String[]{"009", "Suco Detox", gerarResponsavel(), String.valueOf(gerarEstoque()), gerarDataVencimento(), gerarValorCusto() + "", gerarValorVenda(gerarValorCusto()) + "", gerarLote(), LocalDateTime.now().format(formatter)},
                new String[]{"010", "Shake de Whey Chocolate", gerarResponsavel(), String.valueOf(gerarEstoque()), gerarDataVencimento(), gerarValorCusto() + "", gerarValorVenda(gerarValorCusto()) + "", gerarLote(), LocalDateTime.now().format(formatter)},
                new String[]{"011", "Shake de Whey Baunilha", gerarResponsavel(), String.valueOf(gerarEstoque()), gerarDataVencimento(), gerarValorCusto() + "", gerarValorVenda(gerarValorCusto()) + "", gerarLote(), LocalDateTime.now().format(formatter)},
                new String[]{"012", "Barra de Proteína Chocolate", gerarResponsavel(), String.valueOf(gerarEstoque()), gerarDataVencimento(), gerarValorCusto() + "", gerarValorVenda(gerarValorCusto()) + "", gerarLote(), LocalDateTime.now().format(formatter)},
                new String[]{"013", "Barra de Proteína Morango", gerarResponsavel(), String.valueOf(gerarEstoque()), gerarDataVencimento(), gerarValorCusto() + "", gerarValorVenda(gerarValorCusto()) + "", gerarLote(), LocalDateTime.now().format(formatter)},
                new String[]{"014", "Pasta de Amendoim", gerarResponsavel(), String.valueOf(gerarEstoque()), gerarDataVencimento(), gerarValorCusto() + "", gerarValorVenda(gerarValorCusto()) + "", gerarLote(), LocalDateTime.now().format(formatter)},
                new String[]{"015", "Banana Chips", gerarResponsavel(), String.valueOf(gerarEstoque()), gerarDataVencimento(), gerarValorCusto() + "", gerarValorVenda(gerarValorCusto()) + "", gerarLote(), LocalDateTime.now().format(formatter)},
                new String[]{"016", "Mix de Nuts", gerarResponsavel(), String.valueOf(gerarEstoque()), gerarDataVencimento(), gerarValorCusto() + "", gerarValorVenda(gerarValorCusto()) + "", gerarLote(), LocalDateTime.now().format(formatter)},
                new String[]{"017", "Tapioca Recheada", gerarResponsavel(), String.valueOf(gerarEstoque()), gerarDataVencimento(), gerarValorCusto() + "", gerarValorVenda(gerarValorCusto()) + "", gerarLote(), LocalDateTime.now().format(formatter)},
                new String[]{"018", "Omelete Proteico", gerarResponsavel(), String.valueOf(gerarEstoque()), gerarDataVencimento(), gerarValorCusto() + "", gerarValorVenda(gerarValorCusto()) + "", gerarLote(), LocalDateTime.now().format(formatter)},
                new String[]{"019", "Iogurte Proteico", gerarResponsavel(), String.valueOf(gerarEstoque()), gerarDataVencimento(), gerarValorCusto() + "", gerarValorVenda(gerarValorCusto()) + "", gerarLote(), LocalDateTime.now().format(formatter)},
                new String[]{"020", "Café Termogênico", gerarResponsavel(), String.valueOf(gerarEstoque()), gerarDataVencimento(), gerarValorCusto() + "", gerarValorVenda(gerarValorCusto()) + "", gerarLote(), LocalDateTime.now().format(formatter)}
        );

        // Criar os produtos e salvar no banco de dados
        for (String[] produto : produtos) {
            cadastrarProdutoNoBanco(produto, FuncionariosDAO.getUnidadeDoProgama());
        }
    }

    private static int gerarEstoque() {
        return random.nextInt(12) + 1;  // Gera um número aleatório de 1 a 12
    }

    private static String gerarResponsavel() {
        // Lista de nomes de responsáveis
        List<String> responsaveis = Arrays.asList("Lucas", "Pamela", "Fellipe");

        // Escolher um nome aleatório da lista
        int index = random.nextInt(responsaveis.size());
        return responsaveis.get(index);
    }

    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private static double gerarValorCusto() {
        double valorCusto = 2.50 + (random.nextDouble() * (6.00 - 2.50));
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("###0.00", symbols);
        String valorFormatado = df.format(valorCusto);
        return Double.parseDouble(valorFormatado);
    }

    private static double gerarValorVenda(double valorCusto) {
        double valorVenda;
        do {
            valorVenda = 3.50 + (random.nextDouble() * (8.00 - 3.50));
            // Garantir que o valor de venda seja sempre maior que o de custo
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
            DecimalFormat df = new DecimalFormat("###0.00", symbols);
            String valorFormatado = df.format(valorVenda);
            valorVenda = Double.parseDouble(valorFormatado);
        } while (valorVenda <= valorCusto);  // Garante que o valor de venda seja maior que o de custo
        return valorVenda;
    }

    private static Random random1 = new Random();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String gerarDataVencimento() {
        // Aumentando a probabilidade para datas acima de 30 dias (probabilidade de 70% para futuro distante)
        int tipoVencimento = random1.nextInt(10); // 0 a 9
        LocalDate hoje = LocalDate.now();
        LocalDate dataVencimento;

        if (tipoVencimento < 3) { // 30% chance de ser vencido ou perto de vencer
            if (tipoVencimento == 0) { // 10% chance de ser vencido (passado)
                dataVencimento = hoje.minusDays(random1.nextInt(365)); // Vencido até 1 ano atrás
            } else { // 20% chance de estar perto de vencer (próximos 30 dias)
                dataVencimento = hoje.plusDays(random1.nextInt(30) + 1); // Entre 1 e 30 dias à frente
            }
        } else { // 70% chance de ser um vencimento distante (mais de 30 dias)
            dataVencimento = hoje.plusDays(random1.nextInt(365) + 31); // Entre 31 dias e 1 ano à frente
        }

        // Formatar a data no formato "dd/MM/yyyy"
        return dataVencimento.format(formatter);
    }

    // Método para gerar o lote com o formato correto
    private static String gerarLote() {
        DateTimeFormatter loteFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
        return LocalDateTime.now().format(loteFormatter); // Retorna a data e hora no formato desejado
    }

    // Método para cadastrar um produto no banco de dados
    private static void cadastrarProdutoNoBanco(String[] produto, int unidadeDoProgama) {
        String idProduto = produto[0];
        String nomeDoProduto = produto[1];
        String responsavel = produto[2];
        String estoque = produto[3];
        String validade = produto[4];
        String valorDeCusto = produto[5];
        String valorDeVenda = produto[6];
        String lote = produto[7];
        String dataCadastro = produto[8];

        try {
            // Conectar ao banco de dados
            Connection connection = ConexaoBancoDeDados.getConnection();

            // Inserir dados do produto
            String sql = "INSERT INTO tb_produtos (id_produto, nome, responsavel, estoque, validade, valor_custo, valor_venda, lote, data_cadastro, produto_unidade) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, idProduto);
            stmt.setString(2, nomeDoProduto);
            stmt.setString(3, responsavel);
            stmt.setInt(4, Integer.parseInt(estoque));
            stmt.setString(5, validade);
            stmt.setDouble(6, Double.parseDouble(valorDeCusto));
            stmt.setDouble(7, Double.parseDouble(valorDeVenda));
            stmt.setString(8, lote);
            stmt.setString(9, dataCadastro);
            stmt.setInt(10, unidadeDoProgama);
            
            // Executar a inserção
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
