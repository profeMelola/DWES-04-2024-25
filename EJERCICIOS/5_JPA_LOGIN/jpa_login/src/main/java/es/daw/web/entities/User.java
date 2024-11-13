package es.daw.web.entities;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User implements Serializable{
    // ATRIBUTOS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer id;

    @Column(name="username", length = 50, unique = true, nullable = false)
    private String username;

    @Column(name="password", nullable = false)
    private String password;

    /*
     * El uso de CascadeType.ALL podría ser muy arriesgado aquí porque permite también las operaciones de eliminación en cascada (REMOVE), 
     * lo cual puede provocar que al eliminar un User, también se eliminen roles que pueden estar asociados con otros usuarios. 
     * Esto normalmente no es deseable en una relación muchos-a-muchos, ya que los roles suelen ser entidades compartidas.
     * Usando únicamente CascadeType.PERSIST y CascadeType.MERGE garantizas que:
     * PERSIST: Cuando un User se guarda por primera vez, todos los roles asignados también se guardarán automáticamente si aún no existen en la base de datos.
     * MERGE: Al actualizar un User, las entidades Role relacionadas también se actualizarán si corresponde.
     */
    // Relación muchos-a-muchos con Role, con tabla intermedia user_roles

    /*
     * Estrategia de carga (fetch = FetchType.LAZY)
     * Utilizada para optimizar el rendimiento cargando las relaciones muchos-a-muchos solo cuando se necesiten. 
     * Esto es útil en relaciones complejas para evitar que se carguen automáticamente grandes conjuntos de datos.
     */    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "user_roles", //Nombre tabla
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Rol> roles;


    // CONSTRUCTOR
    public User(){
        roles = new LinkedHashSet<>();
    }

    // GETTERS & SETTERS
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }

    // ----------------
    public void addRol(Rol rol){
        roles.add(rol);
        rol.getUsers().add(this);
    }

    public void removeRol(Rol rol){
        roles.remove(rol);
        rol.getUsers().remove(this);
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", password=" + password + ", roles=" + roles + "]";
    }


    

}
