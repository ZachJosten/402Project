package Backend.ICPC.Models.WebsiteSpecific.Codeforces;

import Backend.ICPC.Models.Problem;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Codeforces")
public class CodeforcesProblem extends Problem {

    @ApiModelProperty(notes="ID for contest in which problem was present", example="1932", required=false)
    private int conId;

    @ApiModelProperty(notes="Index for the contest", example="E", required = true)
    private String ind;


    public int getConId() {
        return conId;
    }

    public void setConId(int conId) {
        this.conId = conId;
    }

    public String getInd() {
        return ind;
    }

    public void setInd(String ind) {
        this.ind = ind;
    }

}
