package com.github.dohnal.chat.model.protocol;

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
