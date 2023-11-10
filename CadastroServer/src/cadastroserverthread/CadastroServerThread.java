/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadastroserverthread;

import cadastroserverthread.controller.UsuarioJpaController;
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
public class CadastroServerThread {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CadastroServerThreadPU");
        UsuarioJpaController ctrlUsu = new UsuarioJpaController(emf);

        try (ServerSocket serverSocket = new ServerSocket(1235)) {
            System.out.println("Servidor aguardando conexoes na porta 1235...");
            while (true) {
                Socket socket = serverSocket.accept();

                ThreadTeste teste = new ThreadTeste(ctrlUsu, socket);
                teste.start();
                System.out.println("thread iniciado!");

            }
        } catch (IOException ex) {
            Logger.getLogger(CadastroServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
