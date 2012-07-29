package file_analyzer;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import collection_info2.CollectionInfo;
import collection_info2.SongInfo;
import framework.Analyzer;

public class FileAnalyzer extends Analyzer {
	private File root_;

	public FileAnalyzer(File root, CollectionInfo collection) {
		super(collection);
		root_ = root;
	}

	public void run() {
		FileFilter dir_filter = new FileFilter() {
			public boolean accept(File f) {
				return f.isDirectory();
			}
		};
		File[] artists = root_.listFiles(dir_filter);
		for (int i = 0; i < artists.length; i++) {
			File artist = artists[i];
			File[] albums = artist.listFiles(dir_filter);
			for (int j = 0; j < albums.length; j++) {
				File album = albums[j];
				File[] discs = album.listFiles(dir_filter);
				if (discs.length > 0) {
					for (int k = 0; k < discs.length; k++) {
						if (this.isInterrupted())
							return;
						collectSongs(discs[k]);
					}
				} else {
					if (this.isInterrupted())
						return;
					collectSongs(album);
				}
			}
		}
		iterator_.setBuilding(true);
	}

	protected void collectSongs(File disc_dir) {
		File[] songs = disc_dir.listFiles();
		for (File song : songs) {
			SongInfo info = new SongInfo();
			boolean valid = false;
			Iterator<String> itr = FileAnalyzerSettings.FORMATS.iterator();
			while (!valid && itr.hasNext()) {
				String format = itr.next();
				try {
					Parser p = new Parser(format);
					Hashtable<String, Object> data = p.parse(song
							.getAbsolutePath());
					// reflect
					Iterator<Entry<String, Object>> data_itr = data.entrySet()
							.iterator();
					while (data_itr.hasNext()) {
						Entry<String, Object> e = data_itr.next();
						Field f = SongInfo.class.getDeclaredField(e.getKey()
								+ "_");
						f.set(info, e.getValue());
					}
					iterator_.organize(info);
					valid = true;
				} catch (Exception e) {
					// System.err.println("error: " + format);
				}
			}
		}
	}
}