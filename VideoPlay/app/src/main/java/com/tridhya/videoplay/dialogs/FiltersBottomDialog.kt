package com.tridhya.videoplay.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tridhya.videoplay.R
import com.tridhya.videoplay.databinding.DialogFiltersBinding
import com.tridhya.videoplay.enums.FilterType
import com.tridhya.videoplay.ui.base.BaseBottomSheetDialogFragment
import com.tridhya.videoplay.ui.view.adapters.video.FilterTypeAdapter

class FiltersBottomDialog : BaseBottomSheetDialogFragment(), FilterTypeAdapter.onClicked {
    private lateinit var binding: DialogFiltersBinding
    private var filterTypeAdapter: FilterTypeAdapter? = null

    companion object {
        private lateinit var listener: FilterSelectedListener
        private lateinit var filterTypeList: List<FilterType>

        fun newInstance(
            filterTypeList: List<FilterType>,
            listener: FilterSelectedListener,
        ): FiltersBottomDialog {
            this.listener = listener
            this.filterTypeList = filterTypeList
            return FiltersBottomDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFiltersBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFilterList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        filterTypeAdapter = FilterTypeAdapter(filterTypeList, this)
        binding.rvFilterList.adapter = filterTypeAdapter

    }

    interface FilterSelectedListener {
        fun onFilterSelected(filterType: FilterType)
    }

    override fun filterSelected(filterType: FilterType) {
        dismiss()
        listener.onFilterSelected(filterType)
    }

}