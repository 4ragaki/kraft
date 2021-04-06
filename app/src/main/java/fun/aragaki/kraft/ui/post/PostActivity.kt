package `fun`.aragaki.kraft.ui.post

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.data.CredentialException
import `fun`.aragaki.kraft.data.UnsupportedException
import `fun`.aragaki.kraft.databinding.ActivityPostBinding
import `fun`.aragaki.kraft.extensions.*
import `fun`.aragaki.kraft.ui.JumpActivity
import `fun`.aragaki.kraft.ui.ViewModelFactory
import `fun`.aragaki.kraft.ui.base.BaseSwipeBackActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.view.ActionMode
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import org.kodein.di.DI
import org.kodein.di.android.retainedDI
import org.kodein.di.bind
import org.kodein.di.singleton
import retrofit2.HttpException


class PostActivity : BaseSwipeBackActivity() {
    override val di: DI by retainedDI {
        extend(parentDI)
        bind<PostActivity>() with singleton { this@PostActivity }
        bind<LayoutInflater>() with singleton { layoutInflater }
        bind<Vibrator>() with singleton { getSystemService()!! }
        bind<PostViewModel>() with singleton { viewModel }
    }

    var actionMode: ActionMode? = null
    private val viewModel by viewModels<PostViewModel> { ViewModelFactory }
    private lateinit var binding: ActivityPostBinding
    private var resumeCallback: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding.apply {
            btnInfo.setOnClickListener {
                findNavController(R.id.postHostFragment).navigate(R.id.nav_post_info)
            }
            btnTags.setOnClickListener {
                findNavController(R.id.postHostFragment).navigate(R.id.nav_post_tag)
            }
            btnDownload.setOnClickListener {
                kotlin.runCatching {
                    viewModel.download()
                }.onFailure { it.message?.let { msg -> toast(msg) } }
            }
            btnVote.setOnClickListener { }


//            val appbarPaddingTop = appbarLayout.paddingTop
//            val actionsPaddingTop = btnInfo.paddingTop
//            val actionsPaddingBottom = btnInfo.paddingBottom
//            root.applyVerticalInsets(
//                window, arrayOf(
//                    { appbarLayout.updatePadding(top = appbarPaddingTop + it.top) },
//                    {
//                        btnInfo.updatePadding(
//                            top = actionsPaddingTop + it.bottom,
//                            bottom = actionsPaddingBottom + it.bottom
//                        )
//                    }
//                )
//            )
        }

        launch(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { launch(it) }
    }


    private fun launch(intent: Intent) {
        val uri = if (intent.action == Intent.ACTION_SEND)
            Uri.parse(intent.getStringExtra(Intent.EXTRA_TEXT)?.findUrls()?.first())
        else intent.data!!

        viewModel.handle(uri, {
            resumeCallback = null
            binding.anim.isVisible = false
        }) {
            when (it) {
                is CredentialException -> {
                    binding.apply {
                        anim.play(R.raw.empty_list)
                        root.snack(it.message, getString(R.string.action_jump_to)) {
                            resumeCallback = {
                                launch(intent)
                                binding.anim.play(R.raw.preloader)
                            }
                            startActivity<JumpActivity> {
                                putExtra(
                                    JumpActivity.EXTRA_DESTINATION, R.id.nav_dest_credentials
                                )
                            }
                        }
                    }
                }
                is UnsupportedException -> it.message?.let { msg ->
                    supportActionBar?.title = msg
                    binding.anim.play(R.raw.empty_list)
                }
                is HttpException -> if (it.code() == 404) {
                    supportActionBar?.title = it.message
                    binding.anim.play(R.raw.page_not_found)
                }
                else -> {
                    resumeCallback = { launch(intent) }
                    binding.apply {
                        anim.play(R.raw.empty_list)
                        root.snack(it.message)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        resumeCallback?.let { it() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_post, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        actionMode?.finish()
    }
}