package com.github.dohnal.chat.domain;

import javax.annotation.Nonnull;

/**
 * @author dohnal
 */
public interface ChatService
{
    void joinRoom(final @Nonnull String username);

    void sendMessage(final @Nonnull String username, final @Nonnull String message);

    void leaveRoom(final @Nonnull String username);

    void kickUser(final @Nonnull String username);
}
