package site.ashenstation.base;

import com.mybatisflex.annotation.ColumnMask;
import com.mybatisflex.core.mask.Masks;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public abstract class BaseUser {
    private String username;
    @ColumnMask(Masks.PASSWORD)
    private String password;
    private String avatarPath;
    private Date lastLogin;
    private Boolean locked;
}
