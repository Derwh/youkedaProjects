package pers.Derwh.getHostSongList.test;

import pers.Derwh.getHostSongList.Service.SongCrawlerService;
import pers.Derwh.getHostSongList.Service.impl.SongCrawlerServiceImpl;

public class SongCrawlerTest {

    public static void main(String[] args) {
        SongCrawlerService songCrawlerService = new SongCrawlerServiceImpl();

        songCrawlerService.start("5781");
    }
}
