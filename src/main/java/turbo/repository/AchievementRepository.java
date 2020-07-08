package turbo.repository;

import org.springframework.data.repository.Repository;
import turbo.model.Achievements;

public interface AchievementRepository extends Repository<Achievements, Integer> {
    Achievements getByRef(String ref);
    Achievements getById(Integer id);
}