package com.github.dohnal.chat.domain.protocol.query;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Objects;

import com.github.dohnal.chat.domain.protocol.ChatMessage;

/**
 * @author dohnal
 */
public abstract class ChatQueryResult<T extends Serializable> extends ChatMessage
{
    protected final T value;

    public ChatQueryResult(final @Nonnull T result)
    {
        this.value = result;
    }

    @Nonnull
    public T getValue()
    {
        return value;
    }

    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj)
                && getClass() == obj.getClass()
                && Objects.equals(value, ((ChatQueryResult)obj).value);
    }
}
