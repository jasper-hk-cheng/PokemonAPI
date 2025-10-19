package tw.com.poke.api

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(MyDebugTree())
    }
}

class MyDebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String =
        "(${element.fileName}:${element.lineNumber})#${element.methodName}"
}
