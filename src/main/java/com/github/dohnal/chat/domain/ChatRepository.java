package com.github.dohnal.chat.domain;

import javax.annotation.Nonnull;

/**
 * @author dohnal
 */
public interface ChatRepository
{
    @Nonnull
    ChatRoom getChatRoom();
}
