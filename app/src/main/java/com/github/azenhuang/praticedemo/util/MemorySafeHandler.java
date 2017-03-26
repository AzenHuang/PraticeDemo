package com.github.azenhuang.praticedemo.util;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;
import android.util.Printer;

import java.lang.ref.WeakReference;

/**
 * 防止内存泄漏的Handler，用法同{@link Handler}。参照系统{@link Handler}对外提供的方法，并实现之。
 * 通过弱引用机制，切断Looper线程对引用了外部类（通常为Activity或Fragment）的内部类（Handler、Runnable、Callback）的强引用关系，
 * 因此Looper线程不会阻止外部类被回收，达到防止内存泄漏的目的
 * Memory safer implementation of android.os.Handler
 * <p>
 * Created by elvis on 17/3/26.
 */
public class MemorySafeHandler {
    private final Handler.Callback mCallback; // hard reference to Callback. We need to keep callback in memory
    private final ExecHandler execHandler;
    private final SimpleArrayMap<Runnable, ExecRunnable> runnableMap = new SimpleArrayMap<>();

    /**
     * Subclasses must implement this to receive messages.
     */
    public void handleMessage(Message msg) {
    }

    /**
     * Handle system messages here.
     */
    public void dispatchMessage(Message msg) {
        if (msg.getCallback() != null) {
            msg.getCallback().run();
        } else {
            if (mCallback != null) {
                if (mCallback.handleMessage(msg)) {
                    return;
                }
            }
            handleMessage(msg);
        }
    }

    /**
     * Default constructor associates this handler with the {@link Looper} for the
     * current thread.
     * <p>
     * If this thread does not have a looper, this handler won't be able to receive messages
     * so an exception is thrown.
     */
    public MemorySafeHandler() {
        this((Handler.Callback) null);
    }

    /**
     * Constructor associates this handler with the {@link Looper} for the
     * current thread and takes a callback interface in which you can handle
     * messages.
     * <p>
     * If this thread does not have a looper, this handler won't be able to receive messages
     * so an exception is thrown.
     *
     * @param callback The callback interface in which to handle messages, or null.
     */
    public MemorySafeHandler(Handler.Callback callback) {
        mCallback = callback;
        execHandler = new ExecHandler(this, callback);
    }

    /**
     * Use the provided {@link Looper} instead of the default one.
     *
     * @param looper The looper, must not be null.
     */
    public MemorySafeHandler(Looper looper) {
        this(looper, null);
    }

    /**
     * Use the provided {@link Looper} instead of the default one and take a callback
     * interface in which to handle messages.
     *
     * @param looper   The looper, must not be null.
     * @param callback The callback interface in which to handle messages, or null.
     */
    public MemorySafeHandler(Looper looper, Handler.Callback callback) {
        mCallback = callback;
        execHandler = new ExecHandler(this, looper, callback);
    }


    /**
     * Returns a string representing the name of the specified message.
     * The default implementation will either return the class name of the
     * message callback if any, or the hexadecimal representation of the
     * message "what" field.
     *
     * @param message The message whose name is being queried
     */
    public String getMessageName(Message message) {
        return execHandler.getMessageName(message);
    }

    /**
     * Returns a new {@link android.os.Message Message} from the global message pool. More efficient than
     * creating and allocating new instances. The retrieved message has its handler set to this instance (Message.target == this).
     * If you don't want that facility, just call Message.obtain() instead.
     */
    public final Message obtainMessage() {
        return Message.obtain(execHandler);
    }

    /**
     * Same as {@link #obtainMessage()}, except that it also sets the what member of the returned Message.
     *
     * @param what Value to assign to the returned Message.what field.
     * @return A Message from the global message pool.
     */
    public final Message obtainMessage(int what) {
        return Message.obtain(execHandler, what);
    }

    /**
     * Same as {@link #obtainMessage()}, except that it also sets the what and obj members
     * of the returned Message.
     *
     * @param what Value to assign to the returned Message.what field.
     * @param obj  Value to assign to the returned Message.obj field.
     * @return A Message from the global message pool.
     */
    public final Message obtainMessage(int what, Object obj) {
        return Message.obtain(execHandler, what, obj);
    }

