package ru.versoit.todoapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.versoit.todoapp.R
import ru.versoit.todoapp.data.repository.TodoItemRepositoryImpl
import ru.versoit.todoapp.data.storage.datasources.mock.MockTodoItemDataSource
import ru.versoit.todoapp.databinding.FragmentTodoItemsBinding
import ru.versoit.todoapp.domain.usecase.GetAllTodoItemsUseCase
import ru.versoit.todoapp.domain.usecase.GetTodoItemByIdUseCase
import ru.versoit.todoapp.domain.usecase.TodoItemRemoveUseCase
import ru.versoit.todoapp.domain.usecase.TodoItemUpdateUseCase
import ru.versoit.todoapp.presentation.adapters.TodoItemsAdapter
import ru.versoit.todoapp.presentation.viewmodels.TodoItemsViewModel
import ru.versoit.todoapp.presentation.vmfactory.TodoItemsViewModelFactory

class TodoItemsFragment : Fragment() {

    private var _binding: FragmentTodoItemsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TodoItemsViewModel by viewModels {

        TodoItemsViewModelFactory(
            TodoItemUpdateUseCase(TodoItemRepositoryImpl(MockTodoItemDataSource)),
            TodoItemRemoveUseCase(TodoItemRepositoryImpl(MockTodoItemDataSource)),
            GetTodoItemByIdUseCase(TodoItemRepositoryImpl(MockTodoItemDataSource)),
            GetAllTodoItemsUseCase(TodoItemRepositoryImpl(MockTodoItemDataSource)),
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTodoItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {

        binding.floatingButtonAddNewTask.setOnClickListener {
            findNavController().navigate(R.id.action_tasksFragment_to_newTask)
        }

        binding.textViewAddNewTask.setOnClickListener {
            findNavController().navigate(R.id.action_tasksFragment_to_newTask)
        }

        createItemsList()

        binding.imageViewHideShow.setOnClickListener {

            if (viewModel.isHidden) {
                viewModel.showCompletedTodoItems()
                binding.imageViewHideShow.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_show))
                return@setOnClickListener
            }

            viewModel.hideCompletedTodoItems()
            binding.imageViewHideShow.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_hide))
            viewModel.hideCompletedTodoItems()
        }
    }

    private fun createItemsList() {

        val recyclerView = binding.recyclerViewTasks
        val adapter = TodoItemsAdapter(viewModel)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.todoItemsObservable.observe(viewLifecycleOwner) {
            adapter.submitList(it)

            val completedText = "${getString(R.string.completed)} - ${viewModel.readyStatesAmount}"
            binding.textViewCompleted.text = completedText
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}