package collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import framework.ApolloFramework;

public class CollectionInfo implements Serializable {
	private LinkedHashMap<String, AlbumInfo> collection_;

	private int song_count_;

	private long listening_time_;

	private ArrayList<SongInfo> songs_wo_year_;

	private ArrayList<SongInfo> songs_wo_artist_;

	private ArrayList<SongInfo> songs_wo_genre_;

	private ArrayList<String> artists_;

	public CollectionInfo() {
		listening_time_ = 0;
		song_count_ = 0;
		songs_wo_year_ = new ArrayList<SongInfo>();
		songs_wo_artist_ = new ArrayList<SongInfo>();
		songs_wo_genre_ = new ArrayList<SongInfo>();
		artists_ = new ArrayList<String>();
		collection_ = new LinkedHashMap<String, AlbumInfo>();
	}

	public LinkedHashMap<String, AlbumInfo> getCollection() {
		return collection_;
	}

	public int getAlbumCount() {
		return collection_.size();
	}

	public AlbumInfo getAlbum(String album, boolean similar) {
		if (similar) {
			ArrayList<AlbumInfo> albums = new ArrayList<AlbumInfo>(collection_
					.values());
			int index = albums.indexOf(new AlbumInfo(album));
			if (index < 0)
				return null;
			return albums.get(index);
		}
		return collection_.get(album);
	}

	public boolean containsAlbum(String album) {
		return collection_.containsKey(album);
	}

	public int getSongCount() {
		return this.song_count_;
	}

	public long getListeningTimeSeconds() {
		return this.listening_time_;
	}

	public int getSongsWithoutYearCount() {
		return this.songs_wo_year_.size();
	}

	public int getSongsWithoutArtistCount() {
		return this.songs_wo_artist_.size();
	}

	public int getSongsWithoutGenreCount() {
		return this.songs_wo_genre_.size();
	}

	public int getArtistCount() {
		return artists_.size();
	}

	public ArrayList<SongInfo> getSongsWithoutYear() {
		return this.songs_wo_year_;
	}

	public ArrayList<SongInfo> getSongsWithoutArtist() {
		return this.songs_wo_artist_;
	}

	public ArrayList<SongInfo> getSongsWithoutGenre() {
		return this.songs_wo_genre_;
	}

	public ArrayList<String> getArtists() {
		return this.artists_;
	}

	public boolean removeSong(SongInfo s) {
		String album_name = s.getAlbum();
		if (album_name != null) {
			AlbumInfo ai = this.collection_.get(album_name);
			ApolloFramework.instance().getProject().fireCollectionChanged();
			return ai.getSongs().remove(s);
		}
		return false;
	}

	public void organize(SongInfo song) {
		if (!this.containsAlbum(song.getAlbum())) {
			collection_.put(song.getAlbum(), new AlbumInfo(song.getAlbum()));
		}
		AlbumInfo album = this.collection_.get(song.getAlbum());
		album.addSong(song);

		this.song_count_++;
		this.listening_time_ += song.getLengthSeconds();
		if (song.getYear() == 0) {
			this.songs_wo_year_.add(song);
		}
		if (song.getArtist() == null || song.getArtist().equals("")) {
			this.songs_wo_artist_.add(song);
		} else if (!artists_.contains(song.getArtist())) {
			artists_.add(song.getArtist());
		}
		if (song.getGenre() == null || song.getGenre().equals("")) {
			this.songs_wo_genre_.add(song);
		}
	}

	public void reset() {
		collection_.clear();
		listening_time_ = 0;
		song_count_ = 0;
		songs_wo_artist_.clear();
		songs_wo_year_.clear();
		songs_wo_genre_.clear();
		artists_.clear();
		ApolloFramework.instance().getProject().fireCollectionChanged();
	}
}
