const foods = [
    {
        id: 1,
        name: "Happy Burger Combo",
        category: "Burger",
        price: 420,
        rating: 4.9,
        description: "Juicy beef burger served with a refreshing drink.",
        image: "images/images.jpg"
    },

    {
        id: 2,
        name: "Classic Beef Burger",
        category: "Burger",
        price: 320,
        rating: 4.8,
        description: "Beef patty, cheese, lettuce, tomato and special sauce.",
        image: "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=800&q=80"
    },

    {
        id: 3,
        name: "Pepperoni Pizza",
        category: "Pizza",
        price: 650,
        rating: 4.9,
        description: "Cheesy pizza with pepperoni and rich tomato sauce.",
        image: "https://images.unsplash.com/photo-1574071318508-1cdbab80d002?auto=format&fit=crop&w=800&q=80"
    },

    {
        id: 4,
        name: "Chicken Biryani",
        category: "Rice",
        price: 350,
        rating: 4.8,
        description: "Aromatic basmati rice served with spicy chicken.",
        image: "https://images.unsplash.com/photo-1563379926898-05f4575a45d8?auto=format&fit=crop&w=800&q=80"
    },

    {
        id: 5,
        name: "Chocolate Cake",
        category: "Dessert",
        price: 280,
        rating: 4.7,
        description: "Soft chocolate cake covered with rich chocolate cream.",
        image: "https://images.unsplash.com/photo-1578985545062-69928b1d9587?auto=format&fit=crop&w=800&q=80"
    },

    {
        id: 6,
        name: "Fresh Orange Juice",
        category: "Drinks",
        price: 160,
        rating: 4.6,
        description: "Freshly prepared orange juice served chilled.",
        image: "https://images.unsplash.com/photo-1621506289937-a8e4df240d0b?auto=format&fit=crop&w=800&q=80"
    },

    {
        id: 7,
        name: "Chicken Pizza",
        category: "Pizza",
        price: 580,
        rating: 4.7,
        description: "Chicken pizza with cheese, vegetables and sauce.",
        image: "https://images.unsplash.com/photo-1579751626657-72bc17010498?auto=format&fit=crop&w=800&q=80"
    },

    {
        id: 8,
        name: "Chicken Burger",
        category: "Burger",
        price: 290,
        rating: 4.7,
        description: "Crispy chicken burger with fresh vegetables.",
        image: "https://images.unsplash.com/photo-1550547660-d9450f859349?auto=format&fit=crop&w=800&q=80"
    },

    {
        id: 9,
        name: "Cold Coffee",
        category: "Drinks",
        price: 190,
        rating: 4.5,
        description: "Cold creamy coffee served with ice and chocolate.",
        image: "https://images.unsplash.com/photo-1461023058943-07fcbe16d735?auto=format&fit=crop&w=800&q=80"
    }
];


let selectedCategory = "All";

let cart = JSON.parse(localStorage.getItem("foodJoyCart")) || [];


/* Display Food Cards */

function displayFoods(foodList) {

    const foodContainer =
        document.getElementById("foodContainer");

    const noResult =
        document.getElementById("noResult");

    foodContainer.innerHTML = "";

    if (foodList.length === 0) {
        noResult.style.display = "block";
        return;
    }

    noResult.style.display = "none";

    foodList.forEach(food => {

        foodContainer.innerHTML += `
            <article class="food-card">

                <div class="food-image-box">

                    <img
                        src="${food.image}"
                        alt="${food.name}"
                        onerror="this.onerror=null; this.src='images/images.jpg';">

                    <span class="rating">
                        ⭐ ${food.rating}
                    </span>

                    <span class="food-category">
                        ${food.category}
                    </span>

                </div>

                <div class="food-information">

                    <h3>${food.name}</h3>

                    <p>${food.description}</p>

                    <div class="food-bottom">

                        <span class="food-price">
                            ৳${food.price}
                        </span>

                        <button
                            class="add-button"
                            onclick="addToCart(${food.id})">

                            Add to Cart

                        </button>

                    </div>

                </div>

            </article>
        `;
    });
}


/* Search and Category Filter */

function filterFoods() {

    const searchText =
        document
            .getElementById("searchInput")
            .value
            .toLowerCase()
            .trim();

    const filteredFoods = foods.filter(food => {

        const matchesSearch =
            food.name.toLowerCase().includes(searchText) ||
            food.description.toLowerCase().includes(searchText);

        const matchesCategory =
            selectedCategory === "All" ||
            food.category === selectedCategory;

        return matchesSearch && matchesCategory;
    });

    displayFoods(filteredFoods);
}


function selectCategory(category, button) {

    selectedCategory = category;

    document
        .querySelectorAll(".category-button")
        .forEach(item => {
            item.classList.remove("active");
        });

    button.classList.add("active");

    filterFoods();
}


/* Add Food to Cart */

function addToCart(foodId) {

    const selectedFood =
        foods.find(food => food.id === foodId);

    const existingItem =
        cart.find(item => item.id === foodId);

    if (existingItem) {
        existingItem.quantity++;
    } else {
        cart.push({
            ...selectedFood,
            quantity: 1
        });
    }

    saveCart();

    updateCart();

    showNotification(
        selectedFood.name + " added to cart!"
    );
}


