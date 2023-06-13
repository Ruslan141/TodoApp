package ru.versoit.todoapp.presentation.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.versoit.todoapp.R
import ru.versoit.todoapp.data.repository.TodoItemRepositoryImpl
import ru.versoit.todoapp.data.storage.datasources.mock.MockTodoItemDataSource
import ru.versoit.todoapp.databinding.FragmentNewTaskBinding
import ru.versoit.todoapp.domain.usecase.AddTodoItemUseCase
import ru.versoit.todoapp.presentation.viewmodels.NewTodoItemViewModel
import ru.versoit.todoapp.presentation.vmfactory.NewTodoItemViewModelFactory


class NewTodoItemFragment : Fragment() {

    private var _binding: FragmentNewTaskBinding? = null
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
        _binding = FragmentNewTaskBinding.inflate(inflater, container, false)
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

        binding.textViewAddTask.setOnClickListener {

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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}