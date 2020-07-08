package turbo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import turbo.repository.Repositories;
import turbo.game.Manager;
import turbo.model.UserForm;
import turbo.model.Users;
import turbo.util.Tools;

import javax.servlet.http.HttpSession;

@Controller
public class OtherControllers {

    @GetMapping("/signin")
    public String getSignin(Model model) {
        UserForm signinForm = new UserForm();
        model.addAttribute("signinForm", signinForm);
        return "signin.html";
    }

    @PostMapping("/signin")
    public String postSignin(Model model, @ModelAttribute("signinForm") UserForm signinForm) {
        if (signinForm.validateSignin()) {
            Repositories.getUsers().save(new Users(signinForm));
            return GameController.homePage;
        }
        model.addAttribute("errorMessage", UserForm.errorSignin);
        return "signin.html";
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        UserForm loginForm = new UserForm();
        model.addAttribute("loginForm", loginForm);
        return "login.html";
    }

    @PostMapping("/login")
    public String postLogin(Model model, @ModelAttribute("loginForm") UserForm loginForm, HttpSession session) {
        if (loginForm.validateLogin()) {
            session.setAttribute("loggedInAs", loginForm.getLogin());
            return GameController.homePage;
        }
        model.addAttribute("errorMessage", UserForm.errorLogin);
        return "login.html";
    }

    @GetMapping("/home")
    public String getCreate() {
        return "home.html";
    }

    @GetMapping("/newgame")
    public String getNewGame(HttpSession session) {
        if (Tools.checkAutenticated(session)) {
            String idGame = Manager.getInstance().createGame();
            Repositories.addAchievement((String)session.getAttribute("loggedInAs"), "create_party");
            return "redirect:/game/" + idGame;
        } else {
            return GameController.loginPage;
        }
    }

    @GetMapping("/board")
    public String getBoard(Model model) {
        model.addAttribute("users", Repositories.getBoard());
        return "leaderBoard.html";
    }

    @GetMapping("/disconnection")
    public String getDisconnected(HttpSession session) {
        session.removeAttribute("loggedInAs");
        return GameController.homePage;
    }

}