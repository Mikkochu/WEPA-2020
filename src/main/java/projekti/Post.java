
package projekti;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Post extends AbstractPersistable<Long>{
    private String text;
    
    private LocalDateTime postDate = LocalDateTime.now();
    
    @ManyToOne
    private Account account; //kayttaja voi tehda monta postausta, postaus liittyy vain yhteen tiliin
    
    @OneToMany(mappedBy = "post") // postauksessa voi olla useita eri tykkäyksiä, tykkäys liittyy vain yhteen postaukseen
    private List<PostLike> likes = new ArrayList<>();
    
    @OneToMany(mappedBy = "post") // postauksessa voi olla useita eri kommentteja, kommentti liittyy vain yhteen postaukseen
    private List<Comment> comments = new ArrayList<>();

    
    
}
