package collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import framework.SimilarComparator;

public class AlbumInfo implements Serializable {
	private ArrayList<SongInfo> songs_;
	private String name_;

	public AlbumInfo(String name) {
		songs_ = new ArrayList<SongInfo>();
		name_ = name;
	}

	public String getName() {
		return name_;
	}

	public void addSong(SongInfo s) {
		songs_.add(s);
	}

	public Collection<SongInfo> getSongs() {
		return songs_;
	}

	public Collection<SongInfo> getSongsByDisc(int disc) {
		Collection<SongInfo> retval = new ArrayList<SongInfo>();
		for (SongInfo s : songs_) {
			if (s.getDisc() == disc)
				retval.add(s);
		}
		return retval;
	}

	public String getAlbumArtist() {
		String artist = null;
		for (SongInfo s : songs_) {
			if (artist == null) {
				artist = s.getArtist();
			} else if (!artist.equals(s.getArtist())) {
				return "Various Artists";
			}
		}
		return artist;
	}

	public int getAlbumYear() {
		int year = 0;
		for (SongInfo s : songs_) {
			if (year == 0) {
				year = s.getYear();
			} else if (year != s.getYear())
				return 0;
		}
		return year;
	}

	public void setAlbumYear(int year) {
		for (SongInfo s : songs_) {
			s.setYear(year);
		}
	}

	public void setAlbumArtist(String artist) {
		for (SongInfo s : songs_) {
			s.setArtist(artist);
		}
	}

	public Collection<SongInfo> getTracksByDisc(int i) {
		ArrayList<SongInfo> songs = new ArrayList<SongInfo>();
		for (SongInfo s : songs_) {
			if (s.getDisc() == i)
				songs.add(s);
		}
		return songs;
	}

	public int getDiscCount() {
		Hashtable<Integer, SongInfo> disc_log = new Hashtable<Integer, SongInfo>();
		for (SongInfo s : songs_) {
			disc_log.put(s.getDisc(), s);
		}
		return disc_log.size();
	}

	public boolean equals(Object o) {
		if (o instanceof String) {
			String s = (String) o;
			return SimilarComparator.isSimilar(name_, s);
		} else if (o instanceof AlbumInfo) {
			String s = ((AlbumInfo) o).getName();
			return SimilarComparator.isSimilar(name_, s);
		}
		return false;
	}
}
