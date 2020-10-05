package pers.Derwh.getHostSongList.model;

import java.util.List;

public class Song {

    private String id;
    private String name;
    //歌曲歌手
    private List<User> singers;
    //所属专辑
    private Album album;
    //文件资源地址
    private String sourceUrl;
    //评论
    private List<Comment> comments;
    //热门评论
    private List<Comment> hotComments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getSingers() {
        return singers;
    }

    public void setSingers(List<User> singers) {
        this.singers = singers;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Comment> getHotComments() {
        return hotComments;
    }

    public void setHotComments(List<Comment> hotComments) {
        this.hotComments = hotComments;
    }
}
