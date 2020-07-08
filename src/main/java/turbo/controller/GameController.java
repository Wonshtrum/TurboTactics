package turbo.controller;

import org.springframework.web.bind.annotation.PathVariable;
import turbo.game.Manager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpSession;

import turbo.util.Tools;

@Controller
public class GameController {
    static String loginPage = "redirect:/login";
    static String homePage = "redirect:/home";

    @GetMapping({"/game", "/game/{idGame}"})
    public String getGame(@PathVariable(required = false) String idGame, Model model, HttpSession session) {
        if (idGame != null && Tools.checkAutenticated(session) && Manager.getInstance().existGame(idGame)) {
            model.addAttribute("idGame", idGame);
            return "game.html";
        } else {
            return homePage;
        }
    }

}