    /**
     * Same as {@link #obtainMessage()}, except that it also sets the what, arg1 and arg2 members of the returned
     * Message.
     *
     * @param what Value to assign to the returned Message.what field.
     * @param arg1 Value to assign to the returned Message.arg1 field.
     * @param arg2 Value to assign to the returned Message.arg2 field.
     * @return A Message from the global message pool.
     */
    public final Message obtainMessage(int what, int arg1, int arg2) {
        return Message.obtain(execHandler, what, arg1, arg2);
    }

    /**
     * Same as {@link #obtainMessage()}, except that it also sets the what, obj, arg1,and arg2 values on the
     * returned Message.
     *
     * @param what Value to assign to the returned Message.what field.
     * @param arg1 Value to assign to the returned Message.arg1 field.
     * @param arg2 Value to assign to the returned Message.arg2 field.
     * @param obj  Value to assign to the returned Message.obj field.
     * @return A Message from the global message pool.
     */
    public final Message obtainMessage(int what, int arg1, int arg2, Object obj) {
        return Message.obtain(execHandler, what, arg1, arg2, obj);
    }

    /**
     * Causes the Runnable r to be added to the message queue.
     * The runnable will be run on the thread to which this handler is
     * attached.
     *
     * @param r The Runnable that will be executed.
     * @return Returns true if the Runnable was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.
     */
    public final boolean post(Runnable r) {
        return execHandler.post(wrapRunnable(r));
    }

    /**
     * Causes the Runnable r to be added to the message queue, to be run
     * at a specific time given by <var>uptimeMillis</var>.
     * <b>The time-base is {@link android.os.SystemClock#uptimeMillis}.</b>
     * Time spent in deep sleep will add an additional delay to execution.
     * The runnable will be run on the thread to which this handler is attached.
     *
     * @param r            The Runnable that will be executed.
     * @param uptimeMillis The absolute time at which the callback should run,
     *                     using the {@link android.os.SystemClock#uptimeMillis} time-base.
     * @return Returns true if the Runnable was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.  Note that a
     * result of true does not mean the Runnable will be processed -- if
     * the looper is quit before the delivery time of the message
     * occurs then the message will be dropped.
     */
    public final boolean postAtTime(Runnable r, long uptimeMillis) {
        return execHandler.postAtTime(wrapRunnable(r), uptimeMillis);
    }

    /**
     * Causes the Runnable r to be added to the message queue, to be run
     * at a specific time given by <var>uptimeMillis</var>.
     * <b>The time-base is {@link android.os.SystemClock#uptimeMillis}.</b>
     * Time spent in deep sleep will add an additional delay to execution.
     * The runnable will be run on the thread to which this handler is attached.
     *
     * @param r            The Runnable that will be executed.
     * @param uptimeMillis The absolute time at which the callback should run,
     *                     using the {@link android.os.SystemClock#uptimeMillis} time-base.
     * @return Returns true if the Runnable was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.  Note that a
     * result of true does not mean the Runnable will be processed -- if
     * the looper is quit before the delivery time of the message
     * occurs then the message will be dropped.
     * @see android.os.SystemClock#uptimeMillis
     */
    public final boolean postAtTime(Runnable r, Object token, long uptimeMillis) {
        return execHandler.postAtTime(wrapRunnable(r), token, uptimeMillis);
    }

    /**
     * Causes the Runnable r to be added to the message queue, to be run
     * after the specified amount of time elapses.
     * The runnable will be run on the thread to which this handler
     * is attached.
     * <b>The time-base is {@link android.os.SystemClock#uptimeMillis}.</b>
     * Time spent in deep sleep will add an additional delay to execution.
     *
     * @param r           The Runnable that will be executed.
     * @param delayMillis The delay (in milliseconds) until the Runnable
     *                    will be executed.
     * @return Returns true if the Runnable was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.  Note that a
     * result of true does not mean the Runnable will be processed --
     * if the looper is quit before the delivery time of the message
     * occurs then the message will be dropped.
     */
    public final boolean postDelayed(Runnable r, long delayMillis) {
        return execHandler.postDelayed(wrapRunnable(r), delayMillis);
    }

