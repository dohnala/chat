package com.github.dohnal.chat.domain.protocol.exception;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author dohnal
 */
public final class UserAlreadyJoined extends ChatException
{
    private String username;

    public UserAlreadyJoined(final @Nonnull String username)
    {
        super();

        this.username = username;
    }

    public String getUsername()
    {
        return username;
    }

    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj)
                && getClass() == obj.getClass()
                && Objects.equals(username, ((UserAlreadyJoined) obj).username);
    }
}
