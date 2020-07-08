package turbo.model;

import turbo.repository.Repositories;

public class UserForm {
    private String login;
    private String password;
    public static final String errorLogin = "Login ou mot de passe invalide";
    public static final String errorSignin = "Login déjà utilisé par un utilisateur";

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean validateLogin() {
        return Repositories.getUsers().existsByLoginAndPassword(this.login, this.password);
    }

    public boolean validateSignin() {
        return !Repositories.getUsers().existsByLogin(this.login);
    }
}
