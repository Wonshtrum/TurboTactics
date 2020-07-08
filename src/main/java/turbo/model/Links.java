package turbo.model;

import javax.persistence.*;

@Entity
public class Links {
    private Integer id;
    private Integer idUser;
    private Integer idAchievement;

    public Links(Integer idUser, Integer idAchievement) {
        this.idUser = idUser;
        this.idAchievement = idAchievement;
    }

    public Links() {}

    @Id
    @GeneratedValue
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Links links = (Links) o;

        if (id != null ? !id.equals(links.id) : links.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Column(name = "id_user")
    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    @Column(name = "id_achievement")
    public Integer getIdAchievement() {
        return idAchievement;
    }

    public void setIdAchievement(Integer idAchievement) {
        this.idAchievement = idAchievement;
    }
}
