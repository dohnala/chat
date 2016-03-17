package com.github.dohnal.chat.ui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;

import com.github.dohnal.chat.domain.ChatRepository;
import com.github.dohnal.chat.domain.ChatRoom;
import com.github.dohnal.chat.domain.ChatService;
import com.github.dohnal.chat.domain.Message;
import com.github.dohnal.chat.domain.MessageTools;
import com.github.dohnal.chat.domain.protocol.ChatCommandResult;
import com.github.dohnal.chat.domain.protocol.event.ChatEvent;
import com.github.dohnal.chat.domain.protocol.event.UserKicked;
import com.github.dohnal.chat.domain.protocol.event.UserLeft;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.data.sort.SortOrder;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * @author dohnal
 */
@UIScope
@SpringView(name = ChatRoomView.VIEW_NAME)
public class ChatRoomView extends VerticalLayout implements View
{
    public static final String VIEW_NAME = "chat";

    @Autowired
    private ChatEventBus chatEventBus;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatRepository chatRepository;

    private ChatRoom chatRoom;

    private Property<String> roomNameProperty;

    private Property<String> usernameProperty;

    private BeanItemContainer<Message> messagesContainer;

    private GeneratedPropertyContainer wrappedMessagesContainer;

    private Grid grid;

    private TextField messageField;

    private Button leaveButton;

    private Button sendMessageButton;

    private Button.ClickListener leaveButtonListener;

    private Button.ClickListener sendMessageListener;

