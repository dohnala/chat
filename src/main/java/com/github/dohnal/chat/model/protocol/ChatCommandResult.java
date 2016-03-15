package com.github.dohnal.chat.model.protocol;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Objects;

import com.google.common.collect.Lists;

/**
 * @author dohnal
 */
public class ChatCommandResult extends ChatMessage
{
    private Collection<ChatException> exceptions;

    public static final ChatCommandResult OK = new ChatCommandResult(Lists.newArrayList());

    public static ChatCommandResult ERROR(final @Nonnull Collection<ChatException> exceptions)
    {
        return new ChatCommandResult(exceptions);
    }

    private ChatCommandResult(final @Nonnull Collection<ChatException> exceptions)
    {
        super();

        this.exceptions = exceptions;
    }

    public boolean isOK()
    {
        return exceptions.isEmpty();
    }

    public boolean isERROR()
    {
        return !exceptions.isEmpty();
    }

    public Collection<ChatException> getErrors()
    {
        return exceptions;
    }

    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj) &&
                getClass() == obj.getClass() &&
                Objects.equals(exceptions, ((ChatCommandResult) obj).exceptions);
    }
}
