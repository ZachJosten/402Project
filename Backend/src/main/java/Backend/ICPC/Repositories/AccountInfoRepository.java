package Backend.ICPC.Repositories;

import Backend.ICPC.Models.AccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountInfoRepository extends JpaRepository<AccountInfo, Long> {
    AccountInfo findById(int id);
}
