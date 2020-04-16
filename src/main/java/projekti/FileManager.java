
package projekti;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.AbstractPersistable;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileManager extends AbstractPersistable<Long> {
    private String name;
    private String contentType;
    private Long contentLength;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] profilePicture;
    
     // LISAA TAMA MYOHEMMIN HEROKUA VARTEN //@Type(type = "org.hibernate.type.BinaryType")
    
}