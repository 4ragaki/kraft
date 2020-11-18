package `fun`.aragaki.kraft.ui

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.databinding.ActivityJumpBinding
import `fun`.aragaki.kraft.databinding.FragmentJumpBinding
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController

class JumpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityJumpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupActionBarWithNavController(findNavController(R.id.hostFragment))
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) =
        intent?.extras?.getInt(EXTRA_DESTINATION, EMPTY)?.takeIf { it != EMPTY }?.let { dest ->
            findNavController(R.id.hostFragment).apply {
                navInflater.inflate(R.navigation.navigation_jump).let {
                    it.setStartDestination(dest)
                    graph = it
                }
            }
        }

    companion object {
        private const val EMPTY = -1

        const val EXTRA_DESTINATION = "extra_destination"
    }

    class JumpFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ) = FragmentJumpBinding.inflate(inflater, container, false).root
    }
}