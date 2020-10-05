package pers.Derwh.getHostSongList.Service.impl;

import com.alibaba.fastjson.JSON;
import com.sun.org.apache.bcel.internal.generic.ANEWARRAY;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import pers.Derwh.getHostSongList.Service.SongCrawlerService;
import pers.Derwh.getHostSongList.model.*;
import pers.Derwh.getHostSongList.util.WordCloudUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SongCrawlerServiceImpl implements SongCrawlerService {

    //存储抓取到的歌单数据
    private Map<String, Artist> ARTISTS;
    //创建OkHttpClient对象
    private OkHttpClient okHttpClient;
    //歌单API
    private static final String ARTIST_API = "http://neteaseapi.youkeda.com:3000/artists?id=";
    //歌曲API
    private static final String SONGS_API = "http://neteaseapi.youkeda.com:3000/song/detail?ids=";
    //评论API
    private static final String COMMENT_API = "http://neteaseapi.youkeda.com:3000/comment/music?id=";
    //文件资源API
    private static final String SOURCE_API = "http://neteaseapi.youkeda.com:3000/song/url?id=";

    /**
     * 开始方法
     * @param artistId
     */
    public void start(String artistId) {
        init();

        initArtistHotSongs(artistId);
        assembleSongDetail(artistId);
        assembleSongComment(artistId);
        assembleSongUrl(artistId);
        buildWordCloud(artistId);
    }

    /**
     * 初始化
     */
    private void init(){
        ARTISTS = new HashMap<>();
        okHttpClient = new OkHttpClient();
    }

    /**
     * Artist对象装配初始化
     * @param artistId
     */
    private void initArtistHotSongs(String artistId) {
        //抓取数据
        Map singerData = getContent(getUrl(ARTIST_API, artistId));
        //获取构建的artist对象
        Artist artist = buildArtist(singerData);
        //获取构建的歌曲集合
        List<Song> songList = buildSongs(singerData);
        //将歌曲集合装配进artist中
        artist.setSongList(songList);
        //将装配完毕的artist放入集合中
        ARTISTS.put(artist.getId(), artist);
    }

    /**
     * 根据歌单id在本地歌单库中返回相应歌单
     * @param artistId
     * @return
     */
    public Artist getArtist(String artistId) {
        return ARTISTS.get(artistId);
    }

    /**
     * 根据歌单id和歌曲id返回相应的歌曲
     * @param artistId
     * @param songId
     * @return
     */
    public Song getSong(String artistId, String songId) {
        Artist artist = getArtist(artistId);

        for (Song song : artist.getSongList()) {
            if (song.getId().equals(songId)) {
                return song;
            }
        }
        return null;
    }

    /**
     * 获取url
     * @param Api
     * @param id
     * @return
     */
    private String getUrl(String Api, String id) {
        return Api + id;
    }

    /**
     * 通过url获取内容
     * @param url
     * @return
     */
    private Map getContent(String url) {
        Request request = new Request.Builder().url(url).build();

        String result = null;

        try {
            result = okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            System.out.println("request " + url + " error.");
            e.printStackTrace();
        }

        //反序列化为Map对象
        Map resultObj = JSON.parseObject(result, Map.class);

        return resultObj;
    }

    /**
     * 构建artist对象
     * @param singerData
     * @return
     */
    private Artist buildArtist(Map singerData) {
        Map artistData = (Map) singerData.get("artist");

        Artist artist = new Artist();
        artist.setId(artistData.get("id").toString());
        artist.setName(artistData.get("name").toString());
        artist.setAlias((List) artistData.get("alias"));
        artist.setBriefDesc(artistData.get("briefDesc").toString());
        artist.setImg1v1Url(artistData.get("img1v1Url").toString());
        artist.setPicUrl(artistData.get("picUrl").toString());

        return artist;
    }

    /**
     * 构建歌曲集合
     * @param singerData
     * @return
     */
    private List<Song> buildSongs(Map singerData) {
        List songsData =(List) singerData.get("hotSongs");
        List<Song> SONGS = new ArrayList<Song>();

        for (int i = 0; i < songsData.size(); i++) {
            Map songData = (Map) songsData.get(i);

            Song song = new Song();
            song.setId(songData.get("id").toString());
            song.setName(songData.get("name").toString());

            SONGS.add(song);
        }
        return SONGS;
    }

    /**
     * 获取歌曲id集合并拼接
     * @param songList
     * @return
     */
    private String buildManyIdParam(List<Song> songList) {
        List<String> ids = new ArrayList<String>();
        for (Song song : songList) {
            ids.add(song.getId());
        }
        String songIds = String.join(",", ids);

        return songIds;
    }

    /**
     * 装配歌曲详情
     * @param artistId
     */
    private void assembleSongDetail(String artistId) {
        List<Song> songList = ARTISTS.get(artistId).getSongList();

        String songIds = buildManyIdParam(songList);
        //获取歌曲集合
        Map content = getContent(getUrl(SONGS_API, songIds));
        List<Map> songsList = (List<Map>) content.get("songs");

        //将List集合中的songs转换到Map中
        Map<String, Map> songsMap = new HashMap<>();
        for (Map song : songsList) {
            songsMap.put(song.get("id").toString(), song);
        }

        for (Song song : songList) {
            List<Map> ars =(List<Map>) songsMap.get(song.getId()).get("ar");

            //装配ar到singers
            List<User> singers = new ArrayList<>();
            for (Map ar : ars) {
                User singer = new User();
                singer.setId(ar.get("id").toString());
                singer.setNikeName(ar.get("name").toString());
                singers.add(singer);
            }
            song.setSingers(singers);

            //装配al到album
            Map al = (Map) songsMap.get(song.getId()).get("al");
            Album album = new Album();
            album.setId(al.get("id").toString());
            album.setName(al.get("name").toString());
            album.setPicUrl(al.get("picUrl").toString());
            song.setAlbum(album);
        }

        //ARTISTS.get(artistId).setSongList(songList);
    }

    /**
     * 装配comment
     * @param commentObj
     * @return
     */
    private List<Comment> getComments(List<Map> commentObj) {
        List<Comment> comments = new ArrayList<>();

        for (Map commentSource : commentObj) {
            Comment comment = new Comment();

            //装配comment
            Map userData =(Map) commentSource.get("user");
            User user = new User();
            user.setId(userData.get("userId").toString());
            user.setNikeName(userData.get("nickname").toString());
            user.setAvatar(userData.get("avatarUrl").toString());
            comment.setCommentUser(user);

            comment.setId(commentSource.get("commentId").toString());
            comment.setContent(commentSource.get("content").toString());
            comment.setLikedCount(commentSource.get("likedCount").toString());
            comment.setTime(commentSource.get("time").toString());

            comments.add(comment);
        }
        return comments;
    }

    /**
     * 装配歌曲评论
     * @param artistId
     */
    private void assembleSongComment(String artistId) {
        List<Song> songList = ARTISTS.get(artistId).getSongList();

        for (Song song : songList) {
            String url = getUrl(COMMENT_API, song.getId()) + "&limit=5";
            Map allCommentObj = getContent(url);
            List<Map> hotCommentsObj = (List<Map>) allCommentObj.get("hotComments");
            List<Map> commentsObj = (List<Map>) allCommentObj.get("comments");

            song.setHotComments(getComments(hotCommentsObj));
            song.setComments(getComments(commentsObj));
        }
    }

    /**
     * 装配歌曲链接
     * @param artistId
     */
    private void assembleSongUrl(String artistId) {
        List<Song> songList = ARTISTS.get(artistId).getSongList();

        Map sourceObj = getContent(getUrl(SOURCE_API, buildManyIdParam(songList)));
        List<Map> data =(List<Map>) sourceObj.get("data");

        //将List集合转为Map
        Map<String, Map> dataMap = new HashMap<>();
        for (Map d : data) {
            dataMap.put(d.get("id").toString(), d);
        }

        for (Song song : songList) {

            if (dataMap.get(song.getId()) != null && dataMap.get(song.getId()).get("url") != null) {
                String url = dataMap.get(song.getId()).get("url").toString();
                song.setSourceUrl(url);
            }

        }
    }

    /**
     * 制作图云
     * @param artistId
     */
    private void buildWordCloud(String artistId) {
        List<Song> songList = ARTISTS.get(artistId).getSongList();

        List<String> allContents = new ArrayList<>();
        for (Song song : songList) {
            allContents.addAll(getContents(song.getHotComments()));
            allContents.addAll(getContents(song.getComments()));
        }

        System.out.println(allContents);
        WordCloudUtil.generate(artistId, allContents);
    }

    /**
     * 获取评论内容
     * @param comments
     * @return
     */
    private List<String> getContents(List<Comment> comments) {
        List<String> contents = new ArrayList<>();

        for (Comment comment : comments) {
            contents.add(comment.getContent());
        }
        return contents;
    }
}
