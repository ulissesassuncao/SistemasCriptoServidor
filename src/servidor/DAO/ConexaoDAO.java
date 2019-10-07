package Servidor.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author 6
 */
public class ConexaoDAO {

    String driver = "oracle.jdbc.OracleDriver";
    String url = "jdbc:oracle:thin:@localhost:1521:XE";
    String username = "ulisses";
    String senha = "123";

    Connection con;

   public ConexaoDAO() throws SQLException, ClassNotFoundException {
        try {
            Class.forName(driver);
            System.out.println("Servidor Conectado ao Danco De Dados...");
        }catch(Exception e){
            System.out.println("Driver nao encontrado");
        }
        
        con = DriverManager.getConnection(url, username, senha);
    }
}
  
