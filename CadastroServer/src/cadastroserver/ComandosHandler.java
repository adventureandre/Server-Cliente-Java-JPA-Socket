package cadastroserver;

import cadastroserver.controller.MovimentoJpaController;
import cadastroserver.controller.PessoasJpaController;
import cadastroserver.controller.ProdutoJpaController;
import cadastroserver.controller.UsuarioJpaController;
import cadastroserver.model.Movimento;
import cadastroserver.model.Pessoas;
import cadastroserver.model.Produto;
import cadastroserver.model.Usuario;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

                switch (comando) {
                    case "e":

                        Integer idPessoa = Integer.valueOf((String) in.readObject());
                        Integer idProduto = Integer.valueOf((String) in.readObject());
                        Integer idUsuario = (Integer) in.readObject();
                        Integer quantidade = Integer.valueOf((String) in.readObject());
                        Float valorUnitario = Float.valueOf((String) in.readObject());

                        //Gravar no banco de dados
                        Pessoas pessoa = ctrlPessoa.findPessoas(idPessoa);
                        Produto produto = ctrlProduto.findProduto(idProduto);
                        Usuario usuario = ctrlUsur.findUsuario(idUsuario);

                        Movimento movimento = new Movimento();
                        movimento.setIdPessoa(pessoa);
                        movimento.setIdProduto(produto);
                        movimento.setQuantidade(quantidade);
                        movimento.setIdUsuario(usuario);
                        movimento.setTipo("E");
                        movimento.setValorUnitario(valorUnitario);

                        ctrlMov.create(movimento);

                        break;
                }

            }
        } catch (IOException e) {

        } finally {
            out.close();
            in.close();

        }
    }
}
