package energy.bar.db;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.core.MySQLDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConexaoBancoDeDados {

    private static final String URL = "jdbc:mysql://localhost:3306/energybar";
    private static final String USUARIO = "root";
    private static final String SENHA = "admin";
    
    // Formatar data e hora atual
       public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss - dd-MM-yyyy");
       public static String dataHoraAtual = LocalDateTime.now().format(formatter);

    public static Connection conectar() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);

            // Executa Liquibase após a conexão ser estabelecida
            executarLiquibase(conexao);

            return conexao;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC não encontrado.", e);
        }
    }

    public static void desconectar(Connection conexao) throws SQLException {
        if (conexao != null && !conexao.isClosed()) {
            conexao.close();
        }
    }

    private static void executarLiquibase(Connection conexao) {
        try {
            Database database = new MySQLDatabase();
            database.setConnection(new JdbcConnection(conexao));

            Liquibase liquibase = new Liquibase("db/changelog/db.changelog-master.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update("");

            System.out.println("[" + dataHoraAtual + "] - [ConexaoBancoDeDados.java] - Liquibase executado com sucesso!");
            System.out.println("Liquibase executado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao executar Liquibase", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }
}