    /**
     * Posts a message to an object that implements Runnable.
     * Causes the Runnable r to executed on the next iteration through the
     * message queue. The runnable will be run on the thread to which this
     * handler is attached.
     * <b>This method is only for use in very special circumstances -- it
     * can easily starve the message queue, cause ordering problems, or have
     * other unexpected side-effects.</b>
     *
     * @param r The Runnable that will be executed.
     * @return Returns true if the message was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.
     */
    public final boolean postAtFrontOfQueue(Runnable r) {
        return execHandler.postAtFrontOfQueue(wrapRunnable(r));
    }

    /**
     * Remove any pending posts of Runnable r that are in the message queue.
     */
    public final void removeCallbacks(Runnable r) {
        execHandler.removeCallbacks(runnableMap.get(r));
    }

    /**
     * Remove any pending posts of Runnable <var>r</var> with Object
     * <var>token</var> that are in the message queue.  If <var>token</var> is null,
     * all callbacks will be removed.
     */
    public final void removeCallbacks(Runnable r, Object token) {
        execHandler.removeCallbacks(runnableMap.get(r), token);
    }

    /**
     * Pushes a message onto the end of the message queue after all pending messages
     * before the current time. It will be received in {@link #handleMessage},
     * in the thread attached to this handler.
     *
     * @return Returns true if the message was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.
     */
    public final boolean sendMessage(Message msg) {
        return execHandler.sendMessage(msg);
    }

    /**
     * Sends a Message containing only the what value.
     *
     * @return Returns true if the message was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.
     */
    public final boolean sendEmptyMessage(int what) {
        return execHandler.sendEmptyMessage(what);
    }

    /**
     * Sends a Message containing only the what value, to be delivered
     * after the specified amount of time elapses.
     *
     * @return Returns true if the message was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.
     * @see #sendMessageDelayed(android.os.Message, long)
     */
    public final boolean sendEmptyMessageDelayed(int what, long delayMillis) {
        return execHandler.sendEmptyMessageDelayed(what, delayMillis);
    }

    /**
     * Sends a Message containing only the what value, to be delivered
     * at a specific time.
     *
     * @return Returns true if the message was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.
     * @see #sendMessageAtTime(android.os.Message, long)
     */

    public final boolean sendEmptyMessageAtTime(int what, long uptimeMillis) {
        return execHandler.sendEmptyMessageAtTime(what, uptimeMillis);
    }

    /**
     * Enqueue a message into the message queue after all pending messages
     * before (current time + delayMillis). You will receive it in
     * {@link #handleMessage}, in the thread attached to this handler.
     *
     * @return Returns true if the message was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.  Note that a
     * result of true does not mean the message will be processed -- if
     * the looper is quit before the delivery time of the message
     * occurs then the message will be dropped.
     */
    public final boolean sendMessageDelayed(Message msg, long delayMillis) {
        return execHandler.sendMessageDelayed(msg, delayMillis);
    }

