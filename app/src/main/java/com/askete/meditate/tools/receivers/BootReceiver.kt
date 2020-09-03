package com.askete.meditate.tools.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.View
import com.askete.meditate.App
import com.askete.meditate.components.dependencies.prefs
import com.askete.meditate.components.dependencies.repository
import com.askete.meditate.data.model.DataSnapshot
import com.askete.meditate.tools.services.OverlayService
import com.askete.meditate.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent?) {
            if(!prefs.isEnd){
                GlobalScope.launch(Dispatchers.IO){repository.insertToDatabase(
                    DataSnapshot(
                        prefs.currentTime,
                        prefs.selectedTimeId
                    )
                )}
                App.handler?.postDelayed(
                    {
                        startMain(context)
                    },100
                )

                App.handler?.postDelayed({
                    val flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

                    if(MainActivity.activity != null){
                        MainActivity.activity?.window?.decorView?.systemUiVisibility = flags;
                        val  decorView = MainActivity.activity!!.window.decorView;
                        decorView.setOnSystemUiVisibilityChangeListener {
                            if (it and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                                decorView.systemUiVisibility = flags
                            }
                        }
                    }
                    context.startService(Intent(context, OverlayService::class.java))
                },1000)

            }
    }

    private fun startMain(context: Context) = with(context) {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}