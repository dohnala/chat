package com.github.dohnal.chat.domain.protocol.event;

import javax.annotation.Nonnull;
import java.util.Date;

import com.google.common.base.Objects;

/**
 * @author dohnal
 */
public final class UserJoined extends ChatEvent
{
    private final String username;

    public UserJoined(final @Nonnull String username,
                      final @Nonnull Date date)
    {
        super(date);

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
        return super.equals(obj) &&
                getClass() == obj.getClass() &&
                Objects.equal(username, ((UserJoined) obj).username);
    }
}
