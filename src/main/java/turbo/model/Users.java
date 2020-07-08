package turbo.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Users {
    private Integer id;
    private String login;
    private String password;
    private BigDecimal score;

    public Users(String login, String password, int score) {
        this.login = login;
        this.password = password;
        this.score = BigDecimal.valueOf(score);
    }

    public Users(UserForm loginForm) {
        this.login = loginForm.getLogin();
        this.password = loginForm.getPassword();
        this.score = BigDecimal.valueOf(0);
    }

    public Users() {}

    @Id
    @GeneratedValue
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "login")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "score")
    public int getScore() {
        return score.intValue();
    }

    public void setScore(int score) {
        this.score = BigDecimal.valueOf(score);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Users that = (Users) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (score != null ? !score.equals(that.score) : that.score != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (score != null ? score.hashCode() : 0);
        return result;
    }
}
