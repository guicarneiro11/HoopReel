package com.guicarneirodev.hoopreel.core.utils

object PlayerImages {
    const val LEBRON_JAMES = "https://cdn.nba.com/headshots/nba/latest/1040x760/2544.png"
    const val KEVIN_DURANT = "https://cdn.nba.com/headshots/nba/latest/1040x760/201142.png"
    const val STEPHEN_CURRY = "https://cdn.nba.com/headshots/nba/latest/1040x760/201939.png"
    const val LUKA_DONCIC = "https://cdn.nba.com/headshots/nba/latest/1040x760/1629029.png"
    const val SHAI_GILGEOUS_ALEXANDER = "https://cdn.nba.com/headshots/nba/latest/1040x760/1628983.png"
    const val NIKOLA_JOKIC = "https://cdn.nba.com/headshots/nba/latest/1040x760/203999.png"
    const val GIANNIS_ANTETOKOUNMPO = "https://cdn.nba.com/headshots/nba/latest/1040x760/203507.png"
    const val JAYSON_TATUM = "https://cdn.nba.com/headshots/nba/latest/1040x760/1628369.png"
    const val ANTHONY_DAVIS = "https://cdn.nba.com/headshots/nba/latest/1040x760/203076.png"
    const val TRAE_YOUNG = "https://cdn.nba.com/headshots/nba/latest/1040x760/1629027.png"
    const val VICTOR_WEMBANYAMA = "https://cdn.nba.com/headshots/nba/latest/1040x760/1641705.png"
    const val JAMES_HARDEN = "https://cdn.nba.com/headshots/nba/latest/1040x760/201935.png"
    const val ANTHONY_EDWARDS = "https://cdn.nba.com/headshots/nba/latest/1040x760/1630162.png"
    const val RUSSELL_WESTBROOK = "https://cdn.nba.com/headshots/nba/latest/1040x760/201566.png"
    const val KYRIE_IRVING = "https://cdn.nba.com/headshots/nba/latest/1040x760/202681.png"
    const val JA_MORANT = "https://cdn.nba.com/headshots/nba/latest/1040x760/1629630.png"
    const val DONOVAN_MITCHELL = "https://cdn.nba.com/headshots/nba/latest/1040x760/1628378.png"
    const val CADE_CUNNINGHAM = "https://cdn.nba.com/headshots/nba/latest/1040x760/1630595.png"
    const val JOEL_EMBIID = "https://cdn.nba.com/headshots/nba/latest/1040x760/203954.png"
    const val DAMIAN_LILLARD = "https://cdn.nba.com/headshots/nba/latest/1040x760/203081.png"
    const val DEVIN_BOOKER = "https://cdn.nba.com/headshots/nba/latest/1040x760/1626164.png"
    const val ZION_WILLIAMSON = "https://cdn.nba.com/headshots/nba/latest/1040x760/1629627.png"
    const val LAMELO_BALL = "https://cdn.nba.com/headshots/nba/latest/1040x760/1630163.png"
    const val KAWHI_LEONARD = "https://cdn.nba.com/headshots/nba/latest/1040x760/202695.png"
    const val PAOLO_BANCHERO = "https://cdn.nba.com/headshots/nba/latest/1040x760/1631094.png"
    const val ALLEN_IVERSON = "https://cdn.nba.com/headshots/nba/latest/1040x760/947.png"

    const val DEFAULT_PLAYER = "https://cdn.nba.com/headshots/nba/latest/1040x760/fallback.png"
    
    fun getPlayerImageUrl(playerId: String): String {
        return when (playerId) {
            "lebron-james" -> LEBRON_JAMES
            "kevin-durant" -> KEVIN_DURANT
            "steph-curry" -> STEPHEN_CURRY
            "luka-doncic" -> LUKA_DONCIC
            "shai-gilgeous-alexander" -> SHAI_GILGEOUS_ALEXANDER
            "jokic" -> NIKOLA_JOKIC
            "giannis" -> GIANNIS_ANTETOKOUNMPO
            "tatum" -> JAYSON_TATUM
            "anthony-davis" -> ANTHONY_DAVIS
            "trae-young" -> TRAE_YOUNG
            "victor-wembanyama" -> VICTOR_WEMBANYAMA
            "james-harden" -> JAMES_HARDEN
            "anthony-edwards" -> ANTHONY_EDWARDS
            "russel-westbrook" -> RUSSELL_WESTBROOK
            "kyrie-irving" -> KYRIE_IRVING
            "ja-morant" -> JA_MORANT
            "donovan-mitchell" -> DONOVAN_MITCHELL
            "cade-cunningham" -> CADE_CUNNINGHAM
            "joel-embiid" -> JOEL_EMBIID
            "damian-lillard" -> DAMIAN_LILLARD
            "devin-booker" -> DEVIN_BOOKER
            "zion-williamson" -> ZION_WILLIAMSON
            "lamelo-ball" -> LAMELO_BALL
            "kawhi-leonard" -> KAWHI_LEONARD
            "paolo-banchero" -> PAOLO_BANCHERO
            "allen-iverson" -> ALLEN_IVERSON
            else -> DEFAULT_PLAYER
        }
    }
}