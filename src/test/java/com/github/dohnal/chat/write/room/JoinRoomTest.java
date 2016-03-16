package com.github.dohnal.chat.write.room;

import com.github.dohnal.chat.domain.protocol.command.JoinRoom;
import com.github.dohnal.chat.domain.protocol.command.KickUser;
import com.github.dohnal.chat.domain.protocol.command.LeaveRoom;
import com.github.dohnal.chat.domain.protocol.command.SendMessage;
import com.github.dohnal.chat.domain.protocol.event.UserJoined;
import com.github.dohnal.chat.domain.protocol.exception.UserAlreadyJoined;
import org.junit.Test;

/**
 * @author dohnal
 */
public class JoinRoomTest extends AbstractChatRoomTest
{
    @Test
    public void testJoinRoom()
    {
        new ChatTestCase() {{
            when(new JoinRoom("UserA"));
            thenExpectEvents(new UserJoined("UserA"));
        }};
    }

    @Test
    public void testJoinRoomTwice()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"));
            when(new JoinRoom("UserA"));
            thenExpectExceptions(new UserAlreadyJoined("UserA"));
        }};
    }

    @Test
    public void testJoinRoomAsDifferentUser()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"));
            when(new JoinRoom("UserB"));
            thenExpectEvents(new UserJoined("UserB"));
        }};
    }

    @Test
    public void testJoinRoomAfterMessageSent()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"),
                  new SendMessage("UserA", "message"));
            when(new JoinRoom("UserA"));
            thenExpectExceptions(new UserAlreadyJoined("UserA"));
        }};
    }

    @Test
    public void testJoinRoomAfterLeftRoom()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"),
                  new LeaveRoom("UserA"));
            when(new JoinRoom("UserA"));
            thenExpectEvents(new UserJoined("UserA"));
        }};
    }

    @Test
    public void testJoinRoomAfterKicked()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"),
                  new KickUser("UserA", "Moderator", "No reason"));
            when(new JoinRoom("UserA"));
            thenExpectEvents(new UserJoined("UserA"));
        }};
    }
}
