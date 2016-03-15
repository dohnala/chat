package com.github.dohnal.chat.model.protocol.commands;

import javax.annotation.Nonnull;
import java.util.Objects;

import com.github.dohnal.chat.model.protocol.ChatCommand;

/**
 * @author dohnal
 */
public final class LeaveRoom extends ChatCommand
{
    private final String username;

    public LeaveRoom(final @Nonnull String username)
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
                && Objects.equals(username, ((LeaveRoom) obj).username);
    }
}
