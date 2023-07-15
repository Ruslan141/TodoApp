package ru.versoit.todoapp.presentation.fragments

import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.versoit.todoapp.R
import ru.versoit.todoapp.app.TodoApp
import ru.versoit.todoapp.databinding.FragmentTodoItemsBinding
import ru.versoit.todoapp.presentation.features.ThemeStatusChanger
import ru.versoit.todoapp.presentation.features.TodoItemEditor
import ru.versoit.todoapp.presentation.features.TodoItemsAdapter
import ru.versoit.todoapp.presentation.features.dialogs.AppThemeBottomSheetSelection
import ru.versoit.todoapp.presentation.features.dialogs.NotificationPermissionsDialog
import ru.versoit.todoapp.presentation.features.notifications.NotificationScheduler
import ru.versoit.todoapp.presentation.features.vmfactory.TodoItemsViewModelFactory
import ru.versoit.todoapp.presentation.viewmodels.TodoItemsViewModel
import javax.inject.Inject

/**
 * Fragment to display and manipulate all todo items.
 */
class TodoItemsFragment : Fragment(), TodoItemEditor {
    @Inject
    lateinit var viewModelFactory: TodoItemsViewModelFactory
    lateinit var viewModel: TodoItemsViewModel

    @Inject
    lateinit var notificationsScheduler: NotificationScheduler

    private var _binding: FragmentTodoItemsBinding? = null
    private val binding get() = _binding!!

    private var vibrator: Vibrator? = null

    companion object {
        private const val START_PROGRESS_VIEW_OFFSET = -100
        private const val END_PROGRESS_VIEW_OFFSET = 10
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTodoItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        init()
    }

    private fun init() {
        tryShowNotificationsPermissionsDialog()
        bindSettingsButtonToOpenSettingsBottomSheet()

        vibrator = getSystemService(requireContext(), Vibrator::class.java)

        with(binding) {
            floatingButtonAddNewTask.setOnClickListener {
                findNavController().navigate(R.id.action_tasksFragment_to_newTask)
            }
            textViewAddNewTask.setOnClickListener {
                findNavController().navigate(R.id.action_tasksFragment_to_newTask)
            }
            createItemsList()
            bindTryHidingItemsTo(imageViewHide)
            setSyncFailureCallback(viewModel)
            setRefreshActions()
            setCloseEventOnBackPressed()
        }
    }

    private fun bindSettingsButtonToOpenSettingsBottomSheet() {
        val bottomSheetSelection = AppThemeBottomSheetSelection(
            requireContext(),
            layoutInflater,
            ThemeStatusChanger(viewModel),
            viewModel,
            requireActivity()
        )
        binding.imageViewSettings.setOnClickListener {
            bottomSheetSelection.show()
        }
    }

    private fun tryShowNotificationsPermissionsDialog() {
        val notificationPermissionsDialog =
            NotificationPermissionsDialog(requireContext(), viewModel, layoutInflater)

        CoroutineScope(Dispatchers.Main).launch {
            notificationPermissionsDialog.tryShow()
        }
    }

    private fun setCloseEventOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
    }

    private fun inject() {
        (requireActivity().applicationContext as TodoApp).appComponent.todoItemsFragmentComponent()
            .inject(this)
    }

    private fun setRefreshActions() {

        with(binding) {

            swipeRefreshLayout.setProgressViewOffset(
                false,
                START_PROGRESS_VIEW_OFFSET,
                END_PROGRESS_VIEW_OFFSET
            )

            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = true

                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.synchronizeWithNetwork()
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    private fun setSyncFailureCallback(viewModel: TodoItemsViewModel) {

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.setSyncFailureCallback {
                Snackbar.make(
                    this@TodoItemsFragment.requireView(),
                    R.string.network_error,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.retry) {
                        CoroutineScope(Dispatchers.IO).launch {
                            viewModel.synchronizeWithNetwork()
                        }
                    }
                    .show()
            }
        }
    }

    private fun initRecyclerComponents(): RecyclerView {

        viewModel.loadTodoItems()

        val recyclerView = binding.recyclerViewTasks
        val adapter = TodoItemsAdapter(viewModel, viewModel, this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.todoItemsFlow.collect { list ->
                adapter.submitList(list)
                if (list != null)
                    notificationsScheduler.scheduleNotifications(list)
            }
        }

        return recyclerView
    }

    private fun createItemsList() {

        val recyclerView = initRecyclerComponents()
        val adapter = recyclerView.adapter as TodoItemsAdapter
        setTodoItemsDoneAmountListener()

        RecyclerViewSwiper(
            recyclerView = recyclerView,
            adapter = adapter,
            context = requireContext(),
            undoDeleter = viewModel,
            todoItemCompleter = viewModel,
            todoItemsRemover = viewModel
        ).attachSwipesToRecyclerView()

        launchHideOrShowCompletedEvent()
    }

    private fun launchHideOrShowCompletedEvent() {
        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.hideCompleted.collect { isHidden ->
                if (!isHidden) {
                    binding.imageViewHide.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_show
                        )
                    )
                } else {
                    binding.imageViewHide.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_hide
                        )
                    )
                }
            }
        }
    }

    private fun bindTryHidingItemsTo(view: ImageView) {

        view.setOnClickListener {
            if (viewModel.isCompletedTodoItemsHidden()) {
                viewModel.showCompletedTodoItems()
                return@setOnClickListener
            }
            viewModel.hideCompletedTodoItems()
        }
    }

    private fun setTodoItemsDoneAmountListener() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.todoItemsDoneAmount.collect { doneAmount ->
                val completedText =
                    "${getString(R.string.completed)} - $doneAmount"
                binding.textViewCompleted.text = completedText
            }
        }
    }

    override fun edit(todoItem: ru.versoit.domain.models.TodoItem) {
        val action = TodoItemsFragmentDirections.actionTasksFragmentToEditTodoItemFragment(todoItem)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initViewModel() {
        inject()
        viewModel = ViewModelProvider(this, viewModelFactory)[TodoItemsViewModel::class.java]
    }
}