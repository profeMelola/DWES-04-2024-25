package es.daw.web.entities;

import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "roles") // Nombre de la tabla en la base de datos
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID autoincremental en H2
    @Column(name = "role_id") // Nombre de la columna en la tabla
    private Integer id;

    @Column(name = "role_name", length = 50, unique = true, nullable = false)
    private String roleName; // Nombre único del rol, no nulo

    // Relación muchos-a-muchos con User, con tabla intermedia user_roles
    // mappedBy = roles -> porque en User está -> private Set<Rol> roles = new HashSet<>();
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY) // Relación inversa, carga perezosa
    private Set<User> users;

    // Constructor vacío para JPA
    public Rol() {
        users = new LinkedHashSet<>();
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    // ------------------------
    // Método para agregar un usuario a un rol
    public void addUser(User user) {
        users.add(user);
        user.getRoles().add(this);
    }

    // Método para eliminar un usuario de un rol
    public void removeUser(User user) {
        users.remove(user);
        user.getRoles().remove(this);
    }
}

