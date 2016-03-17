package com.github.dohnal.chat.write;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.github.dohnal.chat.domain.protocol.ChatCommandResult;
import com.github.dohnal.chat.domain.protocol.command.ChatCommand;
import com.github.dohnal.chat.domain.protocol.command.JoinRoom;
import com.github.dohnal.chat.domain.protocol.command.LeaveRoom;
import com.github.dohnal.chat.domain.protocol.command.SendMessage;
import com.github.dohnal.chat.domain.protocol.event.ChatEvent;
import com.github.dohnal.chat.domain.protocol.event.MessageSent;
import com.github.dohnal.chat.domain.protocol.event.UserJoined;
import com.github.dohnal.chat.domain.protocol.event.UserKicked;
import com.github.dohnal.chat.domain.protocol.event.UserLeft;
import com.google.common.collect.Lists;
import scala.PartialFunction;
import scala.concurrent.duration.FiniteDuration;
import scala.runtime.BoxedUnit;

/**
 * @author dohnal
 */
public class ChatBotActor extends AbstractActor
{
    // min/max delay in seconds between bot messages
    private static final int MIN_DELAY = 1;
    private static final int MAX_DELAY = 5;

    private enum DECISION
    {
        SAY_HELLO,
        TALK,
        SAY_BYE,
        LEAVE
    }

    private final String name;

    private final ActorRef chatRoomActor;

    private boolean joined;

    private DECISION decision;

    public static Props props(final @Nonnull String name,
                              final @Nonnull ActorRef chatRoomActor)
    {
        return Props.create(ChatBotActor.class, () -> new ChatBotActor(name, chatRoomActor));
    }

    protected ChatBotActor(final @Nonnull String name,
                           final @Nonnull ActorRef chatRoomActor)
    {
        this.name = name;
        this.chatRoomActor = chatRoomActor;
        this.joined = false;

        context().system().eventStream().subscribe(self(), ChatEvent.class);

        scheduleNextMessage();
    }

    @Override
    public PartialFunction<Object, BoxedUnit> receive()
    {
        return ReceiveBuilder
                .match(UserJoined.class, e -> e.getUsername().equals(name), this::onJoined)
                .match(MessageSent.class, e -> e.getUsername().equals(name), this::onMessageSent)
                .match(UserLeft.class, e -> e.getUsername().equals(name), this::onLeft)
                .match(UserKicked.class, e -> e.getUsername().equals(name), this::onKicked)
                .match(ChatCommandResult.class, ChatCommandResult::isERROR, this::onError)
                .build();
    }

    protected void onJoined(final @Nonnull UserJoined event)
    {
        joined = true;

        // change decision to say hello
        decision = DECISION.SAY_HELLO;

        // schedule next message
        scheduleNextMessage();
    }

    protected void onMessageSent(final @Nonnull MessageSent event)
    {
        // if I said hello, then it is time to talk
        if (decision == DECISION.SAY_HELLO)
        {
            decision = DECISION.TALK;
        }
        // if I am talking, I can talk or say bye
        else if (decision == DECISION.TALK)
        {
            int roll = new Random().nextInt(3);

            // 25% chance to say bye
            if (roll == 0)
            {
                decision = DECISION.SAY_BYE;
            }
            // 75% change to continue talking
            else
            {
                decision = DECISION.TALK;
            }
        }
        // if I have said bye, it is time to leave
        else if (decision == DECISION.SAY_BYE)
        {
            decision = DECISION.LEAVE;
        }

        // schedule next message
        scheduleNextMessage();
    }

    protected void onLeft(final @Nonnull UserLeft event)
    {
        joined = false;
        decision = null;
    }

    protected void onKicked(final @Nonnull UserKicked event)
    {
        joined = false;
        decision = null;
    }

    protected void onError(final @Nonnull ChatCommandResult result)
    {
        // schedule next message
        scheduleNextMessage();
    }

    protected void scheduleNextMessage()
    {
        context().system().scheduler().scheduleOnce(nextDelay(), chatRoomActor,
                nextMessage(), context().dispatcher(), self());
    }

    @Nonnull
    protected ChatCommand nextMessage()
    {
        if (!joined)
        {
            return new JoinRoom(name);
        }

        switch (decision)
        {
            case SAY_HELLO: return new SendMessage(name, chooseSentence(getHelloSentences()));
            case TALK: return new SendMessage(name, chooseSentence(getTalkSentences()));
            case SAY_BYE: return new SendMessage(name, chooseSentence(getByeSentences()));
            case LEAVE: return new LeaveRoom(name);
        }

        return new SendMessage(name, chooseSentence(getTalkSentences()));
    }

    @Nonnull
    protected String chooseSentence(final @Nonnull List<String> sentences)
    {
        return sentences.get(new Random().nextInt(sentences.size() - 1));
    }

    @Nonnull
    protected FiniteDuration nextDelay()
    {
        int delay = new Random().nextInt(MAX_DELAY - MIN_DELAY) + MIN_DELAY;

        return new FiniteDuration(delay, TimeUnit.SECONDS);
    }

    @Nonnull
    private static List<String> getHelloSentences()
    {
        return Lists.newArrayList(
                "Hello everyone",
                "Hi !!",
                "Hello guys",
                "Hello bitches");
    }

    @Nonnull
    private static List<String> getTalkSentences()
    {
        return Lists.newArrayList(
                "Is there anybody?",
                "How are you guys?",
                "Shit",
                "It's nice weather today, isn't it?",
                "What are you doing?");
    }

    @Nonnull
    private static List<String> getByeSentences()
    {
        return Lists.newArrayList(
                "Fuck you all, I'm going out",
                "I'm going to leave, see you soon",
                "Bye mother fuckers",
                "Bye");
    }
}
