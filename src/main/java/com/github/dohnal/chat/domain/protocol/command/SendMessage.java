package com.github.dohnal.chat.domain.protocol.command;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.Objects;

/**
 * @author dohnal
 */
public final class SendMessage extends ChatCommand
{
    private final String username;

    private final String message;

    private final Date date;

    public SendMessage(final @Nonnull String username,
                       final @Nonnull String message,
                       final @Nonnull Date date)
    {
        super();

        this.username = username;
        this.message = message;
        this.date = date;
    }

    public String getUsername()
    {
        return username;
    }

    public String getMessage()
    {
        return message;
    }

    public Date getDate()
    {
        return date;
    }

    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj)
                && getClass() == obj.getClass()
                && Objects.equals(username, ((SendMessage) obj).username)
                && Objects.equals(message, ((SendMessage) obj).message)
                && Objects.equals(date, ((SendMessage) obj).date);
    }
}
