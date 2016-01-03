package org.mcsg.bot.skype.util;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.chat.messages.SentMessage;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.samczsun.skype4j.exceptions.SkypeException;
import com.samczsun.skype4j.formatting.Message;

public class ProgressBar {

    private static final int LENGTH = 20;

    private String title;
    private Chat chat;
    private SentMessage msg;
    private int percent;

    public ProgressBar(String title, Chat chat) throws SkypeException {
        this.title = title;
        this.chat = chat;
        setProgress(0);
    }

    public void setProgress(int percent) throws SkypeException {
        this.percent = percent;

        updateMessage();
    }

    public void finish(String message) {
        try {
            msg.edit(Message.fromHtml(message));
        } catch (Exception e) {
        }
    }

    private void updateMessage() throws ConnectionException {
        if (msg == null) {
            msg = (SentMessage) chat.sendMessage(title + " [" + getProgressString() + "] " + percent + "%");
        } else {
            msg.edit(Message.fromHtml(title + " [" + getProgressString() + "] " + percent + "%"));
        }
    }

    private String getProgressString() {
        StringBuilder sb = new StringBuilder();
        int per = percent / (100 / LENGTH);
        for (int a = 0; a < LENGTH; a++) {
            sb.append(a < per ? "=" : "_");
        }
        return sb.toString();
    }

    public void setProgress(double percent) throws SkypeException {
        this.percent = (int) (percent * 100);
        updateMessage();
    }

    public void setTitle(String string) {
        this.title = string;
    }
}
