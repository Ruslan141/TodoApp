package ru.versoit.todoapp.presentation.fragments

import android.graphics.Canvas
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.versoit.todoapp.R
import ru.versoit.todoapp.data.repository.TodoItemRepositoryImpl
import ru.versoit.todoapp.data.storage.datasources.RetrofitTodoItemDataSource
import ru.versoit.todoapp.data.storage.datasources.RoomTodoItemDataSource
import ru.versoit.todoapp.data.storage.datasources.SharedPrefsRevisionDataSource
import ru.versoit.todoapp.databinding.FragmentTodoItemsBinding
import ru.versoit.todoapp.domain.models.TodoItem
import ru.versoit.todoapp.domain.usecase.AddTodoItemUseCase
import ru.versoit.todoapp.domain.usecase.GetAllTodoItemsUseCase
import ru.versoit.todoapp.domain.usecase.TodoItemRemoveUseCase
import ru.versoit.todoapp.domain.usecase.TodoItemUpdateUseCase
import ru.versoit.todoapp.presentation.features.TodoItemEditor
import ru.versoit.todoapp.presentation.features.TodoItemsAdapter
import ru.versoit.todoapp.presentation.features.vmfactory.TodoItemsViewModelFactory
import ru.versoit.todoapp.presentation.viewmodels.TodoItemsViewModel


class TodoItemsFragment : Fragment(), TodoItemEditor {

    private var _binding: FragmentTodoItemsBinding? = null
    private val binding get() = _binding!!

    private var vibrator: Vibrator? = null

    lateinit var viewModel: TodoItemsViewModel

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

        vibrator = getSystemService(requireContext(), Vibrator::class.java)

        binding.floatingButtonAddNewTask.setOnClickListener {
            findNavController().navigate(R.id.action_tasksFragment_to_newTask)
        }

        binding.textViewAddNewTask.setOnClickListener {
            findNavController().navigate(R.id.action_tasksFragment_to_newTask)
        }

        createItemsList()
        bindTryHidingItemsTo(binding.imageViewHide)

        with(binding) {

            swipeRefreshLayout.setProgressViewOffset(false, -100, 10)

            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = true

                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.synchronizeWithNetwork()
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }

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

    private fun createItemsList() {

        val recyclerView = binding.recyclerViewTasks
        val adapter = TodoItemsAdapter(viewModel, viewModel, this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.showCompletedTodoItems()

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.getAllTodoItems().collect {
                adapter.submitList(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getReadyStatesAmount().collect {amount ->
                val completedText =
                    "${getString(R.string.completed)} - $amount"
                binding.textViewCompleted.text = completedText
            }
        }

        val simpleCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.absoluteAdapterPosition

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        viewModel.removeTodoItem(adapter[position])
                        performVibration()
                        Snackbar.make(
                            recyclerView,
                            R.string.removed_todo_item,
                            Snackbar.LENGTH_LONG
                        )
                            .setAction(R.string.undo) {
                                viewModel.undoDeletedTodoItem()
                            }.show()
                    }

                    ItemTouchHelper.RIGHT -> {
                        if (!adapter[position].done) {
                            viewModel.setCompletedTodoItem(adapter[position])
                            performVibration()
                        }
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val position = viewHolder.absoluteAdapterPosition
                if (position >= 0 && dX > 0 && adapter[position].done) {
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        0f,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                    return
                }
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeLeftBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.important_attention_text
                        )
                    )
                    .addSwipeRightBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.activated
                        )
                    )
                    .addSwipeLeftActionIcon(R.drawable.ic_remove_white)
                    .addSwipeRightActionIcon(R.drawable.ic_completed)
                    .create()
                    .decorate()
                super.onChildDraw(
                    c, recyclerView,
                    viewHolder, dX, dY, actionState, isCurrentlyActive
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.hideCompleted.collect { isHidden ->

                if (!isHidden) {
                    binding.imageViewHide.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(), R.drawable.ic_show
                        )
                    )
                } else {
                    binding.imageViewHide.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(), R.drawable.ic_hide
                        )
                    )
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun bindTryHidingItemsTo(view: ImageView) {

        view.setOnClickListener {

            if (viewModel.isTodoItemsHidden()) {
                viewModel.showCompletedTodoItems()
                return@setOnClickListener
            }

            viewModel.hideCompletedTodoItems()
        }
    }

    private fun performVibration() {
        val vibrationDuration: Long = 100

        vibrator?.let {

            if (it.hasVibrator()) {
                it.vibrate(
                    VibrationEffect.createOneShot(
                        vibrationDuration,
                        VibrationEffect.EFFECT_HEAVY_CLICK
                    )
                )
            }
        }
    }

    override fun edit(todoItem: TodoItem) {
        val action = TodoItemsFragmentDirections.actionTasksFragmentToEditTodoItemFragment(todoItem)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initViewModel() {
        val repository = TodoItemRepositoryImpl(
            RoomTodoItemDataSource(requireContext()),
            RetrofitTodoItemDataSource(),
            SharedPrefsRevisionDataSource(requireContext())
        )

        viewModel = ViewModelProvider(
            this, TodoItemsViewModelFactory(
                TodoItemUpdateUseCase(
                    repository
                ),
                TodoItemRemoveUseCase(
                    repository
                ),
                AddTodoItemUseCase(
                    repository
                ),
                GetAllTodoItemsUseCase(
                    repository
                ),
                repository,
                repository
            )
        )[TodoItemsViewModel::class.java]
    }
}