package com.github.dohnal.chat.ui;

import javax.annotation.Nonnull;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

import com.github.dohnal.chat.domain.ChatEventListener;
import com.github.dohnal.chat.domain.ChatRepository;
import com.github.dohnal.chat.domain.protocol.event.ChatEvent;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.EnableVaadin;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoaderListener;

/**
 * @author dohnal
 */
@Push
@SpringUI
@Theme("valo")
@Widgetset("MyAppWidgetset")
public class ChatUI extends UI implements ChatEventListener
{
    @Autowired
    private SpringViewProvider viewProvider;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatEventBus chatEventBus;

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    public static class Servlet extends SpringVaadinServlet
    { }

    @WebListener
    public static class MyContextLoaderListener extends ContextLoaderListener
    { }

    @Configuration
    @EnableVaadin
    public static class MyConfiguration
    { }

    @Override
    protected void init(VaadinRequest vaadinRequest)
    {
        final VerticalLayout root = new VerticalLayout();

        root.setSizeFull();
        root.setMargin(true);
        root.setSpacing(true);
        setContent(root);

        Navigator navigator = new Navigator(this, root);
        navigator.addProvider(viewProvider);

        chatRepository.addEventListener(this);
    }

    @Override
    public void detach()
    {
        chatRepository.removeEventListener(this);
    }

    @Override
    public void onEvent(final @Nonnull ChatEvent event)
    {
        access(() -> chatEventBus.getEventBus().post(event));
    }
}
