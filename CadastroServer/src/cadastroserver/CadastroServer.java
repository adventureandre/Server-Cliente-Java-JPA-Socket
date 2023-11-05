/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadastroserver;

import cadastroserver.controller.UsuarioJpaController;
import cadastroserver.model.Usuario;
import java.net.ServerSocket;
import java.util.List;
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
        
        for(Usuario user : usuarios){
            System.out.println(user.getLogin());
        }
        
        

        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Servidor aguardando conexoes na porta 1234...");

        } catch (Exception e) {

        }

    }

}