/* Update Cart */

function updateCart() {

    const cartItems =
        document.getElementById("cartItems");

    const emptyCart =
        document.getElementById("emptyCart");

    const cartSummary =
        document.getElementById("cartSummary");

    cartItems.innerHTML = "";

    if (cart.length === 0) {

        emptyCart.style.display = "flex";

        cartSummary.style.display = "none";

    } else {

        emptyCart.style.display = "none";

        cartSummary.style.display = "block";

        cart.forEach(item => {

            cartItems.innerHTML += `
                <div class="cart-item">

                    <img
                        src="${item.image}"
                        alt="${item.name}"
                        onerror="this.onerror=null; this.src='images/images.jpg';">

                    <div class="cart-item-information">

                        <h4>${item.name}</h4>

                        <p>
                            ৳${item.price * item.quantity}
                        </p>

                        <div class="quantity-container">

                            <button
                                onclick="changeQuantity(${item.id}, -1)">
                                −
                            </button>

                            <span>${item.quantity}</span>

                            <button
                                onclick="changeQuantity(${item.id}, 1)">
                                +
                            </button>

                        </div>

                    </div>

                    <button
                        class="remove-button"
                        onclick="removeItem(${item.id})"
                        title="Remove item">

                        🗑

                    </button>

                </div>
            `;
        });
    }

    updatePrice();
}


/* Quantity Controls */

function changeQuantity(foodId, amount) {

    const selectedItem =
        cart.find(item => item.id === foodId);

    if (!selectedItem) {
        return;
    }

    selectedItem.quantity += amount;

    if (selectedItem.quantity <= 0) {
        removeItem(foodId);
        return;
    }

    saveCart();

    updateCart();
}


/* Remove Item */

function removeItem(foodId) {

    cart = cart.filter(item => item.id !== foodId);

    saveCart();

    updateCart();

    showNotification("Item removed from cart.");
}


/* Price Calculation */

function updatePrice() {

    const subtotal = cart.reduce(
        (sum, item) =>
            sum + item.price * item.quantity,
        0
    );

    const totalQuantity = cart.reduce(
        (sum, item) =>
            sum + item.quantity,
        0
    );

    const deliveryFee =
        subtotal === 0
            ? 0
            : subtotal >= 1000
                ? 0
                : 60;

    const total = subtotal + deliveryFee;

    document.getElementById("cartCount").innerText =
        totalQuantity;

    document.getElementById("subtotal").innerText =
        subtotal;

    document.getElementById("deliveryFee").innerText =
        deliveryFee;

    document.getElementById("totalPrice").innerText =
        total;

    const deliveryMessage =
        document.getElementById("deliveryMessage");

    if (subtotal === 0) {

        deliveryMessage.innerText =
            "Free delivery on orders over ৳1000.";

    } else if (subtotal >= 1000) {

        deliveryMessage.innerText =
            "Congratulations! You received free delivery.";

    } else {

        const remainingAmount = 1000 - subtotal;

        deliveryMessage.innerText =
            "Add ৳" +
            remainingAmount +
            " more to receive free delivery.";
    }
}


/* Save Cart */

function saveCart() {

    localStorage.setItem(
        "foodJoyCart",
        JSON.stringify(cart)
    );
}


/* Cart Panel */

function openCart() {

    document
        .getElementById("cartPanel")
        .classList
        .add("active");

    document
        .getElementById("overlay")
        .classList
        .add("active");

    document.body.style.overflow = "hidden";
}


function closeCart() {

    document
        .getElementById("cartPanel")
        .classList
        .remove("active");

    document
        .getElementById("overlay")
        .classList
        .remove("active");

    document.body.style.overflow = "auto";
}


function closeCartAndShowMenu() {

    closeCart();

    document
        .getElementById("menu")
        .scrollIntoView();
}


/* Checkout */

function checkout() {

    if (cart.length === 0) {

        showNotification("Your cart is empty.");

        return;
    }

    const subtotal = cart.reduce(
        (sum, item) =>
            sum + item.price * item.quantity,
        0
    );

    const deliveryFee =
        subtotal >= 1000 ? 0 : 60;

    const total = subtotal + deliveryFee;

    alert(
        "Order placed successfully!\n\n" +
        "Subtotal: ৳" + subtotal + "\n" +
        "Delivery fee: ৳" + deliveryFee + "\n" +
        "Total: ৳" + total
    );

    cart = [];

    saveCart();

    updateCart();

    closeCart();
}


/* Notification */

function showNotification(message) {

    const notification =
        document.getElementById("notification");

    notification.innerText = message;

    notification.classList.add("show");

    setTimeout(() => {
        notification.classList.remove("show");
    }, 2200);
}


/* Scroll to Menu */

function scrollToMenu() {

    document
        .getElementById("menu")
        .scrollIntoView();
}


/* Initial Page Load */

displayFoods(foods);

updateCart();