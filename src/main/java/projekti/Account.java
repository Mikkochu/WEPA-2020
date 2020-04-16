
package projekti;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account extends AbstractPersistable<Long>{
    private String name;
    //private List<Skill> skills = new ArrayList<>();
    // Mieti miten saadaan taidot per henkilo
    
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] profilePicture;

}
