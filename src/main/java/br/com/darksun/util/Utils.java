package br.com.darksun.util;

import br.com.darksun.model.Song;
import br.com.darksun.model.Vote;

public class Utils {
	public static String formatSingerAndSong( Vote vote ) {
		Song song = vote.getItsFor( );
		return formatSingerAndSong( song );
	}

	public static String formatSingerAndSong( Song song ) {
		return new StringBuilder( song.getSinger( ).getName( ) ).append( ": " )
																.append( song.getArtist( ) )
																.append( " - " )
																.append( song.getName( ) )
																.toString( );
	}
}
