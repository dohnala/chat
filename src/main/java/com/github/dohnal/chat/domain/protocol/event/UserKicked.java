package com.github.dohnal.chat.domain.protocol.event;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author dohnal
 */
public final class UserKicked extends ChatEvent
{
    private final String username;

    private final String kickedBy;

    private final String reason;

    public UserKicked(final @Nonnull String username,
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
                && Objects.equals(username, ((UserKicked) obj).username)
                && Objects.equals(kickedBy, ((UserKicked) obj).kickedBy)
                && Objects.equals(reason, ((UserKicked) obj).reason);
    }
}
