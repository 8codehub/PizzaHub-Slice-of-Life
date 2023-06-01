import com.dev.pizzahub.domain.helper.NetworkHelper
import com.dev.pizzahub.domain.model.MenuListItem
import com.dev.pizzahub.domain.use_case.FetchMenuItemsUseCase
import com.dev.pizzahub.domain.use_case.SaveOrderUseCase
import com.dev.pizzahub.presentation.menu_list.MenuListIntent
import com.dev.pizzahub.presentation.menu_list.MenuListViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MenuListViewModelTest {

    private lateinit var viewModel: MenuListViewModel
    private lateinit var fetchMenuItemsUseCase: FetchMenuItemsUseCase
    private lateinit var saveOrderUseCase: SaveOrderUseCase
    private lateinit var networkHelper: NetworkHelper

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined) // Initialize the main dispatcher
        fetchMenuItemsUseCase = mockk()
        saveOrderUseCase = mockk()
        networkHelper = mockk()
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `handleLoadDataIntent should fetch menu items when network is available`() = runBlocking {

        // Mock dependencies
        val fetchMenuItemsUseCase: FetchMenuItemsUseCase = mockk()
        val networkHelper: NetworkHelper = mockk()
        viewModel = MenuListViewModel(fetchMenuItemsUseCase, networkHelper, saveOrderUseCase)

        // Set up network availability
        every { networkHelper.isInternetAvailable() } returns true

        // Set up the expected menu items
        val menuItems = listOf(
            MenuListItem(1, 9.99, "Pizza 1"), MenuListItem(2, 12.99, "Pizza 2"), MenuListItem(3, 11.99, "Pizza 3")
        )

        // Set up the expected state flow
        val stateFlow = flow {
            emit(Result.success(menuItems))
        }

        // Set up the fetchMenuItemsUseCase to return the state flow
        coEvery { fetchMenuItemsUseCase() } returns stateFlow

        // Create an instance of the MenuListViewModel with the mocked dependencies
        val viewModel = MenuListViewModel(fetchMenuItemsUseCase, networkHelper, saveOrderUseCase)

        // Call the handleLoadDataIntent function
        viewModel.handleIntent(MenuListIntent.LoadData)

        // Verify that the fetchMenuItemsUseCase is called
        coVerify { fetchMenuItemsUseCase() }
    }


    @Test
    fun `handleLoadDataIntent should handle data fetch error when network is unavailable`() = runBlockingTest {
        // Arrange
        val fetchMenuItemsUseCase: FetchMenuItemsUseCase = mockk()
        val networkHelper: NetworkHelper = mockk()
        val saveOrderUseCase: SaveOrderUseCase = mockk()

        // Mock the isInternetAvailable() function
        every { networkHelper.isInternetAvailable() } returns false

        val viewModel = MenuListViewModel(fetchMenuItemsUseCase, networkHelper, saveOrderUseCase)

        // Act
        viewModel.sendIntent(MenuListIntent.LoadData)

        // Assert
        val currentState = viewModel.viewState.value
        assertEquals("No Internet Connection", currentState.dataErrorMessage)
        assertEquals(emptyList<MenuListItem>(), currentState.data)
    }

    @Test
    fun `handleLoadDataIntent should handle data fetch error when API response is empty`() = runBlockingTest {
        // Arrange
        val fetchMenuItemsUseCase: FetchMenuItemsUseCase = mockk()
        val networkHelper: NetworkHelper = mockk()
        val saveOrderUseCase: SaveOrderUseCase = mockk()

        // Mock the network availability
        every { networkHelper.isInternetAvailable() } returns true

        // Mock the empty API response
        val emptyResponse: List<MenuListItem> = emptyList()
        coEvery { fetchMenuItemsUseCase.invoke() } returns flow {
            emit(Result.success(emptyResponse))
        }

        val viewModel = MenuListViewModel(fetchMenuItemsUseCase, networkHelper, saveOrderUseCase)

        // Act
        viewModel.sendIntent(MenuListIntent.LoadData)

        // Assert
        val currentState = viewModel.viewState.value
        assertNull(currentState.dataErrorMessage)
        assertEquals(emptyResponse, currentState.data)
    }

    @Test
    fun `handleMenuListItemClickIntent should update selected menu items correctly`() = runBlockingTest {
        // Arrange
        val fetchMenuItemsUseCase: FetchMenuItemsUseCase = mockk()
        val networkHelper: NetworkHelper = mockk()
        val saveOrderUseCase: SaveOrderUseCase = mockk()

        // Mock the network availability
        every { networkHelper.isInternetAvailable() } returns true

        // Create some sample menu items
        val menuItems = listOf(
            MenuListItem(1, 9.99, "Pizza 1"), MenuListItem(2, 8.99, "Pizza 2"), MenuListItem(3, 7.99, "Pizza 3")
        )

        // Mock the successful API response
        coEvery { fetchMenuItemsUseCase.invoke() } returns flow {
            emit(Result.success(menuItems))
        }

        val viewModel = MenuListViewModel(fetchMenuItemsUseCase, networkHelper, saveOrderUseCase)

        // Act
        viewModel.sendIntent(MenuListIntent.LoadData)

        // Simulate selecting some menu items
        viewModel.sendIntent(MenuListIntent.MenuListItemClick(menuItems[0])) // Select Pizza 1
        viewModel.sendIntent(MenuListIntent.MenuListItemClick(menuItems[2])) // Select Pizza 3

        // Assert
        val currentState = viewModel.viewState.value
        assertEquals(listOf(menuItems[0], menuItems[2]), currentState.selectedMenuItems)
    }

    @Test
    fun `handleCalculateTotalPizzaPriceIntent should calculate total pizza price correctly if we select two pizza`() = runBlockingTest {
        // Arrange
        val fetchMenuItemsUseCase: FetchMenuItemsUseCase = mockk()
        val networkHelper: NetworkHelper = mockk()
        val saveOrderUseCase: SaveOrderUseCase = mockk()

        // Mock the network availability
        every { networkHelper.isInternetAvailable() } returns true

        // Create some sample menu items
        val menuItems = listOf(
            MenuListItem(1, 9.99, "Pizza 1"),
            MenuListItem(2, 8.99, "Pizza 2"),
            MenuListItem(3, 7.99, "Pizza 3")
        )

        // Mock the successful API response
        coEvery { fetchMenuItemsUseCase.invoke() } returns flow {
            emit(Result.success(menuItems))
        }

        val viewModel = MenuListViewModel(fetchMenuItemsUseCase, networkHelper, saveOrderUseCase)

        // Act
        viewModel.sendIntent(MenuListIntent.LoadData)

        // Simulate selecting some menu items
        viewModel.sendIntent(MenuListIntent.MenuListItemClick(menuItems[0])) // Select Pizza 1
        viewModel.sendIntent(MenuListIntent.MenuListItemClick(menuItems[1])) // Select Pizza 2

        // Calculate total pizza price
        viewModel.sendIntent(MenuListIntent.CalculateTotalPizzaPrice)

        // Assert
        val currentState = viewModel.viewState.value
        assertEquals(18.98 / 2, currentState.order.totalPrice, 0.01)
    }

    @Test
    fun `handleCalculateTotalPizzaPriceIntent should calculate total pizza price correctly if we select one pizza`() = runBlockingTest {
        // Arrange
        val fetchMenuItemsUseCase: FetchMenuItemsUseCase = mockk()
        val networkHelper: NetworkHelper = mockk()
        val saveOrderUseCase: SaveOrderUseCase = mockk()

        // Mock the network availability
        every { networkHelper.isInternetAvailable() } returns true

        // Create some sample menu items
        val menuItems = listOf(
            MenuListItem(1, 9.99, "Pizza 1"),
            MenuListItem(2, 8.99, "Pizza 2"),
            MenuListItem(3, 7.99, "Pizza 3")
        )

        // Mock the successful API response
        coEvery { fetchMenuItemsUseCase.invoke() } returns flow {
            emit(Result.success(menuItems))
        }

        val viewModel = MenuListViewModel(fetchMenuItemsUseCase, networkHelper, saveOrderUseCase)

        // Act
        viewModel.sendIntent(MenuListIntent.LoadData)

        // Simulate selecting some menu items
        viewModel.sendIntent(MenuListIntent.MenuListItemClick(menuItems[0])) // Select Pizza 1

        // Calculate total pizza price
        viewModel.sendIntent(MenuListIntent.CalculateTotalPizzaPrice)

        // Assert
        val currentState = viewModel.viewState.value
        assertEquals(9.99, currentState.order.totalPrice, 0.01)
    }

    @Test
    fun `handleConfirmButtonClickIntent should save the order and trigger navigation to summary screen`() = runBlockingTest {
        // Arrange
        val fetchMenuItemsUseCase: FetchMenuItemsUseCase = mockk()
        val networkHelper: NetworkHelper = mockk()
        val saveOrderUseCase: SaveOrderUseCase = mockk(relaxed = true) // Relax the behavior of SaveOrderUseCase

        // Mock the network availability
        every { networkHelper.isInternetAvailable() } returns true

        // Create some sample menu items
        val menuItems = listOf(
            MenuListItem(1, 9.99, "Pizza 1"),
            MenuListItem(2, 8.99, "Pizza 2"),
            MenuListItem(3, 7.99, "Pizza 3")
        )

        // Mock the successful API response
        coEvery { fetchMenuItemsUseCase.invoke() } returns flow {
            emit(Result.success(menuItems))
        }

        val viewModel = MenuListViewModel(fetchMenuItemsUseCase, networkHelper, saveOrderUseCase)

        // Act
        viewModel.sendIntent(MenuListIntent.LoadData)

        // Simulate selecting some menu items
        viewModel.sendIntent(MenuListIntent.MenuListItemClick(menuItems[0])) // Select Pizza 1
        viewModel.sendIntent(MenuListIntent.MenuListItemClick(menuItems[1])) // Select Pizza 2

        // Trigger the confirm button click
        viewModel.sendIntent(MenuListIntent.ConfirmButtonClick)

        // Assert
        verify(exactly = 1) { saveOrderUseCase.invoke(any()) } // Verify that SaveOrderUseCase was invoked
    }

}