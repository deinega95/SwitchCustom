package ru.deinega.customswitch

import android.content.Context
import android.os.Build
import androidx.annotation.ColorRes


fun Context.getColorFromRes(@ColorRes resId: Int) =
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        resources.getColor(resId)
    } else {
        getColor(resId)
    }