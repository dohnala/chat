package com.github.dohnal.chat.domain.protocol.event;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.Objects;

import com.github.dohnal.chat.domain.protocol.ChatMessage;

/**
 * @author dohnal
 */
public abstract class ChatEvent extends ChatMessage
{
    private final Date date;

    public ChatEvent(final @Nonnull Date date)
    {
        super();

        this.date = date;
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
                && Objects.equals(date, ((ChatEvent)obj).date);
    }
}
