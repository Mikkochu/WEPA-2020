
package projekti;

import javax.persistence.Entity;
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
    // Tahan joku lista jossa nakyy ketka on tykannyt. Jos on listassa niin ei saa tykata uudestaan
    
    
    
}
