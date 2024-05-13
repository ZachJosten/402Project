package Backend.ICPC.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
public class AccountInfo {

    @ApiModelProperty(notes = "ID of user", example = "1", required = true)
    @Id
    @Column(name = "user_id")
    private int id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ApiModelProperty(notes = "CodeForces handle", example = "Fefer_Ivan", required = false)
    private String cfHandle;

    @ApiModelProperty(notes = "uHunt handle", example = "felix_halim", required = false)
    private String uHandle;

    @ApiModelProperty(notes = "Kattis handle", example = "to-do", required = false)
    private String kHandle;

    public AccountInfo()
    {

    }

    public AccountInfo(String cfHandle, String uHandle)
    {
        this.cfHandle = cfHandle;
        this.uHandle = uHandle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCfHandle() {
        return cfHandle;
    }

    public void setCfHandle(String cfHandle) {
        this.cfHandle = cfHandle;
    }

    public String getuHandle() {
        return uHandle;
    }

    public void setuHandle(String uHandle) {
        this.uHandle = uHandle;
    }

    public String getkHandle() {
        return kHandle;
    }

    public void setkHandle(String kHandle) {
        this.kHandle = kHandle;
    }
}
