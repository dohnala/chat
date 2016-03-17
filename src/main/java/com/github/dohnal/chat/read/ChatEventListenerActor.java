package com.github.dohnal.chat.read;

import javax.annotation.Nonnull;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.github.dohnal.chat.domain.ChatEventListener;
import com.github.dohnal.chat.domain.protocol.event.ChatEvent;

/**
 * @author dohnal
 */
public class ChatEventListenerActor extends AbstractActor
{
    public static Props props(final @Nonnull ChatEventListener listener)
    {
        return Props.create(ChatEventListenerActor.class, () -> new ChatEventListenerActor(listener));
    }

    protected ChatEventListenerActor(final @Nonnull ChatEventListener listener)
    {
        context().system().eventStream().subscribe(self(), ChatEvent.class);

        receive(ReceiveBuilder
                .match(ChatEvent.class, listener::onEvent)
                .build());
    }
}
