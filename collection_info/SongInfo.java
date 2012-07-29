package collection_info;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import file_analyzer.FileAnalyzerSettings;

public class SongInfo {

	private File song_file_;

	private String title_;

	private int track_;

	private int disc_;

	private String album_;

	private String artist_;

	private String format_;

	private String root_;

	private boolean valid_;

	public SongInfo(File song_file) {
		this.song_file_ = song_file;
		this.valid_ = false;
		this.title_ = song_file.getName().substring(0,
				song_file.getName().lastIndexOf("."));
		this.format_ = song_file_.getName().substring(
				song_file_.getName().lastIndexOf("."));
		this.track_ = -1;
		this.disc_ = -1;
		Iterator<String> itr = FileAnalyzerSettings.FORMATS.iterator();
		// System.out.println(Settings.FORMATS.size());
		while (!this.valid_ && itr.hasNext()) {
			try {
				String format = itr.next();
				Parser a = new Parser(format);
				Hashtable<String, Object> data = a.parse(song_file
						.getAbsolutePath());
				reflect(data);
				this.valid_ = true;
				// System.out.println("good : " + song_file);

			} catch (Exception e) {
				// System.out.println("error: " + song_file);
			}
		}
	}

	public File getFile() {
		return song_file_;
	}

	public boolean isValid() {
		return valid_;
	}

	public int getDisc() {
		return disc_;
	}

	public int getTrack() {
		return track_;
	}

	public String getTitle() {
		return title_;
	}

	private void reflect(Hashtable<String, Object> data)
			throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {

		Set<Entry<String, Object>> entries = data.entrySet();
		Iterator<Entry<String, Object>> itr = entries.iterator();
		while (itr.hasNext()) {
			Entry<String, Object> e = itr.next();
			SongInfo.class.getDeclaredField(e.getKey() + "_");
			Field f = SongInfo.class.getDeclaredField(e.getKey() + "_");
			f.set(this, e.getValue());
		}
	}
}
