package com.askete.meditate.tools.test

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.askete.meditate.components.dependencies.database
import com.askete.meditate.components.dependencies.repository
import com.askete.meditate.data.model.DataSnapshot
import com.askete.meditate.tools.receivers.NotificationReceiver
import com.askete.meditate.ui.overlay.OverlayView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val TEST_END = "end"
const val TEST_NOTIFICATION = "notif"
const val TEST_CLEAR = "clearDB"
const val TEST_INSERT = "insertDB"


class TestReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        intent?.let {
            when(it.action){
                TEST_END->{
                    OverlayView.onTestActionListener?.onTest()
                }
                TEST_NOTIFICATION->{
                    NotificationReceiver.instance.notify(context)
                }
                TEST_CLEAR->{
                    GlobalScope.launch(Dispatchers.IO) {
                        database.clearAllTables()
                    }
                }
                TEST_INSERT->{
                    GlobalScope.launch(Dispatchers.IO) {
                        DataSnapshot.createTestData().forEach { item->
                            repository.insertToDatabase(item)
                        }
                    }
                }

                else->{}
            }
        }
    }

    companion object {
        fun register(context: Context){
            try {
                val filter = IntentFilter()
                filter.addAction(TEST_END)
                filter.addAction(TEST_NOTIFICATION)
                filter.addAction(TEST_CLEAR)
                filter.addAction(TEST_INSERT)
                context.registerReceiver(TestReceiver(), filter)
            } catch (e: Exception) {
            }
        }
    }
}