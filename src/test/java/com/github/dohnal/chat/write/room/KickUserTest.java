package com.github.dohnal.chat.write.room;

import java.util.Date;

import com.github.dohnal.chat.domain.protocol.command.JoinRoom;
import com.github.dohnal.chat.domain.protocol.command.KickUser;
import com.github.dohnal.chat.domain.protocol.command.LeaveRoom;
import com.github.dohnal.chat.domain.protocol.command.SendMessage;
import com.github.dohnal.chat.domain.protocol.event.UserKicked;
import com.github.dohnal.chat.domain.protocol.exception.UserNotJoined;
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
                  new SendMessage("UserA", "message", new Date()));
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
