import requests
import random
import concurrent.futures
import threading
import time

# Define the endpoint
API_URL = "http://localhost:8080/trades"

# Initialize thread-safe counters for successful and failed trades
success_count = 0
failure_count = 0
counter_lock = threading.Lock()  # To synchronize counter updates

# Generate random trades
def generate_random_trade():
    user_id = random.randint(1, 10) # Randomize userId (1 or 2)
    trade_type = random.choice(["BUY", "SELL"])  # Randomize trade type
    coin = random.choice(["BTC", "ETH"])  # Randomize coin

    if coin == "BTC":
        price = random.uniform(45000, 50000)  # BTC price range: 45,000 to 50,000
        coin_a_value = 0.001  # Fixed BTC trade amount
        coin_b_value = round(price * coin_a_value, 2)  # Calculate equivalent USDT
    else:  # ETH
        price = random.uniform(2500, 3500)  # ETH price range: 2,500 to 3,500
        coin_a_value = 0.01  # Fixed ETH trade amount
        coin_b_value = round(price * coin_a_value, 2)  # Calculate equivalent USDT

    # Create trade payload
    return {
        "userId": user_id,
        "coinACode": coin,
        "coinBCode": "USDT",
        "coinAValue": str(coin_a_value),  # Convert to string for JSON compatibility
        "coinBValue": str(coin_b_value),
        "price": str(round(price, 2)),  # Round price to 2 decimal places
        "tradeType": trade_type
    }

# Post trades to the API
def post_trade(trade):
    global success_count, failure_count
    max_retries = 1
    for attempt in range(max_retries):
        try:
            response = requests.post(API_URL, json=trade, timeout=5)
            if response.status_code == 200:
                with counter_lock:
                    success_count += 1
                print(f"Trade posted successfully: {trade}")
                return
            else:
                print(f"Failed to post trade: {trade}. Response: {response.text}")
        except Exception as e:
            print(f"Error posting trade: {trade}. Attempt {attempt + 1} of {max_retries}. Exception: {e}")

    # If we exhaust retries, increment failure counter
    with counter_lock:
        failure_count += 1

# Generate and post multiple trades concurrently
def main():
    global success_count, failure_count

    # Start the timer
    start_time = time.time()

    # Use a ThreadPoolExecutor to send up to 5 parallel requests at a time
    with concurrent.futures.ThreadPoolExecutor(max_workers=5) as executor:
        # Generate 100 random trades and submit them as tasks
        futures = [executor.submit(post_trade, generate_random_trade()) for _ in range(100)]

        # Wait for all futures to complete
        concurrent.futures.wait(futures)

    # Stop the timer
    end_time = time.time()

    # Print the summary
    print(f"\nSummary:")
    print(f"Successful Trades: {success_count}")
    print(f"Failed Trades: {failure_count}")
    print(f"Time taken: {end_time - start_time:.2f} seconds")

if __name__ == "__main__":
    main()
