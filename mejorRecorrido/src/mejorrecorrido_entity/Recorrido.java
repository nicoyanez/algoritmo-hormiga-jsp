/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mejorrecorrido_entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author nnn
 */
@Entity
@Table(name = "recorrido")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Recorrido.findAll", query = "SELECT r FROM Recorrido r"),
    @NamedQuery(name = "Recorrido.findById", query = "SELECT r FROM Recorrido r WHERE r.id = :id"),
    @NamedQuery(name = "Recorrido.findByTrayecto", query = "SELECT r FROM Recorrido r WHERE r.trayecto = :trayecto")})
public class Recorrido implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "trayecto")
    private String trayecto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recorrido")
    private Collection<Envio> envioCollection;

    public Recorrido() {
    }

    public Recorrido(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTrayecto() {
        return trayecto;
    }

    public void setTrayecto(String trayecto) {
        this.trayecto = trayecto;
    }

    @XmlTransient
    public Collection<Envio> getEnvioCollection() {
        return envioCollection;
    }

    public void setEnvioCollection(Collection<Envio> envioCollection) {
        this.envioCollection = envioCollection;
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
        if (!(object instanceof Recorrido)) {
            return false;
        }
        Recorrido other = (Recorrido) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mejorrecorrido.Recorrido[ id=" + id + " ]";
    }
    
}
