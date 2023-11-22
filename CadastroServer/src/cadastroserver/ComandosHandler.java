package cadastroserver;

import cadastroserver.controller.MovimentoJpaController;
import cadastroserver.controller.PessoasJpaController;
import cadastroserver.controller.ProdutoJpaController;
import cadastroserver.controller.UsuarioJpaController;
import cadastroserver.controller.exceptions.NonexistentEntityException;
import cadastroserver.model.Movimento;
import cadastroserver.model.Pessoas;
import cadastroserver.model.Produto;
import cadastroserver.model.Usuario;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ComandosHandler {

    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("CadastroServerPU");

    private final MovimentoJpaController ctrlMov;
    private final PessoasJpaController ctrlPessoa;
    private final ProdutoJpaController ctrlProduto;
    private final UsuarioJpaController ctrlUsur;

    public ComandosHandler(ObjectOutputStream out, ObjectInputStream in) {
        this.out = out;
        this.in = in;

        this.ctrlMov = new MovimentoJpaController(emf);
        this.ctrlPessoa = new PessoasJpaController(emf);
        this.ctrlProduto = new ProdutoJpaController(emf);
        this.ctrlUsur = new UsuarioJpaController(emf);
    }

    public void executarComandos() throws IOException, ClassNotFoundException {
        try {
            while (true) {
                String comando = (String) in.readObject();
                Integer idPessoa;
                Integer idUsuario;
                Integer idProduto;
                Integer quantidade;
                Float valorUnitario;

                Pessoas pessoa;
                Produto produto;
                Usuario usuario;

                Movimento movimento;

                switch (comando) {
                    case "e":

                        idPessoa = Integer.valueOf((String) in.readObject());
                        idProduto = Integer.valueOf((String) in.readObject());
                        idUsuario = (Integer) in.readObject();
                        quantidade = Integer.valueOf((String) in.readObject());
                        valorUnitario = Float.valueOf((String) in.readObject());

                        //Gravar no banco de dados
                        pessoa = ctrlPessoa.findPessoas(idPessoa);
                        produto = ctrlProduto.findProduto(idProduto);
                        usuario = ctrlUsur.findUsuario(idUsuario);

                        if (produto == null) {
                            System.out.println("Produto não cadastrado! no banco de dados.");
                            continue;
                        }

                        movimento = new Movimento();
                        movimento.setIdPessoa(pessoa);
                        movimento.setIdProduto(produto);
                        movimento.setQuantidade(quantidade);
                        movimento.setIdUsuario(usuario);
                        movimento.setTipo("E");
                        movimento.setValorUnitario(valorUnitario);

                        produto.setQuantidade(produto.getQuantidade() + quantidade);
                        ctrlProduto.edit(produto);

                        ctrlMov.create(movimento);

                        break;

                    case "s":

                        idPessoa = Integer.valueOf((String) in.readObject());
                        idProduto = Integer.valueOf((String) in.readObject());
                        idUsuario = (Integer) in.readObject();
                        quantidade = Integer.valueOf((String) in.readObject());
                        valorUnitario = Float.valueOf((String) in.readObject());

                        //Gravar no banco de dados
                        pessoa = ctrlPessoa.findPessoas(idPessoa);
                        produto = ctrlProduto.findProduto(idProduto);
                        usuario = ctrlUsur.findUsuario(idUsuario);

                        if (produto == null) {
                            System.out.println("Produto não cadastrado!");
                            continue;
                        }

                        movimento = new Movimento();
                        movimento.setIdPessoa(pessoa);
                        movimento.setIdProduto(produto);
                        movimento.setQuantidade(quantidade);
                        movimento.setIdUsuario(usuario);
                        movimento.setTipo("S");
                        movimento.setValorUnitario(valorUnitario);

                        produto.setQuantidade(produto.getQuantidade() - quantidade);
                        ctrlProduto.edit(produto);

                        ctrlMov.create(movimento);

                        break;
                    case "l":
                        List<Produto> produtoList = ctrlProduto.findProdutoEntities();

                        ArrayList<String> produtoName = new ArrayList<>();
                        ArrayList<Integer> produtoQuantidade = new ArrayList<>();

                        for (Produto iten : produtoList) {
                            produtoName.add(iten.getNome());
                            produtoQuantidade.add(iten.getQuantidade());

                        }

                        out.writeObject(produtoName);
                        out.writeObject(produtoQuantidade);

                        break;

                }

            }
        } catch (IOException e) {

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ComandosHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ComandosHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
            in.close();

        }
    }
}
