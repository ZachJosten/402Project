package Backend.ICPC.Repositories;

import Backend.ICPC.Models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findByTeamId(int teamId);

    @Transactional
    void deleteByTeamId(int teamId);
}
