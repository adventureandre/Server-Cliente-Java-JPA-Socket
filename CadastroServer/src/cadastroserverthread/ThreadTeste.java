/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserverthread;

import cadastroserverthread.controller.UsuarioJpaController;
import cadastroserverthread.model.Usuario;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andre
 */
public class ThreadTeste extends Thread {

    private final UsuarioJpaController ctrlUsu;
    private final Socket s1;

    public ThreadTeste(UsuarioJpaController ctrlUsu, Socket s1) {
        this.ctrlUsu = ctrlUsu;
        this.s1 = s1;
    }

    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(s1.getOutputStream()); ObjectInputStream in = new ObjectInputStream(s1.getInputStream())) {
            String login = (String) in.readObject();
            String senha = (String) in.readObject();
            String mensagem = (String) in.readObject();

            System.out.println("login=" + login + "   senha=" + senha);
            System.out.println("mensagem=" + mensagem);

            out.writeObject("GRAVANDO NO BANCO - login=" + login + " senha=" + login);
            out.flush();

            List<Usuario> usuariosList = ctrlUsu.findUsuarioEntities();

            int tamanhoLista = usuariosList.size();
            System.out.println("tamnho=" + tamanhoLista);
            for (Usuario usuario : usuariosList) {
                System.out.println("login=" + usuario.getLogin());
            }

            System.out.println("GRAVANDO NO BANCO - login=" + login + " senha=" + login);
            Usuario userTeste = new Usuario(null);
            userTeste.setLogin(login);
            userTeste.setSenha(senha);

            ctrlUsu.create(userTeste);

        } catch (IOException ex) {
            Logger.getLogger(ThreadTeste.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ThreadTeste.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
