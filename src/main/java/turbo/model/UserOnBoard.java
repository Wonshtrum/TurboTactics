package turbo.model;

import turbo.repository.Repositories;

import java.util.ArrayList;
import java.util.List;

public class UserOnBoard {
    public String login;
    public int score;
    public List<Achievements> achievements;
    public UserOnBoard(String login, int score, List<Achievements> achievements) {
        this.login = login;
        this.score = score;
        this.achievements = new ArrayList<>();
        this.achievements.add(Repositories.getAchievements().getById(1));
    }
}
