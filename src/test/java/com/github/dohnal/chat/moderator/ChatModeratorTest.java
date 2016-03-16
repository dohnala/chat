package com.github.dohnal.chat.moderator;

import javax.annotation.Nonnull;
import java.util.Set;

import akka.actor.ActorRef;
import com.github.dohnal.chat.ChatModeratorActor;
import com.github.dohnal.chat.model.protocol.commands.KickUser;
import com.github.dohnal.chat.model.protocol.events.MessageSent;
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
            given(new MessageSent("UserA", "Hello"));
            thenExpectNoCommand();
        }};
    }

    @Test
    public void testInvalidWord()
    {
        new TestCase() {{
            given(new MessageSent("UserA", "shit"));
            thenExpectCommands(new KickUser("UserA", ChatModeratorActor.NAME, ChatModeratorActor.REASON));
        }};
    }

    @Test
    public void testInvalidWordIgnoreCase()
    {
        new TestCase() {{
            given(new MessageSent("UserA", "ShIt"));
            thenExpectCommands(new KickUser("UserA", ChatModeratorActor.NAME, ChatModeratorActor.REASON));
        }};
    }

    @Test
    public void testInvalidWordSubstring()
    {
        new TestCase() {{
            given(new MessageSent("UserA", "blablashitbla"));
            thenExpectCommands(new KickUser("UserA", ChatModeratorActor.NAME, ChatModeratorActor.REASON));
        }};
    }

    @Test
    public void testInvalidWordFromDifferentUser()
    {
        new TestCase() {{
            given(new MessageSent("UserA", "Hello"),
                  new MessageSent("UserB", "shit"));
            thenExpectCommands(new KickUser("UserB", ChatModeratorActor.NAME, ChatModeratorActor.REASON));
        }};
    }
}
