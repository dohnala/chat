package com.github.dohnal.chat.model.protocol;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author dohnal
 */
public abstract class ChatMessage implements Serializable
{
    @Override
    public boolean equals(Object obj)
    {
        return obj != null && getClass() == obj.getClass();
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
