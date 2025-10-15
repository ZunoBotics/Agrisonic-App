package com.agrisonic.app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.agrisonic.app.R

class PlaceholderFragment : Fragment() {

    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString(ARG_TITLE) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_placeholder, container, false)
        val textView = view.findViewById<TextView>(R.id.tvPlaceholder)
        textView.text = "$title\n\nComing Soon..."
        return view
    }

    companion object {
        private const val ARG_TITLE = "title"

        fun newInstance(title: String): PlaceholderFragment {
            val fragment = PlaceholderFragment()
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            fragment.arguments = args
            return fragment
        }
    }
}
