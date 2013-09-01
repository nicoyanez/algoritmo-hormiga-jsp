/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mejorrecorrido_fachadas;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import mejorrecorrido_entity.Recorrido;
import mejorrecorrido_entity.Encomienda;
import mejorrecorrido_entity.Envio;
import mejorrecorrido_fachadas.exceptions.NonexistentEntityException;

/**
 *
 * @author nnn
 */
public class EnvioJpaController implements Serializable {

    public EnvioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Envio envio) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Recorrido recorrido = envio.getRecorrido();
            if (recorrido != null) {
                recorrido = em.getReference(recorrido.getClass(), recorrido.getId());
                envio.setRecorrido(recorrido);
            }
            Encomienda encomienda = envio.getEncomienda();
            if (encomienda != null) {
                encomienda = em.getReference(encomienda.getClass(), encomienda.getId());
                envio.setEncomienda(encomienda);
            }
            em.persist(envio);
            if (recorrido != null) {
                recorrido.getEnvioCollection().add(envio);
                recorrido = em.merge(recorrido);
            }
            if (encomienda != null) {
                encomienda.getEnvioCollection().add(envio);
                encomienda = em.merge(encomienda);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Envio envio) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Envio persistentEnvio = em.find(Envio.class, envio.getId());
            Recorrido recorridoOld = persistentEnvio.getRecorrido();
            Recorrido recorridoNew = envio.getRecorrido();
            Encomienda encomiendaOld = persistentEnvio.getEncomienda();
            Encomienda encomiendaNew = envio.getEncomienda();
            if (recorridoNew != null) {
                recorridoNew = em.getReference(recorridoNew.getClass(), recorridoNew.getId());
                envio.setRecorrido(recorridoNew);
            }
            if (encomiendaNew != null) {
                encomiendaNew = em.getReference(encomiendaNew.getClass(), encomiendaNew.getId());
                envio.setEncomienda(encomiendaNew);
            }
            envio = em.merge(envio);
            if (recorridoOld != null && !recorridoOld.equals(recorridoNew)) {
                recorridoOld.getEnvioCollection().remove(envio);
                recorridoOld = em.merge(recorridoOld);
            }
            if (recorridoNew != null && !recorridoNew.equals(recorridoOld)) {
                recorridoNew.getEnvioCollection().add(envio);
                recorridoNew = em.merge(recorridoNew);
            }
            if (encomiendaOld != null && !encomiendaOld.equals(encomiendaNew)) {
                encomiendaOld.getEnvioCollection().remove(envio);
                encomiendaOld = em.merge(encomiendaOld);
            }
            if (encomiendaNew != null && !encomiendaNew.equals(encomiendaOld)) {
                encomiendaNew.getEnvioCollection().add(envio);
                encomiendaNew = em.merge(encomiendaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = envio.getId();
                if (findEnvio(id) == null) {
                    throw new NonexistentEntityException("The envio with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Envio envio;
            try {
                envio = em.getReference(Envio.class, id);
                envio.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The envio with id " + id + " no longer exists.", enfe);
            }
            Recorrido recorrido = envio.getRecorrido();
            if (recorrido != null) {
                recorrido.getEnvioCollection().remove(envio);
                recorrido = em.merge(recorrido);
            }
            Encomienda encomienda = envio.getEncomienda();
            if (encomienda != null) {
                encomienda.getEnvioCollection().remove(envio);
                encomienda = em.merge(encomienda);
            }
            em.remove(envio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Envio> findEnvioEntities() {
        return findEnvioEntities(true, -1, -1);
    }

    public List<Envio> findEnvioEntities(int maxResults, int firstResult) {
        return findEnvioEntities(false, maxResults, firstResult);
    }

    private List<Envio> findEnvioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Envio.class));
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

    public Envio findEnvio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Envio.class, id);
        } finally {
            em.close();
        }
    }

    public int getEnvioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Envio> rt = cq.from(Envio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
