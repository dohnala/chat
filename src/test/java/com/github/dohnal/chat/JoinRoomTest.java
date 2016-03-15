package com.github.dohnal.chat;

import com.github.dohnal.chat.model.protocol.commands.JoinRoom;
import com.github.dohnal.chat.model.protocol.commands.KickUser;
import com.github.dohnal.chat.model.protocol.commands.LeaveRoom;
import com.github.dohnal.chat.model.protocol.commands.SendMessage;
import com.github.dohnal.chat.model.protocol.events.UserJoined;
import com.github.dohnal.chat.model.protocol.exceptions.UserAlreadyJoined;
import org.junit.Test;

/**
 * @author dohnal
 */
public class JoinRoomTest extends AbstractChatTest
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
                  new KickUser("UserA"));
            when(new JoinRoom("UserA"));
            thenExpectEvents(new UserJoined("UserA"));
        }};
    }
}
