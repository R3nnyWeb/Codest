package r3nny.codest.shared.domain

enum class TestType(val description: String) {
    DEFAULT("Точное совпадение"),
    ANY("Любое из"),
    ANY_ORDER("Любой порядок массива")
}