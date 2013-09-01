/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mejorrecorrido_entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author nnn
 */
@Entity
@Table(name = "sucursal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sucursal.findAll", query = "SELECT s FROM Sucursal s"),
    @NamedQuery(name = "Sucursal.findById", query = "SELECT s FROM Sucursal s WHERE s.id = :id")})
public class Sucursal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "ciudad", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Ciudades ciudad;
    @JoinColumn(name = "encargadoBodega", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empleado encargadoBodega;
    @JoinColumn(name = "recepcionista", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empleado recepcionista;

    public Sucursal() {
    }

    public Sucursal(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Ciudades getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudades ciudad) {
        this.ciudad = ciudad;
    }

    public Empleado getEncargadoBodega() {
        return encargadoBodega;
    }

    public void setEncargadoBodega(Empleado encargadoBodega) {
        this.encargadoBodega = encargadoBodega;
    }

    public Empleado getRecepcionista() {
        return recepcionista;
    }

    public void setRecepcionista(Empleado recepcionista) {
        this.recepcionista = recepcionista;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sucursal)) {
            return false;
        }
        Sucursal other = (Sucursal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mejorrecorrido.Sucursal[ id=" + id + " ]";
    }
    
}
