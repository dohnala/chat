package com.github.dohnal.chat.write;

import javax.annotation.Nonnull;
import java.util.Collection;

import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import akka.persistence.AbstractPersistentActor;
import com.github.dohnal.chat.domain.protocol.ChatCommandResult;
import com.github.dohnal.chat.domain.protocol.command.ChatCommand;
import com.github.dohnal.chat.domain.protocol.command.JoinRoom;
import com.github.dohnal.chat.domain.protocol.command.KickUser;
import com.github.dohnal.chat.domain.protocol.command.LeaveRoom;
import com.github.dohnal.chat.domain.protocol.command.SendMessage;
import com.github.dohnal.chat.domain.protocol.event.ChatEvent;
import org.apache.commons.lang.StringUtils;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * Actor representing chat room
 *
 * @author dohnal
 */
public class ChatRoomActor extends AbstractPersistentActor
{
    public static final String NAME = "GlobalChatRoom";

    private LoggingAdapter LOG = Logging.getLogger(getContext().system(), this);

    private final String name;

    private final ChatRoomAggregate state;

    /**
     * Immutable and freely sharable recipe for creating new
     * instances of given actor
     *
     * @return recipe for creating actors
     */
    public static Props props()
    {
        return Props.create(ChatRoomActor.class, () -> new ChatRoomActor(NAME));
    }

    /**
     * Create new instance of this actor, should be private
     * and accessible only in {@link ChatRoomActor#props()}
     *
     * @param name name
     */
    protected ChatRoomActor(final @Nonnull String name)
    {
        this.name = name;
        this.state = new ChatRoomAggregate();
    }

    @Override
    public String persistenceId()
    {
        return name;
    }

    @Override
    public PartialFunction<Object, BoxedUnit> receiveCommand()
    {
        return ReceiveBuilder
                .match(JoinRoom.class, cmd -> handle(cmd, c ->
                        state.joinRoom(c.getUsername())))
                .match(SendMessage.class, cmd -> handle(cmd, c ->
                        state.sendMessage(c.getUsername(), c.getMessage(), c.getDate())))
                .match(LeaveRoom.class, cmd -> handle(cmd, c ->
                        state.leaveRoom(c.getUsername())))
                .match(KickUser.class, cmd -> handle(cmd, c ->
                        state.kickUser(c.getUsername(), c.getKickedBy(), c.getReason())))
                .build();
    }

    @Override
    public PartialFunction<Object, BoxedUnit> receiveRecover()
    {
        return ReceiveBuilder
                .match(ChatEvent.class, state::update)
                .build();
    }

    protected <T extends ChatCommand> void handle(final @Nonnull T command,
                                                  final @Nonnull ChatCommandHandler<T> handler)
    {
        LOG.info("Received - {}", command);

        try
        {
            Collection<ChatEvent> events = handler.handle(command);

            // tell send that command was successfully processed
            sender().tell(ChatCommandResult.OK, self());

            persistAll(events, event -> {
                LOG.info("Stored - {}", event);

                // publish stored events to event stream
                publishEvent(event);

                // update state
                state.update(event);
            });
        }
        catch (ChatRoomException e)
        {
            LOG.info("Exceptions - {}", StringUtils.join(e.getExceptions(), ", "));

            sender().tell(ChatCommandResult.ERROR(e.getExceptions()), self());
        }
    }

    protected void publishEvent(final @Nonnull ChatEvent event)
    {
        context().system().eventStream().publish(event);
    }

    @FunctionalInterface
    protected interface ChatCommandHandler<T extends ChatCommand>
    {
        @Nonnull
        Collection<ChatEvent> handle(final @Nonnull T command) throws ChatRoomException;
    }
}
