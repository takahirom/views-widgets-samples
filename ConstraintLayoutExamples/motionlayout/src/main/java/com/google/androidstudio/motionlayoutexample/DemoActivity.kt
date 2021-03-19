/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.androidstudio.motionlayoutexample

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ModeViewModel() : ViewModel() {
  var currentConstraintId = MutableLiveData<Int>()
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP) // for View#clipToOutline
class DemoActivity : AppCompatActivity() {
  val modeViewModel by viewModels<ModeViewModel>()

  private lateinit var container: MotionLayout

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.motion_26_multistate)
    container = findViewById(R.id.motionLayout)
    findViewById<View>(R.id.arrow_right).setOnClickListener {
      modeViewModel.currentConstraintId.value = R.id.half_people
    }
    findViewById<View>(R.id.people8).setOnClickListener {
      modeViewModel.currentConstraintId.value = R.id.people
    }
    modeViewModel.currentConstraintId.observe(this) {
      container.transitionToState(it)
      Log.d("",
          "Motion:" +
              "constraintSetName:" + resources.getResourceEntryName(it) +
              "start:${resources.getResourceEntryName(container.startState)}" +
              "end:${resources.getResourceEntryName(container.endState)}"
      )
      if (container.endState == it) {
        println("end")
        container.transitionToEnd()
        container.progress = 1.0F
      } else if (container.startState == it) {
        println("start")
        container.transitionToStart()
        container.progress = 0.0F
      }
    }

    val debugMode = if (intent.getBooleanExtra("showPaths", false)) {
      MotionLayout.DEBUG_SHOW_PATH
    } else {
      MotionLayout.DEBUG_SHOW_NONE
    }
    (container as? MotionLayout)?.setDebugMode(debugMode)
  }

  fun changeState(v: View?) {
    val motionLayout = container as? MotionLayout ?: return
    if (motionLayout.progress > 0.5f) {
      motionLayout.transitionToStart()
    } else {
      motionLayout.transitionToEnd()
    }
  }
}