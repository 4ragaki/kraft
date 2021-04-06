package `fun`.aragaki.kraft.extensions

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

fun Vibrator.vbrClick() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
    vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
else vibrate(30)


fun Vibrator.vbrLongClick() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
    vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
else vibrate(60)