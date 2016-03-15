package com.github.dohnal.chat.model;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.github.dohnal.chat.model.protocol.ChatEvent;
import com.github.dohnal.chat.model.protocol.events.MessageSent;
import com.github.dohnal.chat.model.protocol.events.UserJoined;
import com.github.dohnal.chat.model.protocol.events.UserKicked;
import com.github.dohnal.chat.model.protocol.events.UserLeft;
import com.github.dohnal.chat.model.protocol.exceptions.UserAlreadyJoined;
import com.github.dohnal.chat.model.protocol.exceptions.UserNotJoined;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * @author dohnal
 */
public class ChatRoom implements Serializable
{
    private final Set<String> users;

    public ChatRoom()
    {
        this.users = Sets.newHashSet();
    }

    @Nonnull
    public List<ChatEvent> joinRoom(final @Nonnull String username) throws ChatRoomException
    {
        if (users.contains(username))
        {
            throw new ChatRoomException(new UserAlreadyJoined(username));
        }

        return Lists.newArrayList(new UserJoined(username));
    }

    @Nonnull
    public List<ChatEvent> sendMessage(final @Nonnull String username,
                                       final @Nonnull String message) throws ChatRoomException
    {
        if (!users.contains(username))
        {
            throw new ChatRoomException(new UserNotJoined(username));
        }

        return Lists.newArrayList(new MessageSent(username, message));
    }

    @Nonnull
    public List<ChatEvent> leaveRoom(final @Nonnull String username) throws ChatRoomException
    {
        if (!users.contains(username))
        {
            throw new ChatRoomException(new UserNotJoined(username));
        }

        return Lists.newArrayList(new UserLeft(username));
    }

    @Nonnull
    public List<ChatEvent> kickUser(final @Nonnull String username) throws ChatRoomException
    {
        if (!users.contains(username))
        {
            throw new ChatRoomException(new UserNotJoined(username));
        }

        return Lists.newArrayList(new UserKicked(username));
    }

    public void update(final @Nonnull ChatEvent event)
    {
        if (event instanceof UserJoined)
        {
            users.add(((UserJoined)event).getUsername());
        }
        else if (event instanceof UserLeft)
        {
            users.remove(((UserLeft)event).getUsername());
        }
        else if (event instanceof UserKicked)
        {
            users.remove(((UserKicked)event).getUsername());
        }
    }
}
