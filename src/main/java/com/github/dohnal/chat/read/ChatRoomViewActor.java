package com.github.dohnal.chat.read;

import javax.annotation.Nonnull;

import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import akka.persistence.AbstractPersistentView;
import com.github.dohnal.chat.domain.ChatRoom;
import com.github.dohnal.chat.domain.protocol.event.MessageSent;
import com.github.dohnal.chat.domain.protocol.event.UserJoined;
import com.github.dohnal.chat.domain.protocol.event.UserKicked;
import com.github.dohnal.chat.domain.protocol.event.UserLeft;
import com.github.dohnal.chat.write.ChatRoomActor;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * @author dohnal
 */
@SuppressWarnings("deprecation")
public class ChatRoomViewActor extends AbstractPersistentView
{
    public static final String NAME = ChatRoomActor.NAME + "View";

    private LoggingAdapter LOG = Logging.getLogger(getContext().system(), this);

    private ChatRoom chatRoom;

    public static Props props()
    {
        return Props.create(ChatRoomViewActor.class, ChatRoomViewActor::new);
    }

    protected ChatRoomViewActor()
    {
        chatRoom = new ChatRoom();
        chatRoom.setName(ChatRoomActor.NAME);
    }

    @Override
    public String viewId()
    {
        return NAME;
    }

    @Override
    public String persistenceId()
    {
        return chatRoom.getName();
    }

    @Override
    public PartialFunction<Object, BoxedUnit> receive()
    {
        return ReceiveBuilder
                .match(UserJoined.class, e -> isPersistent(), this::onUserJoined)
                .match(MessageSent.class, e -> isPersistent(), this::onMessageSent)
                .match(UserLeft.class, e -> isPersistent(), this::onUserLeft)
        .match(UserKicked.class, e -> isPersistent(), this::onUserKicked)
        .build();
    }

    protected void onUserJoined(final @Nonnull UserJoined event)
    {
        LOG.info("Received: " + event);
    }

    protected void onMessageSent(final @Nonnull MessageSent event)
    {
        LOG.info("Received: " + event);
    }

    protected void onUserLeft(final @Nonnull UserLeft event)
    {
        LOG.info("Received: " + event);
    }

    protected void onUserKicked(final @Nonnull UserKicked event)
    {
        LOG.info("Received: " + event);
    }
}
