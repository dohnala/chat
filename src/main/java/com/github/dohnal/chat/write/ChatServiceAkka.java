package com.github.dohnal.chat.write;

import javax.annotation.Nonnull;

import akka.japi.Util;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.github.dohnal.chat.ChatRuntime;
import com.github.dohnal.chat.domain.ChatService;
import com.github.dohnal.chat.domain.protocol.ChatCommandResult;
import com.github.dohnal.chat.domain.protocol.command.ChatCommand;
import com.github.dohnal.chat.domain.protocol.command.JoinRoom;
import com.github.dohnal.chat.domain.protocol.command.KickUser;
import com.github.dohnal.chat.domain.protocol.command.LeaveRoom;
import com.github.dohnal.chat.domain.protocol.command.SendMessage;
import com.github.dohnal.chat.util.Loggable;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

/**
 * @author dohnal
 */
public class ChatServiceAkka implements ChatService
{
    private final FiniteDuration duration = Duration.create(3, "seconds");
    private final Timeout timeout = new Timeout(duration);

    private final ChatRuntime chatRuntime;

    public ChatServiceAkka(final @Nonnull ChatRuntime chatRuntime)
    {
        this.chatRuntime = chatRuntime;
    }

    @Nonnull
    @Loggable
    @Override
    public ChatCommandResult joinRoom(final @Nonnull String username)
    {
        return waitForResult(joinRoomAsync(username));
    }

    @Nonnull
    @Override
    public Future<ChatCommandResult> joinRoomAsync(final @Nonnull String username)
    {
        return sendCommandAsync(new JoinRoom(username));
    }

    @Nonnull
    @Loggable
    @Override
    public ChatCommandResult sendMessage(final @Nonnull String username,
                                         final @Nonnull String message)
    {
        return waitForResult(sendMessageAsync(username, message));
    }

    @Nonnull
    @Override
    public Future<ChatCommandResult> sendMessageAsync(final @Nonnull String username,
                                                      final @Nonnull String message)
    {
        return sendCommandAsync(new SendMessage(username, message));
    }

    @Nonnull
    @Loggable
    @Override
    public ChatCommandResult leaveRoom(final @Nonnull String username)
    {
        return waitForResult(leaveRoomAsync(username));
    }

    @Nonnull
    @Override
    public Future<ChatCommandResult> leaveRoomAsync(final @Nonnull String username)
    {
        return sendCommandAsync(new LeaveRoom(username));
    }

    @Nonnull
    @Loggable
    @Override
    public ChatCommandResult kickUser(final @Nonnull String username,
                                      final @Nonnull String kickedBy,
                                      final @Nonnull String reason)
    {
        return waitForResult(kickUserAsync(username, kickedBy, reason));
    }

    @Nonnull
    @Override
    public Future<ChatCommandResult> kickUserAsync(final @Nonnull String username,
                                                   final @Nonnull String kickedBy,
                                                   final @Nonnull String reason)
    {
        return sendCommandAsync(new KickUser(username, kickedBy, reason));
    }

    @Nonnull
    protected Future<ChatCommandResult> sendCommandAsync(final @Nonnull ChatCommand command)
    {
        return Patterns
                .ask(chatRuntime.getChatRoom(), command, timeout)
                .mapTo(Util.classTag(ChatCommandResult.class));
    }

    @Nonnull
    protected ChatCommandResult waitForResult(final @Nonnull Future<ChatCommandResult> future)
    {
        try
        {
            return Await.result(future, duration);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Cannot get result from command", e);
        }
    }
}
