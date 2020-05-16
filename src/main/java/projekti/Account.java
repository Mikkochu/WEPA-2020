
package projekti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractPersistable<Long>{
    private String name;
    private String username;
    private String password;
    private String title;
    
    @OneToMany(mappedBy = "account")
    private List<Skill> skills = new ArrayList<>();
        
    @ManyToMany
    private List<Account> sentInvites = new ArrayList<>();
    
    @ManyToMany
    private List<Account> receivedInvites = new ArrayList<>();
        
    @ManyToMany
    private List<Account> connections = new ArrayList<>();
 
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] profilePicture;

}
