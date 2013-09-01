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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "encomienda")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Encomienda.findAll", query = "SELECT e FROM Encomienda e"),
    @NamedQuery(name = "Encomienda.findById", query = "SELECT e FROM Encomienda e WHERE e.id = :id"),
    @NamedQuery(name = "Encomienda.findByAncho", query = "SELECT e FROM Encomienda e WHERE e.ancho = :ancho"),
    @NamedQuery(name = "Encomienda.findByAlto", query = "SELECT e FROM Encomienda e WHERE e.alto = :alto"),
    @NamedQuery(name = "Encomienda.findByLargo", query = "SELECT e FROM Encomienda e WHERE e.largo = :largo"),
    @NamedQuery(name = "Encomienda.findByPeso", query = "SELECT e FROM Encomienda e WHERE e.peso = :peso")})
public class Encomienda implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "ancho")
    private int ancho;
    @Basic(optional = false)
    @Column(name = "alto")
    private int alto;
    @Basic(optional = false)
    @Column(name = "largo")
    private int largo;
    @Basic(optional = false)
    @Column(name = "peso")
    private int peso;
    @JoinColumn(name = "clienter", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Cliente clienter;
    @JoinColumn(name = "clientee", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Cliente clientee;
    @JoinColumn(name = "ciudadD", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Ciudades ciudadD;
    @JoinColumn(name = "ciudadO", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Ciudades ciudadO;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "encomienda")
    private Collection<Envio> envioCollection;

    public Encomienda() {
    }

    public Encomienda(Integer id) {
        this.id = id;
    }

    public Encomienda(Integer id, int ancho, int alto, int largo, int peso) {
        this.id = id;
        this.ancho = ancho;
        this.alto = alto;
        this.largo = largo;
        this.peso = peso;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public int getLargo() {
        return largo;
    }

    public void setLargo(int largo) {
        this.largo = largo;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public Cliente getClienter() {
        return clienter;
    }

    public void setClienter(Cliente clienter) {
        this.clienter = clienter;
    }

    public Cliente getClientee() {
        return clientee;
    }

    public void setClientee(Cliente clientee) {
        this.clientee = clientee;
    }

    public Ciudades getCiudadD() {
        return ciudadD;
    }

    public void setCiudadD(Ciudades ciudadD) {
        this.ciudadD = ciudadD;
    }

    public Ciudades getCiudadO() {
        return ciudadO;
    }

    public void setCiudadO(Ciudades ciudadO) {
        this.ciudadO = ciudadO;
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
        if (!(object instanceof Encomienda)) {
            return false;
        }
        Encomienda other = (Encomienda) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mejorrecorrido.Encomienda[ id=" + id + " ]";
    }
    
}
