package com.github.dohnal.chat;

import javax.annotation.Nonnull;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.github.dohnal.chat.read.ChatRoomViewActor;
import com.github.dohnal.chat.write.ChatBotActor;
import com.github.dohnal.chat.write.ChatModeratorActor;
import com.github.dohnal.chat.write.ChatRoomActor;
import scala.concurrent.ExecutionContext;

/**
 * @author dohnal
 */
public class ChatRuntime
{
    private final ActorSystem system;

    private final ActorRef chatRoom;

    private final ActorRef chatRoomView;

    private final ActorRef moderator;

    public ChatRuntime(final @Nonnull Integer numberOfBots,
                       final @Nonnull List<String> wordBlackList)
    {
        // Create actor system
        system = ActorSystem.create("system");

        // Create chat room actor
        chatRoom = system.actorOf(ChatRoomActor.props(), ChatRoomActor.NAME);

        chatRoomView = system.actorOf(ChatRoomViewActor.props(), ChatRoomViewActor.NAME);

        // Create moderator
        moderator = system.actorOf(ChatModeratorActor.props(wordBlackList, chatRoom));

        // Create bots
        for (int i = 1; i <= numberOfBots; i++)
        {
            system.actorOf(ChatBotActor.props("Bot" + i, chatRoom), "Bot" + i);
        }
    }

    @Nonnull
    public ActorSystem getSystem()
    {
        return system;
    }

    @Nonnull
    public ActorRef getChatRoom()
    {
        return chatRoom;
    }

    @Nonnull
    public ActorRef getChatRoomView()
    {
        return chatRoomView;
    }

    @Nonnull
    public ActorRef getModerator()
    {
        return moderator;
    }

    @Nonnull
    public ExecutionContext getExecutionContext()
    {
        return system.dispatcher();
    }
}
