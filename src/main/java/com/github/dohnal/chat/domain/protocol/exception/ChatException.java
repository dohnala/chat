package com.github.dohnal.chat.domain.protocol.exception;

import com.github.dohnal.chat.domain.protocol.ChatMessage;

/**
 * @author dohnal
 */
public abstract class ChatException extends ChatMessage
{
    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj) && getClass() == obj.getClass();
    }
}
