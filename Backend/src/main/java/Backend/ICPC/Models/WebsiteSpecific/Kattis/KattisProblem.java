package Backend.ICPC.Models.WebsiteSpecific.Kattis;

import Backend.ICPC.Models.Problem;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Kattis")
public class KattisProblem extends Problem {

    @ApiModelProperty(notes = "ID for problem on Kattis", example = "")
    private int katId;

    public int getKatId() {
        return katId;
    }

    public void setKatId(int katId) {
        this.katId = katId;
    }
}
