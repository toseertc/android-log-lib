package com.toseertc.log.impl

import android.os.Build
import android.util.Log
import org.jetbrains.annotations.NonNls
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import java.util.regex.Pattern

class TimberKotlin {

    /** Log a verbose message with optional format args.  */
    fun v(@NonNls message: String?, vararg args: Any?) {
        treeOfSouls.v(message, *args)
    }

    /** Log a verbose exception and a message with optional format args.  */
    fun v(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        treeOfSouls.v(t, message, *args)
    }

    /** Log a verbose exception.  */
    fun v(t: Throwable?) {
        treeOfSouls.v(t)
    }

    /** Log a debug message with optional format args.  */
    fun d(@NonNls message: String?, vararg args: Any?) {
        treeOfSouls.d(message, *args)
    }

    /** Log a debug exception and a message with optional format args.  */
    fun d(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        treeOfSouls.d(t, message, *args)
    }

    /** Log a debug exception.  */
    fun d(t: Throwable?) {
        treeOfSouls.d(t)
    }

    /** Log an info message with optional format args.  */
    fun i(@NonNls message: String?, vararg args: Any?) {
        treeOfSouls.i(message, *args)
    }

    /** Log an info exception and a message with optional format args.  */
    fun i(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        treeOfSouls.i(t, message, *args)
    }

    /** Log an info exception.  */
    fun i(t: Throwable?) {
        treeOfSouls.i(t)
    }

    /** Log a warning message with optional format args.  */
    fun w(@NonNls message: String?, vararg args: Any?) {
        treeOfSouls.w(message, *args)
    }

    /** Log a warning exception and a message with optional format args.  */
    fun w(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        treeOfSouls.w(t, message, *args)
    }

    /** Log a warning exception.  */
    fun w(t: Throwable?) {
        treeOfSouls.w(t)
    }

    /** Log an error message with optional format args.  */
    fun e(@NonNls message: String?, vararg args: Any?) {
        treeOfSouls.e(message, *args)
    }

    /** Log an error exception and a message with optional format args.  */
    fun e(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        treeOfSouls.e(t, message, *args)
    }

    /** Log an error exception.  */
    fun e(t: Throwable?) {
        treeOfSouls.e(t)
    }

    /** Log an assert message with optional format args.  */
    fun wtf(@NonNls message: String?, vararg args: Any?) {
        treeOfSouls.wtf(message, *args)
    }

    /** Log an assert exception and a message with optional format args.  */
    fun wtf(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        treeOfSouls.wtf(t, message, *args)
    }

    /** Log an assert exception.  */
    fun wtf(t: Throwable?) {
        treeOfSouls.wtf(t)
    }

    /** Log at `priority` a message with optional format args.  */
    fun log(priority: Int, @NonNls message: String?, vararg args: Any?) {
        treeOfSouls.log(priority, message, *args)
    }

    /** Log at `priority` an exception and a message with optional format args.  */
    fun log(priority: Int, t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        treeOfSouls.log(priority, t, message, *args)
    }

    /** Log at `priority` an exception.  */
    fun log(priority: Int, t: Throwable?) {
        treeOfSouls.log(priority, t)
    }

    /**
     * A view into Timber's planted trees as a tree itself. This can be used for injecting a logger
     * instance rather than using static methods or to facilitate testing.
     */
    fun asTree(): Tree {
        return treeOfSouls
    }

    /** Set a one-time tag for use on the next logging call.  */
    fun tag(tag: String): Tree {
        val forest = forestAsArray
        for (tree in forest) {
            tree!!.explicitTag.set(tag)
        }
        return treeOfSouls
    }

    /** Add a new logging tree.  */
    // Validating public API contract.
    fun plant(tree: Tree) {
        if (tree == null) {
            throw NullPointerException("tree == null")
        }
        require(!(tree === treeOfSouls)) { "Cannot plant Timber into itself." }
        synchronized(forset) {
            forset.add(tree)
            forestAsArray = forset.toTypedArray()
        }
    }

    /** Adds new logging trees.  */
    // Validating public API contract.
    fun plant(vararg trees: Tree) {
        if (trees == null) {
            throw NullPointerException("trees == null")
        }
        for (tree in trees) {
            if (tree == null) {
                throw NullPointerException("trees contains null")
            }
            require(!(tree === treeOfSouls)) { "Cannot plant Timber into itself." }
        }
        synchronized(forset) {
            Collections.addAll(forset, *trees)
            forestAsArray = forset.toTypedArray()
        }
    }

    /** Remove a planted tree.  */
    fun uproot(tree: Tree) {
        synchronized(forset) {
            require(forset.remove(tree)) { "Cannot uproot tree which is not planted: $tree" }
            forestAsArray = forset.toTypedArray()
        }
    }

