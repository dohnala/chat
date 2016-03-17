package com.github.dohnal.chat.read;

import javax.annotation.Nonnull;
import java.util.Map;

import akka.actor.ActorRef;
import akka.dispatch.Mapper;
import akka.japi.Util;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.github.dohnal.chat.ChatRuntime;
import com.github.dohnal.chat.domain.ChatEventListener;
import com.github.dohnal.chat.domain.ChatRepository;
import com.github.dohnal.chat.domain.ChatRoom;
import com.github.dohnal.chat.domain.protocol.query.GetChatRoom;
import com.github.dohnal.chat.domain.protocol.query.GetChatRoomResult;
import com.google.common.collect.Maps;
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

    private final Map<ChatEventListener, ActorRef> listeners;

    public ChatRepositoryAkka(final @Nonnull ChatRuntime chatRuntime)
    {
        this.chatRuntime = chatRuntime;
        this.listeners = Maps.newHashMap();
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

    @Override
    public void addEventListener(final @Nonnull ChatEventListener listener)
    {
        ActorRef actor = chatRuntime.getSystem().actorOf(ChatEventListenerActor.props(listener));

        listeners.put(listener, actor);
    }

    @Override
    public void removeEventListener(final @Nonnull ChatEventListener listener)
    {
        ActorRef actor = listeners.get(listener);

        if (actor != null)
        {
            chatRuntime.getSystem().stop(actor);

            listeners.remove(listener);
        }
    }
}
