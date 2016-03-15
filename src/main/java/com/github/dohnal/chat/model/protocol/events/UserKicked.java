package com.github.dohnal.chat.model.protocol.events;

import javax.annotation.Nonnull;
import java.util.Objects;

import com.github.dohnal.chat.model.protocol.ChatEvent;

/**
 * @author dohnal
 */
public final class UserKicked extends ChatEvent
{
    private final String username;

    public UserKicked(final @Nonnull String username)
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
                && Objects.equals(username, ((UserKicked) obj).username);
    }
}
