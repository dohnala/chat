package com.github.dohnal.chat.domain.protocol.query;

import com.github.dohnal.chat.domain.protocol.ChatMessage;

/**
 * @author dohnal
 */
public abstract class ChatQuery extends ChatMessage
{
    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj) && getClass() == obj.getClass();
    }
}
