package com.github.dohnal.chat.read;

import javax.annotation.Nonnull;

import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import akka.persistence.AbstractPersistentView;
import com.github.dohnal.chat.domain.ChatRoom;
import com.github.dohnal.chat.domain.Message;
import com.github.dohnal.chat.domain.MessageTools;
import com.github.dohnal.chat.domain.protocol.event.MessageSent;
import com.github.dohnal.chat.domain.protocol.event.UserJoined;
import com.github.dohnal.chat.domain.protocol.event.UserKicked;
import com.github.dohnal.chat.domain.protocol.event.UserLeft;
import com.github.dohnal.chat.domain.protocol.query.GetChatRoom;
import com.github.dohnal.chat.domain.protocol.query.GetChatRoomResult;
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

    private final ChatRoom chatRoom;

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
        return ChatRoomActor.NAME;
    }

    @Override
    public PartialFunction<Object, BoxedUnit> receive()
    {
        return ReceiveBuilder
                .match(UserJoined.class, e -> isPersistent(), this::onUserJoin)
                .match(MessageSent.class, e -> isPersistent(), this::onMessageSent)
                .match(UserLeft.class, e -> isPersistent(), this::onUserLeft)
                .match(UserKicked.class, e -> isPersistent(), this::onUserKicked)
                .match(GetChatRoom.class, this::getChatRoom)
                .build();
    }

    protected void onUserJoin(final @Nonnull UserJoined event)
    {
        LOG.info("Received event: " + event);

        chatRoom.getUsers().add(event.getUsername());

        Message message = MessageTools.convert(event);

        if (message != null)
        {
            chatRoom.getMessages().add(message);
        }
    }

    protected void onMessageSent(final @Nonnull MessageSent event)
    {
        LOG.info("Received event: " + event);

        Message message = MessageTools.convert(event);

        if (message != null)
        {
            chatRoom.getMessages().add(message);
        }
    }

    protected void onUserLeft(final @Nonnull UserLeft event)
    {
        LOG.info("Received event: " + event);

        chatRoom.getUsers().remove(event.getUsername());

        Message message = MessageTools.convert(event);

        if (message != null)
        {
            chatRoom.getMessages().add(message);
        }
    }

    protected void onUserKicked(final @Nonnull UserKicked event)
    {
        LOG.info("Received event: " + event);

        chatRoom.getUsers().remove(event.getUsername());

        Message message = MessageTools.convert(event);

        if (message != null)
        {
            chatRoom.getMessages().add(message);
        }
    }

    protected void getChatRoom(final @Nonnull GetChatRoom query)
    {
        LOG.info("Received query: " + query);

        // send him result
        sender().tell(new GetChatRoomResult(chatRoom), self());
    }
}
