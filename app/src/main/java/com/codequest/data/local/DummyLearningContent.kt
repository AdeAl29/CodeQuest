package com.codequest.data.local

import com.codequest.data.model.BadgeDefinition
import com.codequest.data.model.Difficulty
import com.codequest.data.model.LessonModule
import com.codequest.data.model.QuestionType
import com.codequest.data.model.QuizQuestion

object DummyLearningContent {
    val modules = listOf(
        LessonModule(
            id = "algorithm",
            title = "Pengenalan Algoritma",
            subtitle = "Belajar menyusun langkah logis untuk menyelesaikan masalah.",
            order = 1,
            estimatedMinutes = 8,
            content = "Algoritma adalah urutan langkah yang jelas untuk menyelesaikan sebuah masalah. Dalam coding, algoritma membantu kita berpikir runtut sebelum menulis program.",
            codeExample = """
                fun makeTea() {
                    boilWater()
                    addTea()
                    pourWater()
                    serve()
                }
            """.trimIndent(),
            keyConcept = "Algoritma yang baik memiliki input, proses, output, dan langkah yang tidak ambigu."
        ),
        LessonModule(
            id = "variables",
            title = "Variabel dan Tipe Data",
            subtitle = "Menyimpan nilai dengan nama yang mudah dipahami.",
            order = 2,
            estimatedMinutes = 10,
            content = "Variabel digunakan untuk menyimpan data. Kotlin memakai val untuk nilai tetap dan var untuk nilai yang bisa berubah.",
            codeExample = """
                val name: String = "Ayu"
                var score: Int = 80
                score += 10
            """.trimIndent(),
            keyConcept = "Gunakan val sebagai default agar data lebih aman, lalu pakai var hanya ketika nilainya memang berubah."
        ),
        LessonModule(
            id = "operators",
            title = "Operator",
            subtitle = "Mengolah nilai dengan operasi aritmatika, logika, dan perbandingan.",
            order = 3,
            estimatedMinutes = 9,
            content = "Operator membantu program menghitung, membandingkan, dan membuat keputusan. Contohnya +, -, *, /, >, ==, &&, dan ||.",
            codeExample = """
                val total = 70 + 20
                val passed = total >= 75
                val canContinue = passed && total < 100
            """.trimIndent(),
            keyConcept = "Operator perbandingan menghasilkan Boolean, yaitu true atau false."
        ),
        LessonModule(
            id = "branching",
            title = "Percabangan If Else",
            subtitle = "Membuat program memilih aksi berdasarkan kondisi.",
            order = 4,
            estimatedMinutes = 11,
            content = "Percabangan membuat program mengambil jalur berbeda. Jika kondisi bernilai true, blok if berjalan. Jika tidak, blok else menjadi alternatif.",
            codeExample = """
                val score = 82
                val message = if (score >= 75) {
                    "Lulus"
                } else {
                    "Coba lagi"
                }
            """.trimIndent(),
            keyConcept = "Di Kotlin, if bisa menjadi expression yang langsung menghasilkan nilai."
        ),
        LessonModule(
            id = "loops",
            title = "Perulangan",
            subtitle = "Menjalankan instruksi berulang tanpa menulis kode berkali-kali.",
            order = 5,
            estimatedMinutes = 12,
            content = "Perulangan digunakan saat tugas yang sama perlu dilakukan berkali-kali. Kotlin menyediakan for, while, dan do while.",
            codeExample = """
                for (number in 1..5) {
                    println("Level ${'$'}number")
                }
            """.trimIndent(),
            keyConcept = "Gunakan for saat jumlah pengulangan jelas, dan while saat pengulangan bergantung pada kondisi."
        ),
        LessonModule(
            id = "functions",
            title = "Function",
            subtitle = "Membagi program menjadi bagian kecil yang bisa dipakai ulang.",
            order = 6,
            estimatedMinutes = 10,
            content = "Function adalah blok kode bernama yang menjalankan tugas tertentu. Function membuat program lebih rapi, terstruktur, dan mudah diuji.",
            codeExample = """
                fun calculateXp(score: Int): Int {
                    return score * 2
                }
            """.trimIndent(),
            keyConcept = "Function sebaiknya fokus pada satu tanggung jawab agar mudah dipahami."
        ),
        LessonModule(
            id = "lists",
            title = "Array/List",
            subtitle = "Menyimpan banyak nilai dalam satu struktur data.",
            order = 7,
            estimatedMinutes = 12,
            content = "List digunakan untuk menyimpan kumpulan data. Kotlin memiliki listOf untuk data tetap dan mutableListOf untuk data yang dapat diubah.",
            codeExample = """
                val badges = listOf("First Code", "Loop Master")
                val firstBadge = badges[0]
            """.trimIndent(),
            keyConcept = "Index List dimulai dari 0, jadi elemen pertama berada pada posisi 0."
        ),
        LessonModule(
            id = "oop",
            title = "Dasar Object-Oriented Programming",
            subtitle = "Mengenal class, object, property, dan method.",
            order = 8,
            estimatedMinutes = 14,
            content = "OOP membantu kita memodelkan objek dunia nyata ke dalam program. Class adalah cetakan, object adalah hasil nyata dari cetakan tersebut.",
            codeExample = """
                class Player(val name: String) {
                    var xp: Int = 0

                    fun levelUp() {
                        xp += 100
                    }
                }
            """.trimIndent(),
            keyConcept = "Class menyatukan data dan perilaku agar program lebih mudah dikembangkan."
        )
    )

