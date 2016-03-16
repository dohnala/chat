package com.github.dohnal.chat.domain.protocol.event;

import com.github.dohnal.chat.domain.protocol.ChatMessage;

/**
 * @author dohnal
 */
public abstract class ChatEvent extends ChatMessage
{
    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj) && getClass() == obj.getClass();
    }
}
