# Stock Tracking App for Krasamo Project Interview
Developed by @imjacobbh

This Android native app consist in a app that consumes the API of Alpha Vantage to get the stocks information of the users prefered companies.
User is able to add the companies that he wants and track their behavior in the market.

The app uses the endpoint https://www.alphavantage.co/documentation/#latestprice to get the user stocks list, and use the endpoint https://www.alphavantage.co/documentation/#symbolsearch 
to get accuture symbols of companies when the user wants to add to their list; in this way, we are sure that the added stocks symbols do exists.

## Installation Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/imjacobbh/StockTrackingApp.git
cd StockTrackingApp
```

### 2. Get an API Key from Alpha Vantage

- Visit the [Alpha Vantage website](https://www.alphavantage.co/) and sign up for an API key.
- Copy your API key.

### 3. Add API Key to the App

- Open the project in Android Studio.
- Locate the `local.properties` file and replace the value with your Alpha Vantage API key.

  ```kotlin
  api_key= <YOUR API KEY>
  api_key_debug=demo
  base_url_debug=https://www.alphavantage.co/
  base_url_release=https://www.alphavantage.co/
  ```

### 4. Build and Run the App

- Build and run the app using Android Studio.
- Ensure that you have a compatible emulator or a physical device connected.

### 5. Explore the App

- Once the app is installed and running, explore its features.
- Add your preferred companies, track their behavior, and enjoy the stock tracking experience!

## Usage

The Stock Tracking App allows users to conveniently track the behavior of their preferred companies in the market. Follow the steps below to make the most out of the app:

### Adding Companies

1. Launch the Stock Tracking App on your device.

2. Click on the "Add" button to add a new company to your tracking list.

3. Use the symbol search functionality to find and add companies. The app ensures that added stock symbols exist by utilizing the Alpha Vantage endpoint [Symbol Search](https://www.alphavantage.co/documentation/#symbolsearch).

### Tracking Company Behavior

1. Once you've added companies to your list, the app will fetch and display their stock information using the [Latest Price](https://www.alphavantage.co/documentation/#latestprice) endpoint from Alpha Vantage.

2. Explore the app's user-friendly interface to view and analyze the behavior of your tracked companies.

### Enjoying the Stock Tracking Experience

1. Take advantage of the app's features to monitor market trends and make informed decisions.

2. Stay updated on stock prices and changes in real-time.

3. Use the app regularly to make the most out of its powerful tracking capabilities.

Feel free to explore and customize the app to suit your stock tracking needs!

**Note:**
- Ensure that your device has an active internet connection to fetch the latest stock information.

## ScreenShots

<img src="https://github.com/imjacobbh/StockTrackingApp/assets/56369955/775910db-d66d-4588-9fba-980f732e9b4c" alt="Screenshot 1" width="250"/>  

<img src="https://github.com/imjacobbh/StockTrackingApp/assets/56369955/634edc1b-014d-4338-96d4-acaf4af28422" alt="Screenshot 1" width="250"/>  

<img src="https://github.com/imjacobbh/StockTrackingApp/assets/56369955/bd3dafd1-f493-4a61-b9ac-0c014db0801a" alt="Screenshot 1" width="250"/>  

<img src="https://github.com/imjacobbh/StockTrackingApp/assets/56369955/46d2713d-6244-47de-b56f-736784f9f4fb" alt="Screenshot 1" width="250"/>  

<img src="https://github.com/imjacobbh/StockTrackingApp/assets/56369955/65d66164-48ea-416d-b5a8-717f9952fb87" alt="Screenshot 1" width="250"/>  

 






