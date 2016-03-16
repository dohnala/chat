package com.github.dohnal.chat;

import javax.annotation.Nonnull;
import java.util.Collection;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.github.dohnal.chat.model.protocol.commands.KickUser;
import com.github.dohnal.chat.model.protocol.events.MessageSent;
import org.apache.commons.lang.StringUtils;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * @author dohnal
 */
public class ChatModeratorActor extends AbstractActor
{
    public static final String NAME = "Moderator";

    public static final String REASON = "Using inappropriate words";

    private final Collection<String> wordBlackList;

    private final ActorRef chatRoomActor;

    public static Props props(final @Nonnull Collection<String> wordBlackList,
                              final @Nonnull ActorRef charRoomActor)
    {
        return Props.create(ChatModeratorActor.class, () -> new ChatModeratorActor(wordBlackList, charRoomActor));
    }

    protected ChatModeratorActor(final @Nonnull Collection<String> wordBlackList,
                                 final @Nonnull ActorRef charRoomActor)
    {
        this.wordBlackList = wordBlackList;
        this.chatRoomActor = charRoomActor;

        context().system().eventStream().subscribe(self(), MessageSent.class);
    }

    @Override
    public PartialFunction<Object, BoxedUnit> receive()
    {
        return ReceiveBuilder
                .match(MessageSent.class, this::checkMessage)
                .build();
    }

    protected void checkMessage(final @Nonnull MessageSent event)
    {
        if (wordBlackList.stream().anyMatch(word -> StringUtils.containsIgnoreCase(event.getMessage(), word)))
        {
            chatRoomActor.tell(new KickUser(event.getUsername(), NAME, REASON), self());
        }
    }
}
