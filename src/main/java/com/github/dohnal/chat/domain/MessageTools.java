package com.github.dohnal.chat.domain;

import javax.annotation.Nonnull;

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

    @Nonnull
    public static String convert(final @Nonnull ChatEvent event)
    {
        if (event instanceof UserJoined)
        {
            UserJoined userJoined = (UserJoined)event;

            return String.format(USER_JOINED_FORMAT, userJoined.getUsername());
        }
        else if (event instanceof MessageSent)
        {
            MessageSent messageSent = (MessageSent)event;

            return String.format(SENT_MESSAGE_FORMAT, messageSent.getUsername(), messageSent.getMessage());
        }
        else if (event instanceof UserLeft)
        {
            UserLeft userLeft = (UserLeft)event;

            return String.format(USER_LEFT_FORMAT, userLeft.getUsername());
        }
        else if (event instanceof UserKicked)
        {
            UserKicked userKicked = (UserKicked)event;

            return String.format(USER_KICKED_FORMAT, userKicked.getUsername(), userKicked.getKickedBy(),
                    userKicked.getReason());
        }

        return "";
    }
}
