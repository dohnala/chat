package com.github.dohnal.chat.domain;

import javax.annotation.Nonnull;

import com.github.dohnal.chat.domain.protocol.ChatCommandResult;
import scala.concurrent.Future;

/**
 * @author dohnal
 */
public interface ChatService
{
    @Nonnull
    ChatCommandResult joinRoom(final @Nonnull String username);

    @Nonnull
    Future<ChatCommandResult> joinRoomAsync(final @Nonnull String username);

    @Nonnull
    ChatCommandResult sendMessage(final @Nonnull String username,
                                  final @Nonnull String message);

    @Nonnull
    Future<ChatCommandResult> sendMessageAsync(final @Nonnull String username,
                                               final @Nonnull String message);

    @Nonnull
    ChatCommandResult leaveRoom(final @Nonnull String username);

    @Nonnull
    Future<ChatCommandResult> leaveRoomAsync(final @Nonnull String username);

    @Nonnull
    ChatCommandResult kickUser(final @Nonnull String username,
                               final @Nonnull String kickedBy,
                               final @Nonnull String reason);

    @Nonnull
    Future<ChatCommandResult> kickUserAsync(final @Nonnull String username,
                                            final @Nonnull String kickedBy,
                                            final @Nonnull String reason);
}
