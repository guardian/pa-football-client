package pa

object TeamCodes {
  def codeFor(team: FootballTeam): String = {
    codes.get(team.id).getOrElse {
      val alteredName = team.name.split(" ").toList.filter(_.isDefined) match {
        case word1 :: word2 :: word3 :: Nil => s"${word1.head}${word2.head}${word3.head}"
        case prefix :: rest if skipWords.contains(prefix) => rest.mkString
        case name => name.mkString
      }
      alteredName.take(3).replaceAllLiterally(" ", "").toUpperCase
    }
  }

  private val skipWords = List("Real", "Athletico", "Athletic", "FC")

  val codes= Map(
    // "Premier League"
    ("1006", "ARS"),  // Arsenal
    ("2", "AVL"),     // Aston Villa
    ("52", "CAR"),    // Cardiff
    ("4", "CHE"),     // Chelsea
    ("5", "CRY"),     // Crystal Palace
    ("8", "EVE"),     // Everton
    ("55", "FUL"),    // Fulham
    ("26", "HUL"),    // Hull
    ("9", "LIV"),     // Liverpool
    ("11", "MNC"),    // Manchester City
    ("12", "MNU"),    // Manchester United
    ("31", "NEW"),    // Newcastle
    ("14", "NOR"),    // Norwich
    ("18", "SOU"),    // Southampton
    ("38", "STK"),    // Stoke
    ("39", "SUN"),    // Sunderland
    ("65", "SWA"),    // Swansea
    ("19", "TOT"),    // Totenham
    ("42", "WBA"),    // West Bomich Albion
    ("43", "WHU"),    // West Ham United
    // "Championship"
    ("21", "BAR"),    // Barnsley
    ("45", "BHM"),    // Birmingham
    ("22", "BLA"),    // Blackburn
    ("46", "BLP"),    // Blackpool
    ("47", "BOL"),    // Bolton
    ("23", "BOU"),    // Bournmouth
    ("6795", "BRI"),  // Brighton
    ("70", "BUR"),    // Burnley
    ("3", "CHA"),     // Charlton
    ("7", "DER"),     // Derby
    ("6794", "DON"),  // Doncaster
    ("56", "HUD"),    // Huddersfield
    ("28", "LEE"),    // Leeds
    ("29", "LEI"),    // Leicester
    ("27", "IPS"),    // Ipswich
    ("30", "MID"),    // Middlesborough
    ("13", "MIL"),    // Millwall
    ("15", "NOT"),    // Nottingham Forest
    ("16", "QPR"),    // QPR
    ("62", "REA"),    // Reading
    ("37", "SHW"),    // Sheffield Wednesday
    ("41", "WAT"),    // Watford
    ("68", "WIG"),    // Wigan
    ("6870", "YEO"),  // Yeovil
    // "League One"
    ("24", "BRA"),    // Bradford
    ("48", "BRE"),    // Brentford
    ("49", "BRC"),    // Bristol City
    ("72", "CSL"),    // Carlisle
    ("74", "COL"),    // Colchester
    ("6", "COV"),     // Coventry
    ("188", "CRA"),   // Crawley
    ("54", "CRW"),    // Crewe
    ("77", "GIL"),    // Gillingham
    ("57", "LEY"),    // Leyton Orient
    ("20", "MKD"),    // MK Dons
    ("60", "NTC"),    // Notts County
    ("32", "OLD"),    // Oldham
    ("84", "PET"),    // Peterborough
    ("35", "PTV"),    // Port Vale
    ("61", "PRE"),    // Preston
    ("63", "ROT"),    // Rotherham
    ("37", "SHU"),    // Sheffield United
    ("1073", "STE"),  // Stevenage
    ("64", "SHR"),    // Shrewsbury
    ("40", "SWI"),    // Swindon
    ("66", "TRN"),    // Tranmere
    ("67", "WAL"),    // Walsall
    ("44", "WOL"),    // Wolverhampton
    // "League Two"
    ("1204", "ACC"),  // Accrington
    ("50", "BRO"),    // Bristol Rovers
    ("184", "BRT"),   // Burton Albion
    ("51", "BRY"),    // Bury
    ("73", "CFD"),    // Chesterfield
    ("137", "CHL"),   // Cheltenham
    ("179", "D&R"),   // Dag & Red
    ("76", "EXE"),    // Exeter
    ("11899", "FLE"), // Fleetwood
    ("80", "HAR"),    // Hartlepool
    ("58", "MAN"),    // Mansfield
    ("215", "MOR"),   // Morecombe
    ("19337", "NPT"), // Newport
    ("59", "NMT"),    // Northampton
    ("33", "OXD"),    // Oxford United
    ("80", "PLY"),    // Plymouth
    ("36", "POR"),    // Portsmouth
    ("85", "ROC"),    // Rochdale
    ("87", "SCU"),    // Scunthorpe
    ("88", "SOE"),    // Southend
    ("90", "TOR"),    // Torquay
    ("45987", "WIM"), // Wimbledon
    ("153", "WYC"),   // Wycombe
    ("92", "YRK"),    // York
    // "Scottish Premier League"
    ("93", "ABD"),    // Aberdeen
    ("94", "CEL"),    // Celtic
    ("100", "MOT"),   // Motherwell
    ("96", "DNU"),    // Dundee United
    ("98", "HEA"),    // Hearts
    ("99", "HIB"),    // Hibernian
    ("1456", "ICT"),  // Inverness CT
    ("123", "KIL"),   // Kilmarnock
    ("113", "PAR"),   // Partick Thistle
    ("848", "RSC"),   // Ross County
    ("115", "STJ"),   // St Johnstone
    ("102", "STM"),   // St Mirren
    // "Scottish Championship"
    ("104", "AOA"),   // Alloa
    ("119", "CNB"),   // Cowdenbeath
    ("120", "DBN"),   // Dumbarton
    ("95", "DUN"),    // Dundee
    ("108", "FLK"),   // Falkirk
    ("110", "HAM"),   // Hamilton
    ("111", "LVN"),   // Livingston
    ("112", "MOR"),   // Morton
    ("125", "QOS"),   // QOS FC
    ("114", "RAI"),   // Raith
    // "Scottish League 1"
    ("45938", "AIR"), // Airdrie
    ("116", "ARB"),   // Arbroath
    ("105", "AYR"),   // Ayr
    ("118", "BRE"),   // Brechin
    ("97", "DFN"),    // Dumfermline
    ("121", "EFI"),   // East Fife
    ("109", "FOR"),   // Forfar
    ("101", "RGR"),   // Rangers
    ("127", "STM"),   // Stenhousemuir
    ("129", "STR"),   // Stranraer
    // "Scottish League 2"
    ("103", "ALB"),   // Albion
    ("17635", "ANN"), // Annan
    ("117", "BER"),   // Berwick
    ("106", "CLY"),   // Clyde
    ("7558", "ELG"),  // Elgin
    ("122", "EST"),   // East Stirling
    ("124", "MTR"),   // Montrose
    ("126", "QPK"),   // Queens Park
    ("7596", "PET"),  // Peterhead
    ("128", "STI"),   // Stirling
    // "Champions League (exceptions)"
    ("26291", "AND"), // Anderlecht
    ("42007", "ANZ"), // Anzhi Makhachkala
    ("26320", "AJX"), // Ajax
    ("32703", "APO"), // APOEL
    ("26398", "BSL"), // Basle
    ("26274", "BEN"), // Benfica
    ("26412", "COP"), // Copenhagen
    ("27018", "CSK"), // CSKA Moscow
    ("32166", "DIZ"), // Dinamo Zagreb
    ("7012", "DKV"),  // Dynamo Kiev
    ("26402", "FAV"), // FK Austria Vienna
    ("26449", "FEN"), // Fenebache
    ("26451", "GAL"), // Galatasaray
    ("26388", "GHP"), // Grasshopper
    ("36570", "LEG"), // Legia Warsaw
    ("61179", "LUD"), // Ludgrets Razgrad
    ("7757", "MBR"),  // Maribor
    ("43136", "NJD"), // Nordsjelland
    ("7897", "OLY"),  // Olympiacos
    ("38488", "PAC"), // Pacos de Ferreiea
    ("8417", "PAO"),  // PAOK
    ("26264", "POR"), // Porto
    ("26321", "PSV"), // PSV Eindhoven
    ("26404", "RBS"), // Red Bull Salzburg
    ("38299", "SHK"), // Shaktar Donetsk
    ("6901", "STB"),  // Steaua Bucharest
    ("38336", "PLZ"), // Viktoria Pilzen
    ("26265", "VIT"), // Vitoria de Guimaraes
    ("38276", "ZEN"), // Zenit St Petersberg
    ("32471", "ZUL"), // Zulte Waregem
    // "La Liga"
    ("26303", "ALM"), // Almeria
    ("26313", "ATH"), // Athletic Bilbao
    ("26305", "AMD"), // Atletico Madrid
    ("26300", "BAR"), // Barcelona
    ("26314", "BET"), // Real Betis
    ("26302", "CEL"), // Celta Vigo
    ("37488", "ECF"), // Elche
    ("26306", "ESP"), // Espanyol
    ("37459", "GET"), // Getafe
    ("41000", "GRA"), // Granada
    ("37454", "LEV"), // Levante
    ("27826", "MAL"), // Malaga
    ("27152", "OSA"), // Osasuna
    ("35724", "RAY"), // Rayo Vallecano
    ("26303", "RMD"), // Real Madrid
    ("26308", "RSO"), // Real Sociedad
    ("27821", "SEV"), // Sevilla
    ("26319", "VID"), // Real Valladolid
    ("26316", "VAL"), // Valencia
    ("38295", "VIL"), // Villarreal
    // "Seria A"
    ("26357", "ROM"), // Roma
    ("26368", "MIL"), // AC Milan
    ("26364", "ATL"), // Atalanta
    ("26371", "BOL"), // Bolongia
    ("27788", "CAT"), // Catania
    ("26474", "CGL"), // Cagliari
    ("27611", "CHI"), // Chievo
    ("26366", "FIO"), // Fiorentina
    ("6894", "GEN"),  // Genoa
    ("6136", "INT"),  // Inter Milan
    ("26359", "JUV"), // Juventus
    ("41783", "LIV"), // Livorno
    ("26362", "LAZ"), // Lazio
    ("26370", "NAP"), // Napoli
    ("26358", "PAR"), // Parma
    ("53908", "SAS"), // Sassuolo
    ("26361", "SMP"), // Sampdoria
    ("27038", "TOR"), // Torino
    ("27648", "VER"), // Verona
    ("26360", "UDI"), // Udinese
    // "Bundesliga"
    ("26256", "LEV"), // Bayer Leverkusen
    ("26259", "BMG"), // Borussia M'gladbach
    ("26252", "BRE"), // Werder Bremen
    ("26263", "HER"), // Hertha BSC
    ("26261", "DOR"), // Borussia Dortmund
    ("45562", "EBS"), // Eintracht Braunschweig
    ("32656", "FCA"), // Augsberg
    ("26247", "BAY"), // Bayern Munich
    ("32323", "FCN"), // Nurnberg
    ("26460", "EIN"), // Eintract Frankfurt
    ("32309", "HAN"), // Hanover 96
    ("48490", "HOF"), // Hoffenheim
    ("26254", "HAM"), // Hamburg
    ("32676", "MNZ"), // Mainz 05
    ("26461", "SCF"), // Freiburg
    ("26249", "SCH"), // Schalke
    ("26250", "STU"), // Stuttgart
    ("26257", "WOB"), // Wolfsburg
    // "Ligue 1"
    ("38298", "ACA"), // AC Ajaccio
    ("27408", "ASS"), // AS Saint-Etienne
    ("26340", "BOR"), // Bordeaux
    ("59842", "ETG"), // Evian Thonon Gaillard
    ("26469", "FCL"), // FC Lorient
    ("26347", "GUI"), // EA Guingamp
    ("27372", "LIL"), // Lille
    ("26345", "LYO"), // Lyon
    ("26344", "MAR"), // Olympique de Marseille
    ("26343", "MON"), // Monaco
    ("26351", "MPL"), // Montpellier
    ("26350", "NAN"), // FC Nantes Atlantique
    ("27462", "NIC"), // OGC Nice
    ("26339", "PSG"), // Paris Saint-germain
    ("41053", "REI"), // Reims
    ("26349", "REN"), // Stade Rennais FC
    ("26341", "SCB"), // SC Bastia
    ("8407", "SOC"),  // FC Sochaux
    ("26346", "TFC"), // Toulouse FC
    ("32811", "VAL"), // Valenciennes FC
    // "Internationals (World Cup)"
    ("37264", "ALG"), // Algeria
    ("965", "ARG"),   // Argentina
    ("7317", "AUS"),  // Australia
    ("997", "BEL"),   // Belgium
    ("7531", "BIH"),  // Bosnia & Herzongevina
    ("23104", "BRA"), // Brazil
    ("7924", "CAM"),  // Cameroon
    ("2559", "CHI"),  // Chile
    ("6996", "COL"),  // Columbia
    ("37621", "CRC"), // Costa Rica
    ("37285", "CIV"), // Ivory Coast
    ("2489", "CRO"),  // Croatia
    ("37262", "ECU"), // Ecuador
    ("497", "ENG"),   // England
    ("619", "FRA"),   // France
    ("1678", "GER"),  // Germany
    ("37306", "GHA"), // Ghana
    ("6286", "GRE"),  // Greece
    ("37618", "HON"), // Honduras
    ("8111", "IRN"),  // Iran
    ("717", "ITA"),   // Italy
    ("6736", "JAP"),  // Japan
    ("23120", "KOR"), // South Korea
    ("5837", "MEX"),  // Mexico
    ("631", "NED"),   // Netherlands
    ("8110", "NIG"),  // Nigeria
    ("5539", "POR"),  // Portugal
    ("5827", "RUS"),  // Russia
    ("999", "SPA"),   // Spain
    ("1660", "SWI"),  // Switzerland
    ("37260", "URU"), // Uruguay
    ("7356", "USA"),  // USA
    // "Internationals (home nations)"
    ("630", "WAL"),   // Wales
    ("499", "SCO"),   // Scotland
    ("964", "NIR"),   // Northern Ireland
    ("494", "IRL"),   // Republic of Ireland
    ("6740", "GIB"),  // Gibraltar
    // "Internationals (Euro Qualifiers)"
    ("5536", "ALB"),  // Albania
    ("8230", "AND"),  // Andorra
    ("6604", "ARM"),  // Armenia
    ("992", "AUT"),   // Austria
    ("6602", "AZE"),  // Azerbaijan
    ("6262", "BLR"),  // Belarus
    ("980", "BUL"),   // Bulgaria
    ("5628", "CYP"),  // Cyprus
    ("6318", "CZE"),  // Czech Rep
    ("986", "DEN"),   // Denmark
    ("5603", "EST"),  // Estonia
    ("1677", "FRO"),  // Faroe Islands
    ("991", "FIN"),   // Finland
    ("6603", "MAC"),  // Macedonia
    ("6527", "GEO"),  // Georgia
    ("493", "HUN"),   // Hungary
    ("2340", "ICE"),  // Iceland
    ("6600", "ISR"),  // Israel
    ("37324", "KAZ"), // Kazakstan
    ("5602", "LAT"),  // Latvia
    ("6259", "LIE"),  // Liechtenstein
    ("5527", "LTU"),  // Lithuania
    ("7365", "LUX"),  // Luxembourg
    ("776", "MLT"),   // Malta
    ("6526", "MOL"),  // Moldova
    ("53897", "MON"), // Montenegro
    ("716", "NOR"),   // Norway
    ("629", "POL"),   // Poland
    ("823", "ROM"),   // Romania
    ("1676", "SMR"),  // San Marino
    ("498", "SRB"),   // Serbia
    ("6601", "SVK"),  // Slovakia
    ("6605", "SVN"),  // Slovenia
    ("5845", "SWE"),  // Sweden
    ("1661", "TUR"),  // Turkey
    ("6272", "UKR")   // Urkaine
  )
}
