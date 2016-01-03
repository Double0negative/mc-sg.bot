package org.mcsg.bot.skype.util;

import com.samczsun.skype4j.exceptions.SkypeException;

public class ProgressChatMessage {

    private ProgressBar bar;
    private Progress<?> prog;

    public ProgressChatMessage(Progress<?> prog, ProgressBar bar) {

        this.bar = bar;
        this.prog = prog;

    }

    public ProgressChatMessage(ProgressBar bar) {
        this.bar = bar;
    }

    public void doWait(String... str) throws SkypeException {
        if (prog != null) {
            while (!prog.isFinished()) {
                if (str != null && str.length > 0)
                    bar.setTitle(str[0] + " " + ((prog.getMessage() != null) ? prog.getMessage() : ""));
                bar.setProgress(prog.getPercent());
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                };
            }
        }
    }

    public ProgressChatMessage setProgress(Progress<?> prog) {
        this.prog = prog;
        return this;
    }

}
