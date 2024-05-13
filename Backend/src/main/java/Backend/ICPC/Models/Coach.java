package Backend.ICPC.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import javax.persistence.*;

@Entity
@DiscriminatorValue("coach")
public class Coach extends User {

    @ApiModelProperty(notes="Coachs Teams", example="[{\"teamId\":1,\"coach\":{\"id\":1,\"name\":\"John Doe\",\"emailAddress\":\"johndoe@gmail.com\"},\"students\":[{\"id\":3,\"name\":\"Jack\",\"emailAddress\":\"jack@gmail.com\",\"sid\":352},{\"id\":4,\"name\":\"Jill\",\"emailAddress\":\"jill@gmail.com\",\"sid\":621}]}]", required = false)
    @OneToMany(mappedBy = "coach", cascade = CascadeType.PERSIST, orphanRemoval = false)
    @JsonIgnore
    private List<Team> teams;

    public Coach()
    {
        super();
        teams = null;
    }

    // This constructor should always be used in practice (requires School)
    public Coach(String name, String emailAddress, String password, School school) {
        super(name, emailAddress, password, school);
        teams = null;
    }

    @JsonIgnore
    public List<Team> getTeams() {
        return teams;
    }

    public void addTeam(Team team) {
        this.teams.add(team);
    }
}
