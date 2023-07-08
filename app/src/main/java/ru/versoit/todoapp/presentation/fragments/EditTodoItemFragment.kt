package ru.versoit.todoapp.presentation.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ru.versoit.data.repository.TodoItemRepositoryImpl
import ru.versoit.data.storage.datasources.local.RoomTodoItemDataSource
import ru.versoit.data.storage.datasources.local.SharedPrefsRevisionDataSource
import ru.versoit.data.storage.datasources.local.TokenDataSourceImpl
import ru.versoit.data.storage.datasources.network.RetrofitTodoItemDataSource
import ru.versoit.domain.models.Importance
import ru.versoit.domain.usecase.TodoItemRemoveUseCase
import ru.versoit.domain.usecase.TodoItemUpdateUseCase
import ru.versoit.todoapp.R
import ru.versoit.todoapp.app.TodoApp
import ru.versoit.todoapp.databinding.FragmentEditTodoItemBinding
import ru.versoit.todoapp.presentation.features.vmfactory.EditTodoItemViewModelFactory
import ru.versoit.todoapp.presentation.viewmodels.EditTodoItemViewModel
import javax.inject.Inject

/**
 * Fragment for editing todo item.
 */
class EditTodoItemFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: EditTodoItemViewModelFactory
    private lateinit var viewModel: EditTodoItemViewModel

    private var _binding: FragmentEditTodoItemBinding? = null
    private val binding get() = _binding!!

    private val args: EditTodoItemFragmentArgs by navArgs()

    companion object {

        private const val INACTIVE = 0.2f
        private const val ACTIVE = 1f
    }

    private lateinit var importanceMenu: PopupMenu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditTodoItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    private fun init() {

        initViewModel()
        prepare()

        with(binding) {
            launchDeadlineListener(textViewDeadline)

            inflateImportanceSelectionMenu(binding.textViewImportance)
            setImportanceViewListeners(textViewSelectedImportance)
            setTextViewDeadlineListener(textViewDeadline)
            setSwitchDeadlineListener(binding.switchDeadline)

            launchImportanceListener(textViewSelectedImportance)
            launchFormattedDateListener(textViewLastChange)

            setCancelEvent(imageViewCancel)
            setRemoveEvent(textViewRemove)
            setUpdateEvent(textViewSave, editTextTask)
        }
    }

    private fun initViewModel() {
        inject()
        viewModel = ViewModelProvider(this, viewModelFactory)[EditTodoItemViewModel::class.java]
    }

    private fun inject() {
        (requireActivity().application as TodoApp).appComponent.editTodoItemFragmentComponent()
            .inject(this)
    }

    private fun prepare() {
        val todoItem = args.todoItem

        viewModel.setItemToEdit(todoItem)
        binding.switchDeadline.isChecked = viewModel.isDeadline
        binding.editTextTask.setText(viewModel.text)
    }

    private fun setUpdateEvent(view: View, editTextTask: EditText) {

        view.setOnClickListener {
            viewModel.updateText(editTextTask.text.toString())

            if (viewModel.isInvalidDeadline) {
                Snackbar.make(binding.root, R.string.invalid_date, Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (viewModel.isInvalidText) {
                Snackbar.make(binding.root, R.string.invalid_text, Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            viewModel.update()
            findNavController().navigateUp()
        }
    }

    private fun setCancelEvent(imageView: ImageView) {

        imageView.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setRemoveEvent(view: View) {
        view.setOnClickListener {
            viewModel.removeTodoItem()
            findNavController().navigateUp()
        }
    }

    private fun launchFormattedDateListener(textView: TextView) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.lastChangedFormatted.collect { lastChange ->
                val textFormatted = "${getString(R.string.last_edit)} - $lastChange"
                textView.text = textFormatted
            }
        }
    }

    private fun setTextViewDeadlineListener(view: View) {

        view.setOnClickListener {

            val datePicker = DatePickerDialog(this.requireContext(), R.style.DatePicker)
            datePicker.updateDate(
                viewModel.deadlineYear,
                viewModel.deadlineMonth,
                viewModel.deadlineDay
            )
            datePicker.setOnDateSetListener { _, year, month, dayOfMonth ->
                viewModel.updateDeadline(dayOfMonth, month, year)
            }
            datePicker.show()
        }
    }

    private fun setImportanceViewListeners(view: View) {
        view.setOnClickListener {
            importanceMenu.show()
        }

        view.setOnClickListener {
            importanceMenu.show()
        }
    }

    private fun setSwitchDeadlineListener(checkBox: SwitchCompat) {

        checkBox.setOnCheckedChangeListener { _, isChecked ->

            viewModel.isDeadline = isChecked

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

    private fun launchImportanceListener(textView: TextView) {

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.importance.collect { importance ->

                when (importance) {
                    Importance.UNIMPORTANT -> textView.text =
                        getString(R.string.no)

                    Importance.LESS_IMPORTANT -> textView.text =
                        getString(R.string.less)

                    Importance.IMPORTANT -> textView.text =
                        getString(R.string.important)
                }
            }
        }
    }

    private fun launchDeadlineListener(textView: TextView) {

        lifecycleScope.launch {
            viewModel.deadline.collect {

                if (viewModel.isInvalidDeadline) textView.setTextColor(
                    ContextCompat.getColor(
                        this@EditTodoItemFragment.requireContext(), R.color.invalid
                    )
                )
                else textView.setTextColor(
                    ContextCompat.getColor(
                        this@EditTodoItemFragment.requireContext(), R.color.primary
                    )
                )

                textView.text = viewModel.formattedDeadline
            }
        }
    }

    private fun inflateImportanceSelectionMenu(view: View) {
        importanceMenu = PopupMenu(requireContext(), view)
        val inflater = importanceMenu.menuInflater
        inflater.inflate(R.menu.popup_menu, importanceMenu.menu)
        bindMenuEvents(importanceMenu)
    }

    private fun bindMenuEvents(menu: PopupMenu) {
        val importanceSelector = ImportanceSelector(viewModel)

        menu.setOnMenuItemClickListener { item ->
            importanceSelector.selectImportance(item.itemId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}