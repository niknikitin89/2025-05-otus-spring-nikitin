package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AclServiceWrapperServiceImpl implements AclServiceWrapperService {

    private final MutableAclService mutableAclService;

    @Override
    public void createPermissionForAuthority(String authority, Object object, Permission permission) {

        final Sid sid = new GrantedAuthoritySid(authority);
        ObjectIdentity oid = new ObjectIdentityImpl(object);

        MutableAcl acl = getAcl(oid);

        acl.insertAce(acl.getEntries().size(), permission, sid, true);
        mutableAclService.updateAcl(acl);
    }

    @Override
    public void createPermissionForUser(Object object, Permission permission) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Sid sid = new PrincipalSid(authentication);
        ObjectIdentity oid = new ObjectIdentityImpl(object);

        MutableAcl acl = getAcl(oid);

        acl.insertAce(acl.getEntries().size(), permission, sid, true);
        mutableAclService.updateAcl(acl);
    }

    @Override
    public boolean hasPermission(Object object, Permission permission) {

        List<Sid> sids = getCurrentUserSids();
        ObjectIdentity oid = new ObjectIdentityImpl(object);

        try {
            Acl acl = mutableAclService.readAclById(oid);
            return acl.isGranted(Arrays.asList(permission), sids, false);
        } catch (NotFoundException e) {
            return false;
        }
    }

    private List<Sid> getCurrentUserSids() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Sid> sids = new ArrayList<>();

        if (authentication == null || !authentication.isAuthenticated()) {
            return sids;
        }

        //Пользователь
        sids.add(new PrincipalSid(authentication));
        //Его роли
        authentication.getAuthorities().forEach(a ->
                sids.add(new GrantedAuthoritySid(a)));

        return sids;

    }

    private MutableAcl getAcl(ObjectIdentity oid) {
        try {
            // Пытаемся получить существующий ACL
            return (MutableAcl) mutableAclService.readAclById(oid);
        } catch (NotFoundException e) {
            // Если ACL не найден - создаем новый
            return mutableAclService.createAcl(oid);
        }
    }
}
