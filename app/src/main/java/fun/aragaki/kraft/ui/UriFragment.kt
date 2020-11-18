package `fun`.aragaki.kraft.ui

import `fun`.aragaki.kraft.R
import `fun`.aragaki.kraft.databinding.FragmentUriBinding
import `fun`.aragaki.kraft.extensions.findUrls
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

class UriFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentUriBinding.inflate(inflater, container, false).apply {
        btnLaunch.setOnClickListener {
            startActivity(
                Intent.createChooser(
                    Intent(
                        Intent.ACTION_VIEW, Uri.Builder()
                            .scheme(etScheme.text.toString())
                            .authority(etAuthority.text.toString())
                            .path(etPath.text.toString())
                            .encodedQuery(etQuery.text.toString())
                            .build()
                    ), getString(R.string.title_choose)
                )
            )
        }

        requireActivity().intent.let {
            if (it.action == Intent.ACTION_SEND)
                Uri.parse(it.getStringExtra(Intent.EXTRA_TEXT)?.findUrls()?.first())
            else it.data
        }?.let {
            etScheme.setText(it.scheme)
            etAuthority.setText(it.authority)
            etPath.setText(it.path)
            etQuery.setText(it.query)
        }
    }.root
}