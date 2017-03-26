package com.github.azenhuang.praticedemo.util;

import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import java.lang.ref.WeakReference;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by elvis on 17/3/26.
 */

public abstract class MemorySafeAsyncTask<Params, Progress, Result> {
    private final ExecAsyncTask<Params, Progress, Result> execAsyncTask;

    /**
     * Creates a new asynchronous task. This constructor must be invoked on the UI thread.
     */
    public MemorySafeAsyncTask() {
        execAsyncTask = new ExecAsyncTask<>(this);
    }


    /**
     * Returns the current status of this task.
     *
     * @return The current status.
     */
    public final AsyncTask.Status getStatus() {
        return execAsyncTask.getStatus();
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @WorkerThread
    protected abstract Result doInBackground(Params... params);

    /**
     * Runs on the UI thread before {@link #doInBackground}.
     *
     * @see #onPostExecute
     * @see #doInBackground
     */
    @MainThread
    protected void onPreExecute() {
    }

    /**
     * <p>Runs on the UI thread after {@link #doInBackground}. The
     * specified result is the value returned by {@link #doInBackground}.</p>
     * <p>
     * <p>This method won't be invoked if the task was cancelled.</p>
     *
     * @param result The result of the operation computed by {@link #doInBackground}.
     * @see #onPreExecute
     * @see #doInBackground
     * @see #onCancelled(Object)
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @MainThread
    protected void onPostExecute(Result result) {
    }

    /**
     * Runs on the UI thread after {@link #publishProgress} is invoked.
     * The specified values are the values passed to {@link #publishProgress}.
     *
     * @param values The values indicating progress.
     * @see #publishProgress
     * @see #doInBackground
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @MainThread
    protected void onProgressUpdate(Progress... values) {
    }

    /**
     * <p>Runs on the UI thread after {@link #cancel(boolean)} is invoked and
     * {@link #doInBackground(Object[])} has finished.</p>
     * <p>
     * <p>The default implementation simply invokes {@link #onCancelled()} and
     * ignores the result. If you write your own implementation, do not call
     * <code>super.onCancelled(result)</code>.</p>
     *
     * @param result The result, if any, computed in
     *               {@link #doInBackground(Object[])}, can be null
     * @see #cancel(boolean)
     * @see #isCancelled()
     */
    @SuppressWarnings({"UnusedParameters"})
    @MainThread
    protected void onCancelled(Result result) {
        onCancelled();
    }

    /**
     * <p>Applications should preferably override {@link #onCancelled(Object)}.
     * This method is invoked by the default implementation of
     * {@link #onCancelled(Object)}.</p>
     * <p>
     * <p>Runs on the UI thread after {@link #cancel(boolean)} is invoked and
     * {@link #doInBackground(Object[])} has finished.</p>
     *
     * @see #onCancelled(Object)
     * @see #cancel(boolean)
     * @see #isCancelled()
     */
    @MainThread
    protected void onCancelled() {
    }

    /**
     * Returns <tt>true</tt> if this task was cancelled before it completed
     * normally. If you are calling {@link #cancel(boolean)} on the task,
     * the value returned by this method should be checked periodically from
     * {@link #doInBackground(Object[])} to end the task as soon as possible.
     *
     * @return <tt>true</tt> if task was cancelled before it completed
     * @see #cancel(boolean)
     */
    public final boolean isCancelled() {
        return execAsyncTask.isCancelled();
    }

    /**
     * <p>Attempts to cancel execution of this task.  This attempt will
     * fail if the task has already completed, already been cancelled,
     * or could not be cancelled for some other reason. If successful,
     * and this task has not started when <tt>cancel</tt> is called,
     * this task should never run. If the task has already started,
     * then the <tt>mayInterruptIfRunning</tt> parameter determines
     * whether the thread executing this task should be interrupted in
     * an attempt to stop the task.</p>
     * <p>
     * <p>Calling this method will result in {@link #onCancelled(Object)} being
     * invoked on the UI thread after {@link #doInBackground(Object[])}
     * returns. Calling this method guarantees that {@link #onPostExecute(Object)}
     * is never invoked. After invoking this method, you should check the
     * value returned by {@link #isCancelled()} periodically from
     * {@link #doInBackground(Object[])} to finish the task as early as
     * possible.</p>
     *
     * @param mayInterruptIfRunning <tt>true</tt> if the thread executing this
     *                              task should be interrupted; otherwise, in-progress tasks are allowed
     *                              to complete.
     * @return <tt>false</tt> if the task could not be cancelled,
     * typically because it has already completed normally;
     * <tt>true</tt> otherwise
     * @see #isCancelled()
     * @see #onCancelled(Object)
     */
    public final boolean cancel(boolean mayInterruptIfRunning) {
        return execAsyncTask.cancel(mayInterruptIfRunning);
    }

    /**
     * Waits if necessary for the computation to complete, and then
     * retrieves its result.
     *
     * @return The computed result.
     * @throws CancellationException If the computation was cancelled.
     * @throws ExecutionException    If the computation threw an exception.
     * @throws InterruptedException  If the current thread was interrupted
     *                               while waiting.
     */
    public final Result get() throws InterruptedException, ExecutionException {
        return execAsyncTask.get();
    }

    /**
     * Waits if necessary for at most the given time for the computation
     * to complete, and then retrieves its result.
     *
     * @param timeout Time to wait before cancelling the operation.
     * @param unit    The time unit for the timeout.
     * @return The computed result.
     * @throws CancellationException If the computation was cancelled.
     * @throws ExecutionException    If the computation threw an exception.
     * @throws InterruptedException  If the current thread was interrupted
     *                               while waiting.
     * @throws TimeoutException      If the wait timed out.
     */
    public final Result get(long timeout, TimeUnit unit) throws InterruptedException,
            ExecutionException, TimeoutException {
        return execAsyncTask.get(timeout, unit);
    }

    /**
     * Executes the task with the specified parameters. The task returns
     * itself (this) so that the caller can keep a reference to it.
     * <p>
     * <p>Note: this function schedules the task on a queue for a single background
     * thread or pool of threads depending on the platform version.  When first
     * introduced, AsyncTasks were executed serially on a single background thread.
     * Starting with {@link android.os.Build.VERSION_CODES#DONUT}, this was changed
     * to a pool of threads allowing multiple tasks to operate in parallel. Starting
     * {@link android.os.Build.VERSION_CODES#HONEYCOMB}, tasks are back to being
     * executed on a single thread to avoid common application errors caused
     * by parallel execution.  If you truly want parallel execution, you can use
     * the {@link #executeOnExecutor} version of this method
     * with {@link AsyncTask#THREAD_POOL_EXECUTOR}; however, see commentary there for warnings
     * on its use.
     * <p>
     * <p>This method must be invoked on the UI thread.
     *
     * @param params The parameters of the task.
     * @return This instance of AsyncTask.
     * @throws IllegalStateException If {@link #getStatus()} returns either
     *                               {@link AsyncTask.Status#RUNNING} or {@link AsyncTask.Status#FINISHED}.
     * @see #executeOnExecutor(java.util.concurrent.Executor, Object[])
     * @see #execute(Runnable)
     */
    @MainThread
    public final AsyncTask<Params, Progress, Result> execute(Params... params) {
        return execAsyncTask.execute(params);
    }

    /**
     * Executes the task with the specified parameters. The task returns
     * itself (this) so that the caller can keep a reference to it.
     * <p>
     * <p>This method is typically used with {@link AsyncTask#THREAD_POOL_EXECUTOR} to
     * allow multiple tasks to run in parallel on a pool of threads managed by
     * AsyncTask, however you can also use your own {@link Executor} for custom
     * behavior.
     * <p>
     * <p><em>Warning:</em> Allowing multiple tasks to run in parallel from
     * a thread pool is generally <em>not</em> what one wants, because the order
     * of their operation is not defined.  For example, if these tasks are used
     * to modify any state in common (such as writing a file due to a button click),
     * there are no guarantees on the order of the modifications.
     * Without careful work it is possible in rare cases for the newer version
     * of the data to be over-written by an older one, leading to obscure data
     * loss and stability issues.  Such changes are best
     * executed in serial; to guarantee such work is serialized regardless of
     * platform version you can use this function with {@link AsyncTask#SERIAL_EXECUTOR}.
     * <p>
     * <p>This method must be invoked on the UI thread.
     *
     * @param exec   The executor to use.  {@link AsyncTask#THREAD_POOL_EXECUTOR} is available as a
     *               convenient process-wide thread pool for tasks that are loosely coupled.
     * @param params The parameters of the task.
     * @return This instance of AsyncTask.
     * @throws IllegalStateException If {@link #getStatus()} returns either
     *                               {@link AsyncTask.Status#RUNNING} or {@link AsyncTask.Status#FINISHED}.
     * @see #execute(Object[])
     */
    @MainThread
    public final AsyncTask<Params, Progress, Result> executeOnExecutor(Executor exec,
                                                                       Params... params) {
        return execAsyncTask.executeOnExecutor(exec, params);
    }

    /**
     * Convenience version of {@link #execute(Object...)} for use with
     * a simple Runnable object. See {@link #execute(Object[])} for more
     * information on the order of execution.
     *
     * @see #execute(Object[])
     * @see #executeOnExecutor(java.util.concurrent.Executor, Object[])
     */
    @MainThread
    public static void execute(Runnable runnable) {
        AsyncTask.SERIAL_EXECUTOR.execute(runnable);
    }

    /**
     * This method can be invoked from {@link #doInBackground} to
     * publish updates on the UI thread while the background computation is
     * still running. Each call to this method will trigger the execution of
     * {@link #onProgressUpdate} on the UI thread.
     * <p>
     * {@link #onProgressUpdate} will not be called if the task has been
     * canceled.
     *
     * @param values The progress values to update the UI with.
     * @see #onProgressUpdate
     * @see #doInBackground
     */
    @WorkerThread
    protected final void publishProgress(Progress... values) {
        execAsyncTask.publishTheProgress(values);
    }

    private static final class ExecAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
        private final WeakReference<MemorySafeAsyncTask<Params, Progress, Result>> safeAsyncTaskRef;

        public ExecAsyncTask(MemorySafeAsyncTask<Params, Progress, Result> safeAsyncTask) {
            super();
            safeAsyncTaskRef = new WeakReference<>(safeAsyncTask);
        }

        @Override
        protected Result doInBackground(Params... params) {
            MemorySafeAsyncTask<Params, Progress, Result> safeAsyncTask = safeAsyncTaskRef.get();
            if (safeAsyncTask != null) {
                return safeAsyncTask.doInBackground(params);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            MemorySafeAsyncTask<Params, Progress, Result> safeAsyncTask = safeAsyncTaskRef.get();
            if (safeAsyncTask != null) {
                safeAsyncTask.onPreExecute();
            }
        }

        @Override
        protected void onPostExecute(Result result) {
            MemorySafeAsyncTask<Params, Progress, Result> safeAsyncTask = safeAsyncTaskRef.get();
            if (safeAsyncTask != null) {
                safeAsyncTask.onPostExecute(result);
            }
        }

        @Override
        protected void onProgressUpdate(Progress... values) {
            MemorySafeAsyncTask<Params, Progress, Result> safeAsyncTask = safeAsyncTaskRef.get();
            if (safeAsyncTask != null) {
                safeAsyncTask.onProgressUpdate(values);
            }
        }

        @Override
        protected void onCancelled(Result result) {
            MemorySafeAsyncTask<Params, Progress, Result> safeAsyncTask = safeAsyncTaskRef.get();
            if (safeAsyncTask != null) {
                safeAsyncTask.onCancelled(result);
            }
        }

        @Override
        protected void onCancelled() {
            MemorySafeAsyncTask<Params, Progress, Result> safeAsyncTask = safeAsyncTaskRef.get();
            if (safeAsyncTask != null) {
                safeAsyncTask.onCancelled();
            }
        }

        @WorkerThread
        private final void publishTheProgress(Progress... values) {
            publishProgress(values);
        }
    }
}
