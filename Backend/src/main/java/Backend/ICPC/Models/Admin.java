package Backend.ICPC.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@DiscriminatorValue("admin")
public class Admin extends User {

    public Admin() {
        super();
    }

    public Admin(String name, String emailAddress, String password, School school) {
        super(name, emailAddress, password, school);
    }
}