package com.github.dohnal.chat.domain;

import javax.annotation.Nonnull;

import scala.concurrent.Future;

/**
 * @author dohnal
 */
public interface ChatRepository
{
    @Nonnull
    ChatRoom getChatRoom();

    @Nonnull
    Future<ChatRoom> getChatRoomAsync();

    void addEventListener(final @Nonnull ChatEventListener listener);

    void removeEventListener(final @Nonnull ChatEventListener listener);
}
