package `fun`.aragaki.kraft.ui.base

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI

abstract class BaseActivity : AppCompatActivity(), DIAware {
    protected val parentDI by closestDI()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finishAfterTransition()
        return super.onOptionsItemSelected(item)
    }

}