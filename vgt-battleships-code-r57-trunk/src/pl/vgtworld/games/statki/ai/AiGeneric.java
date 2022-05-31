package pl.vgtworld.games.statki.ai;

import java.util.ArrayList;
import java.util.Random;
import pl.vgtworld.exceptions.ParameterException;
import pl.vgtworld.exceptions.DeveloperException;
import pl.vgtworld.games.ship.Board;
import pl.vgtworld.games.ship.FieldTypeBoard;
import pl.vgtworld.games.ship.ShipIterator;
import pl.vgtworld.tools.Position;

/**
 * Klasa abstrakcyjna zawierajaca zestaw metod uzytecznych do budowania klas sztucznej inteligencji.<br />
 *
 * <p>
 * aktualizacje:<br />
 * 1.1<br />
 * - dodanie metody metody {@link #setProsteLinie(boolean)}<br />
 * </p>
 * 
 * @author VGT
 * @version 1.1
 */
public abstract class AiGeneric
	{
	/**
	 * Kontener statkow nalezacych do gracza sterowanego przez komputer
	 * (uzyteczne w bardziej rozbudowanych wersjach AI do ustalania, jak daleko od potencjalnej przegranej jest komputer).
	 */
	protected ShipIterator oStatki;
	/**
	 * Przechowuje wspolrzedne ostatniego celnego strzalu.<br />
	 * 
	 * Wartosc tego pola nalezy uzupelniac we wszystkich metodach oddajacych strzal na plansze przeciwnika.
	 */
	protected Position oOstatnieTrafienie;
	/**
	 * Kontener wykorzystywany do przechowywania wspolrzednych dla udanych trafien w poprzednich rundach.
	 * 
	 * Na jego podstawie mozliwe jest szukanie kolejnych pol trafionego statku w celu jego dalszego ostrzalu.
	 */
	protected ArrayList<Position> oUzyteczneTrafienia;
	/**
	 * Wlasciwosc okresla, czy statki na planszy moga byc tylko pionowymi/poziomymi liniami (TRUE),
	 * czy tez moga miec dowolne ksztalty (FALSE, domyslnie).
	 */
	protected boolean bProsteLinie;
	/**
	 * Generator liczb losowych.
	 */
	protected Random oRand;
	/**
	 * Konstruktor.
	 * 
	 * @param oStatki Kontener statkow nalezacych do gracza sterowanego przez dany obiekt Ai.
	 */
	public AiGeneric(ShipIterator oStatki)
		{
		this.oStatki = oStatki;
		bProsteLinie = false;
		oRand = new Random();
		oOstatnieTrafienie = new Position(2);
		oOstatnieTrafienie.setX(-1);
		oOstatnieTrafienie.setY(-1);
		oUzyteczneTrafienia = new ArrayList<Position>();
		}
	/**
	 * Metoda pozwala ustawic wlasciwosc okreslajaca dozwolny ksztalt statkow.
	 * 
	 * @param bWartosc Okresla, czy statki moga byc tylko pionowymi/poziomymi liniami.
	 * @since 1.1
	 */
	public void setProsteLinie(boolean bWartosc)
		{
		bProsteLinie = bWartosc;
		}
	/**
	 * Najprostrza mozliwa implementacja wyboru pola do ostrzelania. Metoda wyszukuje wszystkie pola, na ktore mozna oddac strzal
	 * i losowo wybiera jedno z nich.<br />
	 * 
	 * Informacje na temat wspolrzednych oddanego strzalu sa przekazywane do metody strzal() kontenera
	 * statkow przekazanego w parametrze i tam jest zrealizowana pelna obsluga strzalu.
	 * 
	 * @param oStatkiPrzeciwnika Kontener statkow przeciwnika, ktory ma byc ostrzelany.
	 * @return Zwraca TRUE w przypadku trafienia ktoregos ze statkow, lub FALSE, jesli strzal byl niecelny.
	 */
	protected boolean strzalLosowy(ShipIterator oStatkiPrzeciwnika)
		{
		try
			{
			Position oWylosowanePole = losujPole(oStatkiPrzeciwnika.getPlansza());
			boolean bTrafienie = oStatkiPrzeciwnika.strzal(oWylosowanePole.getX(), oWylosowanePole.getY());
			if (bTrafienie == true)
				{
				//zapisanie celnego strzalu w tablicy trafien
				Position oTrafienie = new Position(2);
				oTrafienie.setX(oWylosowanePole.getX());
				oTrafienie.setY(oWylosowanePole.getY());
				oUzyteczneTrafienia.add(oTrafienie);
				}
			return bTrafienie;
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	/**
	 * Metoda wybiera losowo jedno z zapisanych wczesniejszych trafien i sprawdza, czy mozna ostrzelac ktores z sasiadujacych pol.<br />
	 * 
	 * Jesli tak, wybiera jedno z pol do ostrzelania. Jesli nie, usuwa pole z listy, wybiera losowo kolejne zapisane trafienie
	 * i powtarza proces. Jesli wyczerpane zostana zapisane trafienia, wywolywana jest metoda strzalLosowy()
	 * 
	 * @param oStatkiPrzeciwnika Kontener statkow przeciwnika.
	 * @return Zwraca TRUE, w przypadku trafienia statku, lub FALSE, jesli strzal byl niecelny.
	 */
	protected boolean strzalSasiadujacy(ShipIterator oStatkiPrzeciwnika)
		{
		//przygotowanie kontenera przechowujacego do 4 sasiednich pol, ktore nadaja sie do kolejnego strzalu
		ArrayList<Position> oSasiedniePola = new ArrayList<Position>(4);
		//petla wyszukujaca we wczesniejszych trafieniach pola do oddania kolejnego strzalu
		while (oUzyteczneTrafienia.size() > 0)
			{
			//wylosowanie pola do przetestowania
			int iLosowanePole = oRand.nextInt(oUzyteczneTrafienia.size());
			Position oWybranePole = oUzyteczneTrafienia.get(iLosowanePole);
			
			try
				{
				//wczytanie wspolrzednych 4 sasiadow i sprawdzenie, czy sa to pola puste, lub zawierajace statek
				for (int i = -1; i <= 1; ++i)
					for (int j = -1; j <= 1; ++j)
						if (
							oWybranePole.getX() + i >= 0 && oWybranePole.getX() + i < oStatkiPrzeciwnika.getPlansza().getWidth()
							&& oWybranePole.getY() + j >= 0 && oWybranePole.getY() + j < oStatkiPrzeciwnika.getPlansza().getHeight()
							&& (i + j == -1 || i + j == 1)
							)
							{
							if (oStatkiPrzeciwnika.getPlansza().getPole(oWybranePole.getX() + i, oWybranePole.getY() + j) != FieldTypeBoard.BOARD_FIELD_EMPTY
								&& oStatkiPrzeciwnika.getPlansza().getPole(oWybranePole.getX() + i, oWybranePole.getY() + j) != FieldTypeBoard.SHIP_BOARD
								)
								{
								} else {
                                                            Position oPrawidlowe = new Position(2);
                                                            oPrawidlowe.setX(oWybranePole.getX() + i);
                                                            oPrawidlowe.setY(oWybranePole.getY() + j);
                                                            oSasiedniePola.add(oPrawidlowe);
                                    }
							}
				
				if (bProsteLinie == true)
					{
					boolean bPionowy = false;
					boolean bPoziomy = false;
					for (int i = -1; i <= 1; ++i)
						for (int j = -1; j <= 1; ++j)
							if (
								oWybranePole.getX() + i >= 0 && oWybranePole.getX() + i < oStatkiPrzeciwnika.getPlansza().getWidth()
								&& oWybranePole.getY() + j >= 0 && oWybranePole.getY() + j < oStatkiPrzeciwnika.getPlansza().getHeight()
								&& (i + j == -1 || i + j == 1)
								)
								{
								if (oStatkiPrzeciwnika.getPlansza().getPole(oWybranePole.getX() + i, oWybranePole.getY() + j) == FieldTypeBoard.CUSTOMS_SHOT_BOARD)
									{
									if (i == 0)
										bPionowy = true;
									if (j == 0)
										bPoziomy = true;
									}
								}
					if (bPionowy == true && bPoziomy == true)
						throw new DeveloperException();
					if (bPionowy == true)
						{
						for (int i = oSasiedniePola.size() - 1; i >= 0; --i)
							if (oSasiedniePola.get(i).getX() != oWybranePole.getX())
								oSasiedniePola.remove(i);
						}
					if (bPoziomy == true)
						{
						for (int i = oSasiedniePola.size() - 1; i >= 0; --i)
							if (oSasiedniePola.get(i).getY() != oWybranePole.getY())
								oSasiedniePola.remove(i);
						}
					}
				
				if (!oSasiedniePola.isEmpty())
					{
					//sa pola prawidlowe do oddania kolejnego strzalu
					int iWylosowanySasiad = oRand.nextInt(oSasiedniePola.size());
					//oddanie strzalu na wspolrzedne weybranego pola
					boolean bStrzal;
					bStrzal = oStatkiPrzeciwnika.strzal(oSasiedniePola.get(iWylosowanySasiad).getX(), oSasiedniePola.get(iWylosowanySasiad).getY());
					if (bStrzal == true)
						{
						//zapisanie celnego strzalu w tablicy trafien
						Position oTrafienie = new Position(2);
						oTrafienie.setX( oSasiedniePola.get(iWylosowanySasiad).getX() );
						oTrafienie.setY( oSasiedniePola.get(iWylosowanySasiad).getY() );
						oUzyteczneTrafienia.add(oTrafienie);
						}
					return bStrzal;
					}
				else
					{
					//brak prawidlowych pol. usuniecie trafienia z listy i przejscie do kolejnej iteracji petli wyszukujacej
					oUzyteczneTrafienia.remove(iLosowanePole);
					}
				}
			catch (ParameterException e)
				{
				throw new DeveloperException(e);
				}
			
			}
		return strzalLosowy(oStatkiPrzeciwnika);
		}
	/**
	 * Metoda wyszukuje losowo na planszy pole do oddania strzalu, jednak jesli wylosowane pole nie zawiera statku,
	 * nastepuje ponowne losowanie w celu znalezienia lepszego pola do strzalu. Dozwolona ilosc powtorzen okresla
	 * drugi parametr.<br />
	 * 
	 * Jesli w ktorejkolwiek iteracji nastapi wylosowanie pola zawierajacego statek, strzal uznaje sie za trafiony
	 * i nie sa wykonywane kolejne iteracje petli.<br />
	 * 
	 * Jesli w ostatniej iteracji takze zostanie wylosowane pole puste,
	 * wspolrzedne tego pola zostaje uznane za wykonany strzal i jest on niecelny.
	 * 
	 * @param oStatkiPrzeciwnika Kontener statkow przeciwnika.
	 * @param iIloscPowtorzen Dozwolona ilosc powtorzen losowania pola do ostrzalu.
	 * @return Zwraca TRUE, w przypadku trafienia statku, lub FALSE, jesli strzal byl niecelny.
	 */
	protected boolean strzalWielokrotny(ShipIterator oStatkiPrzeciwnika, int iIloscPowtorzen)
		{
		try
			{
			Position oWylosowanePole = null;
			Board oPlansza = oStatkiPrzeciwnika.getPlansza();
			for (int i = 1; i <= iIloscPowtorzen; ++i)
				{
				oWylosowanePole = losujPole(oPlansza);
				if (oPlansza.getPole(oWylosowanePole.getX(), oWylosowanePole.getY()) == FieldTypeBoard.SHIP_BOARD || i == iIloscPowtorzen)
					{
					boolean bStrzal;
					bStrzal = oStatkiPrzeciwnika.strzal(oWylosowanePole.getX(), oWylosowanePole.getY());
					if (bStrzal == true)
						{
						//zapisanie celnego strzalu w tablicy trafien
						Position oTrafienie = new Position(2);
						oTrafienie.setX( oWylosowanePole.getX() );
						oTrafienie.setY( oWylosowanePole.getY() );
						oUzyteczneTrafienia.add(oTrafienie);
						}
					return bStrzal;
					}
				}
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		//petla musi zwrocic strzal. skoro doszlo tutaj - wywal wyjatek
		throw new DeveloperException();
		}
	/**
	 * Metoda wybiera losowe pole dostepne do ostrzelania na planszy przeciwnika i zwraca obiekt typu Position zawierajacy te wspolrzedne.
	 * 
	 * @param oPlanszaPrzeciwnika Board przeciwnika, na ktora ma byc oddany strzal.
	 * @return Wspolrzedne wylosowanego pola do ostrzelania.
	 */
	private Position losujPole(Board oPlanszaPrzeciwnika)
		{
		try
			{
			Position oWylosowanePole = new Position(2);
			int iWylosowanePole = oRand.nextInt( oPlanszaPrzeciwnika.getUnknownQuantity() ) + 1;
			//obliczenie x i y dla wylosowanego pola
			int iX = 0;
			int iY = 0;
			while (iWylosowanePole > 0)
				{
				if (oPlanszaPrzeciwnika.getPole(iX, iY) == FieldTypeBoard.BOARD_FIELD_EMPTY || oPlanszaPrzeciwnika.getPole(iX, iY) == FieldTypeBoard.SHIP_BOARD)
					--iWylosowanePole;
				if (iWylosowanePole > 0)
					{
					++iX;
					if (iX == oPlanszaPrzeciwnika.getWidth())
						{
						++iY;
						iX = 0;
						}
					}
				}
			oWylosowanePole.setX(iX);
			oWylosowanePole.setY(iY);
			return oWylosowanePole;
			}
		catch (ParameterException e)
			{
			throw new DeveloperException(e);
			}
		}
	}
