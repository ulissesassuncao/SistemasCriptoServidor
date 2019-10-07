package servidor;

import Servidor.DAO.UsuariosDAO;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import sistemascriptografados.MODELO.Usuarios;

/**
 *
 * @author Ulisses
 */
public class Servidor {

    private ServerSocket serverSocket;

    private void criarServerSocket(int porta) throws IOException {
        serverSocket = new ServerSocket(porta);
    }

    private void fechaSocket(Socket socket) throws IOException {
        socket.close();
    }

    private Socket esperarConexao() throws IOException {
        Socket socket = serverSocket.accept();
        return socket;
    }

    private void trataConexao(Socket socket) throws IOException, ClassNotFoundException, SQLException, NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        try {
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            try {
                Usuarios usuario = (Usuarios) input.readObject();
                System.out.println("Login: " + usuario.getLogin() + " recebido com sucesso");
                UsuariosDAO usuarioDAO = new UsuariosDAO();
                int i = usuarioDAO.inserirNovoUsuario(usuario);
                String login = usuario.getLogin();
                System.out.println(decripto(login) + " inserido com sucesso");
                output.writeUTF(decripto(login) + " inserido com sucesso");

            } catch (IOException ex) {
                String login = (String) input.readUTF();
                String loginReal = null, senhaReal = null;
                for(int i=0; i<=login.length(); i++){
                   if(login.charAt(i) == '§'){
                       loginReal = login.substring(0,i);
                       senhaReal = login.substring(i+1,login.length());
                       break;
                   }
                }
                System.out.println("Solicitacao de validacao do Login: " + loginReal + " recebido com sucesso");
                UsuariosDAO usuarioDAO = new UsuariosDAO();
                String loginDecrip = decripto(loginReal);
                System.out.println("Login decriptografado: "+loginDecrip);
                int i = usuarioDAO.existeLogin(loginReal, senhaReal);
                if (i == 0) {
                    System.out.println(loginDecrip + " validado com sucesso");
                    output.writeUTF(loginDecrip + " validado com sucesso");
                } else {
                    System.out.println(loginDecrip + " nao existe");
                    output.writeUTF(loginDecrip + " nao existe");
                }
            }

            output.flush();
            input.close();
            output.close();
        } catch (IOException ex) {

        } finally {
            fechaSocket(socket);
        }
    }
     public static String decripto(String txt) {
        //Descriptografa a String passada por parâmetro
        int contador, tamanho, codigoASCII;
        String senhaCriptografada = "";
        tamanho = txt.length();
       // txt = txt.toUpperCase();
        contador = 0;

        while (contador < tamanho) {
            codigoASCII = txt.charAt(contador) - 130;
            senhaCriptografada = senhaCriptografada + (char) codigoASCII;
            contador++;
        }

        return senhaCriptografada;
    }

    public void criarServidor() throws ClassNotFoundException, SQLException, NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        try {
            Servidor servidor = new Servidor();
            System.out.println("Servidor criado!\nAguardando Conexao...");
            servidor.criarServerSocket(5555);
            while (true) {
                Socket socket = servidor.esperarConexao();
                System.out.println("Cliente conectado!");
                servidor.trataConexao(socket);
                System.out.println("Cliente finalizado");
                System.out.println("=============================================\n");
                if (true) {
                    System.out.println("Aguardando nova conexao!");
                } else {
                    System.out.println("Servidor finalizado!");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException, NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Servidor servidor = new Servidor();
        servidor.criarServidor();
    }

}
