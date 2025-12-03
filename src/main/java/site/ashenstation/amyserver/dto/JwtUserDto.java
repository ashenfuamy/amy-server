package site.ashenstation.amyserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import site.ashenstation.amyserver.entity.UserPo;

import java.util.Collection;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
public class JwtUserDto implements UserDetails {

    private final UserPo user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.getLocked();
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
