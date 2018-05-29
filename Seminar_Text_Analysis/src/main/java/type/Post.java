package type;

public class Post {
	private Post parent;
	private String tag;
	private String message;
	private int upvotes;
	private String url;
	private int level;
	
	public Post() {
		
	}
	
	public Post(Post parent) {
		setParent(parent);
	}
	
	public Post(Post parent, String tag, String message, int upvotes, String url, int level) {
		setParent(parent);
		setTag(tag);
		setMessage(message);
		setUpvotes(upvotes);
		setUrl(url);
		setLevel(level);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Post getParent() {
		return parent;
	}

	public void setParent(Post parent) {
		this.parent = parent;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getUpvotes() {
		return upvotes;
	}

	public void setUpvotes(int upvotes) {
		this.upvotes = upvotes;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public Post getMe() {
		return this;
	}
}
