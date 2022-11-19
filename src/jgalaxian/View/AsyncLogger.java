package jgalaxian.View;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

import java.util.logging.*;

public class AsyncLogger extends Thread implements Closeable {

    private Logger logger;
    private Queue<String> coda;
    private boolean esegui;
    private StringBuilder buffer;

    public AsyncLogger(Logger logger) {
        super("Logger");
        coda = new ArrayDeque<>(100000);
        esegui = false;
        this.logger = logger;
        this.setPriority(Thread.MIN_PRIORITY + 1);
        buffer = new StringBuilder();
        buffer.ensureCapacity(35000 * 100);
    }

    public void log(String s) {
		//Si può abilitare per debug ma può rallentare l'esecuzione
        /*synchronized (coda) {
            coda.add(s);
        }
        synchronized (this) {
            this.notify();
        }*/
    }

    @Override
    public void run() {
        while (true) {
            String s = null;
            synchronized (coda) {
                if (!coda.isEmpty()) {
                    s = coda.remove();
                }
            }
            if (s != null) {
                buffer.append(s + "\n");
            } else {
                System.out.print(buffer);
                buffer.setLength(0);
                if (esegui) {
                    aspetta();
                } else {
                    break;
                }
            }
        }
    }

    private void aspetta() {
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(AsyncLogger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void close() throws IOException {
        esegui = false;
        synchronized (this) {
            this.notify();
        }
    }
}
