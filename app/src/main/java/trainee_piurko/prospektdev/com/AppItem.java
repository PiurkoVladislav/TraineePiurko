package trainee_piurko.prospektdev.com;

public class AppItem {
    private String caption;
    private String id;
    private String url;
    private String dates;
    private String userName;
    private String location;
    private String description;
    private String mUrlNormal;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrlNormal(String urlNormal) {
        mUrlNormal = urlNormal;
    }

    public String getUrlNormal() {
        return mUrlNormal;
    }
}
