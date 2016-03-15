package com.github.dohnal.chat.model.protocol.events;

import javax.annotation.Nonnull;
import java.util.Objects;

import com.github.dohnal.chat.model.protocol.ChatEvent;

/**
 * @author dohnal
 */
public final class MessageSent extends ChatEvent
{
    private final String username;

    private final String message;

    public MessageSent(final @Nonnull String username,
                       final @Nonnull String message)
    {
        super();

        this.username = username;
        this.message = message;
    }

    public String getUsername()
    {
        return username;
    }

    public String getMessage()
    {
        return message;
    }

    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj)
                && getClass() == obj.getClass()
                && Objects.equals(username, ((MessageSent) obj).username)
                && Objects.equals(message, ((MessageSent) obj).message);
    }
}
