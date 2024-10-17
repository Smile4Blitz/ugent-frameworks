package be.ugent.reeks1;

public final class BlogPost {
    private final Integer id;
    private final String title;
    private final String content;

    public BlogPost(Integer id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
