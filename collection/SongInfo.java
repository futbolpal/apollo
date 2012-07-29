package collection;

import java.io.Serializable;

import framework.SimilarComparator;

public class SongInfo implements Serializable, Cloneable {

	private String primarykey_;

	private String title_;

	private String artist_;

	private int year_;

	private int length_s_;

	private int track_;

	private int disc_;

	private String album_;

	private String format_;

	private String genre_;

	private String album_cover_location_;

	private String location_;

	private int playcount_;

	public SongInfo() {
	}

	public SongInfo(String title) {
		this.title_ = title;
	}

	public int getPlayCount() {
		return this.playcount_;
	}

	public String getLocation() {
		return this.location_;
	}

	public String getGenre() {
		return this.genre_;
	}

	public String getArtist() {
		return this.artist_;
	}

	public String getTitle() {
		return this.title_;
	}

	public String getAlbum() {
		return this.album_;
	}

	public int getYear() {
		return this.year_;
	}

	public int getTrack() {
		return this.track_;
	}

	public int getDisc() {
		return this.disc_;
	}

	public int getLengthSeconds() {
		return this.length_s_;
	}

	public String getPrimaryKey() {
		return this.primarykey_;
	}

	public String getFormat() {
		return this.format_;
	}

	public String getAlbumCoverLocation() {
		return this.album_cover_location_;
	}

	public void setPrimaryKey(String k) {
		this.primarykey_ = k;
	}

	public void setAlbumCoverLocation(String l) {
		this.album_cover_location_ = l;
	}

	public void setArtist(String artist) {
		this.artist_ = artist;
	}

	public void setTitle(String title) {
		this.title_ = title;
	}

	public void setAlbum(String album) {
		this.album_ = album;
	}

	public void setYear(int y) {
		this.year_ = y;
	}

	public void setTrack(int t) {
		this.track_ = t;
	}

	public void setDisc(int d) {
		this.disc_ = d;
	}

	public void setFormat(String f) {
		this.format_ = f;
	}

	public void setLength(int s) {
		this.length_s_ = s;
	}

	public void setPlayCount(int p) {
		this.playcount_ = p;
	}

	public void setGenre(String genre) {
		this.genre_ = genre;
	}

	public void setLocation(String location) {
		this.location_ = location;
	}

	public SongInfo clone() {
		SongInfo c = new SongInfo();
		c.primarykey_ = this.primarykey_;
		c.title_ = this.title_;
		c.artist_ = this.artist_;
		c.year_ = this.year_;
		c.length_s_ = this.length_s_;
		c.track_ = this.track_;
		c.disc_ = this.disc_;
		c.album_ = this.album_;
		c.format_ = this.format_;
		c.genre_ = this.genre_;
		c.album_cover_location_ = this.album_cover_location_;
		c.location_ = this.location_;
		c.playcount_ = this.playcount_;
		return c;
	}

	public boolean equals(Object o) {
		if (o instanceof String) {
			String s = (String) o;
			return SimilarComparator.isSimilar(title_, s);
		} else if (o instanceof AlbumInfo) {
			String s = ((SongInfo) o).getTitle();
			return SimilarComparator.isSimilar(title_, s);
		}
		return false;

	}
	
	public String toString()
	{
		return this.artist_+": "+this.title_+" (from the album " + this.album_ + ")";
	}
}
