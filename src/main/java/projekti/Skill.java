
package projekti;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Skill extends AbstractPersistable<Long>{
    private String name;
    private Integer likes;
    
    @ManyToOne
    private Account account;

    
    
    
}
