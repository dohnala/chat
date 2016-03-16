package com.github.dohnal.chat.write.room;

import com.github.dohnal.chat.domain.protocol.command.JoinRoom;
import com.github.dohnal.chat.domain.protocol.command.KickUser;
import com.github.dohnal.chat.domain.protocol.command.LeaveRoom;
import com.github.dohnal.chat.domain.protocol.command.SendMessage;
import com.github.dohnal.chat.domain.protocol.event.UserLeft;
import com.github.dohnal.chat.domain.protocol.exception.UserNotJoined;
import org.junit.Test;

/**
 * @author dohnal
 */
public class LeaveRoomTest extends AbstractChatRoomTest
{
    @Test
    public void testLeaveRoom()
    {
        new ChatTestCase() {{
            when(new LeaveRoom("UserA"));
            thenExpectExceptions(new UserNotJoined("UserA"));
        }};
    }

    @Test
    public void testLeaveRoomAfterJoined()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"));
            when(new LeaveRoom("UserA"));
            thenExpectEvents(new UserLeft("UserA", date));
        }};
    }

    @Test
    public void testLeaveRoomAsDifferentUser()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"));
            when(new LeaveRoom("UserB"));
            thenExpectExceptions(new UserNotJoined("UserB"));
        }};
    }

    @Test
    public void testLeaveRoomTwice()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"),
                  new LeaveRoom("UserA"));
            when(new LeaveRoom("UserA"));
            thenExpectExceptions(new UserNotJoined("UserA"));
        }};
    }

    @Test
    public void testLeaveAfterSentMessage()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"),
                  new SendMessage("UserA", "message"));
            when(new LeaveRoom("UserA"));
            thenExpectEvents(new UserLeft("UserA", date));
        }};
    }

    @Test
    public void testLeaveAfterReJoined()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"),
                  new LeaveRoom("UserA"),
                  new JoinRoom("UserA"));
            when(new LeaveRoom("UserA"));
            thenExpectEvents(new UserLeft("UserA", date));
        }};
    }

    @Test
    public void testLeaveAfterKicked()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"),
                  new KickUser("UserA", "Moderator", "No reason"));
            when(new LeaveRoom("UserA"));
            thenExpectExceptions(new UserNotJoined("UserA"));
        }};
    }

    @Test
    public void testLeaveAfterReJoinedFromKick()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"),
                  new KickUser("UserA", "Moderator", "No reason"),
                  new JoinRoom("UserA"));
            when(new LeaveRoom("UserA"));
            thenExpectEvents(new UserLeft("UserA", date));
        }};
    }
}
