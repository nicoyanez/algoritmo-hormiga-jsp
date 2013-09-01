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
import mejorrecorrido_entity.Ciudades;
import mejorrecorrido_entity.Sucursal;
import mejorrecorrido_fachadas.exceptions.IllegalOrphanException;
import mejorrecorrido_fachadas.exceptions.NonexistentEntityException;

/**
 *
 * @author nnn
 */
public class CiudadesJpaController implements Serializable {

    public CiudadesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ciudades ciudades) {
        if (ciudades.getEncomiendaCollection() == null) {
            ciudades.setEncomiendaCollection(new ArrayList<Encomienda>());
        }
        if (ciudades.getEncomiendaCollection1() == null) {
            ciudades.setEncomiendaCollection1(new ArrayList<Encomienda>());
        }
        if (ciudades.getSucursalCollection() == null) {
            ciudades.setSucursalCollection(new ArrayList<Sucursal>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Encomienda> attachedEncomiendaCollection = new ArrayList<Encomienda>();
            for (Encomienda encomiendaCollectionEncomiendaToAttach : ciudades.getEncomiendaCollection()) {
                encomiendaCollectionEncomiendaToAttach = em.getReference(encomiendaCollectionEncomiendaToAttach.getClass(), encomiendaCollectionEncomiendaToAttach.getId());
                attachedEncomiendaCollection.add(encomiendaCollectionEncomiendaToAttach);
            }
            ciudades.setEncomiendaCollection(attachedEncomiendaCollection);
            Collection<Encomienda> attachedEncomiendaCollection1 = new ArrayList<Encomienda>();
            for (Encomienda encomiendaCollection1EncomiendaToAttach : ciudades.getEncomiendaCollection1()) {
                encomiendaCollection1EncomiendaToAttach = em.getReference(encomiendaCollection1EncomiendaToAttach.getClass(), encomiendaCollection1EncomiendaToAttach.getId());
                attachedEncomiendaCollection1.add(encomiendaCollection1EncomiendaToAttach);
            }
            ciudades.setEncomiendaCollection1(attachedEncomiendaCollection1);
            Collection<Sucursal> attachedSucursalCollection = new ArrayList<Sucursal>();
            for (Sucursal sucursalCollectionSucursalToAttach : ciudades.getSucursalCollection()) {
                sucursalCollectionSucursalToAttach = em.getReference(sucursalCollectionSucursalToAttach.getClass(), sucursalCollectionSucursalToAttach.getId());
                attachedSucursalCollection.add(sucursalCollectionSucursalToAttach);
            }
            ciudades.setSucursalCollection(attachedSucursalCollection);
            em.persist(ciudades);
            for (Encomienda encomiendaCollectionEncomienda : ciudades.getEncomiendaCollection()) {
                Ciudades oldCiudadDOfEncomiendaCollectionEncomienda = encomiendaCollectionEncomienda.getCiudadD();
                encomiendaCollectionEncomienda.setCiudadD(ciudades);
                encomiendaCollectionEncomienda = em.merge(encomiendaCollectionEncomienda);
                if (oldCiudadDOfEncomiendaCollectionEncomienda != null) {
                    oldCiudadDOfEncomiendaCollectionEncomienda.getEncomiendaCollection().remove(encomiendaCollectionEncomienda);
                    oldCiudadDOfEncomiendaCollectionEncomienda = em.merge(oldCiudadDOfEncomiendaCollectionEncomienda);
                }
            }
            for (Encomienda encomiendaCollection1Encomienda : ciudades.getEncomiendaCollection1()) {
                Ciudades oldCiudadOOfEncomiendaCollection1Encomienda = encomiendaCollection1Encomienda.getCiudadO();
                encomiendaCollection1Encomienda.setCiudadO(ciudades);
                encomiendaCollection1Encomienda = em.merge(encomiendaCollection1Encomienda);
                if (oldCiudadOOfEncomiendaCollection1Encomienda != null) {
                    oldCiudadOOfEncomiendaCollection1Encomienda.getEncomiendaCollection1().remove(encomiendaCollection1Encomienda);
                    oldCiudadOOfEncomiendaCollection1Encomienda = em.merge(oldCiudadOOfEncomiendaCollection1Encomienda);
                }
            }
            for (Sucursal sucursalCollectionSucursal : ciudades.getSucursalCollection()) {
                Ciudades oldCiudadOfSucursalCollectionSucursal = sucursalCollectionSucursal.getCiudad();
                sucursalCollectionSucursal.setCiudad(ciudades);
                sucursalCollectionSucursal = em.merge(sucursalCollectionSucursal);
                if (oldCiudadOfSucursalCollectionSucursal != null) {
                    oldCiudadOfSucursalCollectionSucursal.getSucursalCollection().remove(sucursalCollectionSucursal);
                    oldCiudadOfSucursalCollectionSucursal = em.merge(oldCiudadOfSucursalCollectionSucursal);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ciudades ciudades) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ciudades persistentCiudades = em.find(Ciudades.class, ciudades.getId());
            Collection<Encomienda> encomiendaCollectionOld = persistentCiudades.getEncomiendaCollection();
            Collection<Encomienda> encomiendaCollectionNew = ciudades.getEncomiendaCollection();
            Collection<Encomienda> encomiendaCollection1Old = persistentCiudades.getEncomiendaCollection1();
            Collection<Encomienda> encomiendaCollection1New = ciudades.getEncomiendaCollection1();
            Collection<Sucursal> sucursalCollectionOld = persistentCiudades.getSucursalCollection();
            Collection<Sucursal> sucursalCollectionNew = ciudades.getSucursalCollection();
            List<String> illegalOrphanMessages = null;
            for (Encomienda encomiendaCollectionOldEncomienda : encomiendaCollectionOld) {
                if (!encomiendaCollectionNew.contains(encomiendaCollectionOldEncomienda)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Encomienda " + encomiendaCollectionOldEncomienda + " since its ciudadD field is not nullable.");
                }
            }
            for (Encomienda encomiendaCollection1OldEncomienda : encomiendaCollection1Old) {
                if (!encomiendaCollection1New.contains(encomiendaCollection1OldEncomienda)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Encomienda " + encomiendaCollection1OldEncomienda + " since its ciudadO field is not nullable.");
                }
            }
            for (Sucursal sucursalCollectionOldSucursal : sucursalCollectionOld) {
                if (!sucursalCollectionNew.contains(sucursalCollectionOldSucursal)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Sucursal " + sucursalCollectionOldSucursal + " since its ciudad field is not nullable.");
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
            ciudades.setEncomiendaCollection(encomiendaCollectionNew);
            Collection<Encomienda> attachedEncomiendaCollection1New = new ArrayList<Encomienda>();
            for (Encomienda encomiendaCollection1NewEncomiendaToAttach : encomiendaCollection1New) {
                encomiendaCollection1NewEncomiendaToAttach = em.getReference(encomiendaCollection1NewEncomiendaToAttach.getClass(), encomiendaCollection1NewEncomiendaToAttach.getId());
                attachedEncomiendaCollection1New.add(encomiendaCollection1NewEncomiendaToAttach);
            }
            encomiendaCollection1New = attachedEncomiendaCollection1New;
            ciudades.setEncomiendaCollection1(encomiendaCollection1New);
            Collection<Sucursal> attachedSucursalCollectionNew = new ArrayList<Sucursal>();
            for (Sucursal sucursalCollectionNewSucursalToAttach : sucursalCollectionNew) {
                sucursalCollectionNewSucursalToAttach = em.getReference(sucursalCollectionNewSucursalToAttach.getClass(), sucursalCollectionNewSucursalToAttach.getId());
                attachedSucursalCollectionNew.add(sucursalCollectionNewSucursalToAttach);
            }
            sucursalCollectionNew = attachedSucursalCollectionNew;
            ciudades.setSucursalCollection(sucursalCollectionNew);
            ciudades = em.merge(ciudades);
            for (Encomienda encomiendaCollectionNewEncomienda : encomiendaCollectionNew) {
                if (!encomiendaCollectionOld.contains(encomiendaCollectionNewEncomienda)) {
                    Ciudades oldCiudadDOfEncomiendaCollectionNewEncomienda = encomiendaCollectionNewEncomienda.getCiudadD();
                    encomiendaCollectionNewEncomienda.setCiudadD(ciudades);
                    encomiendaCollectionNewEncomienda = em.merge(encomiendaCollectionNewEncomienda);
                    if (oldCiudadDOfEncomiendaCollectionNewEncomienda != null && !oldCiudadDOfEncomiendaCollectionNewEncomienda.equals(ciudades)) {
                        oldCiudadDOfEncomiendaCollectionNewEncomienda.getEncomiendaCollection().remove(encomiendaCollectionNewEncomienda);
                        oldCiudadDOfEncomiendaCollectionNewEncomienda = em.merge(oldCiudadDOfEncomiendaCollectionNewEncomienda);
                    }
                }
            }
            for (Encomienda encomiendaCollection1NewEncomienda : encomiendaCollection1New) {
                if (!encomiendaCollection1Old.contains(encomiendaCollection1NewEncomienda)) {
                    Ciudades oldCiudadOOfEncomiendaCollection1NewEncomienda = encomiendaCollection1NewEncomienda.getCiudadO();
                    encomiendaCollection1NewEncomienda.setCiudadO(ciudades);
                    encomiendaCollection1NewEncomienda = em.merge(encomiendaCollection1NewEncomienda);
                    if (oldCiudadOOfEncomiendaCollection1NewEncomienda != null && !oldCiudadOOfEncomiendaCollection1NewEncomienda.equals(ciudades)) {
                        oldCiudadOOfEncomiendaCollection1NewEncomienda.getEncomiendaCollection1().remove(encomiendaCollection1NewEncomienda);
                        oldCiudadOOfEncomiendaCollection1NewEncomienda = em.merge(oldCiudadOOfEncomiendaCollection1NewEncomienda);
                    }
                }
            }
            for (Sucursal sucursalCollectionNewSucursal : sucursalCollectionNew) {
                if (!sucursalCollectionOld.contains(sucursalCollectionNewSucursal)) {
                    Ciudades oldCiudadOfSucursalCollectionNewSucursal = sucursalCollectionNewSucursal.getCiudad();
                    sucursalCollectionNewSucursal.setCiudad(ciudades);
                    sucursalCollectionNewSucursal = em.merge(sucursalCollectionNewSucursal);
                    if (oldCiudadOfSucursalCollectionNewSucursal != null && !oldCiudadOfSucursalCollectionNewSucursal.equals(ciudades)) {
                        oldCiudadOfSucursalCollectionNewSucursal.getSucursalCollection().remove(sucursalCollectionNewSucursal);
                        oldCiudadOfSucursalCollectionNewSucursal = em.merge(oldCiudadOfSucursalCollectionNewSucursal);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ciudades.getId();
                if (findCiudades(id) == null) {
                    throw new NonexistentEntityException("The ciudades with id " + id + " no longer exists.");
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
            Ciudades ciudades;
            try {
                ciudades = em.getReference(Ciudades.class, id);
                ciudades.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ciudades with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Encomienda> encomiendaCollectionOrphanCheck = ciudades.getEncomiendaCollection();
            for (Encomienda encomiendaCollectionOrphanCheckEncomienda : encomiendaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ciudades (" + ciudades + ") cannot be destroyed since the Encomienda " + encomiendaCollectionOrphanCheckEncomienda + " in its encomiendaCollection field has a non-nullable ciudadD field.");
            }
            Collection<Encomienda> encomiendaCollection1OrphanCheck = ciudades.getEncomiendaCollection1();
            for (Encomienda encomiendaCollection1OrphanCheckEncomienda : encomiendaCollection1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ciudades (" + ciudades + ") cannot be destroyed since the Encomienda " + encomiendaCollection1OrphanCheckEncomienda + " in its encomiendaCollection1 field has a non-nullable ciudadO field.");
            }
            Collection<Sucursal> sucursalCollectionOrphanCheck = ciudades.getSucursalCollection();
            for (Sucursal sucursalCollectionOrphanCheckSucursal : sucursalCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ciudades (" + ciudades + ") cannot be destroyed since the Sucursal " + sucursalCollectionOrphanCheckSucursal + " in its sucursalCollection field has a non-nullable ciudad field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(ciudades);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ciudades> findCiudadesEntities() {
        return findCiudadesEntities(true, -1, -1);
    }

    public List<Ciudades> findCiudadesEntities(int maxResults, int firstResult) {
        return findCiudadesEntities(false, maxResults, firstResult);
    }

    private List<Ciudades> findCiudadesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ciudades.class));
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

    public Ciudades findCiudades(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ciudades.class, id);
        } finally {
            em.close();
        }
    }

    public int getCiudadesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ciudades> rt = cq.from(Ciudades.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
