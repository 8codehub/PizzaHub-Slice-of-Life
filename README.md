# PizzaHub

**PizzaHub** is a pizza ordering application built using MVI + Clean Architecture with Kotlin. It utilizes Hilt for dependency injection, Flow for reactive programming, and coroutines for asynchronous operations.

## Tech Stack

- Kotlin
- MVI (Model-View-Intent) architecture
- Clean Architecture principles
- Hilt for dependency injection
- Flow for reactive programming
- Coroutines for asynchronous operations

## Unit Testing

The PizzaHub application includes comprehensive unit tests to ensure functionality and reliability. The `MenuListViewModelTest` class focuses on testing the behavior of the `MenuListViewModel` class. Here are some of the test cases covered in the `MenuListViewModelTest`:

- `handleLoadDataIntent should fetch menu items when the network is available`: This test verifies the correct fetching of menu items when the network is available.
- `handleLoadDataIntent should handle data fetch error when the network is unavailable`: This test ensures proper handling of data fetch errors in case of no internet connection.
- `handleLoadDataIntent should handle data fetch error when the API response is empty`: This test checks the handling of an empty API response.
- `handleMenuListItemClickIntent should update selected menu items correctly`: This test validates the correct update of selected menu items.
- `handleCalculateTotalPizzaPriceIntent should calculate the total pizza price correctly if we select two pizzas`: This test checks the accurate calculation of the total price when two pizzas are selected.
- `handleCalculateTotalPizzaPriceIntent should calculate the total pizza price correctly if we select one pizza`: This test verifies the correct calculation of the total price when one pizza is selected.
- `handleConfirmButtonClickIntent should save the order and trigger navigation to the summary screen`: This test ensures the proper saving of the order and navigation to the summary screen when the confirm button is clicked.

These unit tests contribute to the stability and robustness of the PizzaHub application.

## Features

- Fetches pizza menu items from the server using the `FetchMenuItemsUseCase`.
- Displays the pizza menu items in a list.
- Allows the user to select pizza items.
- Calculates the total price based on the selected pizza items.
- Saves the order using the `SaveOrderUseCase`.
- Provides a summary screen with the order details.
- Handles scenarios when there is no internet connection.

## How It Works

The PizzaHub application follows a model-view-intent (MVI) architecture, ensuring separation of concerns and a reactive approach to UI updates. It uses Hilt for dependency injection, Flow for reactive programming, and coroutines for asynchronous operations.

### Fetching Pizza Menu Items

- The application uses the `FetchMenuItemsUseCase` to fetch pizza menu items from the server.
- It checks for internet availability using the `NetworkHelper` component.
- If there is an internet connection, the menu items are fetched and displayed in the UI.
- If there is no internet connection, an error message is shown.

### Selecting Pizza Items

- The user can select one or two pizza items from the menu.
- When one pizza item is selected, the total price is calculated as the price of the selected item.
- When two pizza items are selected, the total price is calculated as the average price of the two items.

### Saving the Order

- When the user confirms the selection and clicks the "Confirm" button, the order is saved using the `SaveOrderUseCase`.
- The order details are then displayed on the summary screen.

## Video Demo

Click [here](https://drive.google.com/file/d/1BJtHVNszRwojYGOLIayIQw9c32vzVL7k/view) to watch a video demonstration of the PizzaHub application.