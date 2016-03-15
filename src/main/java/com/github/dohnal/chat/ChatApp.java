package com.github.dohnal.chat;

import java.util.Set;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.github.dohnal.chat.model.protocol.commands.JoinRoom;
import com.github.dohnal.chat.model.protocol.commands.SendMessage;
import com.google.common.collect.Sets;

/**
 * @author dohnal
 */
public class ChatApp
{
    private static final Set<String> WORD_BLACK_LIST = Sets.newHashSet("fuck");

    public static void main(String[] args)
    {
        // Create actor system
        final ActorSystem system = ActorSystem.create("system");

        // Create chat room actor
        final ActorRef chatRoom = system.actorOf(ChatRoomActor.props(), ChatRoomActor.NAME);

        // Create moderator
        final ActorRef moderator = system.actorOf(ChatModeratorActor.props(WORD_BLACK_LIST, chatRoom));

        // Join room as John
        chatRoom.tell(new JoinRoom("John"), ActorRef.noSender());

        chatRoom.tell(new SendMessage("John", "Hello!"), ActorRef.noSender());
        chatRoom.tell(new SendMessage("John", "Fuck! ... nobody here"), ActorRef.noSender());
    }
}
