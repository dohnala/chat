package com.github.dohnal.chat.domain.protocol.query;

/**
 * @author dohnal
 */
public final class GetChatRoom extends ChatQuery
{
    @Override
    public boolean equals(Object obj)
{
    return super.equals(obj) && getClass() == obj.getClass();
}
}