    @PostConstruct
    public void init()
    {
        chatEventBus.getEventBus().register(this);

        this.leaveButtonListener = getLeaveButtonListener();
        this.sendMessageListener = getSendMessageListener();

        setSizeFull();
        setSpacing(true);

        usernameProperty = new ObjectProperty<>(null, String.class);
        roomNameProperty = new ObjectProperty<>(null, String.class);
        messagesContainer = new BeanItemContainer<>(Message.class);

        wrappedMessagesContainer = new GeneratedPropertyContainer(messagesContainer);
        wrappedMessagesContainer.removeContainerProperty("username");

        createChatRoomHeader();
        createChatMessageList();
        createSendMessageForm();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent)
    {
        final String loggedUsername = getLoggedUsername();

        if (loggedUsername != null)
        {
            roomNameProperty.setValue("Global");
            usernameProperty.setValue("Adam");

            loadData();

            leaveButton.addClickListener(leaveButtonListener);

            sendMessageButton.addClickListener(sendMessageListener);
            sendMessageButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);

            messageField.clear();
            messageField.focus();
        }
        else
        {
            UI.getCurrent().getNavigator().navigateTo(LoginView.VIEW_NAME);
        }
    }

    protected void loadData()
    {
        chatRoom = chatRepository.getChatRoom();

        roomNameProperty.setValue(chatRoom.getName());

        messagesContainer.removeAllItems();
        messagesContainer.addAll(chatRoom.getMessages());
        grid.scrollToEnd();

        usernameProperty.setValue(getLoggedUsername());
    }

    @Override
    public void detach()
    {
        leaveButton.removeClickListener(leaveButtonListener);
        sendMessageButton.removeClickListener(sendMessageListener);

        super.detach();
    }

    @Subscribe
    public void onUserLeft(final @Nonnull UserLeft event)
    {
        final String loggedUsername = getLoggedUsername();

        if (loggedUsername != null && event.getUsername().equals(loggedUsername))
        {
            VaadinSession.getCurrent().setAttribute(Constants.LOGGED_USERNAME, null);
            UI.getCurrent().getNavigator().navigateTo(LoginView.VIEW_NAME);
        }
    }

    @Subscribe
    public void onUserKicked(final @Nonnull UserKicked event)
    {
        final String loggedUsername = getLoggedUsername();

        if (loggedUsername != null && event.getUsername().equals(loggedUsername))
        {
            VaadinSession.getCurrent().setAttribute(Constants.LOGGED_USERNAME, null);
            showKickedDialog(event.getKickedBy(), event.getReason());
        }
    }

    @Subscribe
    public void onEvent(final @Nonnull ChatEvent event)
    {
        Message message = MessageTools.convert(event);

        if (message != null)
        {
            messagesContainer.addBean(message);

            grid.scrollToEnd();
        }
    }

    protected void createChatRoomHeader()
    {
        HorizontalLayout header = new HorizontalLayout();
        header.setMargin(true);
        header.setWidth("100%");

        HorizontalLayout roomInfoLayout = new HorizontalLayout();
        roomInfoLayout.setSpacing(true);
        roomInfoLayout.setHeight("100%");

        Label roomNameCaption = new Label("Chat room: ");
        roomNameCaption.addStyleName(ValoTheme.LABEL_BOLD);

        Label roomName = new Label(this.roomNameProperty);

        roomInfoLayout.addComponent(roomNameCaption);
        roomInfoLayout.setComponentAlignment(roomNameCaption, Alignment.MIDDLE_LEFT);
        roomInfoLayout.addComponent(roomName);
        roomInfoLayout.setComponentAlignment(roomName, Alignment.MIDDLE_LEFT);

        header.addComponent(roomInfoLayout);
        header.setComponentAlignment(roomInfoLayout, Alignment.MIDDLE_LEFT);

        HorizontalLayout userInfoLayout = new HorizontalLayout();
        userInfoLayout.setSpacing(true);
        userInfoLayout.setHeight("100%");

        Label usernameCaption = new Label("Logged as: ");
        usernameCaption.addStyleName(ValoTheme.LABEL_BOLD);

        Label usernameLabel = new Label(usernameProperty);

        leaveButton = new Button("Leave");
        leaveButton.addStyleName(ValoTheme.BUTTON_LINK);

        userInfoLayout.addComponent(usernameCaption);
        userInfoLayout.setComponentAlignment(usernameCaption, Alignment.MIDDLE_RIGHT);
        userInfoLayout.addComponent(usernameLabel);
        userInfoLayout.setComponentAlignment(usernameLabel, Alignment.MIDDLE_RIGHT);
        userInfoLayout.addComponent(leaveButton);
        userInfoLayout.setComponentAlignment(leaveButton, Alignment.MIDDLE_RIGHT);

        header.addComponent(userInfoLayout);
        header.setComponentAlignment(userInfoLayout, Alignment.MIDDLE_RIGHT);

        addComponent(new Panel(header));
    }

    protected void createChatMessageList()
    {
        grid = new Grid();

        grid.setSizeFull();
        grid.setContainerDataSource(wrappedMessagesContainer);

        grid.getColumn("date")
                .setConverter(new StringToDateConverter())
                .setSortable(true)
                .setWidth(250);

        grid.getColumn("message")
                .setExpandRatio(1);

        grid.setSortOrder(Lists.newArrayList(new SortOrder("date", SortDirection.ASCENDING)));

        addComponent(grid);
        setExpandRatio(grid, 1.0f);
    }

    protected void createSendMessageForm()
    {
        HorizontalLayout sendMessageForm = new HorizontalLayout();
        sendMessageForm.setMargin(true);
        sendMessageForm.setSpacing(true);
        sendMessageForm.setWidth("100%");

        messageField = new TextField();
        messageField.setImmediate(true);
        messageField.setSizeFull();

        sendMessageButton = new Button("Send");
        sendMessageButton.addStyleName(ValoTheme.BUTTON_PRIMARY);

        sendMessageForm.addComponent(messageField);
        sendMessageForm.setExpandRatio(messageField, 1.0f);

        sendMessageForm.addComponent(sendMessageButton);

        addComponent(new Panel(sendMessageForm));
    }

    protected void showKickedDialog(final @Nonnull String kickedBy,
                                    final @Nonnull String reason)
    {
        final Window subWindow = new Window();

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);

        Label message = new Label("You have been kicked by " + kickedBy + " for: " + reason);

        message.addStyleName(ValoTheme.LABEL_FAILURE);

        Button okButton = new Button("OK", event -> subWindow.close());
        okButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        okButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        okButton.focus();

        layout.addComponent(message);
        layout.addComponent(okButton);
        layout.setComponentAlignment(okButton, Alignment.BOTTOM_RIGHT);

        subWindow.setModal(true);
        subWindow.setClosable(false);
        subWindow.setDraggable(false);
        subWindow.setResizable(false);
        subWindow.setContent(layout);
        subWindow.center();
        subWindow.addCloseListener(e -> {
            UI.getCurrent().removeWindow(subWindow);
            UI.getCurrent().getNavigator().navigateTo(LoginView.VIEW_NAME);
        });

        UI.getCurrent().addWindow(subWindow);
    }

    @Nonnull
    protected Button.ClickListener getLeaveButtonListener()
    {
        return event -> {
            final String loggedUsername = getLoggedUsername();

            if (loggedUsername != null)
            {
                final ChatCommandResult result = chatService.leaveRoom(loggedUsername);

                if (result.isERROR())
                {
                    new Notification("Error", Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
                }
            }
        };
    }

    @Nonnull
    protected Button.ClickListener getSendMessageListener()
    {
        return event -> {
            final String loggedUsername = getLoggedUsername();
            final String message = messageField.getValue();

            if (loggedUsername != null && StringUtils.hasText(message))
            {
                final ChatCommandResult result = chatService.sendMessage(loggedUsername, message);

                if (result.isERROR())
                {
                    new Notification("Error", Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
                }

                messageField.clear();
                messageField.focus();
            }
        };
    }

    @Nullable
    protected String getLoggedUsername()
    {
        return (String) VaadinSession.getCurrent().getAttribute(Constants.LOGGED_USERNAME);
    }
}
