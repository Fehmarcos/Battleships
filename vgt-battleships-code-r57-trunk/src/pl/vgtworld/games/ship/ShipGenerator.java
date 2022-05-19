package pl.vgtworld.games.ship;

import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.tools.Position;

/**
 * Klasa zajmujaca sie utworzeniem obiektu kontenera statkow na podstawie dostarczonej planszy z zaznaczonymi polami statkow.
 * 
 * @author VGT
 * @version 1.0
 */
public class ShipGenerator
	{
	/**
	 * Board z naniesionym rozmieszczeniem statkow.<br />
	 * 
	 * Docelowo zostanie ona wykorzystana, jako plansza obiektu kontenera statkow.
	 */
	private Board oPlansza;
	/**
	 * Kontener statkow, ktory zostanie utworzony na podstawie dostarczonej planszy.
	 */
	private ShipIterator oStatki;
	/**
	 * Tablica pomocnicza wczytujaca pozycje wszystkich zaznaczonych pol na planszy.
	 */
	private Position[] aPolaStatkow;
	/**
	 * Aktualna ilosc pol przechowywana w tablicy aPolaStatkow.
	 */
	private int iIloscPolStatkow;
	/**
	 * Konstruktor domyslny.
	 * 
	 * @param oPlansza Board z rozmieszczonymi statkami.
	 */
	public ShipGenerator(Board oPlansza)
		{
		this.oPlansza = oPlansza;
		oStatki = null;
		aPolaStatkow = new Position[0];
		iIloscPolStatkow = 0;
		}
	/**
	 * Glowna metoda rozpoczynajaca proces tworzenia kontenera statkow na podstawie planszy dostarczonej w konstruktorze.
	 * 
	 * @return Zwraca stworzony kontener statkow.
	 */
	public ShipIterator generujStatki()
		{
		try
			{
			//wyszukanie na planszy oznaczonych pol i wyczyszczenie planszy
			znajdzPola();
			//utworzenie kontenera statkow
			oStatki = new ShipIterator(oPlansza);
			int iIloscStatkow = 0;
			//wypelnienie kontenera statkami
			while (iIloscPolStatkow > 0)
				{
				Position[] aStatek = generujStatek();
				oStatki.dodajStatek( aStatek.length );
				++iIloscStatkow;
				for (int i = 0; i < aStatek.length; ++i)
					oStatki.getStatek(iIloscStatkow).setPole(i+1, aStatek[i].getX(), aStatek[i].getY());
				}
			//zwrocenie kontenera
			return this.oStatki;
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Metoda wyszukuje na planszy pola statkow.<br />
	 * 
	 * Liste ich pozycji zapisuje do tablicy aPolaStatkow, a ilosc pol do iIloscPolStatkow.
	 * Na koniec czysci takze plansze z oznaczonych pol, aby przygotowac ja do dzialania w ramach tworzonego obiektu kontenera statkow.
	 */
	private void znajdzPola()
		{
		try
			{
			aPolaStatkow = new Position[0];
			iIloscPolStatkow = 0;
			for (int i = 0; i < oPlansza.getWidth(); ++i)
				for (int j = 0; j < oPlansza.getHeight(); ++j)
					if (oPlansza.getPole(i, j) == FieldTypeBoard.SHIP_BOARD)
						{
						Position[] aNowaLista = new Position[ iIloscPolStatkow + 1 ];
						//przepisanie dotychczasowej listy
						for (int k = 0; k < iIloscPolStatkow; k++)
							aNowaLista[k] = aPolaStatkow[k];
						//dopisanie nowego elementu na koncu
						Position oObj = new Position(2);
						oObj.setX(i);
						oObj.setY(j);
						aNowaLista[iIloscPolStatkow] = oObj;
						++iIloscPolStatkow;
						aPolaStatkow = aNowaLista;
						}
			//zamazanie pol na planszy
			for (int i = 0; i < aPolaStatkow.length; ++i)
				oPlansza.setPole(aPolaStatkow[i].getX(), aPolaStatkow[i].getY(), FieldTypeBoard.BOARD_FIELD_EMPTY);
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Metoda pobiera i usuwa pola z tablicy aPolaStatkow starajac sie wygenerowac liste pol pojedynczego statku.<br />
	 * 
	 * Po pobraniu pierwszego pola skanuje liste pozostalych tak dlugo, az uzyska tablice zawierajaca wszystkie polaczone ze soba pola.
	 * 
	 * @return Zwraca tablice zawierajaca liste pol dla jednego statku.
	 */
	private Position[] generujStatek()
		{
		if (aPolaStatkow.length == 0)
			throw new DeveloperException("Brak pol na liscie");
		try
			{
			//utworzenie tablicy mogacej przechowac liste wszystkich pol aktualnie znajdujacych sie na planszy
			//(w przyszlosci mozna przerobic na kontener)
			int iRozmiar = 0;
			Position[] aPola = new Position[ aPolaStatkow.length ];
			//pobranie pierwszego pola z planszy
			aPola[ iRozmiar++ ] =  pobierzPole(0);
			//petla pobierajaca kolejne pola dopoki jakies sasiadujace sa znajdowane
			boolean bNowySasiad = true;
			while (bNowySasiad == true)
				{
				bNowySasiad = false;
				for (int i = 0; i < iRozmiar; ++i)
					{
					int iNrSzukanegoSasiada = znajdzSasiada(aPola[i]);
					if (iNrSzukanegoSasiada != -1)
						{
						aPola[ iRozmiar++ ] = pobierzPole(iNrSzukanegoSasiada);
						bNowySasiad = true;
						}
					}
				}
			//utworzenie nowej tablicy o rozmiarach przycietych do znalezionego statku, przepisanie do niej pol i return
			Position[] aReturn = new Position[iRozmiar];
			for (int i = 0; i < aReturn.length; ++i)
				aReturn[i] = aPola[i];
			return aReturn;
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Metoda sprawdza, czy na liscie pol jest pole sasiadujace z przekazanym w parametrze.<br />
	 * 
	 * Jesli tak, zwraca jego index w tablicy aPolaStatkow.
	 * Jesli wystepuje wiecej takich pol, zwrocony zostanie index pierwszego znalezionego pola.
	 * Jesli podane pola nie ma sasiadow, zostanie zwrocona wartosc -1.
	 * 
	 * @param oPozycja Wspolrzedne pola, dla ktorego nalezy szukac sasiadow.
	 * @return Zwraca index pola sasiadujacego, lub -1, jesli nie znaleziono zadnego.
	 */
	private int znajdzSasiada(Position oPozycja)
		{
		for (int i = 0; i < aPolaStatkow.length; ++i)
			{
			if (
				(aPolaStatkow[i].getX() == oPozycja.getX() - 1 && aPolaStatkow[i].getY() == oPozycja.getY()) ||
				(aPolaStatkow[i].getX() == oPozycja.getX() + 1 && aPolaStatkow[i].getY() == oPozycja.getY()) ||
				(aPolaStatkow[i].getX() == oPozycja.getX() && aPolaStatkow[i].getY() == oPozycja.getY() - 1) ||
				(aPolaStatkow[i].getX() == oPozycja.getX() && aPolaStatkow[i].getY() == oPozycja.getY() + 1)
				)
				return i;
			}
		return -1;
		}
	/**
	 * Metoda usuwa z listy pol w aPolaStatkow element o podanym indexie i zwraca go.
	 * 
	 * @param iIndex Index pola do usuniecia.
	 * @return Zwraca usuniete pole.
	 * @throws ParameterException Wyrzuca wyjatek, jesli index znajduje sie poza dostepnym zakresem pol.
	 */
	private Position pobierzPole(int iIndex) throws ParameterException
		{
		if (iIndex >= iIloscPolStatkow || iIndex < 0)
			throw new ParameterException("iIndex = " + iIndex);
		Position[] aNowaLista = new Position[iIloscPolStatkow - 1];
		//przepisanie elementow do nowej tablicy z pominieciem usuwanego
		int iLicznik = 0;
		for (int i = 0; i < iIloscPolStatkow; ++i)
			if (i != iIndex)
				{
				aNowaLista[iLicznik] = aPolaStatkow[i];
				++iLicznik;
				}
		//zapisanie elementu do usuniecia i podmiana tablicy pol w obiekcie
		Position oReturn = aPolaStatkow[iIndex];
		aPolaStatkow = aNowaLista;
		--iIloscPolStatkow;
		
		return oReturn;
		}
	}
