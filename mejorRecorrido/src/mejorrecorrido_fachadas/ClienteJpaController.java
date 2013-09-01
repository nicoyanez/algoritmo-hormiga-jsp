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
import mejorrecorrido_entity.Encomienda;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import mejorrecorrido_entity.Cliente;
import mejorrecorrido_entity.Telefono;
import mejorrecorrido_fachadas.exceptions.IllegalOrphanException;
import mejorrecorrido_fachadas.exceptions.NonexistentEntityException;

/**
 *
 * @author nnn
 */
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) {
        if (cliente.getEncomiendaCollection() == null) {
            cliente.setEncomiendaCollection(new ArrayList<Encomienda>());
        }
        if (cliente.getEncomiendaCollection1() == null) {
            cliente.setEncomiendaCollection1(new ArrayList<Encomienda>());
        }
        if (cliente.getTelefonoCollection() == null) {
            cliente.setTelefonoCollection(new ArrayList<Telefono>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Encomienda> attachedEncomiendaCollection = new ArrayList<Encomienda>();
            for (Encomienda encomiendaCollectionEncomiendaToAttach : cliente.getEncomiendaCollection()) {
                encomiendaCollectionEncomiendaToAttach = em.getReference(encomiendaCollectionEncomiendaToAttach.getClass(), encomiendaCollectionEncomiendaToAttach.getId());
                attachedEncomiendaCollection.add(encomiendaCollectionEncomiendaToAttach);
            }
            cliente.setEncomiendaCollection(attachedEncomiendaCollection);
            Collection<Encomienda> attachedEncomiendaCollection1 = new ArrayList<Encomienda>();
            for (Encomienda encomiendaCollection1EncomiendaToAttach : cliente.getEncomiendaCollection1()) {
                encomiendaCollection1EncomiendaToAttach = em.getReference(encomiendaCollection1EncomiendaToAttach.getClass(), encomiendaCollection1EncomiendaToAttach.getId());
                attachedEncomiendaCollection1.add(encomiendaCollection1EncomiendaToAttach);
            }
            cliente.setEncomiendaCollection1(attachedEncomiendaCollection1);
            Collection<Telefono> attachedTelefonoCollection = new ArrayList<Telefono>();
            for (Telefono telefonoCollectionTelefonoToAttach : cliente.getTelefonoCollection()) {
                telefonoCollectionTelefonoToAttach = em.getReference(telefonoCollectionTelefonoToAttach.getClass(), telefonoCollectionTelefonoToAttach.getId());
                attachedTelefonoCollection.add(telefonoCollectionTelefonoToAttach);
            }
            cliente.setTelefonoCollection(attachedTelefonoCollection);
            em.persist(cliente);
            for (Encomienda encomiendaCollectionEncomienda : cliente.getEncomiendaCollection()) {
                Cliente oldClienterOfEncomiendaCollectionEncomienda = encomiendaCollectionEncomienda.getClienter();
                encomiendaCollectionEncomienda.setClienter(cliente);
                encomiendaCollectionEncomienda = em.merge(encomiendaCollectionEncomienda);
                if (oldClienterOfEncomiendaCollectionEncomienda != null) {
                    oldClienterOfEncomiendaCollectionEncomienda.getEncomiendaCollection().remove(encomiendaCollectionEncomienda);
                    oldClienterOfEncomiendaCollectionEncomienda = em.merge(oldClienterOfEncomiendaCollectionEncomienda);
                }
            }
            for (Encomienda encomiendaCollection1Encomienda : cliente.getEncomiendaCollection1()) {
                Cliente oldClienteeOfEncomiendaCollection1Encomienda = encomiendaCollection1Encomienda.getClientee();
                encomiendaCollection1Encomienda.setClientee(cliente);
                encomiendaCollection1Encomienda = em.merge(encomiendaCollection1Encomienda);
                if (oldClienteeOfEncomiendaCollection1Encomienda != null) {
                    oldClienteeOfEncomiendaCollection1Encomienda.getEncomiendaCollection1().remove(encomiendaCollection1Encomienda);
                    oldClienteeOfEncomiendaCollection1Encomienda = em.merge(oldClienteeOfEncomiendaCollection1Encomienda);
                }
            }
            for (Telefono telefonoCollectionTelefono : cliente.getTelefonoCollection()) {
                Cliente oldClienteOfTelefonoCollectionTelefono = telefonoCollectionTelefono.getCliente();
                telefonoCollectionTelefono.setCliente(cliente);
                telefonoCollectionTelefono = em.merge(telefonoCollectionTelefono);
                if (oldClienteOfTelefonoCollectionTelefono != null) {
                    oldClienteOfTelefonoCollectionTelefono.getTelefonoCollection().remove(telefonoCollectionTelefono);
                    oldClienteOfTelefonoCollectionTelefono = em.merge(oldClienteOfTelefonoCollectionTelefono);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getId());
            Collection<Encomienda> encomiendaCollectionOld = persistentCliente.getEncomiendaCollection();
            Collection<Encomienda> encomiendaCollectionNew = cliente.getEncomiendaCollection();
            Collection<Encomienda> encomiendaCollection1Old = persistentCliente.getEncomiendaCollection1();
            Collection<Encomienda> encomiendaCollection1New = cliente.getEncomiendaCollection1();
            Collection<Telefono> telefonoCollectionOld = persistentCliente.getTelefonoCollection();
            Collection<Telefono> telefonoCollectionNew = cliente.getTelefonoCollection();
            List<String> illegalOrphanMessages = null;
            for (Encomienda encomiendaCollectionOldEncomienda : encomiendaCollectionOld) {
                if (!encomiendaCollectionNew.contains(encomiendaCollectionOldEncomienda)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Encomienda " + encomiendaCollectionOldEncomienda + " since its clienter field is not nullable.");
                }
            }
            for (Encomienda encomiendaCollection1OldEncomienda : encomiendaCollection1Old) {
                if (!encomiendaCollection1New.contains(encomiendaCollection1OldEncomienda)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Encomienda " + encomiendaCollection1OldEncomienda + " since its clientee field is not nullable.");
                }
            }
            for (Telefono telefonoCollectionOldTelefono : telefonoCollectionOld) {
                if (!telefonoCollectionNew.contains(telefonoCollectionOldTelefono)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Telefono " + telefonoCollectionOldTelefono + " since its cliente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Encomienda> attachedEncomiendaCollectionNew = new ArrayList<Encomienda>();
            for (Encomienda encomiendaCollectionNewEncomiendaToAttach : encomiendaCollectionNew) {
                encomiendaCollectionNewEncomiendaToAttach = em.getReference(encomiendaCollectionNewEncomiendaToAttach.getClass(), encomiendaCollectionNewEncomiendaToAttach.getId());
                attachedEncomiendaCollectionNew.add(encomiendaCollectionNewEncomiendaToAttach);
            }
            encomiendaCollectionNew = attachedEncomiendaCollectionNew;
            cliente.setEncomiendaCollection(encomiendaCollectionNew);
            Collection<Encomienda> attachedEncomiendaCollection1New = new ArrayList<Encomienda>();
            for (Encomienda encomiendaCollection1NewEncomiendaToAttach : encomiendaCollection1New) {
                encomiendaCollection1NewEncomiendaToAttach = em.getReference(encomiendaCollection1NewEncomiendaToAttach.getClass(), encomiendaCollection1NewEncomiendaToAttach.getId());
                attachedEncomiendaCollection1New.add(encomiendaCollection1NewEncomiendaToAttach);
            }
            encomiendaCollection1New = attachedEncomiendaCollection1New;
            cliente.setEncomiendaCollection1(encomiendaCollection1New);
            Collection<Telefono> attachedTelefonoCollectionNew = new ArrayList<Telefono>();
            for (Telefono telefonoCollectionNewTelefonoToAttach : telefonoCollectionNew) {
                telefonoCollectionNewTelefonoToAttach = em.getReference(telefonoCollectionNewTelefonoToAttach.getClass(), telefonoCollectionNewTelefonoToAttach.getId());
                attachedTelefonoCollectionNew.add(telefonoCollectionNewTelefonoToAttach);
            }
            telefonoCollectionNew = attachedTelefonoCollectionNew;
            cliente.setTelefonoCollection(telefonoCollectionNew);
            cliente = em.merge(cliente);
            for (Encomienda encomiendaCollectionNewEncomienda : encomiendaCollectionNew) {
                if (!encomiendaCollectionOld.contains(encomiendaCollectionNewEncomienda)) {
                    Cliente oldClienterOfEncomiendaCollectionNewEncomienda = encomiendaCollectionNewEncomienda.getClienter();
                    encomiendaCollectionNewEncomienda.setClienter(cliente);
                    encomiendaCollectionNewEncomienda = em.merge(encomiendaCollectionNewEncomienda);
                    if (oldClienterOfEncomiendaCollectionNewEncomienda != null && !oldClienterOfEncomiendaCollectionNewEncomienda.equals(cliente)) {
                        oldClienterOfEncomiendaCollectionNewEncomienda.getEncomiendaCollection().remove(encomiendaCollectionNewEncomienda);
                        oldClienterOfEncomiendaCollectionNewEncomienda = em.merge(oldClienterOfEncomiendaCollectionNewEncomienda);
                    }
                }
            }
            for (Encomienda encomiendaCollection1NewEncomienda : encomiendaCollection1New) {
                if (!encomiendaCollection1Old.contains(encomiendaCollection1NewEncomienda)) {
                    Cliente oldClienteeOfEncomiendaCollection1NewEncomienda = encomiendaCollection1NewEncomienda.getClientee();
                    encomiendaCollection1NewEncomienda.setClientee(cliente);
                    encomiendaCollection1NewEncomienda = em.merge(encomiendaCollection1NewEncomienda);
                    if (oldClienteeOfEncomiendaCollection1NewEncomienda != null && !oldClienteeOfEncomiendaCollection1NewEncomienda.equals(cliente)) {
                        oldClienteeOfEncomiendaCollection1NewEncomienda.getEncomiendaCollection1().remove(encomiendaCollection1NewEncomienda);
                        oldClienteeOfEncomiendaCollection1NewEncomienda = em.merge(oldClienteeOfEncomiendaCollection1NewEncomienda);
                    }
                }
            }
            for (Telefono telefonoCollectionNewTelefono : telefonoCollectionNew) {
                if (!telefonoCollectionOld.contains(telefonoCollectionNewTelefono)) {
                    Cliente oldClienteOfTelefonoCollectionNewTelefono = telefonoCollectionNewTelefono.getCliente();
                    telefonoCollectionNewTelefono.setCliente(cliente);
                    telefonoCollectionNewTelefono = em.merge(telefonoCollectionNewTelefono);
                    if (oldClienteOfTelefonoCollectionNewTelefono != null && !oldClienteOfTelefonoCollectionNewTelefono.equals(cliente)) {
                        oldClienteOfTelefonoCollectionNewTelefono.getTelefonoCollection().remove(telefonoCollectionNewTelefono);
                        oldClienteOfTelefonoCollectionNewTelefono = em.merge(oldClienteOfTelefonoCollectionNewTelefono);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cliente.getId();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Encomienda> encomiendaCollectionOrphanCheck = cliente.getEncomiendaCollection();
            for (Encomienda encomiendaCollectionOrphanCheckEncomienda : encomiendaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the Encomienda " + encomiendaCollectionOrphanCheckEncomienda + " in its encomiendaCollection field has a non-nullable clienter field.");
            }
            Collection<Encomienda> encomiendaCollection1OrphanCheck = cliente.getEncomiendaCollection1();
            for (Encomienda encomiendaCollection1OrphanCheckEncomienda : encomiendaCollection1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the Encomienda " + encomiendaCollection1OrphanCheckEncomienda + " in its encomiendaCollection1 field has a non-nullable clientee field.");
            }
            Collection<Telefono> telefonoCollectionOrphanCheck = cliente.getTelefonoCollection();
            for (Telefono telefonoCollectionOrphanCheckTelefono : telefonoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the Telefono " + telefonoCollectionOrphanCheckTelefono + " in its telefonoCollection field has a non-nullable cliente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
