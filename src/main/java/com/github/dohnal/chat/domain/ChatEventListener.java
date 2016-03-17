package com.github.dohnal.chat.domain;

import javax.annotation.Nonnull;

import com.github.dohnal.chat.domain.protocol.event.ChatEvent;

/**
 * @author dohnal
 */
@FunctionalInterface
public interface ChatEventListener
{
    void onEvent(final @Nonnull ChatEvent event);
}
