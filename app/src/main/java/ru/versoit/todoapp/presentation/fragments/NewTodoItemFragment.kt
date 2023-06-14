package ru.versoit.todoapp.presentation.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.versoit.todoapp.R
import ru.versoit.todoapp.data.repository.TodoItemRepositoryImpl
import ru.versoit.todoapp.data.storage.datasources.mock.MockTodoItemDataSource
import ru.versoit.todoapp.databinding.FragmentNewTodoItemBinding
import ru.versoit.todoapp.domain.models.Importance
import ru.versoit.todoapp.domain.usecase.AddTodoItemUseCase
import ru.versoit.todoapp.presentation.viewmodels.NewTodoItemViewModel
import ru.versoit.todoapp.presentation.vmfactory.NewTodoItemViewModelFactory


class NewTodoItemFragment : Fragment() {

    private var _binding: FragmentNewTodoItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<NewTodoItemViewModel> {
        NewTodoItemViewModelFactory(AddTodoItemUseCase(TodoItemRepositoryImpl(MockTodoItemDataSource)))
    }

    companion object {

        private const val INACTIVE = 0.2f
        private const val ACTIVE = 1f
    }

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

        binding.imageViewCancel.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.currentDate.observe(viewLifecycleOwner) {

            if (viewModel.isInvalidDate) binding.textViewDeadline.setTextColor(
                ContextCompat.getColor(
                    this.requireContext(), R.color.invalid
                )
            )
            else binding.textViewDeadline.setTextColor(
                ContextCompat.getColor(
                    this.requireContext(), R.color.primary
                )
            )

            binding.textViewDeadline.text = viewModel.formattedDate
        }

        binding.textViewImportance.setOnClickListener {
            showPopupMenu(binding.textViewImportance)
        }

        binding.textViewSelectedImportance.setOnClickListener {
            showPopupMenu(binding.textViewImportance)
        }

        binding.textViewDeadline.setOnClickListener {

            val datePicker = DatePickerDialog(this.requireContext())
            datePicker.updateDate(viewModel.year, viewModel.month, viewModel.day)
            datePicker.setOnDateSetListener { _, year, month, dayOfMonth ->
                viewModel.updateDate(dayOfMonth, month, year)
            }
            datePicker.show()
        }

        binding.switchDeadline.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                binding.textViewDeadline.text = viewModel.formattedDate
                binding.textViewDeadline.visibility = View.VISIBLE
                binding.textViewMakeUp.alpha = ACTIVE
                return@setOnCheckedChangeListener
            }
            binding.textViewDeadline.visibility = View.GONE
            binding.textViewMakeUp.alpha = INACTIVE
        }

        binding.textViewSave.setOnClickListener {

            viewModel.text = binding.editTextTask.text.toString()

            if (viewModel.isInvalidDate) {
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

        viewModel.importance.observe(viewLifecycleOwner) {

            when (it!!) {
                Importance.UNIMPORTANT -> binding.textViewSelectedImportance.text =
                    getString(R.string.no)

                Importance.LESS_IMPORTANT -> binding.textViewSelectedImportance.text =
                    getString(R.string.less)

                Importance.IMPORTANT -> binding.textViewSelectedImportance.text =
                    getString(R.string.important)
            }
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.popup_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
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
        popupMenu.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}