    /** Remove all planted trees.  */
    fun uprootAll() {
        synchronized(forset) {
            forset.clear()
            forestAsArray = treeArrayEmpty
        }
    }

    /** Return a copy of all planted [trees][Tree].  */
    fun forest(): List<Tree?> {
        synchronized(forset) { return Collections.unmodifiableList(ArrayList(forset)) }
    }

    fun treeCount(): Int {
        synchronized(forset) { return forset.size }
    }

    private val treeArrayEmpty = arrayOfNulls<Tree>(0)

    // Both fields guarded by 'FOREST'.
    private val forset: MutableList<Tree> = ArrayList()

    @Volatile
    var forestAsArray = treeArrayEmpty

    /** A [Tree] that delegates to all planted trees in the [forest][.FOREST].  */
    private val treeOfSouls: Tree = object : Tree() {
        override fun v(message: String?, vararg args: Any?) {
            val forest = forestAsArray
            for (tree in forest) {
                tree!!.v(message, *args)
            }
        }

        override fun v(t: Throwable?, message: String?, vararg args: Any?) {
            val forest = forestAsArray
            for (tree in forest) {
                tree!!.v(t, message, *args)
            }
        }

        override fun v(t: Throwable?) {
            val forest = forestAsArray
            for (tree in forest) {
                tree!!.v(t)
            }
        }

        override fun d(message: String?, vararg args: Any?) {
            val forest = forestAsArray
            for (tree in forest) {
                tree!!.d(message, *args)
            }
        }

        override fun d(t: Throwable?, message: String?, vararg args: Any?) {
            val forest = forestAsArray
            for (tree in forest) {
                tree!!.d(t, message, *args)
            }
        }

        override fun d(t: Throwable?) {
            val forest = forestAsArray
            for (tree in forest) {
                tree!!.d(t)
            }
        }

        override fun i(message: String?, vararg args: Any?) {
            val forest = forestAsArray
            for (tree in forest) {
                tree!!.i(message, *args)
            }
        }

        override fun i(t: Throwable?, message: String?, vararg args: Any?) {
            val forest = forestAsArray
            for (tree in forest) {
                tree!!.i(t, message, *args)
            }
        }

        override fun i(t: Throwable?) {
            val forest = forestAsArray
            for (tree in forest) {
                tree!!.i(t)
            }
        }

        override fun w(message: String?, vararg args: Any?) {
            val forest = forestAsArray
            for (tree in forest) {
                tree!!.w(message, *args)
            }
        }

        override fun w(t: Throwable?, message: String?, vararg args: Any?) {
            val forest = forestAsArray
            for (tree in forest) {
                tree!!.w(t, message, *args)
            }
        }

        override fun w(t: Throwable?) {
            val forest = forestAsArray
            for (tree in forest) {
                tree!!.w(t)
            }
        }

        override fun e(message: String?, vararg args: Any?) {
            val forest = forestAsArray
            for (tree in forest) {
                tree!!.e(message, *args)
            }
        }

        override fun e(t: Throwable?, message: String?, vararg args: Any?) {
            val forest = forestAsArray
            for (tree in forest) {
                tree!!.e(t, message, *args)
            }
        }

        override fun e(t: Throwable?) {
            val forest = forestAsArray
            for (tree in forest) {
                tree!!.e(t)
            }
        }

        override fun wtf(message: String?, vararg args: Any?) {
            val forest = forestAsArray
            for (tree in forest) {
                tree!!.wtf(message, *args)
            }
        }

        override fun wtf(t: Throwable?, message: String?, vararg args: Any?) {
            val forest = forestAsArray
            for (tree in forest) {
                tree!!.wtf(t, message, *args)
            }
        }

        override fun wtf(t: Throwable?) {
            val forest = forestAsArray
            for (tree in forest) {
                tree!!.wtf(t)
            }
        }

        override fun log(priority: Int, message: String?, vararg args: Any?) {
            val forest = forestAsArray
            for (tree in forest) {
                tree!!.log(priority, message, *args)
            }
        }

        override fun log(priority: Int, t: Throwable?, message: String?, vararg args: Any?) {
            val forest = forestAsArray
            for (tree in forest) {
                tree!!.log(priority, t, message, *args)
            }
        }

        override fun log(priority: Int, t: Throwable?) {
            val forest = forestAsArray
            for (tree in forest) {
                tree!!.log(priority, t)
            }
        }

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            throw AssertionError("Missing override for log method.")
        }
    }

    fun Timber() {}

    /** A facade for handling logging calls. Install instances via [Timber.plant()][.plant].  */
    abstract class Tree {
        val explicitTag = ThreadLocal<String>()
        open val tag: String?
            get() {
                val tag = explicitTag.get()
                if (tag != null) {
                    explicitTag.remove()
                }
                return tag
            }
        val isLoggable = true

        /** Log a verbose message with optional format args.  */
        open fun v(message: String?, vararg args: Any?) {
            prepareLog(Log.VERBOSE, null, message, *args)
        }

        /** Log a verbose exception and a message with optional format args.  */
        open fun v(t: Throwable?, message: String?, vararg args: Any?) {
            prepareLog(Log.VERBOSE, t, message, *args)
        }

        /** Log a verbose exception.  */
        open fun v(t: Throwable?) {
            prepareLog(Log.VERBOSE, t, null)
        }

        /** Log a debug message with optional format args.  */
        open fun d(message: String?, vararg args: Any?) {
            prepareLog(Log.DEBUG, null, message, *args)
        }

        /** Log a debug exception and a message with optional format args.  */
        open fun d(t: Throwable?, message: String?, vararg args: Any?) {
            prepareLog(Log.DEBUG, t, message, *args)
        }

        /** Log a debug exception.  */
        open fun d(t: Throwable?) {
            prepareLog(Log.DEBUG, t, null)
        }

        /** Log an info message with optional format args.  */
        open fun i(message: String?, vararg args: Any?) {
            prepareLog(Log.INFO, null, message, *args)
        }

        /** Log an info exception and a message with optional format args.  */
        open fun i(t: Throwable?, message: String?, vararg args: Any?) {
            prepareLog(Log.INFO, t, message, *args)
        }

        /** Log an info exception.  */
        open fun i(t: Throwable?) {
            prepareLog(Log.INFO, t, null)
        }

        /** Log a warning message with optional format args.  */
        open fun w(message: String?, vararg args: Any?) {
            prepareLog(Log.WARN, null, message, *args)
        }

        /** Log a warning exception and a message with optional format args.  */
        open fun w(t: Throwable?, message: String?, vararg args: Any?) {
            prepareLog(Log.WARN, t, message, *args)
        }

        /** Log a warning exception.  */
        open fun w(t: Throwable?) {
            prepareLog(Log.WARN, t, null)
        }

        /** Log an error message with optional format args.  */
        open fun e(message: String?, vararg args: Any?) {
            prepareLog(Log.ERROR, null, message, *args)
        }

        /** Log an error exception and a message with optional format args.  */
        open fun e(t: Throwable?, message: String?, vararg args: Any?) {
            prepareLog(Log.ERROR, t, message, *args)
        }

        /** Log an error exception.  */
        open fun e(t: Throwable?) {
            prepareLog(Log.ERROR, t, null)
        }

        /** Log an assert message with optional format args.  */
        open fun wtf(message: String?, vararg args: Any?) {
            prepareLog(Log.ASSERT, null, message, *args)
        }

        /** Log an assert exception and a message with optional format args.  */
        open fun wtf(t: Throwable?, message: String?, vararg args: Any?) {
            prepareLog(Log.ASSERT, t, message, *args)
        }

        /** Log an assert exception.  */
        open fun wtf(t: Throwable?) {
            prepareLog(Log.ASSERT, t, null)
        }

        /** Log at `priority` a message with optional format args.  */
        open fun log(priority: Int, message: String?, vararg args: Any?) {
            prepareLog(priority, null, message, *args)
        }

        /** Log at `priority` an exception and a message with optional format args.  */
        open fun log(priority: Int, t: Throwable?, message: String?, vararg args: Any?) {
            prepareLog(priority, t, message, *args)
        }

        /** Log at `priority` an exception.  */
        open fun log(priority: Int, t: Throwable?) {
            prepareLog(priority, t, null)
        }

        /**
         * Return whether a message at `priority` should be logged.
         */
        @Deprecated("use {@link #isLoggable(String, int)} instead.", ReplaceWith("true"))
        protected fun isLoggable(priority: Int): Boolean {
            return isLoggable
        }

        /** Return whether a message at `priority` or `tag` should be logged.  */
        protected fun isLoggable(tag: String?, priority: Int): Boolean {
            return isLoggable(priority)
        }

        private fun prepareLog(priority: Int, t: Throwable?, message: String?, vararg args: Any?) {
            // Consume tag even when message is not loggable so that next message is correctly tagged.
            var message = message
            val tag = tag
            if (!isLoggable(tag, priority)) {
                return
            }
            if (message != null && message.length == 0) {
                message = null
            }
            if (message == null) {
                if (t == null) {
                    return  // Swallow message if it's null and there's no throwable.
                }
                message = getStackTraceString(t)
            } else {
                if (args != null && args.size > 0) {
                    message = formatMessage(message, args)
                }
                if (t != null) {
                    message += """
                    
                    ${getStackTraceString(t)}
                    """.trimIndent()
                }
            }
            log(priority, tag, message, t)
        }

        /**
         * Formats a log message with optional arguments.
         */
        protected fun formatMessage(message: String, vararg args: Any): String {
            return String.format(message, args)
        }

        private fun getStackTraceString(t: Throwable): String {
            // Don't replace this with Log.getStackTraceString() - it hides
            // UnknownHostException, which is not what we want.
            val sw = StringWriter(256)
            val pw = PrintWriter(sw, false)
            t.printStackTrace(pw)
            pw.flush()
            return sw.toString()
        }

        /**
         * Write a log message to its destination. Called for all level-specific methods by default.
         *
         * @param priority Log level. See [Log] for constants.
         * @param tag Explicit or inferred tag. May be `null`.
         * @param message Formatted log message. May be `null`, but then `t` will not be.
         * @param t Accompanying exceptions. May be `null`, but then `message` will not be.
         */
        protected abstract fun log(priority: Int, tag: String?, message: String,
                                   t: Throwable?)
    }

    /** A [Tree] for debug builds. Automatically infers the tag from the calling class.  */
    open class DebugTree : Tree() {
        /**
         * Extract the tag which should be used for the message from the `element`. By default
         * this will use the class name without any anonymous class suffixes (e.g., `Foo$1`
         * becomes `Foo`).
         *
         *
         * Note: This will not be called if a [manual tag][.tag] was specified.
         */
        protected fun createStackElementTag(element: StackTraceElement): String? {
            var tag = element.fileName
            val m = ANONYMOUS_CLASS.matcher(tag)
            if (m.find()) {
                tag = m.replaceAll("")
            }
            tag = tag.substring(tag.lastIndexOf('.') + 1)
            // Tag length limit was removed in API 24.
            return if (tag.length <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tag
            } else tag.substring(0, MAX_TAG_LENGTH)
        }

        // DO NOT switch this to Thread.getCurrentThread().getStackTrace(). The test will pass
        // because Robolectric runs them on the JVM but on Android the elements are different.
        override val tag: String?
            get() {
                val tag = super.tag
                if (tag != null) {
                    return tag
                }

                return getInnerTag()
            }


        private fun getInnerTag(): String? {
            // DO NOT switch this to Thread.getCurrentThread().getStackTrace(). The test will pass
            // because Robolectric runs them on the JVM but on Android the elements are different.
            val stackTrace = Throwable().stackTrace
            val stackIndex = 7
            val targetElement = stackTrace[stackIndex]
            val fileName = getFileName(targetElement)

            val index = fileName?.indexOf('.') // Use proguard may not find '.'.
            val s = if (index == -1) fileName else fileName?.substring(0, index ?: 0)
            return s
        }

        private fun getFileName(targetElement: StackTraceElement): String? {
            val fileName = targetElement.fileName
            if (fileName != null) return fileName
            // If name of file is null, should add
            // "-keepattributes SourceFile,LineNumberTable" in proguard file.
            var className = targetElement.className
            val classNameInfo = className.split("\\.".toRegex()).toTypedArray()
            if (classNameInfo.size > 0) {
                className = classNameInfo[classNameInfo.size - 1]
            }
            val index = className.indexOf('$')
            if (index != -1) {
                className = className.substring(0, index)
            }
            return "$className.java"
        }

        /**
         * Break up `message` into maximum-length chunks (if needed) and send to either
         * [Log.println()][Log.println] or
         * [Log.wtf()][Log.wtf] for logging.
         *
         * {@inheritDoc}
         */
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (message.length < MAX_LOG_LENGTH) {
                if (priority == Log.ASSERT) {
                    Log.wtf(tag, message)
                } else {
                    Log.println(priority, tag, message)
                }
                return
            }

            // Split by line, then ensure each line can fit into Log's maximum length.
            var i = 0
            val length = message.length
            while (i < length) {
                var newline = message.indexOf('\n', i)
                newline = if (newline != -1) newline else length
                do {
                    val end = Math.min(newline, i + MAX_LOG_LENGTH)
                    val part = message.substring(i, end)
                    if (priority == Log.ASSERT) {
                        Log.wtf(tag, part)
                    } else {
                        Log.println(priority, tag, part)
                    }
                    i = end
                } while (i < newline)
                i++
            }
        }

        companion object {
            private const val MAX_LOG_LENGTH = 4000
            private const val MAX_TAG_LENGTH = 23
//            private const val CALL_STACK_INDEX = 6
            private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")
        }
    }
}