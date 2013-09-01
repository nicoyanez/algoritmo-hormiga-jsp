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
import mejorrecorrido_entity.Tipoempleado;
import mejorrecorrido_entity.Sucursal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import mejorrecorrido_entity.Empleado;
import mejorrecorrido_fachadas.exceptions.IllegalOrphanException;
import mejorrecorrido_fachadas.exceptions.NonexistentEntityException;

/**
 *
 * @author nnn
 */
public class EmpleadoJpaController implements Serializable {

    public EmpleadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleado empleado) {
        if (empleado.getSucursalCollection() == null) {
            empleado.setSucursalCollection(new ArrayList<Sucursal>());
        }
        if (empleado.getSucursalCollection1() == null) {
            empleado.setSucursalCollection1(new ArrayList<Sucursal>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipoempleado tipo = empleado.getTipo();
            if (tipo != null) {
                tipo = em.getReference(tipo.getClass(), tipo.getId());
                empleado.setTipo(tipo);
            }
            Collection<Sucursal> attachedSucursalCollection = new ArrayList<Sucursal>();
            for (Sucursal sucursalCollectionSucursalToAttach : empleado.getSucursalCollection()) {
                sucursalCollectionSucursalToAttach = em.getReference(sucursalCollectionSucursalToAttach.getClass(), sucursalCollectionSucursalToAttach.getId());
                attachedSucursalCollection.add(sucursalCollectionSucursalToAttach);
            }
            empleado.setSucursalCollection(attachedSucursalCollection);
            Collection<Sucursal> attachedSucursalCollection1 = new ArrayList<Sucursal>();
            for (Sucursal sucursalCollection1SucursalToAttach : empleado.getSucursalCollection1()) {
                sucursalCollection1SucursalToAttach = em.getReference(sucursalCollection1SucursalToAttach.getClass(), sucursalCollection1SucursalToAttach.getId());
                attachedSucursalCollection1.add(sucursalCollection1SucursalToAttach);
            }
            empleado.setSucursalCollection1(attachedSucursalCollection1);
            em.persist(empleado);
            if (tipo != null) {
                tipo.getEmpleadoCollection().add(empleado);
                tipo = em.merge(tipo);
            }
            for (Sucursal sucursalCollectionSucursal : empleado.getSucursalCollection()) {
                Empleado oldEncargadoBodegaOfSucursalCollectionSucursal = sucursalCollectionSucursal.getEncargadoBodega();
                sucursalCollectionSucursal.setEncargadoBodega(empleado);
                sucursalCollectionSucursal = em.merge(sucursalCollectionSucursal);
                if (oldEncargadoBodegaOfSucursalCollectionSucursal != null) {
                    oldEncargadoBodegaOfSucursalCollectionSucursal.getSucursalCollection().remove(sucursalCollectionSucursal);
                    oldEncargadoBodegaOfSucursalCollectionSucursal = em.merge(oldEncargadoBodegaOfSucursalCollectionSucursal);
                }
            }
            for (Sucursal sucursalCollection1Sucursal : empleado.getSucursalCollection1()) {
                Empleado oldRecepcionistaOfSucursalCollection1Sucursal = sucursalCollection1Sucursal.getRecepcionista();
                sucursalCollection1Sucursal.setRecepcionista(empleado);
                sucursalCollection1Sucursal = em.merge(sucursalCollection1Sucursal);
                if (oldRecepcionistaOfSucursalCollection1Sucursal != null) {
                    oldRecepcionistaOfSucursalCollection1Sucursal.getSucursalCollection1().remove(sucursalCollection1Sucursal);
                    oldRecepcionistaOfSucursalCollection1Sucursal = em.merge(oldRecepcionistaOfSucursalCollection1Sucursal);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empleado empleado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado persistentEmpleado = em.find(Empleado.class, empleado.getId());
            Tipoempleado tipoOld = persistentEmpleado.getTipo();
            Tipoempleado tipoNew = empleado.getTipo();
            Collection<Sucursal> sucursalCollectionOld = persistentEmpleado.getSucursalCollection();
            Collection<Sucursal> sucursalCollectionNew = empleado.getSucursalCollection();
            Collection<Sucursal> sucursalCollection1Old = persistentEmpleado.getSucursalCollection1();
            Collection<Sucursal> sucursalCollection1New = empleado.getSucursalCollection1();
            List<String> illegalOrphanMessages = null;
            for (Sucursal sucursalCollectionOldSucursal : sucursalCollectionOld) {
                if (!sucursalCollectionNew.contains(sucursalCollectionOldSucursal)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Sucursal " + sucursalCollectionOldSucursal + " since its encargadoBodega field is not nullable.");
                }
            }
            for (Sucursal sucursalCollection1OldSucursal : sucursalCollection1Old) {
                if (!sucursalCollection1New.contains(sucursalCollection1OldSucursal)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Sucursal " + sucursalCollection1OldSucursal + " since its recepcionista field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tipoNew != null) {
                tipoNew = em.getReference(tipoNew.getClass(), tipoNew.getId());
                empleado.setTipo(tipoNew);
            }
            Collection<Sucursal> attachedSucursalCollectionNew = new ArrayList<Sucursal>();
            for (Sucursal sucursalCollectionNewSucursalToAttach : sucursalCollectionNew) {
                sucursalCollectionNewSucursalToAttach = em.getReference(sucursalCollectionNewSucursalToAttach.getClass(), sucursalCollectionNewSucursalToAttach.getId());
                attachedSucursalCollectionNew.add(sucursalCollectionNewSucursalToAttach);
            }
            sucursalCollectionNew = attachedSucursalCollectionNew;
            empleado.setSucursalCollection(sucursalCollectionNew);
            Collection<Sucursal> attachedSucursalCollection1New = new ArrayList<Sucursal>();
            for (Sucursal sucursalCollection1NewSucursalToAttach : sucursalCollection1New) {
                sucursalCollection1NewSucursalToAttach = em.getReference(sucursalCollection1NewSucursalToAttach.getClass(), sucursalCollection1NewSucursalToAttach.getId());
                attachedSucursalCollection1New.add(sucursalCollection1NewSucursalToAttach);
            }
            sucursalCollection1New = attachedSucursalCollection1New;
            empleado.setSucursalCollection1(sucursalCollection1New);
            empleado = em.merge(empleado);
            if (tipoOld != null && !tipoOld.equals(tipoNew)) {
                tipoOld.getEmpleadoCollection().remove(empleado);
                tipoOld = em.merge(tipoOld);
            }
            if (tipoNew != null && !tipoNew.equals(tipoOld)) {
                tipoNew.getEmpleadoCollection().add(empleado);
                tipoNew = em.merge(tipoNew);
            }
            for (Sucursal sucursalCollectionNewSucursal : sucursalCollectionNew) {
                if (!sucursalCollectionOld.contains(sucursalCollectionNewSucursal)) {
                    Empleado oldEncargadoBodegaOfSucursalCollectionNewSucursal = sucursalCollectionNewSucursal.getEncargadoBodega();
                    sucursalCollectionNewSucursal.setEncargadoBodega(empleado);
                    sucursalCollectionNewSucursal = em.merge(sucursalCollectionNewSucursal);
                    if (oldEncargadoBodegaOfSucursalCollectionNewSucursal != null && !oldEncargadoBodegaOfSucursalCollectionNewSucursal.equals(empleado)) {
                        oldEncargadoBodegaOfSucursalCollectionNewSucursal.getSucursalCollection().remove(sucursalCollectionNewSucursal);
                        oldEncargadoBodegaOfSucursalCollectionNewSucursal = em.merge(oldEncargadoBodegaOfSucursalCollectionNewSucursal);
                    }
                }
            }
            for (Sucursal sucursalCollection1NewSucursal : sucursalCollection1New) {
                if (!sucursalCollection1Old.contains(sucursalCollection1NewSucursal)) {
                    Empleado oldRecepcionistaOfSucursalCollection1NewSucursal = sucursalCollection1NewSucursal.getRecepcionista();
                    sucursalCollection1NewSucursal.setRecepcionista(empleado);
                    sucursalCollection1NewSucursal = em.merge(sucursalCollection1NewSucursal);
                    if (oldRecepcionistaOfSucursalCollection1NewSucursal != null && !oldRecepcionistaOfSucursalCollection1NewSucursal.equals(empleado)) {
                        oldRecepcionistaOfSucursalCollection1NewSucursal.getSucursalCollection1().remove(sucursalCollection1NewSucursal);
                        oldRecepcionistaOfSucursalCollection1NewSucursal = em.merge(oldRecepcionistaOfSucursalCollection1NewSucursal);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = empleado.getId();
                if (findEmpleado(id) == null) {
                    throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.");
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
            Empleado empleado;
            try {
                empleado = em.getReference(Empleado.class, id);
                empleado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Sucursal> sucursalCollectionOrphanCheck = empleado.getSucursalCollection();
            for (Sucursal sucursalCollectionOrphanCheckSucursal : sucursalCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleado (" + empleado + ") cannot be destroyed since the Sucursal " + sucursalCollectionOrphanCheckSucursal + " in its sucursalCollection field has a non-nullable encargadoBodega field.");
            }
            Collection<Sucursal> sucursalCollection1OrphanCheck = empleado.getSucursalCollection1();
            for (Sucursal sucursalCollection1OrphanCheckSucursal : sucursalCollection1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleado (" + empleado + ") cannot be destroyed since the Sucursal " + sucursalCollection1OrphanCheckSucursal + " in its sucursalCollection1 field has a non-nullable recepcionista field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Tipoempleado tipo = empleado.getTipo();
            if (tipo != null) {
                tipo.getEmpleadoCollection().remove(empleado);
                tipo = em.merge(tipo);
            }
            em.remove(empleado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empleado> findEmpleadoEntities() {
        return findEmpleadoEntities(true, -1, -1);
    }

    public List<Empleado> findEmpleadoEntities(int maxResults, int firstResult) {
        return findEmpleadoEntities(false, maxResults, firstResult);
    }

    private List<Empleado> findEmpleadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleado.class));
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

    public Empleado findEmpleado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleado> rt = cq.from(Empleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
