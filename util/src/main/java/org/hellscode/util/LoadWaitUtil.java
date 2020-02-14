package org.hellscode.util;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Facilitate waiting for loads in a Fragment or Activity
 */
public class LoadWaitUtil<T,R> {
    private final Object _loadLock = new Object();
    private boolean _loaded;
    private ArrayList<Semaphore> _semaphoreColl = new ArrayList<>();

    /**
     * Execute "load" work.
     * Loader will notify any waiting client code
     * when done.
     * @param r method that does load operation
     *          that needs to happen before waiting
     *          client code is called.  The argument to pass in is of type T.
     *          The return type is type R.
     * @param arg argument to pass in to method r
     */
    public R performLoad(@NonNull SimpleMethod<T,R> r, T arg) {
        R retVal;
        synchronized (_loadLock) {
            _loaded = false;
        }

        retVal = r.run(arg);

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
        return retVal;
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
