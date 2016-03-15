package com.github.dohnal.chat.model;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;

import com.github.dohnal.chat.model.protocol.ChatException;

/**
 * @author dohnal
 */
public class ChatRoomException extends Exception
{
    private final ChatException[] exceptions;

    public ChatRoomException(final @Nonnull ChatException... exceptions)
    {
        this.exceptions = exceptions;
    }

    public Collection<ChatException> getExceptions()
    {
        return Arrays.asList(exceptions);
    }
}
