/**
 * 
 */
package collection_info;

import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;

public class AlbumInfo {
	private ArrayList<SongInfo> songs_;

	private File disc_directory_;

	private int disc_number_;

	private String artist_;

	private String album_;

	private boolean valid_;

	public AlbumInfo(File disc_directory) {
		this.valid_ = true;
		this.songs_ = new ArrayList<SongInfo>();
		this.disc_directory_ = disc_directory;
		String disc = this.disc_directory_.getParentFile().getName();
		if (disc.startsWith("Disc ")) {
			this.disc_number_ = Integer.parseInt(disc.split(" ")[1]);
			this.album_ = this.disc_directory_.getParentFile().getName();
			this.artist_ = this.disc_directory_.getParentFile().getParentFile()
					.getName();
		} else {
			this.disc_number_ = 1;
			this.album_ = this.disc_directory_.getName();
			this.artist_ = this.disc_directory_.getParentFile().getName();
		}
	}

	private boolean checkSize() {
		if (songs_.isEmpty()) {
			return false;
		}
		return true;
	}

	private boolean checkContinuity() {
		int size = this.getTrackCount();
		BitSet tracker = new BitSet();
		for (SongInfo s : this.songs_) {
			int t = s.getTrack() - 1;
			if (t < 0 || t > this.getTrackCount() - 1) {
				return false;
			}
			tracker.set(t, true);
		}
		return tracker.cardinality() == size;
	}

	public void addSongInfo(SongInfo info) {
		if (!info.isValid()) {
			this.valid_ = false;
		}
		this.songs_.add(info);
	}

	public File getFile() {
		return disc_directory_;
	}

	public String getAlbum() {
		return album_;
	}

	public String getArtist() {
		return artist_;
	}

	public int getDiscNumber() {
		return disc_number_;
	}

	public int getTrackCount() {
		return this.songs_.size();
	}

	public boolean isComplete() {
		if (!checkSize())
			return false;
		if (!checkContinuity())
			return false;
		return true;
	}

	public boolean isValid() {
		return this.valid_;
	}

	public ArrayList<SongInfo> getSongs() {
		return songs_;
	}
}