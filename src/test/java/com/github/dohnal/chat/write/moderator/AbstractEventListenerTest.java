package com.github.dohnal.chat.write.moderator;

import javax.annotation.Nonnull;
import java.util.Date;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import com.github.dohnal.chat.domain.protocol.command.ChatCommand;
import com.github.dohnal.chat.domain.protocol.event.ChatEvent;
import org.junit.After;
import org.junit.Before;

/**
 * @author dohnal
 */
public abstract class AbstractEventListenerTest
{
    protected ActorSystem system;

    private ActorRef actor;

    @Nonnull
    protected abstract ActorRef setupActor(final @Nonnull ActorRef receiver);

    @Before
    public void setup()
    {
        // create actor system
        system = ActorSystem.create();
    }

    @After
    public void teardown()
    {
        // shutdown actor system
        JavaTestKit.shutdownActorSystem(system);

        system = null;
    }

    protected class TestCase extends JavaTestKit
    {
        protected final Date date;

        public TestCase()
        {
            super(system);

            date = new Date();

            // create actor
            actor = setupActor(getRef());
        }

        public void given(final @Nonnull ChatEvent... events)
        {
            for (ChatEvent event : events)
            {
                actor.tell(event, getRef());
            }
        }

        public void thenExpectCommands(final @Nonnull ChatCommand... commands)
        {
            expectMsgAllOf((Object[])commands);
        }

        public void thenExpectNoCommand()
        {
            expectNoMsg();
        }
    }
}
