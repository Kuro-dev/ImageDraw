package org.kurodev.telegram;

import kurodev.reader.IniInstance;

public class MyTelegramBot implements Runnable {
    private final IniInstance settings;

    public MyTelegramBot(IniInstance settings) {
        this.settings = settings;
    }

    @Override
    public void run() {

    }
}