    /**
     * Enqueue a message into the message queue after all pending messages
     * before the absolute time (in milliseconds) <var>uptimeMillis</var>.
     * <b>The time-base is {@link android.os.SystemClock#uptimeMillis}.</b>
     * Time spent in deep sleep will add an additional delay to execution.
     * You will receive it in {@link #handleMessage}, in the thread attached
     * to this handler.
     *
     * @param uptimeMillis The absolute time at which the message should be
     *                     delivered, using the
     *                     {@link android.os.SystemClock#uptimeMillis} time-base.
     * @return Returns true if the message was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.  Note that a
     * result of true does not mean the message will be processed -- if
     * the looper is quit before the delivery time of the message
     * occurs then the message will be dropped.
     */
    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        return execHandler.sendMessageAtTime(msg, uptimeMillis);
    }

    /**
     * Enqueue a message at the front of the message queue, to be processed on
     * the next iteration of the message loop.  You will receive it in
     * {@link #handleMessage}, in the thread attached to this handler.
     * <b>This method is only for use in very special circumstances -- it
     * can easily starve the message queue, cause ordering problems, or have
     * other unexpected side-effects.</b>
     *
     * @return Returns true if the message was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.
     */
    public final boolean sendMessageAtFrontOfQueue(Message msg) {
        return execHandler.sendMessageAtFrontOfQueue(msg);
    }

    /**
     * Remove any pending posts of messages with code 'what' that are in the
     * message queue.
     */
    public final void removeMessages(int what) {
        execHandler.removeMessages(what);
    }

    /**
     * Remove any pending posts of messages with code 'what' and whose obj is
     * 'object' that are in the message queue.  If <var>object</var> is null,
     * all messages will be removed.
     */
    public final void removeMessages(int what, Object object) {
        execHandler.removeMessages(what, object);
    }

    /**
     * Remove any pending posts of callbacks and sent messages whose
     * <var>obj</var> is <var>token</var>.  If <var>token</var> is null,
     * all callbacks and messages will be removed.
     */
    public final void removeCallbacksAndMessages(Object token) {
        execHandler.removeCallbacksAndMessages(token);
    }

    /**
     * Check if there are any pending posts of messages with code 'what' in
     * the message queue.
     */
    public final boolean hasMessages(int what) {
        return execHandler.hasMessages(what);
    }

    /**
     * Check if there are any pending posts of messages with code 'what' and
     * whose obj is 'object' in the message queue.
     */
    public final boolean hasMessages(int what, Object object) {
        return execHandler.hasMessages(what, object);
    }

    // if we can get rid of this method, the handler need not remember its loop
    // we could instead export a getMessageQueue() method...
    public final Looper getLooper() {
        return execHandler.getLooper();
    }

    public final void dump(Printer pw, String prefix) {
        execHandler.dump(pw, prefix);
    }

    @Override
    public String toString() {
        return "MemorySafeHandler (" + getClass().getName() + ") {"
                + Integer.toHexString(System.identityHashCode(this))
                + "}";
    }

    private ExecRunnable wrapRunnable(@NonNull Runnable r) {
        if (r == null) {
            throw new NullPointerException("Runnable can't be null");
        }
        ExecRunnable execRunnable = new ExecRunnable(r);
        runnableMap.put(r, execRunnable);
        return execRunnable;
    }

    private static final class ExecHandler extends Handler {
        private final WeakReference<Callback> mCallbackRef;
        private final WeakReference<MemorySafeHandler> mSafeHandlerRef;

        ExecHandler(MemorySafeHandler safeHandler) {
            this(safeHandler, (Callback) null);
        }

        ExecHandler(MemorySafeHandler safeHandler, Callback callback) {
            mSafeHandlerRef = new WeakReference<>(safeHandler);
            mCallbackRef = new WeakReference<>(callback);
        }

        ExecHandler(MemorySafeHandler safeHandler, Looper looper) {
            this(safeHandler, looper, null);
        }

        ExecHandler(MemorySafeHandler safeHandler, Looper looper, Callback callback) {
            super(looper);
            mSafeHandlerRef = new WeakReference<>(safeHandler);
            mCallbackRef = new WeakReference<>(callback);
        }

        @Override
        public void dispatchMessage(Message msg) {
            MemorySafeHandler safeHandler = mSafeHandlerRef.get();
            if (safeHandler != null) {
                safeHandler.dispatchMessage(msg);
            }
        }

//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            if (mCallbackRef != null && mCallbackRef.get() != null) {
//                mCallbackRef.get().handleMessage(msg);
//            } else {
//                MemorySafeHandler weakHandler = mSafeHandlerRef.get();
//                if (weakHandler != null) {
//                    weakHandler.handleMessage(msg);
//                }
//            }
//        }
    }

    private static final class ExecRunnable implements Runnable {
        private final WeakReference<Runnable> mDelegate;

        ExecRunnable(Runnable delegate) {
            mDelegate = new WeakReference<>(delegate);
        }

        @Override
        public void run() {
            final Runnable delegate = mDelegate.get();
            if (delegate != null) {
                delegate.run();
            }
        }
    }
}

