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
import mejorrecorrido_entity.Ciudades;
import mejorrecorrido_entity.Empleado;
import mejorrecorrido_entity.Sucursal;
import mejorrecorrido_fachadas.exceptions.NonexistentEntityException;

/**
 *
 * @author nnn
 */
public class SucursalJpaController implements Serializable {

    public SucursalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sucursal sucursal) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ciudades ciudad = sucursal.getCiudad();
            if (ciudad != null) {
                ciudad = em.getReference(ciudad.getClass(), ciudad.getId());
                sucursal.setCiudad(ciudad);
            }
            Empleado encargadoBodega = sucursal.getEncargadoBodega();
            if (encargadoBodega != null) {
                encargadoBodega = em.getReference(encargadoBodega.getClass(), encargadoBodega.getId());
                sucursal.setEncargadoBodega(encargadoBodega);
            }
            Empleado recepcionista = sucursal.getRecepcionista();
            if (recepcionista != null) {
                recepcionista = em.getReference(recepcionista.getClass(), recepcionista.getId());
                sucursal.setRecepcionista(recepcionista);
            }
            em.persist(sucursal);
            if (ciudad != null) {
                ciudad.getSucursalCollection().add(sucursal);
                ciudad = em.merge(ciudad);
            }
            if (encargadoBodega != null) {
                encargadoBodega.getSucursalCollection().add(sucursal);
                encargadoBodega = em.merge(encargadoBodega);
            }
            if (recepcionista != null) {
                recepcionista.getSucursalCollection().add(sucursal);
                recepcionista = em.merge(recepcionista);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sucursal sucursal) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sucursal persistentSucursal = em.find(Sucursal.class, sucursal.getId());
            Ciudades ciudadOld = persistentSucursal.getCiudad();
            Ciudades ciudadNew = sucursal.getCiudad();
            Empleado encargadoBodegaOld = persistentSucursal.getEncargadoBodega();
            Empleado encargadoBodegaNew = sucursal.getEncargadoBodega();
            Empleado recepcionistaOld = persistentSucursal.getRecepcionista();
            Empleado recepcionistaNew = sucursal.getRecepcionista();
            if (ciudadNew != null) {
                ciudadNew = em.getReference(ciudadNew.getClass(), ciudadNew.getId());
                sucursal.setCiudad(ciudadNew);
            }
            if (encargadoBodegaNew != null) {
                encargadoBodegaNew = em.getReference(encargadoBodegaNew.getClass(), encargadoBodegaNew.getId());
                sucursal.setEncargadoBodega(encargadoBodegaNew);
            }
            if (recepcionistaNew != null) {
                recepcionistaNew = em.getReference(recepcionistaNew.getClass(), recepcionistaNew.getId());
                sucursal.setRecepcionista(recepcionistaNew);
            }
            sucursal = em.merge(sucursal);
            if (ciudadOld != null && !ciudadOld.equals(ciudadNew)) {
                ciudadOld.getSucursalCollection().remove(sucursal);
                ciudadOld = em.merge(ciudadOld);
            }
            if (ciudadNew != null && !ciudadNew.equals(ciudadOld)) {
                ciudadNew.getSucursalCollection().add(sucursal);
                ciudadNew = em.merge(ciudadNew);
            }
            if (encargadoBodegaOld != null && !encargadoBodegaOld.equals(encargadoBodegaNew)) {
                encargadoBodegaOld.getSucursalCollection().remove(sucursal);
                encargadoBodegaOld = em.merge(encargadoBodegaOld);
            }
            if (encargadoBodegaNew != null && !encargadoBodegaNew.equals(encargadoBodegaOld)) {
                encargadoBodegaNew.getSucursalCollection().add(sucursal);
                encargadoBodegaNew = em.merge(encargadoBodegaNew);
            }
            if (recepcionistaOld != null && !recepcionistaOld.equals(recepcionistaNew)) {
                recepcionistaOld.getSucursalCollection().remove(sucursal);
                recepcionistaOld = em.merge(recepcionistaOld);
            }
            if (recepcionistaNew != null && !recepcionistaNew.equals(recepcionistaOld)) {
                recepcionistaNew.getSucursalCollection().add(sucursal);
                recepcionistaNew = em.merge(recepcionistaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = sucursal.getId();
                if (findSucursal(id) == null) {
                    throw new NonexistentEntityException("The sucursal with id " + id + " no longer exists.");
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
            Sucursal sucursal;
            try {
                sucursal = em.getReference(Sucursal.class, id);
                sucursal.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sucursal with id " + id + " no longer exists.", enfe);
            }
            Ciudades ciudad = sucursal.getCiudad();
            if (ciudad != null) {
                ciudad.getSucursalCollection().remove(sucursal);
                ciudad = em.merge(ciudad);
            }
            Empleado encargadoBodega = sucursal.getEncargadoBodega();
            if (encargadoBodega != null) {
                encargadoBodega.getSucursalCollection().remove(sucursal);
                encargadoBodega = em.merge(encargadoBodega);
            }
            Empleado recepcionista = sucursal.getRecepcionista();
            if (recepcionista != null) {
                recepcionista.getSucursalCollection().remove(sucursal);
                recepcionista = em.merge(recepcionista);
            }
            em.remove(sucursal);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sucursal> findSucursalEntities() {
        return findSucursalEntities(true, -1, -1);
    }

    public List<Sucursal> findSucursalEntities(int maxResults, int firstResult) {
        return findSucursalEntities(false, maxResults, firstResult);
    }

    private List<Sucursal> findSucursalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sucursal.class));
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

    public Sucursal findSucursal(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sucursal.class, id);
        } finally {
            em.close();
        }
    }

    public int getSucursalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sucursal> rt = cq.from(Sucursal.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
