package com.github.dohnal.chat.domain.protocol.query;

import javax.annotation.Nonnull;

import com.github.dohnal.chat.domain.ChatRoom;

/**
 * @author dohnal
 */
public final class GetChatRoomResult extends ChatQueryResult<ChatRoom>
{
    public GetChatRoomResult(final @Nonnull ChatRoom result)
    {
        super(result);
    }

    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj) && getClass() == obj.getClass();
    }
}
