package Servidor.DAO;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;
import sistemascriptografados.MODELO.Usuarios;

/**
 *
 * @author Ulisses
 */
public class UsuariosDAO {

    Usuarios usuario;
    private PreparedStatement statement;
    public ConexaoDAO bd;
    private ResultSet resultSet;
    private String sql, msg;

    public UsuariosDAO() throws SQLException, ClassNotFoundException {
        bd = new ConexaoDAO();
    }

    public int inserirNovoUsuario(Usuarios usuario) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        sql = "insert into tab_usuarios (ID_USR, NOME_USR,SOBRENOME_USR, EMAIL_USR, LOGIN_USR, SENHA_USR) values (usuariosID.nextval,?,upper(?), upper(?), upper(?), ?)";
        statement = new ConexaoDAO().con.prepareStatement(sql);
        statement.setString(1, (usuario.getNome()));
        statement.setString(2, (usuario.getSobrenome()));
        statement.setString(3, (usuario.getEmail()));
        statement.setString(4, (usuario.getLogin()));
        statement.setString(5, (usuario.getSenha()));
        try {
            statement.execute();
            resultSet = statement.getResultSet();
            return 7;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir registro " + e);
            return 8;
        }
    }

    public int existeLogin(String login, String senha) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte messageDigest[] = md.digest(senha.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : messageDigest) {
            sb.append(String.format("%02X", 0xFF & b));
        }
        String senhaCriptografada = sb.toString();
        sql = "Select 1 from tab_usuarios where upper(LOGIN_USR) = upper(?) and SENHA_USR = ?";
        statement = new ConexaoDAO().con.prepareStatement(sql);
        statement.setString(1, login);
        statement.setString(2, senhaCriptografada);
        statement.executeQuery();
        resultSet = statement.getResultSet();
        while (resultSet.next()) {
            return 0;
        }
        return 3;
    }

    public boolean verificarSenha(String senhaEntrada) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte messageDigest[] = md.digest(senhaEntrada.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : messageDigest) {
            sb.append(String.format("%02X", 0xFF & b));
        }
        String senhaCriptografada = sb.toString();
        sql = "Select LOGIN_USR from tab_usuarios where SENHA_USR = ? ";
        statement = new ConexaoDAO().con.prepareStatement(sql);
        statement.setString(1, senhaCriptografada);
        statement.executeQuery();
        ResultSet rs = statement.getResultSet();
        while (rs.next()) {
            return true;
        }
        return false;
    }

    public String pegaNome(String login) throws SQLException, ClassNotFoundException {
        sql = "Select nome_usr, sobrenome_usr from tab_usuarios where login_usr = upper(?) ";
        statement = new ConexaoDAO().con.prepareStatement(sql);
        statement.setString(1, login);
        statement.executeQuery();
        ResultSet rs = statement.getResultSet();
        while (rs.next()) {
            String nomeUsuAtual = rs.getString(1);
            String sobreNomeUsuAtual = rs.getString(2);
            return (nomeUsuAtual);
        }
        return null;
    }
//apagar se nao for usar ===========================================================

    public String carregaUsuario(int id) throws SQLException, ClassNotFoundException {
        sql = "select nome_usr from usuarios where id_usr = ? ";
        statement = new ConexaoDAO().con.prepareStatement(sql);
        statement.setInt(1, id);
        statement.executeQuery();
        ResultSet rs = statement.getResultSet();
        return rs.toString();
    }

    public int recuperaId(String login) throws SQLException, ClassNotFoundException {
        sql = "select id_usr from tab_usuarios where login_usr = upper(?) ";
        statement = new ConexaoDAO().con.prepareStatement(sql);
        statement.setString(1, login);
        statement.executeQuery();
        ResultSet rs = statement.getResultSet();
        while (rs.next()) {
            return rs.getInt(1);
        }
        return 1;
    }

    public String criptografar(String txt) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        int contador, tamanho, codigoASCII;
        String senhaCriptografada = "";
        tamanho = txt.length();
        // txt = txt.toUpperCase();
        contador = 0;

        while (contador < tamanho) {
            codigoASCII = txt.charAt(contador) + 130;
            senhaCriptografada = senhaCriptografada + (char) codigoASCII;
            contador++;
        }

        return senhaCriptografada;
    }

}
