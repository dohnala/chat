package com.github.dohnal.chat.write.moderator;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.Set;

import akka.actor.ActorRef;
import com.github.dohnal.chat.domain.protocol.command.KickUser;
import com.github.dohnal.chat.domain.protocol.event.MessageSent;
import com.github.dohnal.chat.write.ChatModeratorActor;
import com.google.common.collect.Sets;
import org.junit.Test;

/**
 * @author dohnal
 */
public class ChatModeratorTest extends AbstractEventListenerTest
{
    private static final Set<String> BLACK_LIST = Sets.newHashSet("shit");

    @Nonnull
    @Override
    protected ActorRef setupActor(final @Nonnull ActorRef receiver)
    {
        return system.actorOf(ChatModeratorActor.props(BLACK_LIST, receiver), ChatModeratorActor.NAME);
    }

    @Test
    public void testValidWord()
    {
        new TestCase() {{
            given(new MessageSent("UserA", "Hello", new Date()));
            thenExpectNoCommand();
        }};
    }

    @Test
    public void testInvalidWord()
    {
        new TestCase() {{
            given(new MessageSent("UserA", "shit", new Date()));
            thenExpectCommands(new KickUser("UserA", ChatModeratorActor.NAME, ChatModeratorActor.REASON));
        }};
    }

    @Test
    public void testInvalidWordIgnoreCase()
    {
        new TestCase() {{
            given(new MessageSent("UserA", "ShIt", new Date()));
            thenExpectCommands(new KickUser("UserA", ChatModeratorActor.NAME, ChatModeratorActor.REASON));
        }};
    }

    @Test
    public void testInvalidWordSubstring()
    {
        new TestCase() {{
            given(new MessageSent("UserA", "blablashitbla", new Date()));
            thenExpectCommands(new KickUser("UserA", ChatModeratorActor.NAME, ChatModeratorActor.REASON));
        }};
    }

    @Test
    public void testInvalidWordFromDifferentUser()
    {
        new TestCase() {{
            given(new MessageSent("UserA", "Hello", new Date()),
                  new MessageSent("UserB", "shit", new Date()));
            thenExpectCommands(new KickUser("UserB", ChatModeratorActor.NAME, ChatModeratorActor.REASON));
        }};
    }
}
