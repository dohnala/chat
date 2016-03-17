package com.github.dohnal.chat.ui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;

import com.github.dohnal.chat.domain.ChatService;
import com.github.dohnal.chat.domain.protocol.ChatCommandResult;
import com.github.dohnal.chat.domain.protocol.event.UserJoined;
import com.google.common.eventbus.Subscribe;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * @author dohnal
 */
@UIScope
@SpringView(name = LoginView.VIEW_NAME)
public class LoginView extends VerticalLayout implements View
{
    public static final String VIEW_NAME = "";

    @Autowired
    private ChatEventBus chatEventBus;

    @Autowired
    private ChatService chatService;

    private TextField usernameField;

    private Button joinButton;

    private Button.ClickListener joinButtonListener;

    @PostConstruct
    public void init()
    {
        chatEventBus.getEventBus().register(this);

        this.joinButtonListener = getJoinButtonListener();

        setSizeFull();

        final HorizontalLayout loginLayout = new HorizontalLayout();
        loginLayout.setSizeUndefined();
        loginLayout.setMargin(true);
        loginLayout.setSpacing(true);

        final Panel loginPanel = new Panel(loginLayout);
        loginPanel.setCaption("Select username");
        loginPanel.setSizeUndefined();

        addComponent(loginPanel);
        setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);

        usernameField = new TextField();
        usernameField.setNullRepresentation("");
        usernameField.setImmediate(true);

        joinButton = new Button("Join");
        joinButton.addStyleName(ValoTheme.BUTTON_PRIMARY);

        CssLayout group = new CssLayout();
        group.addStyleName("v-component-group");
        group.addComponents(usernameField, joinButton);

        loginLayout.addComponents(group);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent)
    {
        final String loggedUser = getLoggedUsername();

        if (loggedUser != null)
        {
            UI.getCurrent().getNavigator().navigateTo(ChatRoomView.VIEW_NAME);
        }
        else
        {
            usernameField.setValue(null);
            usernameField.focus();

            joinButton.addClickListener(joinButtonListener);
            joinButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        }
    }

    @Override
    public void detach()
    {
        joinButton.removeClickListener(joinButtonListener);

        super.detach();
    }

    @Subscribe
    public void onUserJoined(final @Nonnull UserJoined event)
    {
        final String loggedUser = getLoggedUsername();

        if (loggedUser != null && event.getUsername().equals(loggedUser))
        {
            UI.getCurrent().getNavigator().navigateTo(ChatRoomView.VIEW_NAME);
        }
    }

    @Nonnull
    protected Button.ClickListener getJoinButtonListener()
    {
        return event -> {
            final String username = usernameField.getValue();

            if (StringUtils.hasText(username))
            {
                final ChatCommandResult result = chatService.joinRoom(username);

                if (result.isERROR())
                {
                    usernameField.clear();
                    usernameField.focus();

                    new Notification("Error", Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
                }
                else
                {
                    VaadinSession.getCurrent().setAttribute(Constants.LOGGED_USERNAME, username);
                }
            }
        };
    }

    @Nullable
    protected String getLoggedUsername()
    {
        return (String)VaadinSession.getCurrent().getAttribute(Constants.LOGGED_USERNAME);
    }
}
