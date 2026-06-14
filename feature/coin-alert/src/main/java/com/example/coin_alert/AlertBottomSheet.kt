package com.example.coin_alert

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.coin_alert.databinding.FragmentAlertBottomSheetBinding
import com.example.coin_alert.di.AlertBottomSheetFeatureInject
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER



class AlertBottomSheet : BottomSheetDialogFragment() {

     var _binding: FragmentAlertBottomSheetBinding?=null
     val binding
         get()=_binding!!
    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,R.style.AppBottomSheetDialogTheme)
    }
    private val symbol by lazy {
        requireArguments().getString(ARG_SYMBOL)?:error("No such symbol")
    }

    @Inject
    lateinit var viewModelFactory: AlertBottomSheetViewModelFactory

    private val viewModel: AlertBottomSheetViewModel by viewModels{
        viewModelFactory.create(symbol)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as AlertBottomSheetFeatureInject).inject(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentAlertBottomSheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpEvents()
        setUpAlertCreation()
        binding.createAlertFab.setOnClickListener {
            viewModel.insertAlert()
        }
    }

    fun setUpAlertCreation(){
        binding.directionChipGroup.check(R.id.chipAbove)
        binding.directionChipGroup.setOnCheckedStateChangeListener { group, ints ->
            val checkedId=ints.firstOrNull()?:return@setOnCheckedStateChangeListener
            val chip=group.findViewById<Chip>(checkedId)
            viewModel.updateDirection(chip.text.toString())
        }
        binding.priceEditText.addTextChangedListener {editable ->
            viewModel.updateTargetPrice(editable.toString())
        }
    }


    private fun setUpEvents(){
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.events.collect {event->
                    observeEvent(event)
                }
            }
        }
    }

    private fun observeEvent(event: AlertBottomSheetEvent){
        when(event){
            is AlertBottomSheetEvent.ShowToast ->{
                Toast.makeText(requireContext(),event.message, Toast.LENGTH_LONG).show()
            }
            AlertBottomSheetEvent.CloseBottomSheet -> {
                dismiss()
            }
            else -> {dismiss()}
        }
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    companion object{
        private const val ARG_SYMBOL="symbol"
        fun newInstance(symbol:String): AlertBottomSheet{
            return AlertBottomSheet().apply {
                arguments=Bundle().apply {
                    putString(ARG_SYMBOL,symbol)
                }
            }
        }
    }


}