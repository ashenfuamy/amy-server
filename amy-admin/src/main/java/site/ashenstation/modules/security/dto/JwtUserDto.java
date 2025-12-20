package site.ashenstation.modules.security.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import site.ashenstation.entity.Admin;

import java.io.Serializable;
import java.util.Collection;

@RequiredArgsConstructor
@Data
public class JwtUserDto implements UserDetails, Serializable {

    private final Admin admin;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    @Override
    public String getUsername() {
        return admin.getUsername();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !admin.getLocked();
    }
}
