package Backend.ICPC.Repositories;

import Backend.ICPC.Models.Problem;
import Backend.ICPC.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

//@NoRepositoryBean
public interface ProblemRepository<T extends Problem> extends JpaRepository<T, Long> {

    T findById(int id);

    @Query(value = "select * from problems where web_ori = ?1", nativeQuery = true)
    List<T> findByOrigin(String origin);

}
