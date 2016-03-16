package com.github.dohnal.chat.domain.protocol.query;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Objects;

import com.github.dohnal.chat.domain.ChatMessage;

/**
 * @author dohnal
 */
public abstract class ChatQueryResult<T extends Serializable> extends ChatMessage
{
    protected final T result;

    public ChatQueryResult(final @Nonnull T result)
    {
        this.result = result;
    }

    @Nonnull
    public T getResult()
    {
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj)
                && getClass() == obj.getClass()
                && Objects.equals(result, ((ChatQueryResult)obj).result);
    }
}
