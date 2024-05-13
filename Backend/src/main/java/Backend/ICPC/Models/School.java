package Backend.ICPC.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import javax.persistence.*;

@Entity
public class School {

    @ApiModelProperty(notes = "School ID", example = "1", required = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @ApiModelProperty(notes = "School's Name", example = "Iowa State University", required = true)
    @Column(name = "name", unique = true)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "school", cascade = CascadeType.PERSIST) // mappedBy refers to the field in User class
    private List<User> users;

    public School() {
    }

    public School(String name) {
        this.name = name;
    }

    // Other school-related properties and relationships

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public List<User> getUsers() {
        return this.users;
    }

    public void addUser(User user){
        this.users.add(user);
    }
}