package gov.healthit.chpl.permissions.domains.secureduser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.stereotype.Component;

import gov.healthit.chpl.auth.Util;
import gov.healthit.chpl.auth.dto.UserDTO;
import gov.healthit.chpl.permissions.domains.ActionPermissions;

@Component("securedUserGetByUserNameActionPermisions")
public class GetByUserNameActionPermissions extends ActionPermissions {
    @Autowired
    private PermissionEvaluator permissionEvaluator;

    @Override
    public boolean hasAccess() {
        return false;
    }

    // Original Security:
    // @PostAuthorize("hasAnyRole('ROLE_INVITED_USER_CREATOR',
    // 'ROLE_USER_AUTHENTICATOR', 'ROLE_ADMIN', 'ROLE_ONC', 'ROLE_ACB',
    // 'ROLE_ATL') or hasPermission(returnObject, 'read') or
    // hasPermission(returnObject, admin)")
    @Override
    public boolean hasAccess(Object obj) {
        if (!(obj instanceof UserDTO)) {
            return false;
        } else {
            return getResourcePermissions().isUserRoleAdmin() || getResourcePermissions().isUserRoleOnc()
                    || getResourcePermissions().isUserRoleAcbAdmin() || getResourcePermissions().isUserRoleAtlAdmin()
                    || getResourcePermissions().isUserRoleUserAuthenticator()
                    || getResourcePermissions().isUserRoleInvitedUserCreator()
                    || permissionEvaluator.hasPermission(Util.getCurrentUser(), (UserDTO) obj,
                            BasePermission.ADMINISTRATION)
                    || permissionEvaluator.hasPermission(Util.getCurrentUser(), (UserDTO) obj, BasePermission.READ);
        }
    }

}
