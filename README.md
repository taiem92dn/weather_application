# Weather App

## Android development
- Implement Clean Architecture using Multi-module architecture
  - Separated to 3 layers or modules: **app**, **data**, **domain**
  - **Domain** layer is full of abstraction: repositories, data sources and usecases
  - **Data** layer is for handling data flow in the app such as Remote Data, Local Data
  - **Presentation** layer or "app" module for handling UI layer using MVVM architecture that associates with Architecture Components including: Navigation, ViewModel, StateFlow, LiveData, DataBinding, Lifecycles
- Hilt for dependency injection
- Retrofit for requesting API
- Use Kotlin Coroutines
- Use Material Design
  
 ## Supported OS versions
 Minimum Android 5.0 Lollipop
 ## Supported devices: 
 Phone only
 ## Instructions to build and launch the app.
- Clone Repository
- Open the project by using Android Studio
- Fill the _OpenWeatherMap Api Key_ in the "apikey.prperties" file located in the root project folder
- Build and run app
  
 ## Supported features
  ### Home screen
  - Show Favorite city list
  ### Search cities screen
  - Allow to search locations by city name, to find out which cities have weather info
  ### City detail screen
  - View the current weather of a city and the next 3 days' weather predictions
  
 ## Requirements to build the app.
  - Android Studio Jellyfish | 2023.3.1
