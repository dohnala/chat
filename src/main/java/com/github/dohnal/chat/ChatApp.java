package com.github.dohnal.chat;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.github.dohnal.chat.model.protocol.commands.SendMessage;
import scala.concurrent.duration.Duration;

/**
 * @author dohnal
 */
public class ChatApp
{
    public static void main(String[] args)
    {
        // Create actor system
        final ActorSystem system = ActorSystem.create("system");

        // Create chat room actor
        final ActorRef chatRoom = system.actorOf(ChatRoomActor.props(), ChatRoomActor.NAME);

        // Send message to chat room every second

        system.scheduler().schedule(
                Duration.Zero(),
                Duration.create(1, TimeUnit.SECONDS),
                chatRoom,
                new SendMessage("UserA", "Hello"),
                system.dispatcher(),
                null);
    }
}
