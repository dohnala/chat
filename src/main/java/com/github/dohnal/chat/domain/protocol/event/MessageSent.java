package com.github.dohnal.chat.domain.protocol.event;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.Objects;

/**
 * @author dohnal
 */
public final class MessageSent extends ChatEvent
{
    private final String username;

    private final String message;

    private final Date date;

    public MessageSent(final @Nonnull String username,
                       final @Nonnull String message,
                       final @Nonnull Date date)
    {
        super();

        this.username = username;
        this.message = message;
        this.date = date;
    }

    @Nonnull
    public String getUsername()
    {
        return username;
    }

    @Nonnull
    public String getMessage()
    {
        return message;
    }

    @Nonnull
    public Date getDate()
    {
        return date;
    }

    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj)
                && getClass() == obj.getClass()
                && Objects.equals(username, ((MessageSent) obj).username)
                && Objects.equals(message, ((MessageSent) obj).message)
                && Objects.equals(date, ((MessageSent) obj).date);
    }
}
