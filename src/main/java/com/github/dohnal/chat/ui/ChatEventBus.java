package com.github.dohnal.chat.ui;

import javax.annotation.Nonnull;
import java.io.Serializable;

import com.google.common.eventbus.EventBus;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

/**
 * @author dohnal
 */
@UIScope
@SpringComponent
public class ChatEventBus implements Serializable
{
    private final EventBus eventBus;

    public ChatEventBus()
    {
        this.eventBus = new EventBus();
    }

    @Nonnull
    public EventBus getEventBus()
    {
        return eventBus;
    }
}
