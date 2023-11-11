/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver.controller;

import cadastroserver.controller.exceptions.IllegalOrphanException;
import cadastroserver.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cadastroserver.model.PessoaFisica;
import cadastroserver.model.PessoaJuridica;
import cadastroserver.model.Movimento;
import cadastroserver.model.Pessoas;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author andre
 */
public class PessoasJpaController implements Serializable {

    public PessoasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pessoas pessoas) {
        if (pessoas.getMovimentoCollection() == null) {
            pessoas.setMovimentoCollection(new ArrayList<Movimento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PessoaFisica pessoaFisica = pessoas.getPessoaFisica();
            if (pessoaFisica != null) {
                pessoaFisica = em.getReference(pessoaFisica.getClass(), pessoaFisica.getIdPessoa());
                pessoas.setPessoaFisica(pessoaFisica);
            }
            PessoaJuridica pessoaJuridica = pessoas.getPessoaJuridica();
            if (pessoaJuridica != null) {
                pessoaJuridica = em.getReference(pessoaJuridica.getClass(), pessoaJuridica.getIdPessoa());
                pessoas.setPessoaJuridica(pessoaJuridica);
            }
            Collection<Movimento> attachedMovimentoCollection = new ArrayList<Movimento>();
            for (Movimento movimentoCollectionMovimentoToAttach : pessoas.getMovimentoCollection()) {
                movimentoCollectionMovimentoToAttach = em.getReference(movimentoCollectionMovimentoToAttach.getClass(), movimentoCollectionMovimentoToAttach.getIdMovimento());
                attachedMovimentoCollection.add(movimentoCollectionMovimentoToAttach);
            }
            pessoas.setMovimentoCollection(attachedMovimentoCollection);
            em.persist(pessoas);
            if (pessoaFisica != null) {
                Pessoas oldPessoasOfPessoaFisica = pessoaFisica.getPessoas();
                if (oldPessoasOfPessoaFisica != null) {
                    oldPessoasOfPessoaFisica.setPessoaFisica(null);
                    oldPessoasOfPessoaFisica = em.merge(oldPessoasOfPessoaFisica);
                }
                pessoaFisica.setPessoas(pessoas);
                pessoaFisica = em.merge(pessoaFisica);
            }
            if (pessoaJuridica != null) {
                Pessoas oldPessoasOfPessoaJuridica = pessoaJuridica.getPessoas();
                if (oldPessoasOfPessoaJuridica != null) {
                    oldPessoasOfPessoaJuridica.setPessoaJuridica(null);
                    oldPessoasOfPessoaJuridica = em.merge(oldPessoasOfPessoaJuridica);
                }
                pessoaJuridica.setPessoas(pessoas);
                pessoaJuridica = em.merge(pessoaJuridica);
            }
            for (Movimento movimentoCollectionMovimento : pessoas.getMovimentoCollection()) {
                Pessoas oldIdPessoaOfMovimentoCollectionMovimento = movimentoCollectionMovimento.getIdPessoa();
                movimentoCollectionMovimento.setIdPessoa(pessoas);
                movimentoCollectionMovimento = em.merge(movimentoCollectionMovimento);
                if (oldIdPessoaOfMovimentoCollectionMovimento != null) {
                    oldIdPessoaOfMovimentoCollectionMovimento.getMovimentoCollection().remove(movimentoCollectionMovimento);
                    oldIdPessoaOfMovimentoCollectionMovimento = em.merge(oldIdPessoaOfMovimentoCollectionMovimento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pessoas pessoas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoas persistentPessoas = em.find(Pessoas.class, pessoas.getIdPessoa());
            PessoaFisica pessoaFisicaOld = persistentPessoas.getPessoaFisica();
            PessoaFisica pessoaFisicaNew = pessoas.getPessoaFisica();
            PessoaJuridica pessoaJuridicaOld = persistentPessoas.getPessoaJuridica();
            PessoaJuridica pessoaJuridicaNew = pessoas.getPessoaJuridica();
            Collection<Movimento> movimentoCollectionOld = persistentPessoas.getMovimentoCollection();
            Collection<Movimento> movimentoCollectionNew = pessoas.getMovimentoCollection();
            List<String> illegalOrphanMessages = null;
            if (pessoaFisicaOld != null && !pessoaFisicaOld.equals(pessoaFisicaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain PessoaFisica " + pessoaFisicaOld + " since its pessoas field is not nullable.");
            }
            if (pessoaJuridicaOld != null && !pessoaJuridicaOld.equals(pessoaJuridicaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain PessoaJuridica " + pessoaJuridicaOld + " since its pessoas field is not nullable.");
            }
            for (Movimento movimentoCollectionOldMovimento : movimentoCollectionOld) {
                if (!movimentoCollectionNew.contains(movimentoCollectionOldMovimento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Movimento " + movimentoCollectionOldMovimento + " since its idPessoa field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pessoaFisicaNew != null) {
                pessoaFisicaNew = em.getReference(pessoaFisicaNew.getClass(), pessoaFisicaNew.getIdPessoa());
                pessoas.setPessoaFisica(pessoaFisicaNew);
            }
            if (pessoaJuridicaNew != null) {
                pessoaJuridicaNew = em.getReference(pessoaJuridicaNew.getClass(), pessoaJuridicaNew.getIdPessoa());
                pessoas.setPessoaJuridica(pessoaJuridicaNew);
            }
            Collection<Movimento> attachedMovimentoCollectionNew = new ArrayList<Movimento>();
            for (Movimento movimentoCollectionNewMovimentoToAttach : movimentoCollectionNew) {
                movimentoCollectionNewMovimentoToAttach = em.getReference(movimentoCollectionNewMovimentoToAttach.getClass(), movimentoCollectionNewMovimentoToAttach.getIdMovimento());
                attachedMovimentoCollectionNew.add(movimentoCollectionNewMovimentoToAttach);
            }
            movimentoCollectionNew = attachedMovimentoCollectionNew;
            pessoas.setMovimentoCollection(movimentoCollectionNew);
            pessoas = em.merge(pessoas);
            if (pessoaFisicaNew != null && !pessoaFisicaNew.equals(pessoaFisicaOld)) {
                Pessoas oldPessoasOfPessoaFisica = pessoaFisicaNew.getPessoas();
                if (oldPessoasOfPessoaFisica != null) {
                    oldPessoasOfPessoaFisica.setPessoaFisica(null);
                    oldPessoasOfPessoaFisica = em.merge(oldPessoasOfPessoaFisica);
                }
                pessoaFisicaNew.setPessoas(pessoas);
                pessoaFisicaNew = em.merge(pessoaFisicaNew);
            }
            if (pessoaJuridicaNew != null && !pessoaJuridicaNew.equals(pessoaJuridicaOld)) {
                Pessoas oldPessoasOfPessoaJuridica = pessoaJuridicaNew.getPessoas();
                if (oldPessoasOfPessoaJuridica != null) {
                    oldPessoasOfPessoaJuridica.setPessoaJuridica(null);
                    oldPessoasOfPessoaJuridica = em.merge(oldPessoasOfPessoaJuridica);
                }
                pessoaJuridicaNew.setPessoas(pessoas);
                pessoaJuridicaNew = em.merge(pessoaJuridicaNew);
            }
            for (Movimento movimentoCollectionNewMovimento : movimentoCollectionNew) {
                if (!movimentoCollectionOld.contains(movimentoCollectionNewMovimento)) {
                    Pessoas oldIdPessoaOfMovimentoCollectionNewMovimento = movimentoCollectionNewMovimento.getIdPessoa();
                    movimentoCollectionNewMovimento.setIdPessoa(pessoas);
                    movimentoCollectionNewMovimento = em.merge(movimentoCollectionNewMovimento);
                    if (oldIdPessoaOfMovimentoCollectionNewMovimento != null && !oldIdPessoaOfMovimentoCollectionNewMovimento.equals(pessoas)) {
                        oldIdPessoaOfMovimentoCollectionNewMovimento.getMovimentoCollection().remove(movimentoCollectionNewMovimento);
                        oldIdPessoaOfMovimentoCollectionNewMovimento = em.merge(oldIdPessoaOfMovimentoCollectionNewMovimento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pessoas.getIdPessoa();
                if (findPessoas(id) == null) {
                    throw new NonexistentEntityException("The pessoas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoas pessoas;
            try {
                pessoas = em.getReference(Pessoas.class, id);
                pessoas.getIdPessoa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pessoas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            PessoaFisica pessoaFisicaOrphanCheck = pessoas.getPessoaFisica();
            if (pessoaFisicaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pessoas (" + pessoas + ") cannot be destroyed since the PessoaFisica " + pessoaFisicaOrphanCheck + " in its pessoaFisica field has a non-nullable pessoas field.");
            }
            PessoaJuridica pessoaJuridicaOrphanCheck = pessoas.getPessoaJuridica();
            if (pessoaJuridicaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pessoas (" + pessoas + ") cannot be destroyed since the PessoaJuridica " + pessoaJuridicaOrphanCheck + " in its pessoaJuridica field has a non-nullable pessoas field.");
            }
            Collection<Movimento> movimentoCollectionOrphanCheck = pessoas.getMovimentoCollection();
            for (Movimento movimentoCollectionOrphanCheckMovimento : movimentoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pessoas (" + pessoas + ") cannot be destroyed since the Movimento " + movimentoCollectionOrphanCheckMovimento + " in its movimentoCollection field has a non-nullable idPessoa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pessoas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pessoas> findPessoasEntities() {
        return findPessoasEntities(true, -1, -1);
    }

    public List<Pessoas> findPessoasEntities(int maxResults, int firstResult) {
        return findPessoasEntities(false, maxResults, firstResult);
    }

    private List<Pessoas> findPessoasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pessoas.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Pessoas findPessoas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pessoas.class, id);
        } finally {
            em.close();
        }
    }

    public int getPessoasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pessoas> rt = cq.from(Pessoas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
