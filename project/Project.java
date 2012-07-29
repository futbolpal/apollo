package project;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import system.CollectionBuilder;
import system.CollectionValidator;
import collection.CollectionInfo;
import framework.ApolloFramework;
import framework.FrameworkListener;

public class Project implements Serializable, FrameworkListener {

	private CollectionInfo collection_;

	private CollectionBuilder builder_;

	private CollectionValidator validator_;

	private ArrayList<ProjectListener> listeners_;

	private File file_;

	private boolean saved_;

	public Project() {
		ApolloFramework.instance().addListener(this);
		this.collection_ = new CollectionInfo();
		this.listeners_ = new ArrayList<ProjectListener>();
	}

	public void addListener(ProjectListener l) {
		this.listeners_.add(l);
	}

	public CollectionInfo getCollectionInfo() {
		return this.collection_;
	}

	public CollectionBuilder getCollectionBuilder() {
		return this.builder_;
	}

	public CollectionValidator getCollectionValidator() {
		return this.validator_;
	}

	public void setCollectionInfo(CollectionInfo info) {
		this.collection_ = info;
		this.fireCollectionChanged();
	}

	public void setCollectionValidator(CollectionValidator v) {
		this.validator_ = v;
		this.fireValidatorChanged();
	}

	public void setCollectionBuilder(CollectionBuilder b) {
		this.builder_ = b;
		this.fireBuilderChanged();
	}

	public String getFilename() {
		return this.file_.getName();
	}

	public void save() {
		if (saved_)
			return;
		if(file_ == null)
		{
			//save as routine
		}
		saved_ = true;
	}

	public void load() {

	}

	public void fireCollectionChanged() {
		for (ProjectListener p : listeners_) {
			p.projectCollectionChanged();
		}
	}

	public void fireValidatorChanged() {
		for (ProjectListener p : listeners_) {
			p.projectValidatorChanged();
		}
	}

	public void fireBuilderChanged() {
		for (ProjectListener p : listeners_) {
			p.projectBuilderChanged();
		}
	}

	public void frameworkOpened() {
		// TODO Auto-generated method stub

	}

	public void frameworkOpening() {
		// TODO Auto-generated method stub

	}
}
