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
import mejorrecorrido_entity.Cliente;
import mejorrecorrido_entity.Ciudades;
import mejorrecorrido_entity.Envio;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import mejorrecorrido_entity.Encomienda;
import mejorrecorrido_fachadas.exceptions.IllegalOrphanException;
import mejorrecorrido_fachadas.exceptions.NonexistentEntityException;

/**
 *
 * @author nnn
 */
public class EncomiendaJpaController implements Serializable {

    public EncomiendaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Encomienda encomienda) {
        if (encomienda.getEnvioCollection() == null) {
            encomienda.setEnvioCollection(new ArrayList<Envio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente clienter = encomienda.getClienter();
            if (clienter != null) {
                clienter = em.getReference(clienter.getClass(), clienter.getId());
                encomienda.setClienter(clienter);
            }
            Cliente clientee = encomienda.getClientee();
            if (clientee != null) {
                clientee = em.getReference(clientee.getClass(), clientee.getId());
                encomienda.setClientee(clientee);
            }
            Ciudades ciudadD = encomienda.getCiudadD();
            if (ciudadD != null) {
                ciudadD = em.getReference(ciudadD.getClass(), ciudadD.getId());
                encomienda.setCiudadD(ciudadD);
            }
            Ciudades ciudadO = encomienda.getCiudadO();
            if (ciudadO != null) {
                ciudadO = em.getReference(ciudadO.getClass(), ciudadO.getId());
                encomienda.setCiudadO(ciudadO);
            }
            Collection<Envio> attachedEnvioCollection = new ArrayList<Envio>();
            for (Envio envioCollectionEnvioToAttach : encomienda.getEnvioCollection()) {
                envioCollectionEnvioToAttach = em.getReference(envioCollectionEnvioToAttach.getClass(), envioCollectionEnvioToAttach.getId());
                attachedEnvioCollection.add(envioCollectionEnvioToAttach);
            }
            encomienda.setEnvioCollection(attachedEnvioCollection);
            em.persist(encomienda);
            if (clienter != null) {
                clienter.getEncomiendaCollection().add(encomienda);
                clienter = em.merge(clienter);
            }
            if (clientee != null) {
                clientee.getEncomiendaCollection().add(encomienda);
                clientee = em.merge(clientee);
            }
            if (ciudadD != null) {
                ciudadD.getEncomiendaCollection().add(encomienda);
                ciudadD = em.merge(ciudadD);
            }
            if (ciudadO != null) {
                ciudadO.getEncomiendaCollection().add(encomienda);
                ciudadO = em.merge(ciudadO);
            }
            for (Envio envioCollectionEnvio : encomienda.getEnvioCollection()) {
                Encomienda oldEncomiendaOfEnvioCollectionEnvio = envioCollectionEnvio.getEncomienda();
                envioCollectionEnvio.setEncomienda(encomienda);
                envioCollectionEnvio = em.merge(envioCollectionEnvio);
                if (oldEncomiendaOfEnvioCollectionEnvio != null) {
                    oldEncomiendaOfEnvioCollectionEnvio.getEnvioCollection().remove(envioCollectionEnvio);
                    oldEncomiendaOfEnvioCollectionEnvio = em.merge(oldEncomiendaOfEnvioCollectionEnvio);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Encomienda encomienda) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Encomienda persistentEncomienda = em.find(Encomienda.class, encomienda.getId());
            Cliente clienterOld = persistentEncomienda.getClienter();
            Cliente clienterNew = encomienda.getClienter();
            Cliente clienteeOld = persistentEncomienda.getClientee();
            Cliente clienteeNew = encomienda.getClientee();
            Ciudades ciudadDOld = persistentEncomienda.getCiudadD();
            Ciudades ciudadDNew = encomienda.getCiudadD();
            Ciudades ciudadOOld = persistentEncomienda.getCiudadO();
            Ciudades ciudadONew = encomienda.getCiudadO();
            Collection<Envio> envioCollectionOld = persistentEncomienda.getEnvioCollection();
            Collection<Envio> envioCollectionNew = encomienda.getEnvioCollection();
            List<String> illegalOrphanMessages = null;
            for (Envio envioCollectionOldEnvio : envioCollectionOld) {
                if (!envioCollectionNew.contains(envioCollectionOldEnvio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Envio " + envioCollectionOldEnvio + " since its encomienda field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (clienterNew != null) {
                clienterNew = em.getReference(clienterNew.getClass(), clienterNew.getId());
                encomienda.setClienter(clienterNew);
            }
            if (clienteeNew != null) {
                clienteeNew = em.getReference(clienteeNew.getClass(), clienteeNew.getId());
                encomienda.setClientee(clienteeNew);
            }
            if (ciudadDNew != null) {
                ciudadDNew = em.getReference(ciudadDNew.getClass(), ciudadDNew.getId());
                encomienda.setCiudadD(ciudadDNew);
            }
            if (ciudadONew != null) {
                ciudadONew = em.getReference(ciudadONew.getClass(), ciudadONew.getId());
                encomienda.setCiudadO(ciudadONew);
            }
            Collection<Envio> attachedEnvioCollectionNew = new ArrayList<Envio>();
            for (Envio envioCollectionNewEnvioToAttach : envioCollectionNew) {
                envioCollectionNewEnvioToAttach = em.getReference(envioCollectionNewEnvioToAttach.getClass(), envioCollectionNewEnvioToAttach.getId());
                attachedEnvioCollectionNew.add(envioCollectionNewEnvioToAttach);
            }
            envioCollectionNew = attachedEnvioCollectionNew;
            encomienda.setEnvioCollection(envioCollectionNew);
            encomienda = em.merge(encomienda);
            if (clienterOld != null && !clienterOld.equals(clienterNew)) {
                clienterOld.getEncomiendaCollection().remove(encomienda);
                clienterOld = em.merge(clienterOld);
            }
            if (clienterNew != null && !clienterNew.equals(clienterOld)) {
                clienterNew.getEncomiendaCollection().add(encomienda);
                clienterNew = em.merge(clienterNew);
            }
            if (clienteeOld != null && !clienteeOld.equals(clienteeNew)) {
                clienteeOld.getEncomiendaCollection().remove(encomienda);
                clienteeOld = em.merge(clienteeOld);
            }
            if (clienteeNew != null && !clienteeNew.equals(clienteeOld)) {
                clienteeNew.getEncomiendaCollection().add(encomienda);
                clienteeNew = em.merge(clienteeNew);
            }
            if (ciudadDOld != null && !ciudadDOld.equals(ciudadDNew)) {
                ciudadDOld.getEncomiendaCollection().remove(encomienda);
                ciudadDOld = em.merge(ciudadDOld);
            }
            if (ciudadDNew != null && !ciudadDNew.equals(ciudadDOld)) {
                ciudadDNew.getEncomiendaCollection().add(encomienda);
                ciudadDNew = em.merge(ciudadDNew);
            }
            if (ciudadOOld != null && !ciudadOOld.equals(ciudadONew)) {
                ciudadOOld.getEncomiendaCollection().remove(encomienda);
                ciudadOOld = em.merge(ciudadOOld);
            }
            if (ciudadONew != null && !ciudadONew.equals(ciudadOOld)) {
                ciudadONew.getEncomiendaCollection().add(encomienda);
                ciudadONew = em.merge(ciudadONew);
            }
            for (Envio envioCollectionNewEnvio : envioCollectionNew) {
                if (!envioCollectionOld.contains(envioCollectionNewEnvio)) {
                    Encomienda oldEncomiendaOfEnvioCollectionNewEnvio = envioCollectionNewEnvio.getEncomienda();
                    envioCollectionNewEnvio.setEncomienda(encomienda);
                    envioCollectionNewEnvio = em.merge(envioCollectionNewEnvio);
                    if (oldEncomiendaOfEnvioCollectionNewEnvio != null && !oldEncomiendaOfEnvioCollectionNewEnvio.equals(encomienda)) {
                        oldEncomiendaOfEnvioCollectionNewEnvio.getEnvioCollection().remove(envioCollectionNewEnvio);
                        oldEncomiendaOfEnvioCollectionNewEnvio = em.merge(oldEncomiendaOfEnvioCollectionNewEnvio);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = encomienda.getId();
                if (findEncomienda(id) == null) {
                    throw new NonexistentEntityException("The encomienda with id " + id + " no longer exists.");
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
            Encomienda encomienda;
            try {
                encomienda = em.getReference(Encomienda.class, id);
                encomienda.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The encomienda with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Envio> envioCollectionOrphanCheck = encomienda.getEnvioCollection();
            for (Envio envioCollectionOrphanCheckEnvio : envioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Encomienda (" + encomienda + ") cannot be destroyed since the Envio " + envioCollectionOrphanCheckEnvio + " in its envioCollection field has a non-nullable encomienda field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cliente clienter = encomienda.getClienter();
            if (clienter != null) {
                clienter.getEncomiendaCollection().remove(encomienda);
                clienter = em.merge(clienter);
            }
            Cliente clientee = encomienda.getClientee();
            if (clientee != null) {
                clientee.getEncomiendaCollection().remove(encomienda);
                clientee = em.merge(clientee);
            }
            Ciudades ciudadD = encomienda.getCiudadD();
            if (ciudadD != null) {
                ciudadD.getEncomiendaCollection().remove(encomienda);
                ciudadD = em.merge(ciudadD);
            }
            Ciudades ciudadO = encomienda.getCiudadO();
            if (ciudadO != null) {
                ciudadO.getEncomiendaCollection().remove(encomienda);
                ciudadO = em.merge(ciudadO);
            }
            em.remove(encomienda);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Encomienda> findEncomiendaEntities() {
        return findEncomiendaEntities(true, -1, -1);
    }

    public List<Encomienda> findEncomiendaEntities(int maxResults, int firstResult) {
        return findEncomiendaEntities(false, maxResults, firstResult);
    }

    private List<Encomienda> findEncomiendaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Encomienda.class));
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

    public Encomienda findEncomienda(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Encomienda.class, id);
        } finally {
            em.close();
        }
    }

    public int getEncomiendaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Encomienda> rt = cq.from(Encomienda.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
