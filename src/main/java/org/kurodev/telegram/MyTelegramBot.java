package org.kurodev.telegram;

import kurodev.reader.IniInstance;
//TODO make telegram bot. Do research on how Telegram commands work.
public class MyTelegramBot implements Runnable {
    private final IniInstance settings;

    public MyTelegramBot(IniInstance settings) {
        this.settings = settings;
    }

    @Override
    public void run() {

    }
}
