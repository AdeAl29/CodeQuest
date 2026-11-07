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
            content = "Algoritma adalah urutan langkah yang jelas dan terstruktur untuk menyelesaikan sebuah masalah. Dalam pemrograman, menyusun algoritma membantu kita berpikir runtut dari input menuju output sebelum menulis baris kode pertama.",
            codeExample = """
                fun makeTea() {
                    println("1. Rebus air hingga mendidih")
                    println("2. Masukkan kantong teh ke cangkir")
                    println("3. Tuangkan air panas")
                    println("4. Teh siap disajikan!")
                }
            """.trimIndent(),
            codeOutput = """
                1. Rebus air hingga mendidih
                2. Masukkan kantong teh ke cangkir
                3. Tuangkan air panas
                4. Teh siap disajikan!
            """.trimIndent(),
            keyConcept = "Algoritma yang baik memiliki input, proses terurut, output pasti, dan langkah yang tidak ambigu."
        ),
        LessonModule(
            id = "variables",
            title = "Variabel dan Tipe Data",
            subtitle = "Menyimpan nilai dengan nama yang mudah dipahami.",
            order = 2,
            estimatedMinutes = 10,
            content = "Variabel adalah wadah bernama untuk menyimpan data di memori. Kotlin menyediakan keyword val untuk nilai tetap (immutable) dan var untuk nilai yang bisa diubah (mutable), serta mengenali tipe data seperti String, Int, Double, dan Boolean.",
            codeExample = """
                val heroName: String = "Ayu"
                var score: Int = 80
                score += 15
                println("${'$'}heroName memiliki skor ${'$'}score")
            """.trimIndent(),
            codeOutput = "Ayu memiliki skor 95",
            keyConcept = "Gunakan val sebagai default agar data lebih aman, lalu pakai var hanya ketika nilainya memang berubah."
        ),
        LessonModule(
            id = "operators",
            title = "Operator",
            subtitle = "Mengolah nilai dengan operasi aritmatika, logika, dan perbandingan.",
            order = 3,
            estimatedMinutes = 9,
            content = "Operator membantu program menghitung angka, membandingkan dua nilai, dan menggabungkan kondisi logika. Contohnya aritmatika (+, -, *, /, %), perbandingan (>, <, >=, ==, !=), serta logika (&&, ||, !).",
            codeExample = """
                val total = 70 + 20
                val passed = total >= 75
                val bonus = passed && (total % 10 == 0)
                println("Total: ${'$'}total | Lulus: ${'$'}passed | Bonus: ${'$'}bonus")
            """.trimIndent(),
            codeOutput = "Total: 90 | Lulus: true | Bonus: true",
            keyConcept = "Operator perbandingan dan logika menghasilkan nilai Boolean, yaitu true atau false."
        ),
        LessonModule(
            id = "branching",
            title = "Percabangan If Else",
            subtitle = "Membuat program memilih aksi berdasarkan kondisi.",
            order = 4,
            estimatedMinutes = 11,
            content = "Percabangan membuat program mengambil jalur eksekusi yang berbeda. Jika kondisi di dalam if bernilai true, blok utama dijalankan. Jika false, program dapat memeriksa else if atau menjalankan blok alternatif else.",
            codeExample = """
                val score = 82
                val rank = if (score >= 85) {
                    "Gold"
                } else if (score >= 75) {
                    "Silver"
                } else {
                    "Bronze"
                }
                println("Rank kamu: ${'$'}rank")
            """.trimIndent(),
            codeOutput = "Rank kamu: Silver",
            keyConcept = "Di Kotlin, if merupakan expression yang bisa langsung mengembalikan nilai ke dalam variabel."
        ),
        LessonModule(
            id = "loops",
            title = "Perulangan",
            subtitle = "Menjalankan instruksi berulang tanpa menulis kode berkali-kali.",
            order = 5,
            estimatedMinutes = 12,
            content = "Perulangan (loop) digunakan saat instruksi yang sama perlu dieksekusi berkali-kali. Kotlin menyediakan for untuk mengulang pada rentang atau koleksi, serta while dan do-while untuk pengulangan berbasis kondisi.",
            codeExample = """
                for (level in 1..3) {
                    println("Menyelesaikan Quest Level ${'$'}level")
                }
            """.trimIndent(),
            codeOutput = """
                Menyelesaikan Quest Level 1
                Menyelesaikan Quest Level 2
                Menyelesaikan Quest Level 3
            """.trimIndent(),
            keyConcept = "Gunakan for saat jumlah iterasi jelas, dan while saat pengulangan bergantung pada kondisi dinamis."
        ),
        LessonModule(
            id = "functions",
            title = "Function",
            subtitle = "Membagi program menjadi bagian kecil yang bisa dipakai ulang.",
            order = 6,
            estimatedMinutes = 10,
            content = "Function adalah blok kode bernama yang menerima parameter, menjalankan tugas spesifik, dan dapat mengembalikan nilai (return value). Function membuat kode lebih modular, rapi, dan mudah dirawat.",
            codeExample = """
                fun calculateXp(score: Int, multiplier: Int = 2): Int {
                    return score * multiplier
                }
                println("XP diperoleh: ${'$'}{calculateXp(90)}")
            """.trimIndent(),
            codeOutput = "XP diperoleh: 180",
            keyConcept = "Function sebaiknya fokus pada satu tanggung jawab jelas agar mudah diuji dan digunakan kembali."
        ),
        LessonModule(
            id = "lists",
            title = "Array/List",
            subtitle = "Menyimpan banyak nilai dalam satu struktur data terurut.",
            order = 7,
            estimatedMinutes = 12,
            content = "List digunakan untuk menyimpan kumpulan data berurutan. Kotlin menyediakan listOf untuk koleksi baca-saja (read-only) dan mutableListOf untuk koleksi yang elemennya dapat ditambah, diubah, atau dihapus.",
            codeExample = """
                val badges = mutableListOf("First Code", "Loop Master")
                badges.add("Quiz Ace")
                println("Badge pertama: ${'$'}{badges[0]} | Total: ${'$'}{badges.size}")
            """.trimIndent(),
            codeOutput = "Badge pertama: First Code | Total: 3",
            keyConcept = "Index pada List dimulai dari 0, sehingga elemen pertama diakses melalui index [0]."
        ),
        LessonModule(
            id = "oop",
            title = "Dasar Object-Oriented Programming",
            subtitle = "Mengenal class, object, property, dan method.",
            order = 8,
            estimatedMinutes = 14,
            content = "Object-Oriented Programming (OOP) memodelkan entitas ke dalam program. Class berperan sebagai blueprint atau cetakan, sedangkan object adalah instansiasi nyata yang memiliki data (property) dan perilaku (method).",
            codeExample = """
                class Player(val name: String, var xp: Int = 0) {
                    fun completeQuest(reward: Int) {
                        xp += reward
                        println("${'$'}name naik ke ${'$'}xp XP!")
                    }
                }
                val player = Player("Code Hero", 100)
                player.completeQuest(50)
            """.trimIndent(),
            codeOutput = "Code Hero naik ke 150 XP!",
            keyConcept = "Class menyatukan state (property) dan behavior (method) agar arsitektur program rapi dan terukur."
        )
    )

    val questions = listOf(
        // Module 1: Algorithm (5 questions)
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
            id = "q21",
            moduleId = "algorithm",
            type = QuestionType.COMPLETE_CODE,
            difficulty = Difficulty.MEDIUM,
            topic = "eksekusi berurutan",
            prompt = "Agar teh tidak tumpah, langkah `pourWater()` harus dijalankan ___ menyiapkan cangkir.",
            options = listOf("setelah", "sebelum", "tanpa", "bersamaan saat menghapus"),
            correctAnswer = "setelah",
            explanation = "Instruksi dalam algoritma dieksekusi secara sekuensial (berurutan) sesuai logika sebab-akibat."
        ),
        QuizQuestion(
            id = "q22",
            moduleId = "algorithm",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.HARD,
            topic = "komponen algoritma",
            prompt = "Tiga komponen utama dalam alur kerja algoritma pemrograman adalah...",
            options = listOf("Input -> Proses -> Output", "Desain -> Warna -> Animasi", "Install -> Hapus -> Restart", "Server -> Kabel -> Layar"),
            correctAnswer = "Input -> Proses -> Output",
            explanation = "Setiap algoritma menerima input data, mengolahnya melalui proses terstruktur, dan menghasilkan output."
        ),

        // Module 2: Variables (5 questions)
        QuizQuestion(
            id = "q4",
            moduleId = "variables",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.EASY,
            topic = "variabel",
            prompt = "Keyword Kotlin untuk nilai yang tidak berubah adalah...",
            options = listOf("val", "var", "let", "constOnly"),
            correctAnswer = "val",
            explanation = "val membuat referensi read-only yang tidak bisa diganti setelah diberi nilai awal."
        ),
        QuizQuestion(
            id = "q5",
            moduleId = "variables",
            type = QuestionType.COMPLETE_CODE,
            difficulty = Difficulty.MEDIUM,
            topic = "tipe data",
            prompt = "Lengkapi kode untuk menyimpan umur sebagai angka tetap: ___ age: Int = 18",
            options = listOf("val", "String", "if", "fun"),
            correctAnswer = "val",
            explanation = "Deklarasi yang benar adalah val age: Int = 18."
        ),
        QuizQuestion(
            id = "q23",
            moduleId = "variables",
            type = QuestionType.TRUE_FALSE,
            difficulty = Difficulty.EASY,
            topic = "mutable variable",
            prompt = "Variabel yang dideklarasikan dengan `var` dapat diubah nilainya di baris berikutnya.",
            options = listOf("Benar", "Salah"),
            correctAnswer = "Benar",
            explanation = "Keyword var (variable) mengizinkan nilai di dalamnya diperbarui (mutable)."
        ),
        QuizQuestion(
            id = "q24",
            moduleId = "variables",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.MEDIUM,
            topic = "tipe data",
            prompt = "Tipe data yang tepat untuk menyimpan teks `\"CodeQuest\"` adalah...",
            options = listOf("String", "Int", "Boolean", "Double"),
            correctAnswer = "String",
            explanation = "String digunakan untuk menyimpan rangkaian karakter atau teks yang diapit tanda kutip ganda."
        ),
        QuizQuestion(
            id = "q25",
            moduleId = "variables",
            type = QuestionType.COMPLETE_CODE,
            difficulty = Difficulty.HARD,
            topic = "type inference",
            prompt = "Lengkapi tipe data untuk status aktif: val isActive: ___ = true",
            options = listOf("Boolean", "Int", "String", "Char"),
            correctAnswer = "Boolean",
            explanation = "Nilai true dan false merupakan tipe data Boolean."
        ),

        // Module 3: Operators (5 questions)
        QuizQuestion(
            id = "q6",
            moduleId = "operators",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.EASY,
            topic = "operator perbandingan",
            prompt = "Operator untuk mengecek dua nilai sama adalah...",
            options = listOf("==", "=", "!=", ">="),
            correctAnswer = "==",
            explanation = "Satu tanda = untuk assignment, sedangkan == untuk perbandingan kesamaan nilai."
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
            explanation = "&& adalah operator AND logis yang mensyaratkan kedua sisi bernilai benar."
        ),
        QuizQuestion(
            id = "q26",
            moduleId = "operators",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.EASY,
            topic = "operator aritmatika",
            prompt = "Berapakah hasil dari operasi modulo `10 % 3` di Kotlin?",
            options = listOf("1", "3", "0", "30"),
            correctAnswer = "1",
            explanation = "Operator % (modulo) menghitung sisa pembagian: 10 dibagi 3 adalah 3 dengan sisa 1."
        ),
        QuizQuestion(
            id = "q27",
            moduleId = "operators",
            type = QuestionType.COMPLETE_CODE,
            difficulty = Difficulty.MEDIUM,
            topic = "operator logika",
            prompt = "Lengkapi agar bernilai true jika salah satu syarat terpenuhi: val canEnter = hasTicket ___ isVip",
            options = listOf("||", "&&", "==", "+"),
            correctAnswer = "||",
            explanation = "Operator || (OR) menghasilkan true apabila minimal salah satu kondisi bernilai true."
        ),
        QuizQuestion(
            id = "q28",
            moduleId = "operators",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.HARD,
            topic = "evaluasi ekspresi",
            prompt = "Nilai dari ekspresi `(5 > 3) && !(2 == 4)` adalah...",
            options = listOf("true", "false", "2", "Syntax error"),
            correctAnswer = "true",
            explanation = "5 > 3 adalah true, dan 2 == 4 adalah false sehingga !(false) menjadi true. true && true menghasilkan true."
        ),

        // Module 4: Branching (5 questions)
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
            explanation = "if expression membutuhkan cabang else untuk menentukan nilai alternatif."
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
            explanation = "60 tidak memenuhi kondisi score >= 75 sehingga masuk ke cabang else."
        ),
        QuizQuestion(
            id = "q29",
            moduleId = "branching",
            type = QuestionType.TRUE_FALSE,
            difficulty = Difficulty.EASY,
            topic = "else if",
            prompt = "Kita dapat menggunakan `else if` untuk memeriksa beberapa kondisi secara berurutan.",
            options = listOf("Benar", "Salah"),
            correctAnswer = "Benar",
            explanation = "Rangkaian if - else if - else memungkinkan pengecekan banyak kondisi bertingkat."
        ),
        QuizQuestion(
            id = "q30",
            moduleId = "branching",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.HARD,
            topic = "when expression",
            prompt = "Selain `if-else`, struktur percabangan multi-kondisi yang idiomatik di Kotlin adalah...",
            options = listOf("when", "switchCase", "select", "choose"),
            correctAnswer = "when",
            explanation = "Kotlin menyediakan expression `when` yang lebih ringkas dan jelas dibanding switch-case."
        ),

        // Module 5: Loops (5 questions)
        QuizQuestion(
            id = "q11",
            moduleId = "loops",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.EASY,
            topic = "for loop",
            prompt = "Perulangan yang cocok saat jumlah iterasi jelas adalah...",
            options = listOf("for", "if", "class", "val"),
            correctAnswer = "for",
            explanation = "for cocok untuk mengulang berdasarkan range atau koleksi yang terukur."
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
            explanation = "1..5 membuat range angka inklusif dari 1 sampai 5."
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
            explanation = "Itu disebut infinite loop dan perlu dihindari dengan memperbarui variabel kondisi di dalam loop."
        ),
        QuizQuestion(
            id = "q31",
            moduleId = "loops",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.MEDIUM,
            topic = "range loop",
            prompt = "Berapa kali `for (i in 1..4)` akan mengeksekusi blok di dalamnya?",
            options = listOf("4 kali", "3 kali", "5 kali", "1 kali"),
            correctAnswer = "4 kali",
            explanation = "Range 1..4 mencakup nilai 1, 2, 3, dan 4 sehingga berjalan 4 kali."
        ),
        QuizQuestion(
            id = "q32",
            moduleId = "loops",
            type = QuestionType.COMPLETE_CODE,
            difficulty = Difficulty.HARD,
            topic = "kontrol loop",
            prompt = "Keyword untuk menghentikan perulangan secara langsung sebelum selesai adalah...",
            options = listOf("break", "stop", "pause", "val"),
            correctAnswer = "break",
            explanation = "Pernyataan break langsung menghentikan eksekusi loop terdekat."
        ),

        // Module 6: Functions (5 questions)
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
            explanation = "return mengembalikan hasil perhitungan dari function ke pemanggilnya."
        ),
        QuizQuestion(
            id = "q33",
            moduleId = "functions",
            type = QuestionType.TRUE_FALSE,
            difficulty = Difficulty.EASY,
            topic = "parameter function",
            prompt = "Sebuah function boleh memiliki lebih dari satu parameter input.",
            options = listOf("Benar", "Salah"),
            correctAnswer = "Benar",
            explanation = "Function dapat menerima beberapa parameter yang dipisahkan dengan tanda koma."
        ),
        QuizQuestion(
            id = "q34",
            moduleId = "functions",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.MEDIUM,
            topic = "pemanggilan function",
            prompt = "Apa hasil dari `add(3, 4)` jika `fun add(a: Int, b: Int) = a + b`?",
            options = listOf("7", "34", "12", "Error"),
            correctAnswer = "7",
            explanation = "Single-expression function `a + b` menjumlahkan argumen 3 dan 4 menjadi 7."
        ),
        QuizQuestion(
            id = "q35",
            moduleId = "functions",
            type = QuestionType.COMPLETE_CODE,
            difficulty = Difficulty.HARD,
            topic = "default parameter",
            prompt = "Lengkapi tipe kembalian function: fun greet(name: String): ___ = \"Halo, ${'$'}name\"",
            options = listOf("String", "Int", "Boolean", "Unit"),
            correctAnswer = "String",
            explanation = "Karena function mengembalikan teks, tipe return yang tepat adalah String."
        ),

        // Module 7: Lists (5 questions)
        QuizQuestion(
            id = "q16",
            moduleId = "lists",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.EASY,
            topic = "list index",
            prompt = "Index elemen pertama pada List adalah...",
            options = listOf("0", "1", "-1", "10"),
            correctAnswer = "0",
            explanation = "List pada Kotlin memakai zero-based index (dimulai dari 0)."
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
            explanation = "Mutable list menyediakan operasi modifikasi seperti add, remove, dan update elemen."
        ),
        QuizQuestion(
            id = "q36",
            moduleId = "lists",
            type = QuestionType.COMPLETE_CODE,
            difficulty = Difficulty.EASY,
            topic = "deklarasi list",
            prompt = "Lengkapi kode untuk membuat list tetap: val scores = ___(80, 90, 100)",
            options = listOf("listOf", "if", "fun", "class"),
            correctAnswer = "listOf",
            explanation = "Fungsi listOf(...) membuat daftar elemen read-only di Kotlin."
        ),
        QuizQuestion(
            id = "q37",
            moduleId = "lists",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.MEDIUM,
            topic = "akses elemen",
            prompt = "Jika `val items = listOf(\"A\", \"B\", \"C\")`, maka `items[1]` bernilai...",
            options = listOf("\"B\"", "\"A\"", "\"C\"", "IndexOutOfBounds"),
            correctAnswer = "\"B\"",
            explanation = "Index 0 adalah \"A\", index 1 adalah \"B\", dan index 2 adalah \"C\"."
        ),
        QuizQuestion(
            id = "q38",
            moduleId = "lists",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.HARD,
            topic = "properti list",
            prompt = "Properti untuk mengetahui jumlah elemen di dalam sebuah List adalah...",
            options = listOf(".size", ".width", ".total()", ".index"),
            correctAnswer = ".size",
            explanation = "Properti `.size` mengembalikan banyaknya elemen yang tersimpan pada List."
        ),

        // Module 8: OOP (5 questions)
        QuizQuestion(
            id = "q18",
            moduleId = "oop",
            type = QuestionType.MULTIPLE_CHOICE,
            difficulty = Difficulty.EASY,
            topic = "class object",
            prompt = "Dalam OOP, class paling tepat disebut sebagai...",
            options = listOf("Cetakan object", "Nilai Boolean", "Operator logika", "Perulangan"),
            correctAnswer = "Cetakan object",
            explanation = "Class adalah blueprint atau cetakan untuk membuat object."
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
            explanation = "val name: String membuat constructor property yang otomatis tersimpan di object."
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
            explanation = "Method merepresentasikan perilaku atau aksi yang dimiliki oleh suatu class/object."
        ),
        QuizQuestion(
            id = "q39",
            moduleId = "oop",
            type = QuestionType.TRUE_FALSE,
            difficulty = Difficulty.EASY,
            topic = "instansiasi object",
            prompt = "Satu class dapat digunakan untuk membuat banyak object dengan nilai property berbeda.",
            options = listOf("Benar", "Salah"),
            correctAnswer = "Benar",
            explanation = "Dari satu class Player, kita bisa membuat banyak instance object pemain yang masing-masing independen."
        ),
        QuizQuestion(
            id = "q40",
            moduleId = "oop",
            type = QuestionType.COMPLETE_CODE,
            difficulty = Difficulty.HARD,
            topic = "pemanggilan method",
            prompt = "Untuk memanggil method `levelUp()` milik object `hero`, kita menulis: hero___levelUp()",
            options = listOf(".", ":", "->", "::"),
            correctAnswer = ".",
            explanation = "Dot notation (`.`) digunakan untuk mengakses property maupun memanggil method pada object."
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
