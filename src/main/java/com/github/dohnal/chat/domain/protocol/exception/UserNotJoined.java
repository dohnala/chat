package com.github.dohnal.chat.domain.protocol.exception;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author dohnal
 */
public final class UserNotJoined extends ChatException
{
    private String username;

    public UserNotJoined(final @Nonnull String username)
    {
        super();

        this.username = username;
    }

    @Nonnull
    public String getUsername()
    {
        return username;
    }

    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj)
                && getClass() == obj.getClass()
                && Objects.equals(username, ((UserNotJoined) obj).username);
    }
}
