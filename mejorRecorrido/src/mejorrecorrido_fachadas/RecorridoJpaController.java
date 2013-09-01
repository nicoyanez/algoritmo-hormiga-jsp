/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mejorrecorrido_fachadas;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import mejorrecorrido_entity.Envio;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import mejorrecorrido_entity.Recorrido;
import mejorrecorrido_fachadas.exceptions.IllegalOrphanException;
import mejorrecorrido_fachadas.exceptions.NonexistentEntityException;

/**
 *
 * @author nnn
 */
public class RecorridoJpaController implements Serializable {

    public RecorridoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Recorrido recorrido) {
        if (recorrido.getEnvioCollection() == null) {
            recorrido.setEnvioCollection(new ArrayList<Envio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Envio> attachedEnvioCollection = new ArrayList<Envio>();
            for (Envio envioCollectionEnvioToAttach : recorrido.getEnvioCollection()) {
                envioCollectionEnvioToAttach = em.getReference(envioCollectionEnvioToAttach.getClass(), envioCollectionEnvioToAttach.getId());
                attachedEnvioCollection.add(envioCollectionEnvioToAttach);
            }
            recorrido.setEnvioCollection(attachedEnvioCollection);
            em.persist(recorrido);
            for (Envio envioCollectionEnvio : recorrido.getEnvioCollection()) {
                Recorrido oldRecorridoOfEnvioCollectionEnvio = envioCollectionEnvio.getRecorrido();
                envioCollectionEnvio.setRecorrido(recorrido);
                envioCollectionEnvio = em.merge(envioCollectionEnvio);
                if (oldRecorridoOfEnvioCollectionEnvio != null) {
                    oldRecorridoOfEnvioCollectionEnvio.getEnvioCollection().remove(envioCollectionEnvio);
                    oldRecorridoOfEnvioCollectionEnvio = em.merge(oldRecorridoOfEnvioCollectionEnvio);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Recorrido recorrido) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Recorrido persistentRecorrido = em.find(Recorrido.class, recorrido.getId());
            Collection<Envio> envioCollectionOld = persistentRecorrido.getEnvioCollection();
            Collection<Envio> envioCollectionNew = recorrido.getEnvioCollection();
            List<String> illegalOrphanMessages = null;
            for (Envio envioCollectionOldEnvio : envioCollectionOld) {
                if (!envioCollectionNew.contains(envioCollectionOldEnvio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Envio " + envioCollectionOldEnvio + " since its recorrido field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Envio> attachedEnvioCollectionNew = new ArrayList<Envio>();
            for (Envio envioCollectionNewEnvioToAttach : envioCollectionNew) {
                envioCollectionNewEnvioToAttach = em.getReference(envioCollectionNewEnvioToAttach.getClass(), envioCollectionNewEnvioToAttach.getId());
                attachedEnvioCollectionNew.add(envioCollectionNewEnvioToAttach);
            }
            envioCollectionNew = attachedEnvioCollectionNew;
            recorrido.setEnvioCollection(envioCollectionNew);
            recorrido = em.merge(recorrido);
            for (Envio envioCollectionNewEnvio : envioCollectionNew) {
                if (!envioCollectionOld.contains(envioCollectionNewEnvio)) {
                    Recorrido oldRecorridoOfEnvioCollectionNewEnvio = envioCollectionNewEnvio.getRecorrido();
                    envioCollectionNewEnvio.setRecorrido(recorrido);
                    envioCollectionNewEnvio = em.merge(envioCollectionNewEnvio);
                    if (oldRecorridoOfEnvioCollectionNewEnvio != null && !oldRecorridoOfEnvioCollectionNewEnvio.equals(recorrido)) {
                        oldRecorridoOfEnvioCollectionNewEnvio.getEnvioCollection().remove(envioCollectionNewEnvio);
                        oldRecorridoOfEnvioCollectionNewEnvio = em.merge(oldRecorridoOfEnvioCollectionNewEnvio);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = recorrido.getId();
                if (findRecorrido(id) == null) {
                    throw new NonexistentEntityException("The recorrido with id " + id + " no longer exists.");
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
            Recorrido recorrido;
            try {
                recorrido = em.getReference(Recorrido.class, id);
                recorrido.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recorrido with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Envio> envioCollectionOrphanCheck = recorrido.getEnvioCollection();
            for (Envio envioCollectionOrphanCheckEnvio : envioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Recorrido (" + recorrido + ") cannot be destroyed since the Envio " + envioCollectionOrphanCheckEnvio + " in its envioCollection field has a non-nullable recorrido field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(recorrido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Recorrido> findRecorridoEntities() {
        return findRecorridoEntities(true, -1, -1);
    }

    public List<Recorrido> findRecorridoEntities(int maxResults, int firstResult) {
        return findRecorridoEntities(false, maxResults, firstResult);
    }

    private List<Recorrido> findRecorridoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Recorrido.class));
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

    public Recorrido findRecorrido(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Recorrido.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecorridoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Recorrido> rt = cq.from(Recorrido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
