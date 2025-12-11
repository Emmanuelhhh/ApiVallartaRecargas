package com.tde.apiVallartaRecargas.security;

import java.io.Serializable;

public class JwtUserPrincipal implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long userId;
    private final String username;

    public JwtUserPrincipal(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}

