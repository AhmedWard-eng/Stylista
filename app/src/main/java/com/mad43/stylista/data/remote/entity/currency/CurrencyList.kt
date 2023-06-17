package com.mad43.stylista.data.remote.entity.currency


val currencies: Map<String, String> by lazy {
    hashMapOf(
        "AED" to "United Arab Emirates Dirham",
        "ANG" to "Netherlands Antillean Guilder",
        "AUD" to "Australian Dollar",
        "BHD" to "Bahraini Dinar",
        "BRL" to "Brazilian Real",
        "CAD" to "Canadian Dollar",
        "CHF" to "Swiss Franc",
        "CNY" to "Chinese Yuan",
        "DOP" to "Dominican Peso",
        "DZD" to "Algerian Dinar",
        "EGP" to "Egyptian Pound",
        "ERN" to "Eritrean Nakfa",
        "EUR" to "Euro",
        "INR" to "Indian Rupee",
        "IQD" to "Iraqi Dinar",
        "IRR" to "Iranian Rial",
        "JOD" to "Jordanian Dinar",
        "JPY" to "Japanese Yen",
        "KRW" to "South Korean Won",
        "KWD" to "Kuwaiti Dinar",
        "LBP" to "Lebanese Pound",
        "LYD" to "Libyan Dinar",
        "MAD" to "Moroccan Dirham",
        "MXN" to "Mexican Peso",
        "MYR" to "Malaysian Ringgit",
        "NOK" to "Norwegian Krone",
        "NZD" to "New Zealand Dollar",
        "OMR" to "Omani Rial",
        "QAR" to "Qatari Rial",
        "RUB" to "Russian Ruble",
        "SAR" to "Saudi Riyal",
        "SDG" to "Sudanese Pound",
        "SEK" to "Swedish Krona",
        "SGD" to "Singapore Dollar",
        "SYP" to "Syrian Pound",
        "TND" to "Tunisian Dinar",
        "TRY" to "Turkish Lira",
        "USD" to "United States Dollar",
        "XAG" to "Silver (troy ounce)",
        "XAU" to "Gold (troy ounce)",
        "YER" to "Yemeni Rial"
    )
}

val listOfCurrencies = currencies.keys.toList().sorted()

