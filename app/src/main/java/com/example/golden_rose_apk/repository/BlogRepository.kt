package com.example.golden_rose_apk.repository


import com.example.golden_rose_apk.model.BlogPost

object BlogRepository {
    fun getBlogs(): List<BlogPost> {
        return listOf(
            BlogPost(1, "Las 5 skins de Vandal más codiciadas este 2025",
                "Orion" ,"03/12/2025",
                "El Vandal es el rey de los rifles en Valorant, y tener una skin que impresione es casi tan importante como acertar los disparos. En este post, analizamos las 5 skins que todos desean tener en su colec...",
                200,
                "El Vandal es el rey de los rifles en Valorant, y tener una skin que impresione es casi tan importante como acertar los disparos. En este post, analizamos las 5 skins que todos desean tener en su colección, desde la clásica Reaver hasta la nueva y exclusiva Champions 2025. Veremos por qué su sonido, animaciones y efectos de remate las hacen tan especiales y por qué dominan el mercado.",
                2),
            BlogPost(2, "Guía de economía: Cuándo forzar y cuándo ahorrar",
                "Panditax", "02/12/2025",
                "Saber manejar tu economía en Valorant es tan importante como tu puntería. Un error común en rangos bajos es forzar la compra en rondas donde no deberías. En esta guía completa, desglosamos las matemát...",
                100,"Saber manejar tu economía en Valorant es tan importante como tu puntería. Un error común en rangos bajos es forzar la compra en rondas donde no deberías. En esta guía completa, desglosamos las matemáticas detrás de las rondas de eco, las semi-compras y cuándo es el momento correcto para hacer una 'full buy' con tu equipo. Domina tu economía y empezarás a ganar más partidas, garantizado.",
                1),
            BlogPost(3,
                "Análisis del nuevo Agente: ¿Cómo 'Clove' cambia el meta?",
                "Jane",
                "01/12/2025",
                "El nuevo agente 'Clove' ha llegado y está rompiendo esquemas. Como Controlador, su habilidad para colocar humos incluso después de morir cambia fundamentalmente la forma en que se juegan las rondas de...",
                20,
                "El nuevo agente 'Clove' ha llegado y está rompiendo esquemas. Como Controlador, su habilidad para colocar humos incluso después de morir cambia fundamentalmente la forma en que se juegan las rondas de post-plant. ¿Es demasiado poderoso? ¿Cómo puedes contrarrestarlo? Aquí analizamos sus habilidades una por una y te damos los mejores consejos para sacarle el máximo partido o para no sufrir en su contra.",
                0),
        )
    }
}