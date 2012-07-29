package collection.analyzers;

import collection.AlbumInfo;
import framework.ApolloFramework;

public abstract class AlbumAnalyzer extends Analyzer<AlbumInfo> {
	public AlbumAnalyzer(String name) {
		super(name);
	}

	public void init() {
		this.iterator_ = ApolloFramework.instance().getProject()
				.getCollectionInfo().getCollection().values().iterator();
		this.length_ = ApolloFramework.instance().getProject()
				.getCollectionInfo().getAlbumCount();
		super.init();
	}

	protected void analyze(AlbumInfo next) {
		analyzeAlbum(next);
		this.progress_++;
	}
	
	protected abstract void analyzeAlbum(AlbumInfo next);
}
