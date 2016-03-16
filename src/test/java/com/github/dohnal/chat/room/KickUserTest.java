package com.github.dohnal.chat.room;

import com.github.dohnal.chat.model.protocol.commands.JoinRoom;
import com.github.dohnal.chat.model.protocol.commands.KickUser;
import com.github.dohnal.chat.model.protocol.commands.LeaveRoom;
import com.github.dohnal.chat.model.protocol.commands.SendMessage;
import com.github.dohnal.chat.model.protocol.events.UserKicked;
import com.github.dohnal.chat.model.protocol.exceptions.UserNotJoined;
import org.junit.Test;

/**
 * @author dohnal
 */
public class KickUserTest extends AbstractChatRoomTest
{
    @Test
    public void testKickUser()
    {
        new ChatTestCase() {{
            when(new KickUser("UserA", "Moderator", "No reason"));
            thenExpectExceptions(new UserNotJoined("UserA"));
        }};
    }

    @Test
    public void testKickUserAfterJoined()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"));
            when(new KickUser("UserA", "Moderator", "No reason"));
            thenExpectEvents(new UserKicked("UserA", "Moderator", "No reason"));
        }};
    }

    @Test
    public void testKickUserDifferentUser()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"));
            when(new KickUser("UserB", "Moderator", "No reason"));
            thenExpectExceptions(new UserNotJoined("UserB"));
        }};
    }

    @Test
    public void testKickUserTwice()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"),
                  new KickUser("UserA", "Moderator", "No reason"));
            when(new KickUser("UserA", "Moderator", "No reason"));
            thenExpectExceptions(new UserNotJoined("UserA"));
        }};
    }

    @Test
    public void testKickUserAfterSentMessage()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"),
                  new SendMessage("UserA", "message"));
            when(new KickUser("UserA", "Moderator", "No reason"));
            thenExpectEvents(new UserKicked("UserA", "Moderator", "No reason"));
        }};
    }

    @Test
    public void testKickUserAfterLeft()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"),
                  new LeaveRoom("UserA"));
            when(new KickUser("UserA", "Moderator", "No reason"));
            thenExpectExceptions(new UserNotJoined("UserA"));
        }};
    }

    @Test
    public void testKickUserAfterReJoined()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"),
                  new LeaveRoom("UserA"),
                  new JoinRoom("UserA"));
            when(new KickUser("UserA", "Moderator", "No reason"));
            thenExpectEvents(new UserKicked("UserA", "Moderator", "No reason"));
        }};
    }

    @Test
    public void testKickUserAfterReJoinedFromKick()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"),
                  new KickUser("UserA", "Moderator", "No reason"),
                  new JoinRoom("UserA"));
            when(new KickUser("UserA", "Moderator", "No reason"));
            thenExpectEvents(new UserKicked("UserA", "Moderator", "No reason"));
        }};
    }
}
