package turbo.repository;

import org.springframework.data.repository.Repository;
import turbo.model.Links;

import java.util.List;

public interface LinkRepository extends Repository<Links, Integer> {
    void save(Links link);
    boolean existsByIdAchievementAndIdUser(Integer idAchievement, Integer idUser);
    List<Integer> getAllByIdUser(Integer idUsuer);
}