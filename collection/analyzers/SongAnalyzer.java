package collection.analyzers;

import collection.AlbumInfo;
import collection.SongInfo;
import framework.ApolloFramework;

public abstract class SongAnalyzer extends Analyzer<AlbumInfo> {
	public SongAnalyzer(String name) {
		super(name);
	}

	public void init() {
		this.iterator_ = ApolloFramework.instance().getProject()
				.getCollectionInfo().getCollection().values().iterator();
		this.length_ = ApolloFramework.instance().getProject()
				.getCollectionInfo().getSongCount();
		super.init();
	}

	protected void analyze(AlbumInfo next) {
		for (SongInfo s : next.getSongs()) {
			analyzeSong(s);
			this.progress_++;
		}
	}

	protected abstract void analyzeSong(SongInfo next);
}
