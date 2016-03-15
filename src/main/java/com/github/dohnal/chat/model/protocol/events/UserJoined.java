package com.github.dohnal.chat.model.protocol.events;

import javax.annotation.Nonnull;

import com.github.dohnal.chat.model.protocol.ChatEvent;
import com.google.common.base.Objects;

/**
 * @author dohnal
 */
public final class UserJoined extends ChatEvent
{
    private final String username;

    public UserJoined(final @Nonnull String username)
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
        return super.equals(obj) &&
                getClass() == obj.getClass() &&
                Objects.equal(username, ((UserJoined) obj).username);
    }
}
