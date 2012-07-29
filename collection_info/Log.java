package collection_info;

import java.io.File;
import java.util.ArrayList;


public class Log {

	private ArrayList<AlbumInfo> complete_;

	private ArrayList<AlbumInfo> incomplete_;

	private ArrayList<AlbumInfo> empty_;

	private ArrayList<AlbumInfo> invalid_;

	private int discs_processed_;

	private int disc_count_;

	private int albums_processed_;

	private int album_count_;

	private int artists_processed_;

	private int artist_count_;

	private File current_album_;

	private boolean done_;

	public Log() {
		done_ = false;
		complete_ = new ArrayList<AlbumInfo>();
		incomplete_ = new ArrayList<AlbumInfo>();
		empty_ = new ArrayList<AlbumInfo>();
		invalid_ = new ArrayList<AlbumInfo>();

		artists_processed_ = 0;
		artist_count_ = 0;
		albums_processed_ = 0;
		album_count_ = 0;
		discs_processed_ = 0;
		disc_count_ = 0;
	}

	public synchronized void insertAlbum(AlbumInfo album) {
		if (album.isComplete()) {
			complete_.add(album);
		} else if (album.getTrackCount() == 0) {
			empty_.add(album);
		} else if (!album.isValid()) {
			invalid_.add(album);
		} else {
			incomplete_.add(album);
		}
	}

	public void setArtistsProcessed(int p) {
		artists_processed_ = p;
	}

	public void setAlbumsProcessed(int p) {
		albums_processed_ = p;
	}

	public void setDiscsProcessed(int p) {
		discs_processed_ = p;
	}

	public void setDiscCount(int c) {
		disc_count_ = c;
	}

	public void setAlbumCount(int c) {
		album_count_ = c;
	}

	public void setArtistCount(int c) {
		artist_count_ = c;
	}

	public void setCurrentAlbum(File f) {
		current_album_ = f;
	}

	public int getDiscCount() {
		return disc_count_;
	}

	public int getAlbumCount() {
		return album_count_;
	}

	public int getArtistCount() {
		return artist_count_;
	}

	public int getArtistsProcessed() {
		return artists_processed_;
	}

	public int getAlbumsProcessed() {
		return albums_processed_;
	}

	public int getDiscsProcessed() {
		return discs_processed_;
	}

	public File getCurrentAlbum() {
		return current_album_;
	}

	public synchronized int getOverallAlbumsProcessed() {
		return complete_.size() + invalid_.size() + empty_.size()
				+ incomplete_.size();
	}

	public synchronized int getCompleteAlbumsProcessed() {
		return complete_.size();
	}

	public synchronized int getIncompleteAlbumsProcessed() {
		return incomplete_.size();
	}

	public synchronized int getEmptyAlbumsProcessed() {
		return empty_.size();
	}

	public synchronized int getInvalidAlbumsProcessed() {
		return invalid_.size();
	}

	public void done() {
		done_ = true;
	}

	public boolean isDone() {
		return done_;
	}

	public ArrayList<AlbumInfo> getCompleteAlbums() {
		return complete_;
	}

	public ArrayList<AlbumInfo> getIncompleteAlbums() {
		return incomplete_;
	}

	public ArrayList<AlbumInfo> getEmptyAlbums() {
		return empty_;
	}

	public ArrayList<AlbumInfo> getInvalidAlbums() {
		return invalid_;
	}

}
