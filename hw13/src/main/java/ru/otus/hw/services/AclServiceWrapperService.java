package ru.otus.hw.services;

import org.springframework.security.acls.model.Permission;
import ru.otus.hw.models.Book;

public interface AclServiceWrapperService {

    void createPermissionForAuthority(String authority, Object object, Permission permission);

    void createPermissionForUser(Object object, Permission permission);

    boolean hasPermission(Object object, Permission permission);
}
