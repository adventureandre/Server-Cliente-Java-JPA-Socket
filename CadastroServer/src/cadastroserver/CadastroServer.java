/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadastroserver;

import cadastroserver.controller.UsuarioJpaController;
import cadastroserver.model.Usuario;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author andre
 */
public class CadastroServer {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CadastroServerPU");

        UsuarioJpaController ctrlUsu = new UsuarioJpaController(emf);

        List<Usuario> usuarios = ctrlUsu.findUsuarioEntities();

        for (Usuario user : usuarios) {

            System.out.println(user.getLogin() + "senha: " + user.getSenha());
        }

        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Servidor aguardando conexoes na porta 1234...");

            Socket socket = serverSocket.accept();

            try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                //		++++SECAO IN E OUT+++++
                String login = (String) in.readObject();
                String senha = (String) in.readObject();
                String mensagem = (String) in.readObject();

                System.out.println("login=" + login + "   senha=" + senha);
                System.out.println("mensagem=" + mensagem);

                out.writeObject("GRAVANDO NO BANCO - login=" + login + " senha=" + login);
               // out.flush();
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CadastroServer.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (IOException e) {
            System.out.println("Sem Conexão");

        }

    }

}
