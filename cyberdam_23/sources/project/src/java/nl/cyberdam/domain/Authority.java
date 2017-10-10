package nl.cyberdam.domain;

import org.acegisecurity.GrantedAuthority;

/**
 * implement the GrantedAuthority interface of acegi
 */
public enum Authority implements GrantedAuthority {

    ROLE_PARTICIPANT, // NOTE: deprecated, not used anywhere!
    ROLE_GAMESESSIONMASTER,
    ROLE_GAMEMANIFESTCOMPOSER,
    ROLE_GAMEAUTHOR,
    ROLE_PLAYGROUNDAUTHOR,
    ROLE_LCMSADMININSTRATOR,
    ROLE_LMSADMINISTRATOR,
    ROLE_VLEADMINISTRATOR,
    ROLE_USERADMINISTRATOR,
    ROLE_SYSTEMADMINISTRATOR;
    
    public String getAuthority() {
        return toString();
    }
}
