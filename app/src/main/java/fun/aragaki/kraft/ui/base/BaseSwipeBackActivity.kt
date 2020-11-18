package `fun`.aragaki.kraft.ui.base

import android.view.MenuItem
import me.imid.swipebacklayout.lib.app.SwipeBackActivity
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI

abstract class BaseSwipeBackActivity : SwipeBackActivity(), DIAware {
    protected val parentDI by closestDI()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finishAfterTransition()
        return super.onOptionsItemSelected(item)
    }
}