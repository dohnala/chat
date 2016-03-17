package com.github.dohnal.chat.domain;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.dohnal.chat.domain.protocol.event.ChatEvent;
import com.github.dohnal.chat.domain.protocol.event.MessageSent;
import com.github.dohnal.chat.domain.protocol.event.UserJoined;
import com.github.dohnal.chat.domain.protocol.event.UserKicked;
import com.github.dohnal.chat.domain.protocol.event.UserLeft;

/**
 * @author dohnal
 */
public class MessageTools
{
    private static final String USER_JOINED_FORMAT = "%s has joined the chat room";
    private static final String SENT_MESSAGE_FORMAT = "%s: %s";
    private static final String USER_LEFT_FORMAT = "%s has left the chat room";
    private static final String USER_KICKED_FORMAT = "%s has been kicked by %s for: %s";

    @Nullable
    public static Message convert(final @Nonnull ChatEvent event)
    {
        final Message message = new Message();

        message.setDate(event.getDate());

        if (event instanceof UserJoined)
        {
            UserJoined userJoined = (UserJoined)event;

            message.setUsername(userJoined.getUsername());
            message.setMessage(String.format(USER_JOINED_FORMAT, userJoined.getUsername()));

            return message;
        }
        else if (event instanceof MessageSent)
        {
            MessageSent messageSent = (MessageSent)event;

            message.setUsername(messageSent.getUsername());
            message.setMessage(String.format(SENT_MESSAGE_FORMAT, messageSent.getUsername(), messageSent.getMessage()));

            return message;
        }
        else if (event instanceof UserLeft)
        {
            UserLeft userLeft = (UserLeft)event;

            message.setUsername(userLeft.getUsername());
            message.setMessage(String.format(USER_LEFT_FORMAT, userLeft.getUsername()));

            return message;
        }
        else if (event instanceof UserKicked)
        {
            UserKicked userKicked = (UserKicked)event;

            message.setUsername(userKicked.getUsername());
            message.setMessage(String.format(USER_KICKED_FORMAT, userKicked.getUsername(), userKicked.getKickedBy(),
                    userKicked.getReason()));

            return message;
        }

        return null;
    }
}
