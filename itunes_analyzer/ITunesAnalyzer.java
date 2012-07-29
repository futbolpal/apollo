package itunes_analyzer;

import main.Analyzer;
import collection_info2.CollectionInfo;
import collection_info2.TagInfo;
import de.axelwernicke.mypod.ipod.ITunesDB;

public class ITunesAnalyzer extends Analyzer {
	public ITunesAnalyzer(CollectionInfo collection) {
		super(collection);
	}

	public void run() {
		ITunesDB db = new ITunesDB();
		while (rs.next()) {
			TagInfo info = new TagInfo();
			info.artist_ = rs.getString("artist.name");
			info.album_ = rs.getString("album.name");
			info.disc_ = rs.getInt("discnumber");
			info.track_ = rs.getInt("track");
			info.title_ = rs.getString("title");
			collection_.organize(info);
		}
	}
}