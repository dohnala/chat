package com.github.dohnal.chat.write.room;

import com.github.dohnal.chat.domain.protocol.command.JoinRoom;
import com.github.dohnal.chat.domain.protocol.command.KickUser;
import com.github.dohnal.chat.domain.protocol.command.LeaveRoom;
import com.github.dohnal.chat.domain.protocol.command.SendMessage;
import com.github.dohnal.chat.domain.protocol.event.MessageSent;
import com.github.dohnal.chat.domain.protocol.exception.UserNotJoined;
import org.junit.Test;

/**
 * @author dohnal
 */
public class SendMessageTest extends AbstractChatRoomTest
{
    @Test
    public void testSendMessage()
    {
        new ChatTestCase() {{
            when(new SendMessage("UserA", "message"));
            thenExpectExceptions(new UserNotJoined("UserA"));
        }};
    }

    @Test
    public void testSendMessageAfterJoined()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"));
            when(new SendMessage("UserA", "message"));
            thenExpectEvents(new MessageSent("UserA", "message", date));
        }};
    }

    @Test
    public void testSendMessageAsDifferentUser()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"));
            when(new SendMessage("UserB", "message"));
            thenExpectExceptions(new UserNotJoined("UserB"));
        }};
    }

    @Test
    public void testSendMessageTwice()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"),
                  new SendMessage("UserA", "message"));
            when(new SendMessage("UserA", "message"));
            thenExpectEvents(new MessageSent("UserA", "message", date));
        }};
    }

    @Test
    public void testSendMessageAfterLeft()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"),
                  new LeaveRoom("UserA"));
            when(new SendMessage("UserA", "message"));
            thenExpectExceptions(new UserNotJoined("UserA"));
        }};
    }

    @Test
    public void testSendMessageAfterReJoined()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"),
                  new LeaveRoom("UserA"),
                  new JoinRoom("UserA"));
            when(new SendMessage("UserA", "message"));
            thenExpectEvents(new MessageSent("UserA", "message", date));
        }};
    }

    @Test
    public void testSendMessageAfterKicked()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"),
                  new KickUser("UserA", "Moderator", "No reason"));
            when(new SendMessage("UserA", "message"));
            thenExpectExceptions(new UserNotJoined("UserA"));
        }};
    }

    @Test
    public void testSendMessageAfterReJoinedFromKick()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"),
                  new KickUser("UserA", "Moderator", "No reason"),
                  new JoinRoom("UserA"));
            when(new SendMessage("UserA", "message"));
            thenExpectEvents(new MessageSent("UserA", "message", date));
        }};
    }
}
