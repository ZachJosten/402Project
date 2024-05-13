package Backend.ICPC.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Backend.ICPC.Models.Levels.LevelKey;
import Backend.ICPC.Models.Levels.Levels;

public interface LevelRepository extends JpaRepository<Levels, Long> {
    Levels findById(LevelKey id);

    List<Levels> findByIdLevelNum(int level_num);

    List<Levels> findByIdSchoolId(int school_id);
}
