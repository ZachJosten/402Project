package Backend.ICPC.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity(name="users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="user_type", discriminatorType = DiscriminatorType.STRING)
public abstract class User {

    @ApiModelProperty(notes = "User ID", example = "1", required = true)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private int id;

    @ApiModelProperty(notes = "User's Name", example = "John Doe", required = true)
    private String name;

    @ApiModelProperty(notes = "User's Email Address", example = "johndoe@gmail.com", required = true)
    @Column(name = "email_address", unique = true)
    private String emailAddress;

    @ApiModelProperty(notes = "User's Password", example = "password123", required = true)
    @JsonIgnore
    private String password;

    @ApiModelProperty(notes = "Account info of user", example = "{\"id\":4,\"cfHandle\":\"Fefer_Ivan\",\"uHandle\":\"felix_halim\"}", required = false)
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    @PrimaryKeyJoinColumn
    @JsonIgnore
    private AccountInfo accountInfo;

    @ApiModelProperty(notes="Users School", required = false)
    @ManyToOne
    @JoinColumn(name = "school_id", nullable = true) // specifies the name of the foreign key column in users table
    @JsonIgnore
    private School school;

    public User()
    {
        accountInfo = null;
        school = null;
    }

    public User(String name, String emailAddress, String password, School school)
    {
        accountInfo = null;
        this.name = name;
        this.emailAddress = emailAddress;
        this.password = password;
        this.school = school;
    }

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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    @JsonIgnore
    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}
