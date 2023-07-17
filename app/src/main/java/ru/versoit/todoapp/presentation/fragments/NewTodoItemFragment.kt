package ru.versoit.todoapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.versoit.todoapp.app.TodoApp
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            initializeViewModel()
            setContent {
                NewTodoItemScreen(findNavController())
            }
        }
    }

    private fun initializeViewModel() {
        inject()
        viewModel = ViewModelProvider(this, viewModelFactory)[NewTodoItemViewModel::class.java]
    }

    private fun inject() {
        (requireActivity().application as TodoApp).appComponent.newTodoItemFragmentComponent()
            .inject(this)
    }
}
