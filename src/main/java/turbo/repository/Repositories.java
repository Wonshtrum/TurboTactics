package turbo.repository;

import org.springframework.stereotype.Component;
import turbo.model.Links;
import turbo.model.UserOnBoard;
import turbo.model.Users;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Repositories {
    private static UserRepository userRepository;
    private static AchievementRepository achievementRepository;
    private static LinkRepository linkRepository;

    public static UserRepository getUsers() { return userRepository; }
    public static AchievementRepository getAchievements() { return achievementRepository; }
    public static LinkRepository getLinks() { return linkRepository; }

    public Repositories(UserRepository userRepository, AchievementRepository achievementRepository, LinkRepository linkRepository) {
        Repositories.userRepository = userRepository;
        Repositories.achievementRepository = achievementRepository;
        Repositories.linkRepository = linkRepository;
        System.out.println("REPOSITORY");
    }

    public static void updateScore(String login, int score) {
        Users player = userRepository.getByLogin(login);
        if (player.getScore() < score) {
            player.setScore(score);
            userRepository.save(player);
        }
    }

    public static void addAchievement(String login, String ref) {
        Integer idUser = userRepository.getByLogin(login).getId();
        Integer idAchievement = achievementRepository.getByRef(ref).getId();
        if (!linkRepository.existsByIdAchievementAndIdUser(idAchievement, idUser)) {
            linkRepository.save(new Links(idUser, idAchievement));
        }
    }

    public static List<UserOnBoard> getBoard() {
        List<Users> users = userRepository.findAllByOrderByScoreDesc();
        return users.stream().map(e -> new UserOnBoard(e.getLogin(), e.getScore(), null)).collect(Collectors.toList());
        /*List<UserOnBoard> usersOnBoard = users.stream().map(e -> new UserOnBoard(e.getLogin(), e.getScore(), linkRepository.getAllByIdUser(e.getId()).stream().map(a -> achievementRepository.getById(a)).collect(Collectors.toList()))).collect(Collectors.toList());
        return usersOnBoard;*/
    }
}
