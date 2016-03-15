package com.github.dohnal.chat.model.protocol.commands;

import javax.annotation.Nonnull;
import java.util.Objects;

import com.github.dohnal.chat.model.protocol.ChatCommand;

/**
 * @author dohnal
 */
public final class KickUser extends ChatCommand
{
    private final String username;

    private final String kickedBy;

    private final String reason;

    public KickUser(final @Nonnull String username,
                    final @Nonnull String kickedBy,
                    final @Nonnull String reason)
    {
        super();

        this.username = username;
        this.kickedBy = kickedBy;
        this.reason = reason;
    }

    @Nonnull
    public String getUsername()
    {
        return username;
    }

    @Nonnull
    public String getKickedBy()
    {
        return kickedBy;
    }

    @Nonnull
    public String getReason()
    {
        return reason;
    }

    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj)
                && getClass() == obj.getClass()
                && Objects.equals(username, ((KickUser) obj).username)
                && Objects.equals(kickedBy, ((KickUser) obj).kickedBy)
                && Objects.equals(reason, ((KickUser) obj).reason);
    }
}
