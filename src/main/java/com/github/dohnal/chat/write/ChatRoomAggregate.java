package com.github.dohnal.chat.write;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.github.dohnal.chat.domain.protocol.event.ChatEvent;
import com.github.dohnal.chat.domain.protocol.event.MessageSent;
import com.github.dohnal.chat.domain.protocol.event.UserJoined;
import com.github.dohnal.chat.domain.protocol.event.UserKicked;
import com.github.dohnal.chat.domain.protocol.event.UserLeft;
import com.github.dohnal.chat.domain.protocol.exception.UserAlreadyJoined;
import com.github.dohnal.chat.domain.protocol.exception.UserNotJoined;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * @author dohnal
 */
public class ChatRoomAggregate implements Serializable
{
    private final Set<String> users;

    public ChatRoomAggregate()
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

        return Lists.newArrayList(new UserJoined(username, new Date()));
    }

    @Nonnull
    public List<ChatEvent> sendMessage(final @Nonnull String username,
                                       final @Nonnull String message) throws ChatRoomException
    {
        if (!users.contains(username))
        {
            throw new ChatRoomException(new UserNotJoined(username));
        }

        return Lists.newArrayList(new MessageSent(username, message, new Date()));
    }

    @Nonnull
    public List<ChatEvent> leaveRoom(final @Nonnull String username) throws ChatRoomException
    {
        if (!users.contains(username))
        {
            throw new ChatRoomException(new UserNotJoined(username));
        }

        return Lists.newArrayList(new UserLeft(username, new Date()));
    }

    @Nonnull
    public List<ChatEvent> kickUser(final @Nonnull String username,
                                    final @Nonnull String kickedBy,
                                    final @Nonnull String reason) throws ChatRoomException
    {
        if (!users.contains(username))
        {
            throw new ChatRoomException(new UserNotJoined(username));
        }

        return Lists.newArrayList(new UserKicked(username, kickedBy, reason, new Date()));
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
