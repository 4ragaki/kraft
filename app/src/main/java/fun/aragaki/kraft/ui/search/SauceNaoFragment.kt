package `fun`.aragaki.kraft.ui.search

import `fun`.aragaki.kraft.Kraft
import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.adapters.QuickAdapter
import `fun`.aragaki.kraft.databinding.FragmentSaucenaoBinding
import `fun`.aragaki.kraft.databinding.ItemSaucenaoBinding
import `fun`.aragaki.kraft.ext.resolveColor
import `fun`.aragaki.kraft.ui.ViewModelFactory
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI

class SauceNaoFragment(private val image: Uri) : Fragment(), DIAware {
    override val di: DI by closestDI()

    lateinit var binding: FragmentSaucenaoBinding
    private val viewModel by viewModels<ReverseViewModel> { ViewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().title = getString(R.string.reverse_provider_saucenao)

        if (this::binding.isInitialized) {
            return binding.root
        }

        binding = FragmentSaucenaoBinding.inflate(inflater, container, false).apply {
            swipeSauce.apply {
                setColorSchemeColors(requireActivity().resolveColor(R.attr.colorSecondary))
                setOnRefreshListener { request() }
            }
        }

        viewModel.apply {
            saucenaoResponse.observe(requireActivity()) {
                binding.swipeSauce.isRefreshing = false
                binding.progressSauce.visibility = View.GONE

                binding.rvSauce.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    it.results?.let { response ->
                        adapter = QuickAdapter(
                            response, response.map {
                                { _: SauceNaoHolder ->
                                    it.data?.ext_urls?.first()?.let {
                                        startActivity(
                                            Intent(Intent.ACTION_VIEW, Uri.parse(it))
                                        )
                                    }
                                }
                            },
                            { parent, _ ->
                                SauceNaoHolder(
                                    ItemSaucenaoBinding.inflate(layoutInflater, parent, false)
                                )
                            },
                            { holder, result -> holder.bind(result) }
                        )
                    }
                }
            }
        }
        request()

        return binding.root
    }

    private fun request() {
        viewModel.sauceNao(image) {
            binding.swipeSauce.isRefreshing = false
            Toast.makeText(Kraft.app, it.message, Toast.LENGTH_SHORT)
                .show()
        }
    }
}