package collection_info;

import java.io.File;
import java.io.FileFilter;



public class Analyzer extends Thread {
	private Log log_;

	private File root_;

	private boolean init_;

	public void init(File root, Log log) {
		root_ = root;
		log_ = log;
		init_ = true;
	}

	public void kill() throws InterruptedException {
		this.interrupt();
		this.init_ = false;
	}

	public void run() {
		if (!init_) {
			throw new IllegalStateException();
		}
		FileFilter dir_filter = new FileFilter() {
			public boolean accept(File f) {
				return f.isDirectory();
			}
		};
		File[] artists = root_.listFiles(dir_filter);
		int artist_count = artists.length;
		this.log_.setArtistCount(artist_count);
		for (int i = 0; i < artist_count; i++) {
			File artist = artists[i];
			File[] albums = artist.listFiles(dir_filter);
			int album_count = albums.length;
			this.log_.setAlbumCount(album_count);
			for (int j = 0; j < album_count; j++) {
				File album = albums[j];
				this.log_.setCurrentAlbum(album);
				File[] discs = album.listFiles(dir_filter);
				int disc_count = discs.length;
				if (disc_count > 0) {
					for (int k = 0; k < disc_count; k++) {
						if (this.isInterrupted())
							return;
						File disc = discs[k];
						AlbumInfo album_info = collectAlbumInfo(disc);
						this.log_.insertAlbum(album_info);
						this.log_.setDiscsProcessed(k + 1);

					}
				} else {
					if (this.isInterrupted())
						return;
					AlbumInfo album_info = collectAlbumInfo(album);
					this.log_.insertAlbum(album_info);

				}
				this.log_.setDiscsProcessed(0);
				this.log_.setAlbumsProcessed(j + 1);
			}
			this.log_.setAlbumsProcessed(0);
			this.log_.setArtistsProcessed(i + 1);
		}
		this.log_.done();
	}

	private AlbumInfo collectAlbumInfo(File disc_dir) {
		FileFilter song_filter = new FileFilter() {
			public boolean accept(File f) {
				return f.getName().endsWith(".m4a")
						|| f.getName().endsWith(".mp3")
						|| f.getName().endsWith(".MP3")
						|| f.getName().endsWith(".wma");
			}
		};
		File[] songs = disc_dir.listFiles(song_filter);
		AlbumInfo album_info = new AlbumInfo(disc_dir);
		for (File song : songs) {
			album_info.addSongInfo(new SongInfo(song));
		}

		return album_info;
	}
}