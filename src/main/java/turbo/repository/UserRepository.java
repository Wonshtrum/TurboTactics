package turbo.repository;

import org.springframework.data.repository.Repository;
import turbo.model.Users;

import java.util.List;

public interface UserRepository extends Repository<Users, Integer> {
    void save(Users user);
    List<Users> findAllByOrderByScoreDesc();
    boolean existsByLoginAndPassword(String login, String password);
    boolean existsByLogin(String login);
    Users getByLogin(String login);
}