package com.example.data.model

/**
 * Les 20 langues nationales officielles et patrimoniales du Sénégal + Français
 * conformément à la vision inclusive et citoyenne de Téranga Moov.
 */
enum class NationalLanguage(
    val code: String,
    val displayName: String,
    val regionOrGroup: String,
    val greeting: String,
    val liveTabLabel: String,
    val routesTabLabel: String,
    val ticketsTabLabel: String,
    val reportsTabLabel: String,
    val profileTabLabel: String,
    val buyTicketLabel: String,
    val trafficAlertVocalSummary: String
) {
    FRENCH(
        code = "fr",
        displayName = "Français",
        regionOrGroup = "Langue officielle",
        greeting = "Bienvenue sur Téranga Moov",
        liveTabLabel = "En Direct",
        routesTabLabel = "Trajets",
        ticketsTabLabel = "Tickets",
        reportsTabLabel = "Alertes",
        profileTabLabel = "Mon Espace",
        buyTicketLabel = "Acheter un ticket",
        trafficAlertVocalSummary = "Attention : circulation ralentie sur la VDN et autoroute. Les lignes BRT et TER circulent normalement."
    ),
    WOLOF(
        code = "wo",
        displayName = "Wolof",
        regionOrGroup = "National • 80% des usagers",
        greeting = "Dalal ak jàmm ci Téranga Moov",
        liveTabLabel = "Téy jii",
        routesTabLabel = "Yoon yi",
        ticketsTabLabel = "Tike yi",
        reportsTabLabel = "Yégle yi",
        profileTabLabel = "Sama Bërëb",
        buyTicketLabel = "Jënd tike",
        trafficAlertVocalSummary = "Moytul : yoonu VDN bi dafa xat ndax tali bu fess. BRT ak TER ñu ngi dox bu baax ci jàmm."
    ),
    PULAAR(
        code = "ff",
        displayName = "Pulaar (Peul)",
        regionOrGroup = "Vallée du Fleuve, Fouta & Ferlo",
        greeting = "Bismillah mon e Téranga Moov",
        liveTabLabel = "Jooni",
        routesTabLabel = "Laabi",
        ticketsTabLabel = "Tikeeji",
        reportsTabLabel = "Tinndinooje",
        profileTabLabel = "Nokku am",
        buyTicketLabel = "Soodu tike",
        trafficAlertVocalSummary = "Reentee : laawol VDN ngol heewi otooji jooni. BRT e TER e ngol ndoondi no haanirta."
    ),
    SERERE(
        code = "sr",
        displayName = "Sérère",
        regionOrGroup = "Sine-Saloum & Petite-Côte",
        greeting = "Kani yaam ci Téranga Moov",
        liveTabLabel = "Xaye",
        routesTabLabel = "O yoon",
        ticketsTabLabel = "Tike ke",
        reportsTabLabel = "O faam",
        profileTabLabel = "Bërëb ale",
        buyTicketLabel = "Ret tike",
        trafficAlertVocalSummary = "Faañee : o yoon ole dafa heew. BRT na TER a gara no jàmm."
    ),
    MANDINKA(
        code = "mn",
        displayName = "Mandinka (Malinké)",
        regionOrGroup = "Casamance & Sénégal Oriental",
        greeting = "Bisimila Téranga Moov koto",
        liveTabLabel = "Sising",
        routesTabLabel = "Silo lu",
        ticketsTabLabel = "Tiketi lu",
        reportsTabLabel = "Kumoo lu",
        profileTabLabel = "Nna yiroo",
        buyTicketLabel = "Tiketi sango",
        trafficAlertVocalSummary = "I hakili tu : sila baa be fanoo la. BRT aning TER be taama kang ko beteyaa."
    ),
    SONINKE(
        code = "sn",
        displayName = "Soninké",
        regionOrGroup = "Bakel, Tambacounda & Falémé",
        greeting = "Bismila Téranga Moov do",
        liveTabLabel = "Saxe",
        routesTabLabel = "Kille",
        ticketsTabLabel = "Tikette",
        reportsTabLabel = "Kibaare",
        profileTabLabel = "N’kene",
        buyTicketLabel = "Tiket yaame",
        trafficAlertVocalSummary = "Xa kottu : kille kille ga bundu. BRT do TER ga na taaxu kenme."
    ),
    DIOLA(
        code = "dy",
        displayName = "Diola (Jola)",
        regionOrGroup = "Basse-Casamance, Ziguinchor & Oussouye",
        greeting = "Kassumay ci Téranga Moov",
        liveTabLabel = "Kati",
        routesTabLabel = "Kareg",
        ticketsTabLabel = "Tike",
        reportsTabLabel = "Kawambal",
        profileTabLabel = "Éyiro",
        buyTicketLabel = "E-manj tike",
        trafficAlertVocalSummary = "Kajool : eloong eyi e-wumb. BRT di TER ga burowor kasumay."
    ),
    BALANTE(
        code = "bl",
        displayName = "Balante",
        regionOrGroup = "Sédhiou & Haute-Casamance",
        greeting = "Badiam ci Téranga Moov",
        liveTabLabel = "Bli",
        routesTabLabel = "Btaan",
        ticketsTabLabel = "Tiketi",
        reportsTabLabel = "Bfank",
        profileTabLabel = "Bfay",
        buyTicketLabel = "Jom tiketi",
        trafficAlertVocalSummary = "Cuidau : btaan bfuli. BRT na TER ba de na calma."
    ),
    MANCAGNE(
        code = "mc",
        displayName = "Mancagne (Mankanya)",
        regionOrGroup = "Bignona & Goudomp",
        greeting = "Ukalem ci Téranga Moov",
        liveTabLabel = "Bëgësh",
        routesTabLabel = "Batim",
        ticketsTabLabel = "Tike",
        reportsTabLabel = "Kapat",
        profileTabLabel = "Nteñ",
        buyTicketLabel = "Lëb tike",
        trafficAlertVocalSummary = "Pësh : batim ba bënë bëtëk. BRT bë TER ba kani u këtë."
    ),
    NOON(
        code = "nn",
        displayName = "Noon",
        regionOrGroup = "Thiès & Plateaux",
        greeting = "Kowukow ci Téranga Moov",
        liveTabLabel = "Tiis",
        routesTabLabel = "Yoon",
        ticketsTabLabel = "Tike",
        reportsTabLabel = "Woot",
        profileTabLabel = "Bërëb",
        buyTicketLabel = "Jënd tike",
        trafficAlertVocalSummary = "Mooteel : yoon fada heew. BRT en TER sofi deede."
    ),
    MANJAQUE(
        code = "mj",
        displayName = "Manjaque (Mandjak)",
        regionOrGroup = "Basse-Casamance & Dakar",
        greeting = "Mabali ci Téranga Moov",
        liveTabLabel = "Unkëk",
        routesTabLabel = "Mpe",
        ticketsTabLabel = "Tike",
        reportsTabLabel = "Katen",
        profileTabLabel = "Përa",
        buyTicketLabel = "Mën tike",
        trafficAlertVocalSummary = "Cuidar : mpe ma puki. BRT de TER ba jëk bëkac."
    ),
    SAAFI(
        code = "sf",
        displayName = "Saafi (Safen)",
        regionOrGroup = "Sindia, Popenguine & Mbour",
        greeting = "Gikoon ci Téranga Moov",
        liveTabLabel = "Kañaa",
        routesTabLabel = "Yoon",
        ticketsTabLabel = "Tike",
        reportsTabLabel = "Pëël",
        profileTabLabel = "Këk",
        buyTicketLabel = "Kool tike",
        trafficAlertVocalSummary = "Tukee : yoon a fës. BRT ka TER fa jàmm no."
    ),
    BASSARI(
        code = "bs",
        displayName = "Bassari",
        regionOrGroup = "Pays Bassari & Kédougou",
        greeting = "Aniké ci Téranga Moov",
        liveTabLabel = "Mënë",
        routesTabLabel = "Kori",
        ticketsTabLabel = "Tike",
        reportsTabLabel = "Wasa",
        profileTabLabel = "Nanga",
        buyTicketLabel = "Sa tike",
        trafficAlertVocalSummary = "Mëte : kori ka nyala. BRT na TER ga hira."
    ),
    BAYOT(
        code = "by",
        displayName = "Bayot",
        regionOrGroup = "Sud de Ziguinchor & Nyassia",
        greeting = "Iwaani ci Téranga Moov",
        liveTabLabel = "Kadi",
        routesTabLabel = "Karoo",
        ticketsTabLabel = "Tike",
        reportsTabLabel = "Kaba",
        profileTabLabel = "Ébo",
        buyTicketLabel = "Banj tike",
        trafficAlertVocalSummary = "Kafañ : karoo ka bumb. BRT na TER ba tiir no bujoka."
    ),
    BEDIK(
        code = "bd",
        displayName = "Bédik",
        regionOrGroup = "Iwol & Bandafassi (Kédougou)",
        greeting = "Oban ci Téranga Moov",
        liveTabLabel = "Oko",
        routesTabLabel = "Kuro",
        ticketsTabLabel = "Tike",
        reportsTabLabel = "Woli",
        profileTabLabel = "Eba",
        buyTicketLabel = "Wali tike",
        trafficAlertVocalSummary = "Okun : kuro a gobi. BRT ni TER na dola."
    ),
    NDUT(
        code = "nd",
        displayName = "Ndut",
        regionOrGroup = "Mont-Rolland & Thiès Nord",
        greeting = "Sooduk ci Téranga Moov",
        liveTabLabel = "Taal",
        routesTabLabel = "Yoon",
        ticketsTabLabel = "Tike",
        reportsTabLabel = "Taax",
        profileTabLabel = "Bërëb",
        buyTicketLabel = "Jënd tike",
        trafficAlertVocalSummary = "Sool : yoon a fës. BRT ak TER ñu ngi dox bu baax."
    ),
    PALOR(
        code = "pl",
        displayName = "Palor",
        regionOrGroup = "Pout & Forêt de Thiès",
        greeting = "Bari ci Téranga Moov",
        liveTabLabel = "Lii",
        routesTabLabel = "Yoon",
        ticketsTabLabel = "Tike",
        reportsTabLabel = "Kibaar",
        profileTabLabel = "Këk",
        buyTicketLabel = "Jënd tike",
        trafficAlertVocalSummary = "Moytu : yoon we xat. BRT ak TER ga baaxe."
    ),
    LEHAR(
        code = "lh",
        displayName = "Léhar (Laalaa)",
        regionOrGroup = "Vallée du Léhar (Thiès)",
        greeting = "Tooduk ci Téranga Moov",
        liveTabLabel = "Téy",
        routesTabLabel = "Yoon",
        ticketsTabLabel = "Tike",
        reportsTabLabel = "Kibaar",
        profileTabLabel = "Bërëb",
        buyTicketLabel = "Jënd tike",
        trafficAlertVocalSummary = "Faañ : yoon fada fës. BRT ak TER no dox jàmm."
    ),
    BADIARANKE(
        code = "bk",
        displayName = "Badiaranké",
        regionOrGroup = "Vélingara & Médina Gounass",
        greeting = "Ndimbaa ci Téranga Moov",
        liveTabLabel = "Saña",
        routesTabLabel = "Kili",
        ticketsTabLabel = "Tiketi",
        reportsTabLabel = "Kuma",
        profileTabLabel = "Mba",
        buyTicketLabel = "Sodi tiketi",
        trafficAlertVocalSummary = "Reente : kili ka fari. BRT ni TER de doodi."
    ),
    BAINOUK(
        code = "bn",
        displayName = "Baïnouk",
        regionOrGroup = "Bignona & Sédhiou",
        greeting = "Mundom ci Téranga Moov",
        liveTabLabel = "Kano",
        routesTabLabel = "Kuroon",
        ticketsTabLabel = "Tike",
        reportsTabLabel = "Kibaar",
        profileTabLabel = "Nsañ",
        buyTicketLabel = "Hët tike",
        trafficAlertVocalSummary = "Moyten : kuroon ku fër. BRT di TER ga sës bu baaxe."
    ),
    JALONKE(
        code = "jl",
        displayName = "Jalonké",
        regionOrGroup = "Fongolimbi & Kédougou Sud",
        greeting = "Tansoma ci Téranga Moov",
        liveTabLabel = "Kasi",
        routesTabLabel = "Kiraa",
        ticketsTabLabel = "Tiketi",
        reportsTabLabel = "Khibaru",
        profileTabLabel = "Ndeere",
        buyTicketLabel = "Tiketi sara",
        trafficAlertVocalSummary = "Kolu : kiraa bara tongo. BRT nun TER nira bata ma feere."
    );

    companion object {
        val ALL_NATIONAL_LANGUAGES = values().toList()
        val TWENTY_NATIONAL_LANGUAGES = values().filter { it != FRENCH }

        fun fromCode(code: String?): NationalLanguage {
            if (code.isNullOrBlank()) return FRENCH
            return values().find { it.code.equals(code, ignoreCase = true) } ?: FRENCH
        }
    }
}
