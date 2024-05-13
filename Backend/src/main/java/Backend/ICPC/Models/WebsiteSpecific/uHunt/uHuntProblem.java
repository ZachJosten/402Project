package Backend.ICPC.Models.WebsiteSpecific.uHunt;

import Backend.ICPC.Models.Problem;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("uHunt")
public class uHuntProblem extends Problem {

    @ApiModelProperty(notes="The id on Online Judge", example="36", required = true)
    private int uid;

    @ApiModelProperty(notes="The number of the problem", example="100", required=true)
    private int numP;


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getNumP() {
        return numP;
    }

    public void setNumP(int numP) {
        this.numP = numP;
    }
}
