package com.guicarneirodev.hoopreel.core.theme

import androidx.compose.ui.graphics.Color

enum class NbaTeam(
    val teamId: String,
    val fullName: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val logoUrl: String,
    val isDarkTheme: Boolean
) {
    // CONFERÊNCIA LESTE

    // Atlantic Division
    CELTICS(
        teamId = "celtics",
        fullName = "Boston Celtics",
        primaryColor = Color(0xFF008348),  // Verde
        secondaryColor = Color(0xFFFFFFFF),  // Branco
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/bos.png",
        isDarkTheme = true
    ),

    NETS(
        teamId = "nets",
        fullName = "Brooklyn Nets",
        primaryColor = Color(0xFF000000),  // Preto
        secondaryColor = Color(0xFFFFFFFF),  // Branco
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/bkn.png",
        isDarkTheme = true
    ),

    KNICKS(
        teamId = "knicks",
        fullName = "New York Knicks",
        primaryColor = Color(0xFFF58426),  // Laranja
        secondaryColor = Color(0xFF1D428A),  // Azul
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/ny.png",
        isDarkTheme = true
    ),

    SEVENTY_SIXERS(
        teamId = "76ers",
        fullName = "Philadelphia 76ers",
        primaryColor = Color(0xFF016BB7),  // Azul
        secondaryColor = Color(0xFFED174D),  // Vermelho
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/phi.png",
        isDarkTheme = true
    ),

    RAPTORS(
        teamId = "raptors",
        fullName = "Toronto Raptors",
        primaryColor = Color(0xFFBD1B21),  // Vermelho
        secondaryColor = Color(0xFF000000),  // Preto
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/tor.png",
        isDarkTheme = true
    ),

    // Central Division
    BULLS(
        teamId = "bulls",
        fullName = "Chicago Bulls",
        primaryColor = Color(0xFFCE1141),  // Vermelho
        secondaryColor = Color(0xFF000000),  // Preto
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/chi.png",
        isDarkTheme = true
    ),

    CAVALIERS(
        teamId = "cavaliers",
        fullName = "Cleveland Cavaliers",
        primaryColor = Color(0xFF860038),  // Vinho
        secondaryColor = Color(0xFFBC945C),  // Dourado
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/cle.png",
        isDarkTheme = true
    ),

    PISTONS(
        teamId = "pistons",
        fullName = "Detroit Pistons",
        primaryColor = Color(0xFFE9072B),  // Vermelho
        secondaryColor = Color(0xFF00408D),  // Azul
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/det.png",
        isDarkTheme = true
    ),

    PACERS(
        teamId = "pacers",
        fullName = "Indiana Pacers",
        primaryColor = Color(0xFF002D62),  // Azul Marinho
        secondaryColor = Color(0xFFFDBB30),  // Dourado
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/ind.png",
        isDarkTheme = true
    ),

    BUCKS(
        teamId = "bucks",
        fullName = "Milwaukee Bucks",
        primaryColor = Color(0xFF00471B),  // Verde Escuro
        secondaryColor = Color(0xFFEEE1C6),  // Creme
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/mil.png",
        isDarkTheme = true
    ),

    // Southeast Division
    HAWKS(
        teamId = "hawks",
        fullName = "Atlanta Hawks",
        primaryColor = Color(0xFFC8102E),  // Vermelho
        secondaryColor = Color(0xFFFFFFFF),  // Branco
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/atl.png",
        isDarkTheme = true
    ),

    HORNETS(
        teamId = "hornets",
        fullName = "Charlotte Hornets",
        primaryColor = Color(0xFF1D1160),  // Turquesa
        secondaryColor = Color(0xFF00788C),  // Roxo
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/cha.png",
        isDarkTheme = true
    ),

    HEAT(
        teamId = "heat",
        fullName = "Miami Heat",
        primaryColor = Color(0xFF98002E),  // Vermelho
        secondaryColor = Color(0xFFF9A01B),  // Amarelo
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/mia.png",
        isDarkTheme = true
    ),

    MAGIC(
        teamId = "magic",
        fullName = "Orlando Magic",
        primaryColor = Color(0xFF0B77BD),  // Azul
        secondaryColor = Color(0xFFC2CCD2),  // Cinza
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/orl.png",
        isDarkTheme = true
    ),

    WIZARDS(
        teamId = "wizards",
        fullName = "Washington Wizards",
        primaryColor = Color(0xFF002B5C),  // Azul Marinho
        secondaryColor = Color(0xFFE31837),  // Vermelho
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/wsh.png",
        isDarkTheme = true
    ),

    // CONFERÊNCIA OESTE

    // Northwest Division
    NUGGETS(
        teamId = "nuggets",
        fullName = "Denver Nuggets",
        primaryColor = Color(0xFF0D2240),  // Azul Marinho
        secondaryColor = Color(0xFFFFC627),  // Amarelo
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/den.png",
        isDarkTheme = true
    ),

    TIMBERWOLVES(
        teamId = "timberwolves",
        fullName = "Minnesota Timberwolves",
        primaryColor = Color(0xFF0C2340),  // Azul Escuro
        secondaryColor = Color(0xFF78BE20),  // Verde
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/min.png",
        isDarkTheme = true
    ),

    THUNDER(
        teamId = "thunder",
        fullName = "Oklahoma City Thunder",
        primaryColor = Color(0xFF0A7EC2),  // Azul
        secondaryColor = Color(0xFFF05333),  // Laranja
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/okc.png",
        isDarkTheme = true
    ),

    TRAIL_BLAZERS(
        teamId = "blazers",
        fullName = "Portland Trail Blazers",
        primaryColor = Color(0xFFFF373C),  // Vermelho
        secondaryColor = Color(0xFFFFFFFF),  // Preto
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/por.png",
        isDarkTheme = true
    ),

    JAZZ(
        teamId = "jazz",
        fullName = "Utah Jazz",
        primaryColor = Color(0xFF542C68),  // Roxo
        secondaryColor = Color(0xFF001E3F),  // Azul
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/utah.png",
        isDarkTheme = true
    ),

    // Pacific Division
    WARRIORS(
        teamId = "warriors",
        fullName = "Golden State Warriors",
        primaryColor = Color(0xFF1D428A),  // Azul
        secondaryColor = Color(0xFFFDB927),  // Dourado
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/gs.png",
        isDarkTheme = true
    ),

    CLIPPERS(
        teamId = "clippers",
        fullName = "Los Angeles Clippers",
        primaryColor = Color(0xFF0A2240),  // Azul
        secondaryColor = Color(0xFFCE0E2D),  // Vermelho
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/lac.png",
        isDarkTheme = true
    ),

    LAKERS(
        teamId = "lakers",
        fullName = "Los Angeles Lakers",
        primaryColor = Color(0xFF31006F),  // Roxo
        secondaryColor = Color(0xFFFDB927),  // Amarelo/Dourado
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/lal.png",
        isDarkTheme = true
    ),

    SUNS(
        teamId = "suns",
        fullName = "Phoenix Suns",
        primaryColor = Color(0xFFD55F1F),  // Laranja
        secondaryColor = Color(0xFF391366),  // Roxo
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/phx.png",
        isDarkTheme = true
    ),

    KINGS(
        teamId = "kings",
        fullName = "Sacramento Kings",
        primaryColor = Color(0xFF662984),  // Roxo
        secondaryColor = Color(0xFF707372),  // Cinza
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/sac.png",
        isDarkTheme = true
    ),

    // Southwest Division
    MAVERICKS(
        teamId = "mavericks",
        fullName = "Dallas Mavericks",
        primaryColor = Color(0xFF007DC5),  // Azul
        secondaryColor = Color(0xFFFFFFFF),  // Branco
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/dal.png",
        isDarkTheme = true
    ),

    ROCKETS(
        teamId = "rockets",
        fullName = "Houston Rockets",
        primaryColor = Color(0xFF000000),  // Preto
        secondaryColor = Color(0xFFF00840),  // Vermelho
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/hou.png",
        isDarkTheme = true
    ),

    GRIZZLIES(
        teamId = "grizzlies",
        fullName = "Memphis Grizzlies",
        primaryColor = Color(0xFF12173F),  // Azul Escuro
        secondaryColor = Color(0xFF5D76A9),  // Azul Claro
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/mem.png",
        isDarkTheme = true
    ),

    PELICANS(
        teamId = "pelicans",
        fullName = "New Orleans Pelicans",
        primaryColor = Color(0xFF001644),  // Azul Marinho
        secondaryColor = Color(0xFFC69E66),  // Creme
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/no.png",
        isDarkTheme = true
    ),

    SPURS(
        teamId = "spurs",
        fullName = "San Antonio Spurs",
        primaryColor = Color(0xFFC1CFD5),  // Cinza
        secondaryColor = Color(0xFF000000),  // Preto
        logoUrl = "https://a.espncdn.com/i/teamlogos/nba/500/sa.png",
        isDarkTheme = true
    ),

    // Tema padrão do HoopReel
    DEFAULT(
        teamId = "default",
        fullName = "HoopReel",
        primaryColor = Color(0xFF0056A1),  // Azul NBA
        secondaryColor = Color(0xFFCC2C30),  // Vermelho NBA
        logoUrl = "",
        isDarkTheme = true
    );

    companion object {
        fun getById(teamId: String): NbaTeam {
            return entries.find { it.teamId == teamId } ?: DEFAULT
        }
    }
}