package com.github.dohnal.chat.domain.protocol.event;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author dohnal
 */
public final class UserLeft extends ChatEvent
{
    private final String username;

    public UserLeft(final @Nonnull String username)
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
                && Objects.equals(username, ((UserLeft) obj).username);
    }
}