    val questions = listOf(
        QuizQuestion(
            id = "q1",
            moduleId = "algorithm",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.EASY,
            topic = "algoritma",
            prompt = "Apa definisi paling tepat dari algoritma?",
            options = listOf("Urutan langkah untuk menyelesaikan masalah", "Tampilan aplikasi", "Bahasa pemrograman", "Database lokal"),
            correctAnswer = "Urutan langkah untuk menyelesaikan masalah",
            explanation = "Algoritma adalah langkah-langkah logis yang disusun untuk mencapai output tertentu."
        ),
        QuizQuestion(
            id = "q2",
            moduleId = "algorithm",
            type = QuestionType.TRUE_FALSE,
            difficulty = Difficulty.EASY,
            topic = "algoritma",
            prompt = "Algoritma harus memiliki langkah yang jelas dan tidak ambigu.",
            options = listOf("Benar", "Salah"),
            correctAnswer = "Benar",
            explanation = "Langkah yang ambigu membuat program sulit diterjemahkan ke kode."
        ),
        QuizQuestion(
            id = "q3",
            moduleId = "algorithm",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.MEDIUM,
            topic = "urutan langkah",
            prompt = "Sebelum menulis kode, hal paling penting dalam algoritma adalah...",
            options = listOf("Menentukan langkah dan output", "Memilih warna UI", "Membuka internet", "Menghapus semua variabel"),
            correctAnswer = "Menentukan langkah dan output",
            explanation = "Algoritma membantu menentukan proses dari input menuju output."
        ),
        QuizQuestion(
            id = "q4",
            moduleId = "variables",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.EASY,
            topic = "variabel",
            prompt = "Keyword Kotlin untuk nilai yang tidak berubah adalah...",
            options = listOf("val", "var", "let", "constOnly"),
            correctAnswer = "val",
            explanation = "val membuat referensi tidak bisa diganti setelah diberi nilai."
        ),
        QuizQuestion(
            id = "q5",
            moduleId = "variables",
            type = QuestionType.COMPLETE_CODE,
            difficulty = Difficulty.MEDIUM,
            topic = "tipe data",
            prompt = "Lengkapi kode untuk menyimpan umur sebagai angka: ___ age: Int = 18",
            options = listOf("val", "String", "if", "fun"),
            correctAnswer = "val",
            explanation = "Deklarasi yang benar adalah val age: Int = 18."
        ),
        QuizQuestion(
            id = "q6",
            moduleId = "operators",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.EASY,
            topic = "operator perbandingan",
            prompt = "Operator untuk mengecek dua nilai sama adalah...",
            options = listOf("==", "=", "!=", ">="),
            correctAnswer = "==",
            explanation = "Satu tanda = untuk assignment, sedangkan == untuk perbandingan."
        ),
        QuizQuestion(
            id = "q7",
            moduleId = "operators",
            type = QuestionType.TRUE_FALSE,
            difficulty = Difficulty.MEDIUM,
            topic = "operator logika",
            prompt = "Operator && bernilai true hanya jika kedua kondisi bernilai true.",
            options = listOf("Benar", "Salah"),
            correctAnswer = "Benar",
            explanation = "&& adalah operator AND logis."
        ),
        QuizQuestion(
            id = "q8",
            moduleId = "branching",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.EASY,
            topic = "if else",
            prompt = "Blok else dijalankan ketika kondisi if bernilai...",
            options = listOf("false", "true", "null", "Int"),
            correctAnswer = "false",
            explanation = "else adalah jalur alternatif saat kondisi if tidak terpenuhi."
        ),
        QuizQuestion(
            id = "q9",
            moduleId = "branching",
            type = QuestionType.COMPLETE_CODE,
            difficulty = Difficulty.MEDIUM,
            topic = "if expression",
            prompt = "Lengkapi kode: val result = if (score >= 75) \"Lulus\" ___ \"Remedial\"",
            options = listOf("else", "then", "repeat", "return"),
            correctAnswer = "else",
            explanation = "if expression membutuhkan else untuk nilai alternatif."
        ),
        QuizQuestion(
            id = "q10",
            moduleId = "branching",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.HARD,
            topic = "if else",
            prompt = "Jika score = 60 pada if (score >= 75), output yang dipilih adalah...",
            options = listOf("Cabang else", "Cabang if", "Keduanya", "Program pasti error"),
            correctAnswer = "Cabang else",
            explanation = "60 tidak memenuhi kondisi score >= 75."
        ),
        QuizQuestion(
            id = "q11",
            moduleId = "loops",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.EASY,
            topic = "for loop",
            prompt = "Perulangan yang cocok saat jumlah iterasi jelas adalah...",
            options = listOf("for", "if", "class", "val"),
            correctAnswer = "for",
            explanation = "for cocok untuk mengulang berdasarkan range atau koleksi."
        ),
        QuizQuestion(
            id = "q12",
            moduleId = "loops",
            type = QuestionType.COMPLETE_CODE,
            difficulty = Difficulty.MEDIUM,
            topic = "range loop",
            prompt = "Lengkapi kode: for (i in ___) { println(i) }",
            options = listOf("1..5", "if..else", "String", "true..false"),
            correctAnswer = "1..5",
            explanation = "1..5 membuat range angka dari 1 sampai 5."
        ),
        QuizQuestion(
            id = "q13",
            moduleId = "loops",
            type = QuestionType.TRUE_FALSE,
            difficulty = Difficulty.HARD,
            topic = "while loop",
            prompt = "while dapat berjalan tanpa henti jika kondisinya selalu true.",
            options = listOf("Benar", "Salah"),
            correctAnswer = "Benar",
            explanation = "Itu disebut infinite loop dan perlu dihindari jika tidak disengaja."
        ),
        QuizQuestion(
            id = "q14",
            moduleId = "functions",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.EASY,
            topic = "function",
            prompt = "Keyword untuk membuat function di Kotlin adalah...",
            options = listOf("fun", "function", "def", "method"),
            correctAnswer = "fun",
            explanation = "Kotlin memakai keyword fun untuk mendeklarasikan function."
        ),
        QuizQuestion(
            id = "q15",
            moduleId = "functions",
            type = QuestionType.COMPLETE_CODE,
            difficulty = Difficulty.MEDIUM,
            topic = "return value",
            prompt = "Lengkapi kode: fun double(x: Int): Int { ___ x * 2 }",
            options = listOf("return", "val", "loop", "class"),
            correctAnswer = "return",
            explanation = "return mengembalikan hasil dari function."
        ),
        QuizQuestion(
            id = "q16",
            moduleId = "lists",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.EASY,
            topic = "list index",
            prompt = "Index elemen pertama pada List adalah...",
            options = listOf("0", "1", "-1", "10"),
            correctAnswer = "0",
            explanation = "List pada Kotlin memakai zero-based index."
        ),
        QuizQuestion(
            id = "q17",
            moduleId = "lists",
            type = QuestionType.TRUE_FALSE,
            difficulty = Difficulty.MEDIUM,
            topic = "mutable list",
            prompt = "mutableListOf dapat digunakan untuk List yang isinya bisa berubah.",
            options = listOf("Benar", "Salah"),
            correctAnswer = "Benar",
            explanation = "Mutable list menyediakan operasi seperti add dan remove."
        ),
        QuizQuestion(
            id = "q18",
            moduleId = "oop",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.EASY,
            topic = "class object",
            prompt = "Dalam OOP, class paling tepat disebut sebagai...",
            options = listOf("Cetakan object", "Nilai Boolean", "Operator logika", "Perulangan"),
            correctAnswer = "Cetakan object",
            explanation = "Class adalah blueprint untuk membuat object."
        ),
        QuizQuestion(
            id = "q19",
            moduleId = "oop",
            type = QuestionType.COMPLETE_CODE,
            difficulty = Difficulty.MEDIUM,
            topic = "property",
            prompt = "Lengkapi kode: class Player(___ name: String)",
            options = listOf("val", "if", "for", "else"),
            correctAnswer = "val",
            explanation = "val name: String membuat constructor property yang bisa dibaca."
        ),
        QuizQuestion(
            id = "q20",
            moduleId = "oop",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.HARD,
            topic = "method",
            prompt = "Function di dalam class biasanya disebut...",
            options = listOf("method", "array", "literal", "range"),
            correctAnswer = "method",
            explanation = "Method adalah perilaku yang dimiliki object/class."
        )
    )

    val badges = listOf(
        BadgeDefinition("first_code", "First Code", "Menyelesaikan latihan pertama.", "code"),
        BadgeDefinition("algorithm_path", "Algorithm Scout", "Menyelesaikan modul algoritma.", "route"),
        BadgeDefinition("logic_starter", "Logic Starter", "Menyelesaikan materi percabangan.", "bolt"),
        BadgeDefinition("loop_master", "Loop Master", "Menjawab 5 soal perulangan dengan benar.", "repeat"),
        BadgeDefinition("consistent_learner", "Consistent Learner", "Mempertahankan streak 3 hari.", "calendar"),
        BadgeDefinition("xp_hunter", "XP Hunter", "Mengumpulkan 500 XP.", "star"),
        BadgeDefinition("quiz_ace", "Quiz Ace", "Mendapat skor minimal 90 pada quiz.", "trophy"),
        BadgeDefinition("path_finisher", "Path Finisher", "Menyelesaikan seluruh modul.", "flag")
    )
}
