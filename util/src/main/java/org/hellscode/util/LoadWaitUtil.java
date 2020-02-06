package org.hellscode.util;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Facilitate waiting for loads in a Fragment or Activity
 */
public class LoadWaitUtil {
    private final Object _loadLock = new Object();
    private boolean _loaded;
    private ArrayList<Semaphore> _semaphoreColl = new ArrayList<>();

    /**
     * Execute "load" work.
     * Loader will notify any waiting client code
     * when done.
     * @param r method that does load operation
     *          that needs to happen before waiting
     *          client code is called
     */
    public void performLoad(@NonNull Runnable r) {
        synchronized (_loadLock) {
            _loaded = false;
        }

        r.run();

        synchronized (_loadLock) {
            _loaded = true;

            // go through semaphore collection and release
            for(Semaphore s : _semaphoreColl) {
                if (s != null) {
                    s.release();
                }
            }
            _semaphoreColl.clear();
        }
    }

    /**
     * Tell this object to execute a given method once the loading
     * is complete.
     *
     * @param r method to execute once the registered load
     *          operation is complete.
     */
    public void waitForLoad(@NonNull final Runnable r) {
        synchronized (_loadLock) {
            if (!_loaded) {
                // we'll have to wait.
                // Make a semaphore.  Shove it into the collection and block with call to acquire().

                Thread t = new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                Semaphore s = new Semaphore(0);
                                _semaphoreColl.add(s);
                                try {
                                    s.acquire();
                                } catch(InterruptedException ex) {
                                    // Fuck it.  We tried.  Do nothing and proceed.
                                }

                                synchronized (_loadLock) {
                                    r.run();
                                }
                            }
                        }
                );
                t.start();

            } else {
                r.run();
            }
        }
    }
}
