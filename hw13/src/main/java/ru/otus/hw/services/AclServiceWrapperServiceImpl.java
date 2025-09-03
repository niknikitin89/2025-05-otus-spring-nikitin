package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AclServiceWrapperServiceImpl implements AclServiceWrapperService {

    private final MutableAclService mutableAclService;

    @Override
    public void createPermission(Object object, Permission permission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Sid owner =
//                new GrantedAuthoritySid("ROLE_USER");
                //authentication.getAuthorities().toString());
                new PrincipalSid(authentication);
        ObjectIdentity oid = new ObjectIdentityImpl(object);

        final Sid admin = new GrantedAuthoritySid("ROLE_ADMIN");

        MutableAcl acl;
        try {
            // Пытаемся получить существующий ACL
            acl = (MutableAcl) mutableAclService.readAclById(oid);
        } catch (NotFoundException e) {
            // Если ACL не найден - создаем новый
            acl = mutableAclService.createAcl(oid);
        }

        acl.insertAce(acl.getEntries().size(), permission, owner, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, admin, true);
        mutableAclService.updateAcl(acl);
    }

}
