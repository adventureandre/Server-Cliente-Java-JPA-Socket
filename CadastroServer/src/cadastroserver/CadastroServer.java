/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadastroserver;

import cadastroserver.controller.UsuarioJpaController;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author andre
 */
public class CadastroServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CadastroServerPU");
        UsuarioJpaController ctrlUsu = new UsuarioJpaController(emf);

        try (ServerSocket serverSocket = new ServerSocket(4321)) {
            System.out.println("Servidor aguardando conexoes na porta 4321...");
            while (true) {
                Socket socket = serverSocket.accept();

                CadastroThread teste = new CadastroThread(ctrlUsu, socket);
                teste.start();
                System.out.println("thread iniciado!");

            }
        } catch (IOException ex) {
            Logger.getLogger(CadastroServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
