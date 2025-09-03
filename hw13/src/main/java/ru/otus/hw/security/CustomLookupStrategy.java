package ru.otus.hw.security;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AuditLogger;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.Sid;

import javax.sql.DataSource;

public class CustomLookupStrategy extends BasicLookupStrategy {

    public CustomLookupStrategy(DataSource dataSource, AclCache aclCache,
                                AclAuthorizationStrategy aclAuthorizationStrategy,
                                AuditLogger auditLogger) {
        super(dataSource, aclCache, aclAuthorizationStrategy, auditLogger);
    }

@Override
    protected Sid createSid(boolean isPrincipal, String sid) {
    try {
        return super.createSid(isPrincipal, sid);
    } catch (DuplicateKeyException e) {
        // Игнорируем ошибку дубликата и возвращаем существующий SID
        return isPrincipal ?
                new PrincipalSid(sid) :
                new GrantedAuthoritySid(sid);
    }
    }

}
