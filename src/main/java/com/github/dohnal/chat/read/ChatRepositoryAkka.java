package com.github.dohnal.chat.read;

import javax.annotation.Nonnull;

import akka.dispatch.Mapper;
import akka.japi.Util;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.github.dohnal.chat.ChatRuntime;
import com.github.dohnal.chat.domain.ChatRepository;
import com.github.dohnal.chat.domain.ChatRoom;
import com.github.dohnal.chat.domain.protocol.query.GetChatRoom;
import com.github.dohnal.chat.domain.protocol.query.GetChatRoomResult;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

/**
 * @author dohnal
 */
public class ChatRepositoryAkka implements ChatRepository
{
    private final FiniteDuration duration = Duration.create(3, "seconds");
    private final Timeout timeout = new Timeout(duration);

    private final ChatRuntime chatRuntime;

    public ChatRepositoryAkka(final @Nonnull ChatRuntime chatRuntime)
    {
        this.chatRuntime = chatRuntime;
    }

    @Nonnull
    @Override
    public ChatRoom getChatRoom()
    {
        try
        {
            return Await.result(getChatRoomAsync(), duration);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Cannot get result from query", e);
        }
    }

    @Nonnull
    @Override
    public Future<ChatRoom> getChatRoomAsync()
    {
        return Patterns
                .ask(chatRuntime.getChatRoomView(), new GetChatRoom(), timeout)
                .mapTo(Util.classTag(GetChatRoomResult.class))
                .map(new Mapper<GetChatRoomResult, ChatRoom>()
                {
                    @Override
                    public ChatRoom apply(GetChatRoomResult result)
                    {
                        return result.getValue();
                    }
                }, chatRuntime.getExecutionContext());
    }
}
