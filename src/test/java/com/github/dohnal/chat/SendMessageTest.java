package com.github.dohnal.chat;

import com.github.dohnal.chat.model.protocol.commands.JoinRoom;
import com.github.dohnal.chat.model.protocol.commands.KickUser;
import com.github.dohnal.chat.model.protocol.commands.LeaveRoom;
import com.github.dohnal.chat.model.protocol.commands.SendMessage;
import com.github.dohnal.chat.model.protocol.events.MessageSent;
import com.github.dohnal.chat.model.protocol.exceptions.UserNotJoined;
import org.junit.Test;

/**
 * @author dohnal
 */
public class SendMessageTest extends AbstractChatTest
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
            thenExpectEvents(new MessageSent("UserA", "message"));
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
            thenExpectEvents(new MessageSent("UserA", "message"));
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
            thenExpectEvents(new MessageSent("UserA", "message"));
        }};
    }

    @Test
    public void testSendMessageAfterKicked()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"),
                  new KickUser("UserA"));
            when(new SendMessage("UserA", "message"));
            thenExpectExceptions(new UserNotJoined("UserA"));
        }};
    }

    @Test
    public void testSendMessageAfterReJoinedFromKick()
    {
        new ChatTestCase() {{
            given(new JoinRoom("UserA"),
                  new KickUser("UserA"),
                  new JoinRoom("UserA"));
            when(new SendMessage("UserA", "message"));
            thenExpectEvents(new MessageSent("UserA", "message"));
        }};
    }
}
