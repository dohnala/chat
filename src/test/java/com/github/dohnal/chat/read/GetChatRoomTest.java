package com.github.dohnal.chat.read;

import com.github.dohnal.chat.domain.ChatRoom;
import com.github.dohnal.chat.domain.protocol.event.MessageSent;
import com.github.dohnal.chat.domain.protocol.event.UserJoined;
import com.github.dohnal.chat.domain.protocol.event.UserKicked;
import com.github.dohnal.chat.domain.protocol.event.UserLeft;
import com.github.dohnal.chat.domain.protocol.query.GetChatRoom;
import com.github.dohnal.chat.domain.protocol.query.GetChatRoomResult;
import com.github.dohnal.chat.write.ChatRoomAggregate;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author dohnal
 */
public class GetChatRoomTest extends AbstractChatRoomViewTest
{
    @Test
    public void testGetChatRoom()
    {
        new ChatTestCase() {{
            when(new GetChatRoom());
            ChatRoom chatRoom = thenExpectResult(GetChatRoomResult.class).getValue();

            Assert.assertNotNull(chatRoom);
            Assert.assertEquals(ChatRoomAggregate.ROOM_NAME, chatRoom.getName());
            Assert.assertTrue(chatRoom.getUsers().isEmpty());
            Assert.assertTrue(chatRoom.getMessages().isEmpty());
        }};
    }

    @Test
    public void testGetChatRoomAfterUserJoined()
    {
        new ChatTestCase() {{
            given(new UserJoined("UserA", date));
            when(new GetChatRoom());
            ChatRoom chatRoom = thenExpectResult(GetChatRoomResult.class).getValue();

            Assert.assertNotNull(chatRoom);
            Assert.assertEquals(ChatRoomAggregate.ROOM_NAME, chatRoom.getName());

            Assert.assertEquals(1, chatRoom.getUsers().size());
            Assert.assertTrue(chatRoom.getUsers().contains("UserA"));

            Assert.assertEquals(1, chatRoom.getMessages().size());
            Assert.assertEquals("UserA", chatRoom.getMessages().get(0).getUsername());
            Assert.assertEquals(date, chatRoom.getMessages().get(0).getDate());
            Assert.assertEquals("UserA has joined the chat room", chatRoom.getMessages().get(0).getMessage());
        }};
    }

    @Test
    public void testGetChatRoomAfterMessageSent()
    {
        new ChatTestCase() {{
            given(new UserJoined("UserA", date),
                  new MessageSent("UserA", "Hello", date));
            when(new GetChatRoom());
            ChatRoom chatRoom = thenExpectResult(GetChatRoomResult.class).getValue();

            Assert.assertNotNull(chatRoom);
            Assert.assertEquals(ChatRoomAggregate.ROOM_NAME, chatRoom.getName());

            Assert.assertEquals(1, chatRoom.getUsers().size());
            Assert.assertTrue(chatRoom.getUsers().contains("UserA"));

            Assert.assertEquals(2, chatRoom.getMessages().size());
            Assert.assertEquals("UserA", chatRoom.getMessages().get(1).getUsername());
            Assert.assertEquals(date, chatRoom.getMessages().get(1).getDate());
            Assert.assertEquals("UserA: Hello", chatRoom.getMessages().get(1).getMessage());
        }};
    }

    @Test
    public void testGetChatRoomAfterUserLeft()
    {
        new ChatTestCase() {{
            given(new UserJoined("UserA", date),
                    new UserLeft("UserA", date));
            when(new GetChatRoom());
            ChatRoom chatRoom = thenExpectResult(GetChatRoomResult.class).getValue();

            Assert.assertNotNull(chatRoom);
            Assert.assertEquals(ChatRoomAggregate.ROOM_NAME, chatRoom.getName());

            Assert.assertTrue(chatRoom.getUsers().isEmpty());

            Assert.assertEquals(2, chatRoom.getMessages().size());
            Assert.assertEquals("UserA", chatRoom.getMessages().get(1).getUsername());
            Assert.assertEquals(date, chatRoom.getMessages().get(1).getDate());
            Assert.assertEquals("UserA has left the chat room", chatRoom.getMessages().get(1).getMessage());
        }};
    }

    @Test
    public void testGetChatRoomAfterUserKicked()
    {
        new ChatTestCase() {{
            given(new UserJoined("UserA", date),
                  new UserKicked("UserA", "Moderator", "No reason", date));
            when(new GetChatRoom());
            ChatRoom chatRoom = thenExpectResult(GetChatRoomResult.class).getValue();

            Assert.assertNotNull(chatRoom);
            Assert.assertEquals(ChatRoomAggregate.ROOM_NAME, chatRoom.getName());

            Assert.assertTrue(chatRoom.getUsers().isEmpty());

            Assert.assertEquals(2, chatRoom.getMessages().size());
            Assert.assertEquals("UserA", chatRoom.getMessages().get(1).getUsername());
            Assert.assertEquals(date, chatRoom.getMessages().get(1).getDate());
            Assert.assertEquals("UserA has been kicked by Moderator for: No reason",
                    chatRoom.getMessages().get(1).getMessage());
        }};
    }
}
