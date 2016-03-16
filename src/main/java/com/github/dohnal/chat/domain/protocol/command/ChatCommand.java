package com.github.dohnal.chat.domain.protocol.command;

import com.github.dohnal.chat.domain.protocol.ChatMessage;

/**
 * @author dohnal
 */
public abstract class ChatCommand extends ChatMessage
{
    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj) && getClass() == obj.getClass();
    }
}
