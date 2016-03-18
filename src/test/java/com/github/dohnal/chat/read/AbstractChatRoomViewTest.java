package com.github.dohnal.chat.read;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import akka.testkit.JavaTestKit;
import com.github.dohnal.chat.domain.protocol.event.ChatEvent;
import com.github.dohnal.chat.domain.protocol.query.ChatQuery;
import com.github.dohnal.chat.domain.protocol.query.ChatQueryResult;
import com.github.dohnal.chat.write.ChatRoomAggregate;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * @author dohnal
 */
public abstract class AbstractChatRoomViewTest
{
    private static final String RECOVERY_STARTED = "recoveryStarted";
    private static final String RECOVERY_FINISHED = "recoveryFinished";

    private ActorSystem system;

    private ActorRef chatRoomViewActor;

    @Before
    public void setup() throws IOException
    {
        // delete journal
        FileUtils.deleteDirectory(new File("target/chat"));

        // create actor system
        system = ActorSystem.create();

        // create test chat room view actor
        chatRoomViewActor = system.actorOf(Props.create(TestChatRoomViewActor.class,
                () -> new TestChatRoomViewActor(ChatRoomAggregate.ROOM_NAME)), ChatRoomViewActor.NAME);
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
        }

        public void given(final @Nonnull ChatEvent... events)
        {
            chatRoomViewActor.tell(RECOVERY_STARTED, getRef());

            for (ChatEvent event : events)
            {
                chatRoomViewActor.tell(event, getRef());
            }

            chatRoomViewActor.tell(RECOVERY_FINISHED, getRef());
        }

        public void when(final @Nonnull ChatQuery query)
        {
            chatRoomViewActor.tell(query, getRef());
        }

        @Nonnull
        public <T extends ChatQueryResult> T thenExpectResult(final @Nonnull Class<T> resultType)
        {
            T result = expectMsgClass(resultType);

            Assert.assertNotNull("Query result should not be null", result);

            return result;
        }
    }

    protected class TestChatRoomViewActor extends ChatRoomViewActor
    {
        private boolean recovering;

        private TestChatRoomViewActor(final @Nonnull String roomName)
        {
            super(roomName);

            this.recovering = false;
        }

        @Override
        public PartialFunction<Object, BoxedUnit> receive()
        {
            return super.receive().orElse(ReceiveBuilder
                    .matchEquals(RECOVERY_STARTED, m -> recovering = true)
                    .matchEquals(RECOVERY_FINISHED, m -> recovering = false)
                    .build());
        }

        @Override
        public boolean isPersistent()
        {
            return recovering;
        }
    }
}
