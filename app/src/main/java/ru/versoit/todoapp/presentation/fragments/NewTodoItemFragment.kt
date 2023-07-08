package ru.versoit.todoapp.presentation.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ru.versoit.domain.models.Importance
import ru.versoit.todoapp.R
import ru.versoit.todoapp.app.TodoApp
import ru.versoit.todoapp.databinding.FragmentNewTodoItemBinding
import ru.versoit.todoapp.presentation.features.vmfactory.NewTodoItemViewModelFactory
import ru.versoit.todoapp.presentation.viewmodels.NewTodoItemViewModel
import javax.inject.Inject

/**
 * Fragment for creating new todo item.
 */
class NewTodoItemFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: NewTodoItemViewModelFactory
    private lateinit var viewModel: NewTodoItemViewModel

    private var _binding: FragmentNewTodoItemBinding? = null
    private val binding get() = _binding!!

    companion object {

        private const val INACTIVE = 0.2f
        private const val ACTIVE = 1f
    }

    private lateinit var importanceMenu: PopupMenu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewTodoItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initializeViewModel()

        updateTextColorByDeadlineState()
        with(binding) {
            inflateImportanceSelectionMenu(textViewImportance)
            inflateImportanceSelectionMenu(textViewSelectedImportance)
            showImportanceMenuByClick(textViewImportance, textViewSelectedImportance)

            setDeadlineTextViewOnClickListener()
            setDeadlineSwitchListener()
            launchImportanceListener()
            bindCancelEventTo(imageViewCancel)
            bindSaveEventTo(textViewSave)
        }
    }

    private fun initializeViewModel() {
        inject()
        viewModel = ViewModelProvider(this, viewModelFactory)[NewTodoItemViewModel::class.java]
    }

    private fun bindCancelEventTo(view: View) {
        view.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showImportanceMenuByClick(vararg views: View) {

        repeat (views.size) {
            view?.setOnClickListener {
                importanceMenu.show()
            }
        }
    }

    private fun setDeadlineTextViewOnClickListener() {

        binding.textViewDeadline.setOnClickListener {

            val datePicker = DatePickerDialog(this.requireContext(), R.style.DatePicker)
            datePicker.updateDate(viewModel.year, viewModel.month, viewModel.day)
            datePicker.setOnDateSetListener { _, year, month, dayOfMonth ->
                viewModel.updateDeadline(dayOfMonth, month, year)
            }
            datePicker.show()
        }
    }

    private fun updateTextColorByDeadlineState() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.deadline.collect {
                if (viewModel.isInvalidDeadline) binding.textViewDeadline.setTextColor(
                    ContextCompat.getColor(
                        this@NewTodoItemFragment.requireContext(), R.color.invalid
                    )
                )
                else binding.textViewDeadline.setTextColor(
                    ContextCompat.getColor(
                        this@NewTodoItemFragment.requireContext(), R.color.primary
                    )
                )
                binding.textViewDeadline.text = viewModel.formattedDeadline
            }
        }
    }

    private fun setDeadlineSwitchListener() {

        binding.switchDeadline.setOnCheckedChangeListener { _, isChecked ->

            viewModel.hasDeadline = isChecked
            if (isChecked) {
                binding.textViewDeadline.text = viewModel.formattedDeadline
                binding.textViewDeadline.visibility = View.VISIBLE
                binding.textViewMakeUp.alpha = ACTIVE
                return@setOnCheckedChangeListener
            }
            binding.textViewDeadline.visibility = View.GONE
            binding.textViewMakeUp.alpha = INACTIVE
        }
    }

    private fun launchImportanceListener() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.importance.collect { importance ->
                when (importance) {
                    Importance.UNIMPORTANT -> binding.textViewSelectedImportance.text = getString(R.string.no)
                    Importance.LESS_IMPORTANT -> binding.textViewSelectedImportance.text = getString(R.string.less)
                    Importance.IMPORTANT -> binding.textViewSelectedImportance.text = getString(R.string.important)
                }
            }
        }
    }

    private fun inflateImportanceSelectionMenu(view: View) {
        importanceMenu = PopupMenu(requireContext(), view)
        val inflater = importanceMenu.menuInflater

        inflater.inflate(R.menu.popup_menu, importanceMenu.menu)
        val menu = setImportanceSelectionEventsForMenu(importanceMenu)
        view.setOnClickListener {
            menu.show()
        }
    }

    private fun setImportanceSelectionEventsForMenu(menu: PopupMenu): PopupMenu {

        menu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.unimportant -> {
                    viewModel.updateImportance(Importance.UNIMPORTANT)
                    true
                }
                R.id.less_important -> {
                    viewModel.updateImportance(Importance.LESS_IMPORTANT)
                    true
                }
                R.id.important -> {
                    viewModel.updateImportance(Importance.IMPORTANT)
                    true
                }
                else -> false
            }
        }

        return menu
    }

    private fun inject() {
        (requireActivity().application as TodoApp).appComponent.newTodoItemFragmentComponent()
            .inject(this)
    }

    private fun bindSaveEventTo(view: View) {

        view.setOnClickListener {
            viewModel.text = binding.editTextTask.text.toString()
            if (viewModel.isInvalidDeadline) {
                Snackbar.make(binding.root, R.string.invalid_date, Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (viewModel.isInvalidText) {
                Snackbar.make(binding.root, R.string.invalid_text, Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            viewModel.save()
            findNavController().navigateUp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}