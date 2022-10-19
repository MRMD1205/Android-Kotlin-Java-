import java.util.*

fun isValid(s: String): Boolean {
    val stack = Stack<Char>()

    // check for empty or blank string
    if (s == null || s.isEmpty() || s.trim() == "") {
        return false
    }

    // check range of string
    if (s.isEmpty() || s.length > 10000) {
        return false
    }

    // valid parentheses
    val map = mapOf(
        ')' to '(',
        ']' to '[',
        '}' to '{'
    )

    //push and pop operation according to fetched parentheses character
    s.forEach { c ->
        when {
            stack.empty() -> stack.push(c)
            stack.peek() == map[c] -> stack.pop()
            else -> stack.push(c)
        }
    }
    return stack.empty()
}


fun main(args: Array<String>) {
    //print output according to input string
    println(isValid("([]))("))
}
