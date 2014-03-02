package model.audio;

abstract public class Genres {

	/**
	 * list with all ID3v2 genres
	 */
	private static String[] genres = { "Blues", "Classic Rock", "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz", "Metal", "New Age", "Oldies", "Other", "Pop",
			"R&B", "Rap", "Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska", "Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient", "Trip-Hop", "Vocal",
			"Jazz+Funk", "Fusion", "Trance", "Classical", "Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel", "Noise", "Alt Rock", "Bass", "Soul", "Punk", "Space",
			"Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic", "Gothic", "Darkwave", "Techno-Industrial", "Electronic", "Pop-Folk", "Eurodance", "Dream",
			"Southern Rock", "Comedy", "Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle", "Native American", "Cabaret", "New Wave", "Psychedelic", "Rave",
			"Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll", "Hard Rock", "Folk", "Folk/Rock", "National Folk",
			"Swing", "Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass", "Avantgarde", "Gothic Rock", "Progressive Rock", "Psychedelic Rock", "Symphonic Rock",
			"Slow Rock", "Big Band", "Chorus", "Easy Listening", "Acoustic", "Humour", "Speech", "Chanson", "Opera", "Chamber Music", "Sonata", "Symphony", "Booty Bass", "Primus",
			"Porn Groove", "Satire/Parody", "Slow Jam", "Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad", "Rhythmic Soul", "Freestyle", "Duet", "Punk Rock",
			"Drum Solo", "Acapella", "Euro-House", "Dance Hall", "Goa", "Drum & Bass", "Club-House", "Hardcore", "Terror", "Indie", "BritPop", "Negerpunk", "Polsk Punk", "Beat",
			"Christian Gangsta", "Heavy Metal", "Thrash Metal", "Anime", "JPop", "Synthpop" };

	/**
	 * index for unknown
	 */
	public final static int UNKNOWN = 149;

	public final static String UNKNOWN_DESCRIPTION = "unknown";

	/**
	 * gets the genre depending on the given ID
	 * 
	 * @param id
	 *            given id
	 * 
	 * @return the genre description
	 */
	public static String getGenre(int id) {
		if (id < 0 || id > 148)
			return UNKNOWN_DESCRIPTION;

		else
			return genres[id];
	}

	/**
	 * gets the ID depending on the given genre
	 * 
	 * @param genre
	 *            genre description
	 * 
	 * @return the genre ID, if no description is found unknown ID
	 */
	public static int getGenre(String genre) {
		for (int i = 0; i < genres.length; i++) {
			if (genres[i].equalsIgnoreCase(genre))
				return i + 1;
		}

		return UNKNOWN;
	}

	/**
	 * gets the ID depending on the given genre. The genre will be compared by
	 * removing all spaces and & and - and ignore cases e.g. New Age = 9 and newage =
	 * 9
	 * 
	 * @param genre
	 *            given genre
	 * 
	 * @return ID of the genre
	 */
	public static int getGenreLoose(String genre) {
		for (int i = 0; i < genres.length; i++) {
			if (genres[i].replace(" ", "").replace("&", "").replace("-", "").equalsIgnoreCase(genre.replace(" ", "").replace("&", "").replace("-", "")))
				return i;
		}

		return UNKNOWN;
	}

	/**
	 * gets all genres in an array
	 * 
	 * @return the genre array
	 */
	public static String[] getGenres() {
		return genres;
	}
}