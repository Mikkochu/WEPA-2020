
package projekti;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>{
    List<Post> findByAccount(Account account);
    List<Post> findByAccountIn(List<Account> connections, Pageable pageable);

}
