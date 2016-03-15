package com.github.dohnal.chat.model.protocol.commands;

import javax.annotation.Nonnull;
import java.util.Objects;

import com.github.dohnal.chat.model.protocol.ChatCommand;

/**
 * @author dohnal
 */
public final class SendMessage extends ChatCommand
{
    private final String username;

    private final String message;

    public SendMessage(final @Nonnull String username,
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
                && Objects.equals(username, ((SendMessage) obj).username);
    }
}
