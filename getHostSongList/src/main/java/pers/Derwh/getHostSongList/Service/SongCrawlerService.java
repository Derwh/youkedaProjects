package pers.Derwh.getHostSongList.Service;

import pers.Derwh.getHostSongList.model.Artist;
import pers.Derwh.getHostSongList.model.Song;

public interface SongCrawlerService {

    //根据歌单id抓取数据
    void start(String artistId);

    //根据歌单id获取相应歌单
    Artist getArtist(String artistId);

    //根据歌单id和歌曲id
    Song getSong(String artistId, String songId);
}
