package `fun`.aragaki.kraft.extensions

fun List<String?>.joinNoNull(separator: String): String {
    return buildString {
        var started = false
        this@joinNoNull.forEachIndexed { index, s ->
            if (index == 0) {
                s?.let {
                    append(it)
                    started = true
                }
            } else {
                if (!started) {
                    s?.let {
                        append(it)
                        started = true
                    }
                } else {
                    s?.let {
                        append(separator)
                        append(it)
                    }
                }
            }
        }
    }
}