package com.github.dohnal.chat.write.room;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Date;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import akka.testkit.JavaTestKit;
import com.github.dohnal.chat.domain.protocol.ChatCommandResult;
import com.github.dohnal.chat.domain.protocol.command.ChatCommand;
import com.github.dohnal.chat.domain.protocol.event.ChatEvent;
import com.github.dohnal.chat.domain.protocol.exception.ChatException;
import com.github.dohnal.chat.write.ChatRoomActor;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * @author dohnal
 */
public abstract class AbstractChatRoomTest
{
    private static final String PUBLISH = "publish";

    private ActorSystem system;

    private ActorRef chatRoomActor;

    @Before
    public void setup() throws IOException
    {
        // create actor system
        system = ActorSystem.create();

        // create test chat room actor
        chatRoomActor = system.actorOf(Props.create(TestChatRoomActor.class,
                () -> new TestChatRoomActor(ChatRoomActor.NAME)), ChatRoomActor.NAME);
    }

    @After
    public void teardown()
    {
        // shutdown actor system
        JavaTestKit.shutdownActorSystem(system);

        system = null;
    }

    protected class ChatTestCase extends JavaTestKit
    {
        protected final Date date;

        public ChatTestCase()
        {
            super(system);

            this.date = new Date();

            // subscribe test kit actor to event stream
            system.eventStream().subscribe(getRef(), ChatEvent.class);
        }

        public void given(final @Nonnull ChatCommand... commands)
        {
            for (ChatCommand command : commands)
            {
                chatRoomActor.tell(command, getRef());

                expectMsgEquals(ChatCommandResult.OK);
            }
        }

        public void when(final @Nonnull ChatCommand command)
        {
            // tell chat room actor to publish events
            chatRoomActor.tell(PUBLISH, getRef());

            chatRoomActor.tell(command, getRef());
        }

        public void thenExpectExceptions(final @Nonnull ChatException... exceptions)
        {
            ChatCommandResult result = expectMsgClass(ChatCommandResult.class);

            Assert.assertNotNull("Command result should not be null", result);
            Assert.assertTrue("Command should result in exception", result.isERROR());

            for (ChatException exception : exceptions)
            {
                Assert.assertTrue("Command result should contain exception of type: " + exception.getClass(),
                        result.getErrors().contains(exception));
            }
        }

        public void thenExpectEvents(final @Nonnull ChatEvent... events)
        {
            expectMsgEquals(ChatCommandResult.OK);

            for (ChatEvent event : events)
            {
                ChatEvent receivedEvent = expectMsgClass(event.getClass());

                Assert.assertTrue(EqualsBuilder.reflectionEquals(event, receivedEvent, new String[]{"date"}));
            }
        }
    }

    /**
     * Extend {@link ChatRoomActor} for test purposes, to control
     * which events are published
     */
    protected class TestChatRoomActor extends ChatRoomActor
    {
        private boolean publish = false;

        private TestChatRoomActor(final @Nonnull String name)
        {
            super(name);
        }

        @Override
        public PartialFunction<Object, BoxedUnit> receiveCommand()
        {
            return super.receiveCommand().orElse(ReceiveBuilder.matchEquals(PUBLISH, m -> publish = true).build());
        }

        @Override
        protected void publishEvent(final @Nonnull ChatEvent event)
        {
            if (publish)
            {
                context().system().eventStream().publish(event);
            }
        }
    }
}
