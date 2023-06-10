package com.mad43.stylista.ui.productInfo.model

data class Review (val title: String, val rating: Float, val comment: String)

val reviews = listOf(
    Review("Great product", 5.0f, "I love this product! It works great and has exceeded my expectations."),
    Review("Not worth the money", 2.0f, "I was disappointed with this product. It didn't work as advertised and was overpriced."),
    Review("Good value", 4.0f, "This is a good product for the price. It's not perfect, but it gets the job done."),
    Review("Excellent product", 5.0f, "This product is amazing! I've never been so satisfied with a purchase."),
    Review("Terrible quality", 1.5f, "I regret buying this product. It broke after just a few uses."),
    Review("Average performance", 3.0f, "This product is okay. It's not great, but it's not terrible either."),
    Review("Awesome product", 5.0f, "This product is awesome! It does exactly what it's supposed to do."),
    Review("Disappointing", 2.3f, "I was disappointed with this product. It didn't live up to my expectations."),
    Review("Decent product", 3.5f, "This is a decent product. It's not the best, but it's not the worst either."),
    Review("Not worth it", 1.9f, "This product is not worth the money. It's poorly made and doesn't work well."),
    Review("Just okay", 3.5f, "This product is just okay. It's not great, but it's not terrible either."),
    Review("Waste of money", 1.4f, "I regret buying this product. It doesn't work as advertised.")
)
